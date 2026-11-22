#Requires -Version 5.1
<#
.SYNOPSIS
    Suramadu Server management script (Windows port of run.sh).
.DESCRIPTION
    run.ps1 { start | stop | restart | status | list }
    run.ps1 -v VERSION { start | restart }
    run.ps1 -d { stop | restart }
    run.ps1 -h | --help

    Same command line as run.sh. All logic lives here; run.bat only launches this script.
    ASCII only on purpose: Windows PowerShell 5.1 misreads UTF-8 without BOM.
#>

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

# -- Helpers needed by the configuration block -----------------------------

function Resolve-First([string[]]$Paths) {
    foreach ($p in $Paths) {
        if ($p -and (Test-Path -LiteralPath $p -PathType Leaf)) { return $p }
    }
    return $null
}

# -- Configuration ---------------------------------------------------------

$App       = 'Suramadu'
$WarPrefix = 'suramadu-server'
$Base      = $PSScriptRoot
if (-not $Base) { $Base = Split-Path -Parent $MyInvocation.MyCommand.Path }
$IsWin     = ([Environment]::OSVersion.Platform -eq [PlatformID]::Win32NT)
$ExeSuffix = if ($IsWin) { '.exe' } else { '' }
$UserHome  = [Environment]::GetFolderPath('UserProfile')
if (-not $UserHome) { $UserHome = [Environment]::GetEnvironmentVariable('HOME') }
if (-not $UserHome) { $UserHome = $Base }

# Config directory (always absolute): ~\.manticore if it holds a Suramadu config
# (legacy layout), otherwise the installation directory.
$ManticoreDir = Join-Path $UserHome '.manticore'
$ConfigDir = $Base
if ((Test-Path -LiteralPath (Join-Path $ManticoreDir 'suramadu.config')) -or
    (Test-Path -LiteralPath (Join-Path $ManticoreDir 'webswing.config'))) {
    $ConfigDir = $ManticoreDir
}

# Prefer the current configuration file names; fall back to the legacy ones.
$ConfigFile = Resolve-First @((Join-Path $ConfigDir 'suramadu.config'), (Join-Path $ConfigDir 'webswing.config'))
if (-not $ConfigFile) { $ConfigFile = Join-Path $ConfigDir 'suramadu.config' }
$PropsFile = Resolve-First @((Join-Path $ConfigDir 'suramadu.properties'), (Join-Path $ConfigDir 'webswing.properties'))
if (-not $PropsFile) { $PropsFile = Join-Path $ConfigDir 'suramadu.properties' }
$JettyFile = Join-Path $ConfigDir 'jetty.properties'

# JUL config: next to the Suramadu config, else ~\.manticore; none = JDK defaults
$LoggingOpts = @()
foreach ($c in @((Join-Path $ConfigDir 'logging.properties'), (Join-Path $ManticoreDir 'logging.properties'))) {
    if (Test-Path -LiteralPath $c -PathType Leaf) {
        $LoggingOpts = @("-Djava.util.logging.config.file=$c")
        break
    }
}

$MinJava          = 17
$LogRetentionDays = 10
$LowPriority      = $true      # Windows equivalent of "nice -n 10": BelowNormal priority class

$LogDir    = Join-Path $Base 'logs'
$PidFile   = Join-Path $LogDir "$App.pid"
$LogFile   = Join-Path $LogDir "$App.log"
$DetachLog = Join-Path $LogDir "$App-restart.log"
$Launcher  = Join-Path $LogDir "$App-launch.cmd"

# SSL certificates to import into the JVM truststore on startup.
# Format: 'alias|host|port' (one entry per line). Needs no openssl.
$SslCerts = @(
    # 'myalias|intranet.example.com|443'
)
# Empty = cacerts of the resolved JDK (also valid after the PATH fallback)
$SslKeystore     = ''
$SslKeystorePass = 'changeit'

# Optional, per host: export server certs from a keystore into the truststore of the JDK
# that Suramadu launches the Swing apps with. Disabled while ServerCertAliases is empty.
$AppJdkHome          = ''      # e.g. C:\jdk-25-lite
$AppTruststorePass   = 'changeit'
$ServerKeystore      = ''      # e.g. C:\vbox\ssl\keystore.jks
$ServerKeystorePass  = ''
$ServerCertAliases   = @()     # e.g. @('myrootca')

# Server JVM flags (GC flags are added by Initialize-Java). -Xms is the floor idle uncommit can reach.
$JavaOpts = @(
    '-server', '-Xmx2G', '-Xms64m', '-Xss228k'
    '-XX:+ExitOnOutOfMemoryError'
    '-XX:+HeapDumpOnOutOfMemoryError'
    "-XX:HeapDumpPath=$LogDir"
    "-XX:ErrorFile=$LogDir\hs_err_%p.log"
    '-Dcom.sun.jndi.ldap.object.disableEndpointIdentification=true'
    '-Dwebswing.websocketMessageSizeLimit=1024000'
    '-Djsse.enableSNIExtension=false'
    # kept from the previous Windows scripts
    '-Duser.country=US', '-Duser.language=en'
    '-Doracle.jdbc.timezoneAsRegion=false'
    '--add-modules=java.desktop'
    '--add-exports=java.desktop/sun.awt=ALL-UNNAMED'
    '--add-exports=java.desktop/sun.awt.dnd=ALL-UNNAMED'
    '--add-exports=java.desktop/sun.awt.datatransfer=ALL-UNNAMED'
    '--add-exports=java.desktop/sun.awt.image=ALL-UNNAMED'
    '--add-exports=java.desktop/sun.java2d=ALL-UNNAMED'
    '--add-exports=java.desktop/sun.java2d.pipe=ALL-UNNAMED'
    '--add-exports=java.desktop/sun.java2d.loops=ALL-UNNAMED'
    '--add-exports=java.desktop/sun.font=ALL-UNNAMED'
    '--add-exports=java.desktop/sun.print=ALL-UNNAMED'
    '--add-exports=java.desktop/java.awt.peer=ALL-UNNAMED'
    '--add-exports=java.desktop/java.awt.dnd=ALL-UNNAMED'
    '--add-exports=java.base/sun.nio.cs=ALL-UNNAMED'
    '--add-opens=java.desktop/sun.awt.image=ALL-UNNAMED'
)

# Window theme. Set to @() for the neutral default theme.
$BrandingOpts = @(
    '-Dorg.webswing.window.theme.primaryColor=0xd70900'
    '-Dorg.webswing.window.theme.secondaryColor=0xd70900'
    '-Dorg.webswing.window.theme.contentBgColor=0xffffff'
)

# Server arguments. -c / -pf are only passed when the file exists (the earlier Windows
# scripts never passed them, so legacy installs keep working).
$SuramaduOpts = @('-h', '0.0.0.0', '-j', $JettyFile)
if (Test-Path -LiteralPath $ConfigFile) { $SuramaduOpts += @('-c', $ConfigFile) }
if (Test-Path -LiteralPath $PropsFile)  { $SuramaduOpts += @('-pf', $PropsFile) }

# Runtime state
$script:JavaBin       = ''
$script:JavaHome      = ''
$script:GcOpts        = @()
$script:FullOpts      = @()
$script:War           = $null
$script:PreflightDone = $false

# -- Command line (same as run.sh) -----------------------------------------

$RequestedVersion  = ''
$Action            = ''
$ForceDetach       = $false
$IsDetachedChild   = $false     # internal: set by Invoke-Detached
$InheritedJavaHome = ''         # internal: JDK already validated by the parent

function Show-Help {
    Write-Host ''
    Write-Host "$App Server" -ForegroundColor Cyan
    Write-Host ''
    Write-Host 'Usage:'
    Write-Host '    run.ps1 { start | stop | restart | status | list }'
    Write-Host '    run.ps1 -v VERSION { start | restart }'
    Write-Host '    run.ps1 -d { stop | restart }'
    Write-Host '    run.ps1 -h | --help'
    Write-Host ''
    Write-Host 'Options:'
    Write-Host '    -v VERSION    Use a specific WAR version (e.g. -v 26.0)'
    Write-Host '                  Default: auto-detect latest version'
    Write-Host '    -d            Force detached mode for stop/restart (see below)'
    Write-Host ''
    Write-Host 'Commands:'
    Write-Host '    start         Start the server'
    Write-Host '    stop          Stop the server (and its child processes)'
    Write-Host '    restart       Stop + Start'
    Write-Host '    status        Check if the server is running'
    Write-Host '    list          List all available WAR versions'
    Write-Host ''
    Write-Host 'Detached mode:'
    Write-Host "    stop/restart are automatically detached when invoked from inside a"
    Write-Host "    $App session (e.g. the Jediterm shell). The script re-launches itself"
    Write-Host '    as a hidden background process, so it survives the server going down.'
    Write-Host '    Your session WILL drop; reconnect after ~30-60 s.'
    Write-Host "    Output goes to logs\$App-restart.log"
    Write-Host ''
}

for ($i = 0; $i -lt $args.Count; $i++) {
    $a = [string]$args[$i]
    if ($a -in @('-h', '--help', '-?', 'help')) {
        Show-Help
        exit 0
    }
    elseif ($a -in @('-v', '--version', '-Version')) {
        if (($i + 1) -ge $args.Count) { Write-Host 'ERROR: -v requires a version argument' -ForegroundColor Red; exit 1 }
        $i++
        $RequestedVersion = [string]$args[$i]
    }
    elseif ($a -in @('-d', '--detach', '-Detach')) { $ForceDetach = $true }
    elseif ($a -eq '--detached-child') { $IsDetachedChild = $true }
    elseif ($a -eq '--java-home') {
        if (($i + 1) -ge $args.Count) { exit 1 }
        $i++
        $InheritedJavaHome = [string]$args[$i]
    }
    elseif ($a -in @('start', 'stop', 'restart', 'status', 'list')) { $Action = $a.ToLowerInvariant() }
    else {
        Write-Host "Unknown argument: $a"
        Show-Help
        exit 1
    }
}

if (-not $Action) {
    Write-Host 'No action specified.'
    Show-Help
    exit 1
}

# -- Native process helpers ------------------------------------------------

# Quote one argument for a Windows command line (CommandLineToArgvW rules).
function Quote-Arg([string]$s) {
    if ($s.Length -eq 0) { return '""' }
    if ($s -notmatch '[\s"]') { return $s }
    $t = $s -replace '(\\*)"', '$1$1\"'
    $t = $t -replace '(\\+)$', '$1$1'
    return '"' + $t + '"'
}

# Run a native program, capture stdout+stderr and the exit code. Avoids the
# "native stderr + ErrorActionPreference=Stop" trap of Windows PowerShell 5.1
# (java -version writes to stderr) and cannot hang: it is killed after TimeoutSec.
function Invoke-Native {
    param([string]$Exe, [string[]]$ArgList = @(), [int]$TimeoutSec = 60)
    $psi = New-Object System.Diagnostics.ProcessStartInfo
    $psi.FileName = $Exe
    $psi.Arguments = (@($ArgList | ForEach-Object { Quote-Arg $_ }) -join ' ')
    $psi.UseShellExecute = $false
    $psi.RedirectStandardOutput = $true
    $psi.RedirectStandardError = $true
    $psi.CreateNoWindow = $true
    $proc = New-Object System.Diagnostics.Process
    $proc.StartInfo = $psi
    try {
        [void]$proc.Start()
    } catch {
        return [pscustomobject]@{ ExitCode = -1; Output = $_.Exception.Message }
    }
    $out = $proc.StandardOutput.ReadToEndAsync()
    $err = $proc.StandardError.ReadToEndAsync()
    if (-not $proc.WaitForExit($TimeoutSec * 1000)) {
        try { $proc.Kill() } catch { }
        return [pscustomobject]@{ ExitCode = -1; Output = "timed out after ${TimeoutSec}s" }
    }
    $proc.WaitForExit()
    return [pscustomobject]@{ ExitCode = $proc.ExitCode; Output = ([string]$out.Result + [string]$err.Result) }
}

# -- WAR version detection -------------------------------------------------

# Sortable key: every segment zero padded, so 26.0.1 > 26.0 > 20.2.5 (no [version] cast,
# which throws for single-segment or 5+ segment versions).
function Get-VersionKey([string]$Ver) {
    $nums = @()
    foreach ($p in @($Ver -split '\.')) { $nums += ('{0:D9}' -f [int64]$p) }
    while ($nums.Count -lt 8) { $nums += '000000000' }
    return ($nums -join '.')
}

function Get-WarVersions {
    $wars = @()
    foreach ($f in @(Get-ChildItem -LiteralPath $Base -Filter "$WarPrefix-*.war" -File -ErrorAction SilentlyContinue)) {
        $ver = $f.BaseName.Substring($WarPrefix.Length + 1)
        if ($ver -match '^\d+(\.\d+)*$') {      # skip SNAPSHOT etc.
            $wars += [pscustomobject]@{ Key = (Get-VersionKey $ver); VersionStr = $ver; File = $f.FullName; FileName = $f.Name }
        }
    }
    return @($wars | Sort-Object -Property Key -Descending)
}

function Find-LatestWar {
    $wars = @(Get-WarVersions)
    if ($wars.Count -gt 0) { return $wars[0] }

    $unversioned = Join-Path $Base "$WarPrefix.war"
    if (Test-Path -LiteralPath $unversioned) {
        return [pscustomobject]@{ Key = ''; VersionStr = '(unversioned)'; File = $unversioned; FileName = "$WarPrefix.war" }
    }

    Write-Host "ERROR: No WAR files found matching $WarPrefix-*.war in $Base" -ForegroundColor Red
    Write-Host ''
    Write-Host '  Expected files like:'
    Write-Host "    $WarPrefix-26.0.war"
    Write-Host "    $WarPrefix-20.2.5.war"
    Write-Host "    $WarPrefix.war"
    return $null
}

function Find-WarByVersion([string]$TargetVersion) {
    $targetFile = Join-Path $Base "$WarPrefix-$TargetVersion.war"
    if (Test-Path -LiteralPath $targetFile) {
        return [pscustomobject]@{ Key = ''; VersionStr = $TargetVersion; File = $targetFile; FileName = "$WarPrefix-$TargetVersion.war" }
    }
    Write-Host "ERROR: WAR file not found: $targetFile" -ForegroundColor Red
    Write-Host ''
    Write-Host '  Available versions:'
    $wars = @(Get-WarVersions)
    if ($wars.Count -eq 0) { Write-Host '    (none found)' }
    else { foreach ($w in $wars) { Write-Host "    $($w.VersionStr)" } }
    return $null
}

# -- Java detection, GC selection, preflight -------------------------------

function Test-JavaHome([string]$Dir) {
    if (-not $Dir) { return $false }
    return (Test-Path -LiteralPath (Join-Path (Join-Path $Dir 'bin') "java$ExeSuffix") -PathType Leaf)
}

function Get-RegistryJavaHome {
    if (-not $IsWin) { return $null }
    foreach ($k in @('HKLM:\SOFTWARE\JavaSoft\JDK', 'HKLM:\SOFTWARE\JavaSoft\Java Development Kit')) {
        try {
            $cur = (Get-ItemProperty -Path $k -Name CurrentVersion -ErrorAction Stop).CurrentVersion
            $jh = (Get-ItemProperty -Path (Join-Path $k $cur) -Name JavaHome -ErrorAction Stop).JavaHome
            if (Test-JavaHome $jh) { return $jh }
        } catch { }
    }
    try {
        foreach ($item in @(Get-ChildItem 'HKLM:\SOFTWARE\Eclipse Adoptium\JDK' -ErrorAction Stop | Sort-Object Name -Descending)) {
            try {
                $jh = (Get-ItemProperty -Path (Join-Path $item.PSPath 'hotspot\MSI') -Name Path -ErrorAction Stop).Path
                if (Test-JavaHome $jh) { return $jh }
            } catch { }
        }
    } catch { }
    return $null
}

# Returns @{ Bin; Home } or $null (after printing install hints).
function Find-Java {
    # 1. JDK already validated by the parent (detached child), 2. bundled next to the
    # installation, 3. JAVA_HOME, 4. registry, 5. java on PATH.
    $bundled = $null
    try { $bundled = [IO.Path]::GetFullPath((Join-Path $Base '..\java')) } catch { }
    foreach ($cand in @($InheritedJavaHome, $bundled, $env:JAVA_HOME)) {
        if ($cand -and (Test-JavaHome $cand)) {
            return [pscustomobject]@{ Bin = (Join-Path (Join-Path $cand 'bin') "java$ExeSuffix"); Home = $cand }
        }
    }

    $reg = Get-RegistryJavaHome
    if ($reg) {
        Write-Host "WARN: no bundled JDK and JAVA_HOME not usable, using JDK from registry ($reg)." -ForegroundColor Yellow
        return [pscustomobject]@{ Bin = (Join-Path (Join-Path $reg 'bin') "java$ExeSuffix"); Home = $reg }
    }

    $onPath = Get-Command "java$ExeSuffix" -ErrorAction SilentlyContinue | Select-Object -First 1
    if ($onPath) {
        $bin = $onPath.Source
        $jh = ''
        $binDir = Split-Path -Parent $bin
        if ((Split-Path -Leaf $binDir) -ieq 'bin') { $jh = Split-Path -Parent $binDir }
        Write-Host "WARN: no bundled JDK and JAVA_HOME not usable, using java from PATH ($bin)." -ForegroundColor Yellow
        return [pscustomobject]@{ Bin = $bin; Home = $jh }
    }

    Write-Host ''
    Write-Host 'ERROR: Java not found.' -ForegroundColor Red
    Write-Host ''
    Write-Host "  Looked for: bundled $Base\..\java, JAVA_HOME, registry, PATH."
    Write-Host '  Install a JDK 21+ (recommended):'
    Write-Host ''
    Write-Host '    winget install EclipseAdoptium.Temurin.21.JDK'
    Write-Host '    choco install temurin21'
    Write-Host '    scoop bucket add java ; scoop install temurin21-jdk'
    Write-Host '    https://adoptium.net/'
    Write-Host ''
    return $null
}

function Get-JavaMajor([string]$JavaBin) {
    $r = Invoke-Native $JavaBin @('-version') 30
    if ($r.Output -match '(?m)version\s+"(\d+)(?:\.(\d+))?') {
        $major = [int]$Matches[1]
        if ($major -eq 1 -and $Matches[2]) { $major = [int]$Matches[2] }   # 1.8 -> 8
        return $major
    }
    return $null
}

# check_java of run.sh: resolve JDK, check version, choose GC, assemble the JVM flags.
function Initialize-Java {
    $found = Find-Java
    if (-not $found) { exit 1 }
    $script:JavaBin = $found.Bin
    $script:JavaHome = $found.Home

    $major = Get-JavaMajor $script:JavaBin
    if ($null -eq $major) {
        Write-Host 'WARN: Could not determine Java version.' -ForegroundColor Yellow
    }
    elseif ($major -lt $MinJava) {
        Write-Host "ERROR: Java $major detected, but JDK $MinJava+ is required." -ForegroundColor Red
        exit 1
    }
    else {
        Write-Host "Java $major detected."
    }

    # GC: Shenandoah (very low pauses, compressed oops, uncommits idle memory) where the
    # JDK includes it (not in Oracle builds); otherwise G1, told to shrink the heap when idle.
    # Single-generation Shenandoah on purpose: generational mode is not wanted yet.
    $probe = Invoke-Native $script:JavaBin @('-XX:+UseShenandoahGC', '-version') 30
    if ($probe.ExitCode -eq 0) {
        $script:GcOpts = @('-XX:+UseShenandoahGC', '-XX:+UseStringDeduplication')
    } else {
        Write-Host 'WARN: this JDK has no Shenandoah, falling back to G1.' -ForegroundColor Yellow
        $script:GcOpts = @('-XX:+UseG1GC', '-XX:G1PeriodicGCInterval=60000', '-XX:MinHeapFreeRatio=10',
                           '-XX:MaxHeapFreeRatio=30', '-XX:+UseStringDeduplication')
    }
    $script:FullOpts = @($JavaOpts) + @($script:GcOpts) + @($BrandingOpts)
}

# Everything that can make a (re)start fail, checked BEFORE the running instance is
# touched. Exits non-zero (and prints why) if the start would fail.
function Invoke-Preflight {
    Initialize-Java

    if ($RequestedVersion) { $war = Find-WarByVersion $RequestedVersion }
    else { $war = Find-LatestWar }
    if (-not $war) { exit 1 }
    $script:War = $war

    # Dry-run the JVM with the exact start flags: catches options this JDK rejects.
    $dry = Invoke-Native $script:JavaBin (@($script:FullOpts) + @('-version')) 60
    if ($dry.ExitCode -ne 0) {
        Write-Host 'ERROR: the JVM rejects its start options -- NOT touching the running instance.' -ForegroundColor Red
        foreach ($line in @($dry.Output -split "`r?`n")) { if ($line) { Write-Host "  $line" } }
        exit 1
    }

    foreach ($f in @($ConfigFile, $PropsFile, $JettyFile)) {
        if (-not (Test-Path -LiteralPath $f)) { Write-Host "WARN: $f not found" -ForegroundColor Yellow }
    }

    $script:PreflightDone = $true
    if ($Action -eq 'restart') {
        Write-Host ("Preflight OK: {0}, {1}, GC: {2}" -f $war.FileName, $script:JavaBin, ($script:GcOpts[0]))
    }
}

# -- Certificates ----------------------------------------------------------

function Get-RemoteCertChain([string]$HostName, [int]$Port) {
    $client = New-Object System.Net.Sockets.TcpClient
    try {
        $ar = $client.BeginConnect($HostName, $Port, $null, $null)
        if (-not $ar.AsyncWaitHandle.WaitOne(10000)) { return @() }
        $client.EndConnect($ar)
        $callback = [System.Net.Security.RemoteCertificateValidationCallback]{ param($s, $c, $ch, $e) $true }
        $ssl = New-Object System.Net.Security.SslStream($client.GetStream(), $false, $callback)
        try { $ssl.AuthenticateAsClient($HostName) }
        catch { $ssl.AuthenticateAsClient($HostName, $null, [System.Security.Authentication.SslProtocols]::Tls12, $false) }
        $leaf = New-Object System.Security.Cryptography.X509Certificates.X509Certificate2($ssl.RemoteCertificate)
        $chain = New-Object System.Security.Cryptography.X509Certificates.X509Chain
        $chain.ChainPolicy.RevocationMode = [System.Security.Cryptography.X509Certificates.X509RevocationMode]::NoCheck
        [void]$chain.Build($leaf)
        $certs = @()
        foreach ($el in $chain.ChainElements) { $certs += $el.Certificate }
        return $certs
    } catch {
        return @()
    } finally {
        $client.Close()
    }
}

function Import-SslCerts {
    if (@($SslCerts).Count -eq 0) { return }
    if (-not $script:JavaHome) {
        Write-Host 'WARN: JDK home unknown (java taken from PATH), skipping SSL certificate import.' -ForegroundColor Yellow
        return
    }
    $keytool = Join-Path (Join-Path $script:JavaHome 'bin') "keytool$ExeSuffix"
    if (-not (Test-Path -LiteralPath $keytool)) {
        Write-Host "WARN: keytool not found at $keytool, skipping SSL certificate import." -ForegroundColor Yellow
        return
    }
    $keystore = $SslKeystore
    if (-not $keystore) { $keystore = Join-Path (Join-Path (Join-Path $script:JavaHome 'lib') 'security') 'cacerts' }

    $tmp = Join-Path ([IO.Path]::GetTempPath()) ('suramadu-' + [guid]::NewGuid().ToString('N'))
    [void](New-Item -ItemType Directory -Path $tmp)
    try {
        foreach ($entry in @($SslCerts)) {
            $parts = @(([string]$entry) -split '\|')
            if ($parts.Count -lt 3 -or -not $parts[0].Trim()) { continue }
            $alias = $parts[0].Trim()
            $hostName = $parts[1].Trim()
            $port = [int]$parts[2].Trim()
            Write-Host "  Importing SSL cert: $alias (${hostName}:${port})"

            $certs = @(Get-RemoteCertChain $hostName $port)
            if ($certs.Count -eq 0) {
                Write-Host "    WARN: Failed to retrieve certificate from ${hostName}:${port}, skipping." -ForegroundColor Yellow
                continue
            }
            $idx = 0
            foreach ($cert in $certs) {
                $file = Join-Path $tmp "$alias-$idx.cer"
                [IO.File]::WriteAllBytes($file, $cert.Export([System.Security.Cryptography.X509Certificates.X509ContentType]::Cert))
                $certAlias = "$alias-$idx"
                [void](Invoke-Native $keytool @('-delete', '-keystore', $keystore, '-storepass', $SslKeystorePass, '-alias', $certAlias) 60)
                $r = Invoke-Native $keytool @('-import', '-noprompt', '-trustcacerts', '-alias', $certAlias, '-file', $file,
                                              '-keystore', $keystore, '-storepass', $SslKeystorePass) 60
                if ($r.ExitCode -eq 0) { Write-Host "    Imported: $certAlias" }
                else { Write-Host "    WARN: Failed to import $certAlias" -ForegroundColor Yellow }
                $idx++
            }
            Write-Host "    Imported $idx certificate(s) for $alias."
        }
    } finally {
        Remove-Item -LiteralPath $tmp -Recurse -Force -ErrorAction SilentlyContinue
    }
}

function Import-ServerCert {
    if (@($ServerCertAliases).Count -eq 0) { return }
    if (-not $ServerKeystore -or -not (Test-Path -LiteralPath $ServerKeystore)) {
        Write-Host "  WARN: keystore '$ServerKeystore' missing, skip server-cert import" -ForegroundColor Yellow
        return
    }
    if (-not $AppJdkHome) {
        Write-Host '  WARN: AppJdkHome not set, skip server-cert import' -ForegroundColor Yellow
        return
    }
    $truststore = Join-Path (Join-Path (Join-Path $AppJdkHome 'lib') 'security') 'cacerts'
    if (-not (Test-Path -LiteralPath $truststore)) {
        Write-Host "  WARN: $truststore not found, skip server-cert import" -ForegroundColor Yellow
        return
    }
    $keytool = "keytool$ExeSuffix"
    if ($script:JavaHome) {
        $own = Join-Path (Join-Path $script:JavaHome 'bin') "keytool$ExeSuffix"
        if (Test-Path -LiteralPath $own) { $keytool = $own }
    }

    $tmp = Join-Path ([IO.Path]::GetTempPath()) ('suramadu-' + [guid]::NewGuid().ToString('N'))
    [void](New-Item -ItemType Directory -Path $tmp)
    try {
        foreach ($alias in @($ServerCertAliases)) {
            $crt = Join-Path $tmp "$alias.crt"
            $r = Invoke-Native $keytool @('-exportcert', '-rfc', '-keystore', $ServerKeystore, '-storepass', $ServerKeystorePass,
                                          '-alias', $alias, '-file', $crt) 60
            if ($r.ExitCode -ne 0) { Write-Host "  WARN: alias '$alias' not in keystore, skip" -ForegroundColor Yellow; continue }
            [void](Invoke-Native $keytool @('-delete', '-keystore', $truststore, '-storepass', $AppTruststorePass,
                                            '-alias', "suramadu-$alias") 60)
            $r = Invoke-Native $keytool @('-importcert', '-noprompt', '-trustcacerts', '-keystore', $truststore,
                                          '-storepass', $AppTruststorePass, '-alias', "suramadu-$alias", '-file', $crt) 60
            if ($r.ExitCode -eq 0) { Write-Host "  Imported '$alias' into app truststore" }
            else { Write-Host "  WARN: failed to import '$alias'" -ForegroundColor Yellow }
        }
    } finally {
        Remove-Item -LiteralPath $tmp -Recurse -Force -ErrorAction SilentlyContinue
    }
}

# -- Process helpers -------------------------------------------------------

function Get-SavedPid {
    if (Test-Path -LiteralPath $PidFile) {
        $raw = Get-Content -LiteralPath $PidFile -Raw -ErrorAction SilentlyContinue
        if ($raw) {
            $txt = $raw.Trim()
            if ($txt -match '^\d+$') { return [int]$txt }
        }
    }
    return $null
}

function Test-ServerRunning {
    $savedPid = Get-SavedPid
    if ($null -eq $savedPid) { return $false }
    $proc = Get-Process -Id $savedPid -ErrorAction SilentlyContinue
    return ($null -ne $proc -and $proc.ProcessName -match '^(java|javaw|cmd)$')
}

function Get-ProcessTable {
    try {
        return @(Get-CimInstance -ClassName Win32_Process -ErrorAction Stop |
                 Select-Object ProcessId, ParentProcessId, Name, CommandLine, CreationDate)
    } catch {
        return @()
    }
}

# True if this script runs below the server JVM, i.e. from a Jediterm shell opened inside
# a Suramadu session.
function Test-InsideServerTree {
    $srv = Get-SavedPid
    if ($null -eq $srv) { return $false }
    $parents = @{}
    foreach ($p in (Get-ProcessTable)) { $parents[[int]$p.ProcessId] = [int]$p.ParentProcessId }
    $cur = [int]$PID
    $guard = 0
    while ($parents.ContainsKey($cur) -and $guard -lt 64) {
        $cur = $parents[$cur]
        if ($cur -eq $srv) { return $true }
        $guard++
    }
    return $false
}

# Kill a process and all its descendants (children first), never this script itself.
# Windows has no SIGTERM for console programs, so this is a hard stop; killing the
# tree also takes down the per-session app JVMs that Suramadu launched.
function Stop-ProcessTree([int]$RootPid) {
    $table = Get-ProcessTable
    $created = @{}
    foreach ($p in $table) { if ($p.CreationDate) { $created[[int]$p.ProcessId] = $p.CreationDate } }
    $children = @{}
    foreach ($p in $table) {
        $id = [int]$p.ProcessId
        $pp = [int]$p.ParentProcessId
        # a reused parent PID must not adopt older, unrelated processes
        if ($created.ContainsKey($id) -and $created.ContainsKey($pp) -and $created[$id] -lt $created[$pp]) { continue }
        if (-not $children.ContainsKey($pp)) { $children[$pp] = New-Object System.Collections.ArrayList }
        [void]$children[$pp].Add($id)
    }
    $order = New-Object System.Collections.ArrayList
    $queue = New-Object System.Collections.Queue
    $queue.Enqueue($RootPid)
    $seen = @{}
    while ($queue.Count -gt 0) {
        $cur = [int]$queue.Dequeue()
        if ($seen.ContainsKey($cur)) { continue }
        $seen[$cur] = $true
        [void]$order.Add($cur)
        if ($children.ContainsKey($cur)) { foreach ($c in $children[$cur]) { $queue.Enqueue($c) } }
    }
    for ($i = $order.Count - 1; $i -ge 0; $i--) {
        $id = [int]$order[$i]
        if ($id -eq [int]$PID -or $id -le 4) { continue }
        Stop-Process -Id $id -Force -ErrorAction SilentlyContinue
    }
}

function Get-OrphanPids {
    $ids = @()
    foreach ($p in (Get-ProcessTable)) {
        if ($p.Name -match '^javaw?\.exe$' -and $p.CommandLine -and $p.CommandLine -like "*-jar*$WarPrefix*") {
            $ids += [int]$p.ProcessId
        }
    }
    return $ids
}

function Write-Log([string]$Message) {
    if (-not (Test-Path -LiteralPath $LogDir)) { [void](New-Item -ItemType Directory -Path $LogDir) }
    Add-Content -LiteralPath $LogFile -Value ("{0}: {1}" -f (Get-Date -Format 'yyyy-MM-dd HH:mm:ss'), $Message)
}

function Rotate-Logs {
    $cutoff = (Get-Date).AddDays(-$LogRetentionDays)
    Get-ChildItem -LiteralPath $LogDir -Filter "$App.log*" -File -ErrorAction SilentlyContinue |
        Where-Object { $_.LastWriteTime -lt $cutoff } |
        Remove-Item -Force -ErrorAction SilentlyContinue
    if (Test-Path -LiteralPath $LogFile) {
        $rotated = "${LogFile}_" + (Get-Date -Format 'yyyyMMddHHmmss')
        try {
            Move-Item -LiteralPath $LogFile -Destination $rotated -Force
            [void](New-Item -ItemType File -Path $LogFile -Force)
        } catch {
            Copy-Item -LiteralPath $LogFile -Destination $rotated -Force -ErrorAction SilentlyContinue
            Clear-Content -LiteralPath $LogFile -ErrorAction SilentlyContinue
        }
    }
}

# Re-launch this script fully detached from the current process tree (see run.sh):
#   - Win32_Process.Create (WMI) starts it outside this process tree and outside any job
#     object the server may have put its children in; fallback: Start-Process
#   - hidden window, own console, so closing the session's pseudo console cannot reach it
#   - the JVM-related environment variables of the (app-JVM) shell are cleared first
#   - output goes to logs\Suramadu-restart.log (transcript in the child)
function Invoke-Detached {
    if (-not (Test-Path -LiteralPath $LogDir)) { [void](New-Item -ItemType Directory -Path $LogDir) }
    Remove-Item -LiteralPath $DetachLog -Force -ErrorAction SilentlyContinue

    foreach ($v in @('JAVA_TOOL_OPTIONS', '_JAVA_OPTIONS', 'JDK_JAVA_OPTIONS', 'CLASSPATH')) {
        Remove-Item -LiteralPath "Env:\$v" -ErrorAction SilentlyContinue
    }

    $psExe = (Get-Process -Id $PID).Path
    $psArgs = @('-NoProfile', '-ExecutionPolicy', 'Bypass', '-WindowStyle', 'Hidden',
                '-File', $PSCommandPath, '--detached-child')
    if ($script:JavaHome) { $psArgs += @('--java-home', $script:JavaHome) }
    if ($RequestedVersion) { $psArgs += @('-v', $RequestedVersion) }
    $psArgs += $Action
    $argString = (@($psArgs | ForEach-Object { Quote-Arg $_ }) -join ' ')

    $launched = $false
    try {
        $r = Invoke-CimMethod -ClassName Win32_Process -MethodName Create -ErrorAction Stop -Arguments @{
            CommandLine      = ((Quote-Arg $psExe) + ' ' + $argString)
            CurrentDirectory = $Base
        }
        if ($r.ReturnValue -eq 0) { $launched = $true }
    } catch { }
    if (-not $launched) {
        Start-Process -FilePath $psExe -ArgumentList $argString -WorkingDirectory $Base -WindowStyle Hidden | Out-Null
    }

    Write-Host ''
    Write-Host "==== $Action of $App handed over to a detached process"
    Write-Host "  Log:  $DetachLog"
    Write-Host '  This session will drop in ~2 s. Reconnect after 30-60 s.'
}

# -- Commands --------------------------------------------------------------

function Show-WarList {
    Write-Host ''
    Write-Host "==== Available $App versions in $Base"
    Write-Host ''
    $wars = @(Get-WarVersions)
    if ($wars.Count -eq 0) {
        $unversioned = Join-Path $Base "$WarPrefix.war"
        if (Test-Path -LiteralPath $unversioned) { Write-Host "    (unversioned)  $WarPrefix.war" }
        else { Write-Host '    (none found)' }
    } else {
        $first = $true
        foreach ($w in $wars) {
            $marker = if ($first) { ' <- latest' } else { '' }
            Write-Host ('    {0,-12}  {1}{2}' -f $w.VersionStr, $w.FileName, $marker)
            $first = $false
        }
    }
    Write-Host ''
}

function Show-Status {
    Write-Host ''
    Write-Host "==== Status of $App"
    if (Test-ServerRunning) {
        $savedPid = Get-SavedPid
        $proc = Get-Process -Id $savedPid -ErrorAction SilentlyContinue
        Write-Host ''
        Write-Host "$App is running with PID [$savedPid]" -ForegroundColor Green
        if ($proc) {
            Write-Host ("  Memory:     {0} MB" -f [math]::Round($proc.WorkingSet64 / 1MB, 1))
            Write-Host "  Start time: $($proc.StartTime)"
        }
    } elseif (Test-Path -LiteralPath $PidFile) {
        Write-Host ''
        Write-Host "$App is NOT running, but stale PID file found" -ForegroundColor Yellow
        Remove-Item -LiteralPath $PidFile -Force -ErrorAction SilentlyContinue
    } else {
        Write-Host ''
        Write-Host "$App is NOT running"
    }
}

function Start-Server {
    if (Test-ServerRunning) {
        Write-Host "$App already running with PID [$(Get-SavedPid)]"
        return
    }

    Initialize-Java
    Import-SslCerts
    Import-ServerCert

    if ($RequestedVersion) { $war = Find-WarByVersion $RequestedVersion }
    else { $war = Find-LatestWar }
    if (-not $war) { exit 1 }

    Write-Host "==== Starting $App v$($war.VersionStr)"
    Write-Host "  WAR: $($war.FileName)"
    if (-not (Test-Path -LiteralPath $LogDir)) { [void](New-Item -ItemType Directory -Path $LogDir) }

    Rotate-Logs

    # A tiny launcher script merges stdout+stderr into one log (like run.sh), writes the
    # STOPPED line and removes the PID file when the JVM exits. Written to disk so that
    # no nested quoting is needed (and so you can read exactly what is started).
    $jvmArgs = @()
    $jvmArgs += @($script:FullOpts)
    $jvmArgs += @($LoggingOpts)
    $jvmArgs += @('-jar', $war.File)
    $jvmArgs += @($SuramaduOpts)
    $javaLine = (Quote-Arg $script:JavaBin) + ' ' + (@($jvmArgs | ForEach-Object { Quote-Arg $_ }) -join ' ')
    $logEsc = $LogFile -replace '%', '%%'
    $pidEsc = $PidFile -replace '%', '%%'
    $lines = @(
        '@echo off',
        (($javaLine -replace '%', '%%') + ' >> "' + $logEsc + '" 2>&1'),
        ('echo %date% %time%: ' + $App + ' STOPPED>> "' + $logEsc + '"'),
        ('del /f /q "' + $pidEsc + '" >nul 2>&1')
    )
    [IO.File]::WriteAllLines($Launcher, $lines, [Text.Encoding]::Default)

    $cmdExe = $env:ComSpec
    if (-not $cmdExe) { $cmdExe = 'cmd.exe' }
    $launchProc = Start-Process -FilePath $cmdExe -ArgumentList ('/d /s /c ""' + $Launcher + '""') `
        -WorkingDirectory $Base -WindowStyle Hidden -PassThru

    # The server PID is the java.exe below the launcher (fallback: the launcher itself)
    $serverPid = $null
    $deadline = (Get-Date).AddSeconds(15)
    while ((Get-Date) -lt $deadline -and $null -eq $serverPid) {
        Start-Sleep -Milliseconds 300
        foreach ($p in (Get-ProcessTable)) {
            if ([int]$p.ParentProcessId -eq $launchProc.Id -and $p.Name -match '^javaw?\.exe$') { $serverPid = [int]$p.ProcessId; break }
        }
        if ($launchProc.HasExited) { break }
    }
    if ($null -eq $serverPid -and -not $launchProc.HasExited) { $serverPid = $launchProc.Id }

    if ($null -eq $serverPid) {
        Write-Host "ERROR: $App exited right after start. Last log lines:" -ForegroundColor Red
        if (Test-Path -LiteralPath $LogFile) { Get-Content -LiteralPath $LogFile -Tail 30 }
        exit 1
    }

    $serverPid | Out-File -FilePath $PidFile -Encoding ASCII -NoNewline
    if ($LowPriority) {
        try { (Get-Process -Id $serverPid -ErrorAction Stop).PriorityClass = 'BelowNormal' }
        catch { Write-Host 'WARN: could not lower the process priority.' -ForegroundColor Yellow }
    }
    Write-Host "Started with PID [$serverPid]"
    Write-Log "$App v$($war.VersionStr) STARTED ($($war.FileName))"

    # No terminal to show the log in when running detached
    if (-not $IsDetachedChild) {
        Start-Sleep -Seconds 2
        Write-Host ''
        Write-Host "Last log lines (follow with: Get-Content -Wait -Tail 50 `"$LogFile`"):" -ForegroundColor Cyan
        if (Test-Path -LiteralPath $LogFile) { Get-Content -LiteralPath $LogFile -Tail 20 }
    }
}

function Stop-Server {
    Write-Host "==== Stopping $App"

    $savedPid = Get-SavedPid
    if ($null -eq $savedPid) {
        Write-Host "No PID file found -- $App already stopped?"
        foreach ($orphan in @(Get-OrphanPids)) {
            Write-Host "Found running process with PID [$orphan], stopping..."
            Stop-ProcessTree $orphan
        }
        return
    }

    $proc = Get-Process -Id $savedPid -ErrorAction SilentlyContinue
    if ($null -eq $proc) {
        Write-Host "Process [$savedPid] not running"
        Remove-Item -LiteralPath $PidFile -Force -ErrorAction SilentlyContinue
        return
    }

    Write-Host "Stopping PID [$savedPid] and its child processes..."
    Stop-ProcessTree $savedPid

    $count = 0
    while ($count -lt 30 -and (Get-Process -Id $savedPid -ErrorAction SilentlyContinue)) {
        Start-Sleep -Seconds 1
        $count++
    }
    if (Get-Process -Id $savedPid -ErrorAction SilentlyContinue) {
        Write-Host 'Process did not stop -- force killing' -ForegroundColor Yellow
        Stop-Process -Id $savedPid -Force -ErrorAction SilentlyContinue
    }

    Remove-Item -LiteralPath $PidFile -Force -ErrorAction SilentlyContinue
    Write-Log "$App STOPPED"
    Write-Host "$App stopped."
}

function Restart-Server {
    # Never kill the old instance unless the new one can actually start
    # (already done in main when not detached; a detached child does its own)
    if (-not $script:PreflightDone) { Invoke-Preflight }
    Stop-Server
    Write-Host 'Sleeping...'
    Start-Sleep -Seconds 3
    Start-Server
}

# -- Main ------------------------------------------------------------------

Set-Location -LiteralPath $Base

if ($IsDetachedChild) {
    try { Start-Transcript -Path $DetachLog -Force | Out-Null } catch { }
}

if ($Action -eq 'stop' -or $Action -eq 'restart') {
    if ($Action -eq 'restart' -and -not $IsDetachedChild) { Invoke-Preflight }
    if ($IsDetachedChild) {
        # Detached child: give the launching session time to print and exit
        Start-Sleep -Seconds 2
    }
    elseif ($ForceDetach -or (Test-InsideServerTree)) {
        Invoke-Detached
        exit 0
    }
}

switch ($Action) {
    'list'    { Show-WarList }
    'status'  { Show-Status }
    'start'   { Start-Server }
    'stop'    { Stop-Server }
    'restart' { Restart-Server }
}

if ($IsDetachedChild) {
    try { Stop-Transcript | Out-Null } catch { }
}
