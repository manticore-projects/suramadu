#Requires -Version 5.1
<#
.SYNOPSIS
    Suramadu Server management script.
.DESCRIPTION
    Start, stop, restart, check status, or list available versions.
.PARAMETER Action
    The action: start, stop, restart, status, list, or help.
.PARAMETER Version
    Optional: specific WAR version to use (e.g. 26.0). Default: latest.
.EXAMPLE
    .\run.ps1 start
    .\run.ps1 -Version 26.0 start
    .\run.ps1 list
    .\run.ps1 stop
#>

param(
    [Parameter(Position = 0)]
    [ValidateSet("start", "stop", "restart", "status", "list", "help")]
    [string]$Action = "help",

    [Alias("v")]
    [string]$Version = ""
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

# ── Configuration ────────────────────────────────────────────────────────

$App         = "Suramadu"
$WarPrefix   = "suramadu-server"
$Base        = Split-Path -Parent $MyInvocation.MyCommand.Path
$MinJava     = 13
$LogDir      = Join-Path $Base "logs"
$PidFile     = Join-Path $LogDir "$App.pid"
$LogFile     = Join-Path $LogDir "$App.log"

# Set JAVA_HOME here or rely on the environment variable
# $env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21"

$Logging = "-Djava.util.logging.config.file=logging.properties"

$JavaOpts = @(
    "-Dwebswing.websocketMessageSizeLimit=512000"
    "--add-modules=java.desktop"
    "--add-exports=java.desktop/sun.awt=ALL-UNNAMED"
    "--add-exports=java.desktop/sun.awt.dnd=ALL-UNNAMED"
    "--add-exports=java.desktop/sun.awt.datatransfer=ALL-UNNAMED"
    "--add-exports=java.desktop/sun.awt.image=ALL-UNNAMED"
    "--add-exports=java.desktop/sun.java2d=ALL-UNNAMED"
    "--add-exports=java.desktop/sun.java2d.pipe=ALL-UNNAMED"
    "--add-exports=java.desktop/sun.java2d.loops=ALL-UNNAMED"
    "--add-exports=java.desktop/sun.font=ALL-UNNAMED"
    "--add-exports=java.desktop/sun.print=ALL-UNNAMED"
    "--add-exports=java.desktop/java.awt.peer=ALL-UNNAMED"
    "--add-exports=java.desktop/java.awt.dnd=ALL-UNNAMED"
    "--add-exports=java.base/sun.nio.cs=ALL-UNNAMED"
    "--add-opens=java.desktop/sun.awt.image=ALL-UNNAMED"
)

$SuramaduOpts = @("-h", "0.0.0.0", "-j", "jetty.properties")

# ── WAR Version Detection ───────────────────────────────────────────────

function Get-WarVersions {
    $wars = @()

    Get-ChildItem -Path $Base -Filter "$WarPrefix-*.war" -File | ForEach-Object {
        $ver = $_.BaseName -replace "^$([regex]::Escape($WarPrefix))-", ""
        # Skip non-numeric versions
        if ($ver -match '^\d+(\.\d+)*$') {
            $wars += [PSCustomObject]@{
                Version = [version]$ver
                VersionStr = $ver
                File = $_.FullName
                FileName = $_.Name
            }
        }
    }

    # Sort descending by version
    $wars | Sort-Object -Property Version -Descending
}

function Find-LatestWar {
    $wars = Get-WarVersions
    if ($wars.Count -gt 0) {
        return $wars[0]
    }

    # Fallback to unversioned WAR
    $unversioned = Join-Path $Base "$WarPrefix.war"
    if (Test-Path $unversioned) {
        return [PSCustomObject]@{
            Version = $null
            VersionStr = "(unversioned)"
            File = $unversioned
            FileName = "$WarPrefix.war"
        }
    }

    Write-Host ""
    Write-Host "ERROR: No WAR files found matching $WarPrefix-*.war in $Base" -ForegroundColor Red
    Write-Host ""
    Write-Host "  Expected files like:"
    Write-Host "    $WarPrefix-26.0.war"
    Write-Host "    $WarPrefix-20.2.5.war"
    Write-Host "    $WarPrefix.war"
    return $null
}

function Find-WarByVersion {
    param([string]$TargetVersion)

    $targetFile = Join-Path $Base "$WarPrefix-$TargetVersion.war"
    if (Test-Path $targetFile) {
        return [PSCustomObject]@{
            Version = [version]$TargetVersion
            VersionStr = $TargetVersion
            File = $targetFile
            FileName = "$WarPrefix-$TargetVersion.war"
        }
    }

    Write-Host "ERROR: WAR file not found: $targetFile" -ForegroundColor Red
    Write-Host ""
    Write-Host "  Available versions:" -ForegroundColor Cyan
    $wars = Get-WarVersions
    if ($wars.Count -eq 0) {
        Write-Host "    (none found)"
    } else {
        $wars | ForEach-Object { Write-Host "    $($_.VersionStr)" }
    }
    return $null
}

# ── Dependency Checks ────────────────────────────────────────────────────

function Find-Java {
    if ($env:JAVA_HOME -and (Test-Path "$env:JAVA_HOME\bin\java.exe")) {
        return "$env:JAVA_HOME\bin\java.exe"
    }

    $javaOnPath = Get-Command java.exe -ErrorAction SilentlyContinue
    if ($javaOnPath) {
        if (-not $env:JAVA_HOME) {
            Write-Host "WARN: JAVA_HOME not set, using java from PATH: $($javaOnPath.Source)" -ForegroundColor Yellow
        }
        return $javaOnPath.Source
    }

    Write-Host ""
    Write-Host "ERROR: Java not found." -ForegroundColor Red
    Write-Host ""
    Write-Host "  Install JDK 21 or later (recommended):" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "    winget install EclipseAdoptium.Temurin.21.JDK"
    Write-Host "    choco install temurin21"
    Write-Host "    scoop bucket add java && scoop install temurin21-jdk"
    Write-Host "    https://adoptium.net/"
    Write-Host ""
    return $null
}

function Test-JavaVersion {
    param([string]$JavaBin)

    $versionOutput = & $JavaBin -version 2>&1 | Select-Object -First 1
    if ($versionOutput -match '"(\d+)[\._]') {
        $major = [int]$Matches[1]
        if ($major -eq 1 -and $versionOutput -match '"1\.(\d+)') {
            $major = [int]$Matches[1]
        }
        if ($major -lt $MinJava) {
            Write-Host "ERROR: Java $major detected, but JDK $MinJava+ is required." -ForegroundColor Red
            return $false
        }
        Write-Host "Java $major detected."
        return $true
    }
    Write-Host "WARN: Could not determine Java version." -ForegroundColor Yellow
    return $true
}

# ── Helpers ──────────────────────────────────────────────────────────────

function Get-SavedPid {
    if (Test-Path $PidFile) {
        $pid = (Get-Content $PidFile -Raw).Trim()
        if ($pid -match '^\d+$') { return [int]$pid }
    }
    return $null
}

function Test-ServerRunning {
    $savedPid = Get-SavedPid
    if ($null -eq $savedPid) { return $false }
    $proc = Get-Process -Id $savedPid -ErrorAction SilentlyContinue
    return ($null -ne $proc -and $proc.ProcessName -eq "java")
}

function Write-Log {
    param([string]$Message)
    $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    Add-Content -Path $LogFile -Value "${timestamp}: $Message"
}

# ── Commands ─────────────────────────────────────────────────────────────

function Show-WarList {
    Write-Host ""
    Write-Host "==== Available $App versions in $Base"
    Write-Host ""

    $wars = Get-WarVersions
    if ($wars.Count -eq 0) {
        $unversioned = Join-Path $Base "$WarPrefix.war"
        if (Test-Path $unversioned) {
            Write-Host "    (unversioned)  $WarPrefix.war"
        } else {
            Write-Host "    (none found)"
        }
    } else {
        $first = $true
        foreach ($w in $wars) {
            $marker = if ($first) { " <- latest" } else { "" }
            Write-Host ("    {0,-12}  {1}{2}" -f $w.VersionStr, $w.FileName, $marker)
            $first = $false
        }
    }
    Write-Host ""
}

function Start-Server {
    if (Test-ServerRunning) {
        $savedPid = Get-SavedPid
        Write-Host "$App already running with PID [$savedPid]"
        return
    }

    $javaBin = Find-Java
    if (-not $javaBin) { exit 1 }
    if (-not (Test-JavaVersion $javaBin)) { exit 1 }

    # Resolve WAR
    if ($Version) {
        $war = Find-WarByVersion $Version
    } else {
        $war = Find-LatestWar
    }
    if (-not $war) { exit 1 }

    Write-Host "==== Starting $App v$($war.VersionStr)"
    Write-Host "  WAR: $($war.FileName)"

    if (-not (Test-Path $LogDir)) { New-Item -ItemType Directory -Path $LogDir | Out-Null }

    # Rotate log
    if (Test-Path $LogFile) {
        $timestamp = Get-Date -Format "yyyyMMddHHmmss"
        Copy-Item $LogFile "${LogFile}_${timestamp}"
        Clear-Content $LogFile
    }

    $allArgs = $JavaOpts + @($Logging, "-jar", $war.File) + $SuramaduOpts

    $proc = Start-Process -FilePath $javaBin `
        -ArgumentList $allArgs `
        -WorkingDirectory $Base `
        -RedirectStandardOutput $LogFile `
        -RedirectStandardError (Join-Path $LogDir "$App-error.log") `
        -WindowStyle Hidden `
        -PassThru

    $proc.Id | Out-File -FilePath $PidFile -Encoding ASCII -NoNewline

    Write-Host "Started with PID [$($proc.Id)]"
    Write-Log "$App v$($war.VersionStr) STARTED ($($war.FileName))"

    Start-Sleep -Seconds 2
    Write-Host ""
    Write-Host "Initial log output:" -ForegroundColor Cyan
    if (Test-Path $LogFile) {
        Get-Content $LogFile -Tail 20
    }
}

function Stop-Server {
    Write-Host "==== Stopping $App"

    $savedPid = Get-SavedPid

    if ($null -eq $savedPid) {
        Write-Host "No PID file found -- $App already stopped?"
        # Try to find orphans
        Get-Process java -ErrorAction SilentlyContinue |
            Where-Object { $_.CommandLine -like "*$WarPrefix*" } |
            ForEach-Object {
                Write-Host "Found running process with PID [$($_.Id)], stopping..."
                Stop-Process -Id $_.Id -Force -ErrorAction SilentlyContinue
            }
        return
    }

    $proc = Get-Process -Id $savedPid -ErrorAction SilentlyContinue
    if ($null -eq $proc) {
        Write-Host "Process [$savedPid] not running"
        Remove-Item -Force $PidFile -ErrorAction SilentlyContinue
        return
    }

    Write-Host "Stopping PID [$savedPid]..."
    Stop-Process -Id $savedPid -ErrorAction SilentlyContinue

    $count = 0
    while ($count -lt 30) {
        $proc = Get-Process -Id $savedPid -ErrorAction SilentlyContinue
        if ($null -eq $proc) { break }
        Start-Sleep -Seconds 1
        $count++
    }

    $proc = Get-Process -Id $savedPid -ErrorAction SilentlyContinue
    if ($null -ne $proc) {
        Write-Host "Process did not stop gracefully -- force killing" -ForegroundColor Yellow
        Stop-Process -Id $savedPid -Force -ErrorAction SilentlyContinue
    }

    Remove-Item -Force $PidFile -ErrorAction SilentlyContinue
    Write-Log "$App STOPPED"
    Write-Host "$App stopped."
}

function Restart-Server {
    Stop-Server
    Write-Host "Sleeping..."
    Start-Sleep -Seconds 3
    Start-Server
}

function Show-Status {
    Write-Host ""
    Write-Host "==== Status of $App"

    if (Test-ServerRunning) {
        $savedPid = Get-SavedPid
        $proc = Get-Process -Id $savedPid -ErrorAction SilentlyContinue
        Write-Host ""
        Write-Host "$App is running with PID [$savedPid]" -ForegroundColor Green
        Write-Host "  CPU time:   $($proc.TotalProcessorTime)"
        Write-Host "  Memory:     $([math]::Round($proc.WorkingSet64 / 1MB, 1)) MB"
        Write-Host "  Start time: $($proc.StartTime)"
    } elseif (Test-Path $PidFile) {
        Write-Host ""
        Write-Host "$App is NOT running, but stale PID file found" -ForegroundColor Yellow
        Remove-Item -Force $PidFile -ErrorAction SilentlyContinue
    } else {
        Write-Host ""
        Write-Host "$App is NOT running"
    }
}

function Show-Help {
    Write-Host ""
    Write-Host "$App Server" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Usage:"
    Write-Host "    .\run.ps1 { start | stop | restart | status | list }"
    Write-Host "    .\run.ps1 -Version 26.0 start"
    Write-Host "    .\run.ps1 help"
    Write-Host ""
}

# ── Main ─────────────────────────────────────────────────────────────────

Set-Location $Base

switch ($Action) {
    "start"   { Start-Server }
    "stop"    { Stop-Server }
    "restart" { Restart-Server }
    "status"  { Show-Status }
    "list"    { Show-WarList }
    "help"    { Show-Help }
}
