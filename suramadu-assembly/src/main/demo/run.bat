@echo off
setlocal enabledelayedexpansion

set "APP=Suramadu"
set "WAR_PREFIX=suramadu-server"
set "BASE=%~dp0"
set "BASE=%BASE:~0,-1%"
set "MIN_JAVA_VERSION=13"
set "LOGGING=-Djava.util.logging.config.file=logging.properties"

rem ── Java Configuration ─────────────────────────────────────────────────
rem Uncomment and adjust if needed:
rem set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21"

set JAVA_OPTS=-server -Xmx4G -Xms2G -Xss228k
set JAVA_OPTS=%JAVA_OPTS% -XX:+UseG1GC -XX:+UseStringDeduplication
set JAVA_OPTS=%JAVA_OPTS% -XX:MinHeapFreeRatio=10 -XX:MaxHeapFreeRatio=15 -XX:GCTimeRatio=6
set JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.jndi.ldap.object.disableEndpointIdentification=true
set JAVA_OPTS=%JAVA_OPTS% -Dwebswing.websocketMessageSizeLimit=512000
set JAVA_OPTS=%JAVA_OPTS% -Duser.country=US -Duser.language=en
set JAVA_OPTS=%JAVA_OPTS% -Doracle.jdbc.timezoneAsRegion=false
set JAVA_OPTS=%JAVA_OPTS% --add-modules=java.desktop
set JAVA_OPTS=%JAVA_OPTS% --add-exports=java.desktop/sun.awt=ALL-UNNAMED
set JAVA_OPTS=%JAVA_OPTS% --add-exports=java.desktop/sun.awt.dnd=ALL-UNNAMED
set JAVA_OPTS=%JAVA_OPTS% --add-exports=java.desktop/sun.awt.datatransfer=ALL-UNNAMED
set JAVA_OPTS=%JAVA_OPTS% --add-exports=java.desktop/sun.awt.image=ALL-UNNAMED
set JAVA_OPTS=%JAVA_OPTS% --add-exports=java.desktop/sun.java2d=ALL-UNNAMED
set JAVA_OPTS=%JAVA_OPTS% --add-exports=java.desktop/sun.java2d.pipe=ALL-UNNAMED
set JAVA_OPTS=%JAVA_OPTS% --add-exports=java.desktop/sun.java2d.loops=ALL-UNNAMED
set JAVA_OPTS=%JAVA_OPTS% --add-exports=java.desktop/sun.font=ALL-UNNAMED
set JAVA_OPTS=%JAVA_OPTS% --add-exports=java.desktop/sun.print=ALL-UNNAMED
set JAVA_OPTS=%JAVA_OPTS% --add-exports=java.desktop/java.awt.peer=ALL-UNNAMED
set JAVA_OPTS=%JAVA_OPTS% --add-exports=java.desktop/java.awt.dnd=ALL-UNNAMED
set JAVA_OPTS=%JAVA_OPTS% --add-exports=java.base/sun.nio.cs=ALL-UNNAMED
set JAVA_OPTS=%JAVA_OPTS% --add-opens=java.desktop/sun.awt.image=ALL-UNNAMED

set "SURAMADU_OPTS=-h 0.0.0.0 -j jetty.properties"
set "PID_FILE=%BASE%\logs\%APP%.pid"
set "LOG_FILE=%BASE%\logs\%APP%.log"

rem ── Parse Arguments ────────────────────────────────────────────────────

set "REQUESTED_VERSION="
set "ACTION="

:parse_args
if "%~1"=="" goto :args_done
if "%~1"=="-h" goto :show_help
if "%~1"=="--help" goto :show_help
if "%~1"=="-v" (
    if "%~2"=="" (
        echo ERROR: -v requires a version argument
        exit /b 1
    )
    set "REQUESTED_VERSION=%~2"
    shift
    shift
    goto :parse_args
)
if "%~1"=="--version" (
    if "%~2"=="" (
        echo ERROR: --version requires a version argument
        exit /b 1
    )
    set "REQUESTED_VERSION=%~2"
    shift
    shift
    goto :parse_args
)
if "%~1"=="start" set "ACTION=start"
if "%~1"=="stop" set "ACTION=stop"
if "%~1"=="restart" set "ACTION=restart"
if "%~1"=="status" set "ACTION=status"
if "%~1"=="list" set "ACTION=list"
if not defined ACTION (
    echo Unknown argument: %~1
    goto :usage
)
shift
goto :parse_args

:args_done
if not defined ACTION goto :usage

if "%ACTION%"=="list" goto :cmd_list
if "%ACTION%"=="status" goto :cmd_status
if "%ACTION%"=="start" goto :cmd_start
if "%ACTION%"=="stop" goto :cmd_stop
if "%ACTION%"=="restart" goto :cmd_restart
goto :usage

rem ── WAR Version Detection ──────────────────────────────────────────────

:find_latest_war
    set "WAR_FILE="
    set "WAR_VERSION="
    set "LATEST_VER="

    for %%f in ("%BASE%\%WAR_PREFIX%-*.war") do (
        set "fname=%%~nf"
        rem Strip prefix to get version
        set "ver=!fname:%WAR_PREFIX%-=!"
        call :compare_and_set "!ver!" "%%f"
    )

    rem Fallback to unversioned WAR
    if not defined WAR_FILE (
        if exist "%BASE%\%WAR_PREFIX%.war" (
            set "WAR_FILE=%BASE%\%WAR_PREFIX%.war"
            set "WAR_VERSION=(unversioned)"
        )
    )

    if not defined WAR_FILE (
        echo.
        echo ERROR: No WAR files found matching %WAR_PREFIX%-*.war in %BASE%
        echo.
        echo   Expected files like:
        echo     %WAR_PREFIX%-26.0.war
        echo     %WAR_PREFIX%-20.2.5.war
        echo     %WAR_PREFIX%.war
        exit /b 1
    )
    goto :eof

:compare_and_set
    set "candidate_ver=%~1"
    set "candidate_file=%~2"
    if not defined LATEST_VER (
        set "LATEST_VER=%candidate_ver%"
        set "WAR_FILE=%candidate_file%"
        set "WAR_VERSION=%candidate_ver%"
        goto :eof
    )
    rem Simple string comparison — works for same-length versions
    rem For robust comparison, use PowerShell script instead
    if "%candidate_ver%" gtr "!LATEST_VER!" (
        set "LATEST_VER=%candidate_ver%"
        set "WAR_FILE=%candidate_file%"
        set "WAR_VERSION=%candidate_ver%"
    )
    goto :eof

:find_war_by_version
    set "target_war=%BASE%\%WAR_PREFIX%-%REQUESTED_VERSION%.war"
    if exist "%target_war%" (
        set "WAR_FILE=%target_war%"
        set "WAR_VERSION=%REQUESTED_VERSION%"
        goto :eof
    )
    echo ERROR: WAR file not found: %target_war%
    echo.
    echo   Available versions:
    call :do_list_wars
    exit /b 1

:do_list_wars
    set "found=0"
    for %%f in ("%BASE%\%WAR_PREFIX%-*.war") do (
        set "fname=%%~nf"
        set "ver=!fname:%WAR_PREFIX%-=!"
        echo     !ver!
        set "found=1"
    )
    if exist "%BASE%\%WAR_PREFIX%.war" (
        echo     ^(unversioned^)
        set "found=1"
    )
    if "%found%"=="0" echo     ^(none found^)
    goto :eof

rem ── Dependency Checks ──────────────────────────────────────────────────

:check_java
    set "JAVA_BIN="
    if defined JAVA_HOME (
        if exist "%JAVA_HOME%\bin\java.exe" (
            set "JAVA_BIN=%JAVA_HOME%\bin\java.exe"
            goto :check_java_version
        )
    )
    where java.exe >nul 2>&1
    if %errorlevel% equ 0 (
        for /f "tokens=*" %%i in ('where java.exe') do (
            set "JAVA_BIN=%%i"
            goto :check_java_path_found
        )
    )
    echo.
    echo ERROR: Java not found.
    echo.
    echo   Install JDK 21 or later:
    echo.
    echo     winget install EclipseAdoptium.Temurin.21.JDK
    echo     choco install temurin21
    echo     https://adoptium.net/
    echo.
    exit /b 1

:check_java_path_found
    if not defined JAVA_HOME echo WARN: JAVA_HOME not set, using java from PATH: %JAVA_BIN%

:check_java_version
    for /f "tokens=3 delims= " %%v in ('"%JAVA_BIN%" -version 2^>^&1 ^| findstr /i "version"') do (
        set "JAVA_VER_RAW=%%~v"
        goto :parse_java_ver
    )
    echo WARN: Could not determine Java version.
    goto :eof

:parse_java_ver
    set "JAVA_VER_RAW=%JAVA_VER_RAW:"=%"
    for /f "tokens=1 delims=." %%m in ("%JAVA_VER_RAW%") do set "JAVA_MAJOR=%%m"
    if "%JAVA_MAJOR%"=="1" (
        for /f "tokens=2 delims=." %%m in ("%JAVA_VER_RAW%") do set "JAVA_MAJOR=%%m"
    )
    if %JAVA_MAJOR% lss %MIN_JAVA_VERSION% (
        echo ERROR: Java %JAVA_MAJOR% detected, but JDK %MIN_JAVA_VERSION%+ is required.
        exit /b 1
    )
    echo Java %JAVA_MAJOR% detected.
    goto :eof

rem ── Commands ───────────────────────────────────────────────────────────

:cmd_list
    echo.
    echo ==== Available %APP% versions in %BASE%
    echo.
    call :do_list_wars

    call :find_latest_war 2>nul
    if defined WAR_VERSION (
        echo.
        echo   Latest: %WAR_VERSION%
    )
    echo.
    goto :eof

:cmd_start
    call :check_running
    if %errorlevel% equ 0 (
        for /f "tokens=*" %%p in (%PID_FILE%) do echo %APP% already running with PID [%%p]
        goto :eof
    )

    call :check_java
    if %errorlevel% neq 0 exit /b 1

    rem Resolve WAR
    if defined REQUESTED_VERSION (
        call :find_war_by_version
    ) else (
        call :find_latest_war
    )
    if %errorlevel% neq 0 exit /b 1

    echo ==== Starting %APP% v%WAR_VERSION%
    echo   WAR: %WAR_FILE%
    if not exist "%BASE%\logs" mkdir "%BASE%\logs"

    rem Rotate log
    if exist "%LOG_FILE%" (
        for /f "tokens=1-3 delims=/ " %%a in ("%date%") do set "DATESTAMP=%%c%%a%%b"
        for /f "tokens=1-3 delims=:." %%a in ("%time: =0%") do set "TIMESTAMP=%%a%%b%%c"
        copy "%LOG_FILE%" "%LOG_FILE%_!DATESTAMP!!TIMESTAMP!" >nul 2>&1
        type nul > "%LOG_FILE%"
    )

    start "%APP%" /b /min cmd /c ""%JAVA_BIN%" %JAVA_OPTS% %LOGGING% -jar "%WAR_FILE%" %SURAMADU_OPTS% >> "%LOG_FILE%" 2>&1"

    timeout /t 2 /nobreak >nul

    set "SERVER_PID="
    for /f %%p in ('powershell -NoProfile -Command "(Get-Process java -ErrorAction SilentlyContinue | Where-Object {$_.CommandLine -like '*suramadu-server*'})[0].Id" 2^>nul') do (
        set "SERVER_PID=%%p"
    )

    if defined SERVER_PID (
        echo !SERVER_PID!> "%PID_FILE%"
        echo Started with PID [!SERVER_PID!]
        echo %date% %time%: %APP% v%WAR_VERSION% STARTED >> "%LOG_FILE%"
    ) else (
        echo Started ^(could not determine PID^)
        echo %date% %time%: %APP% v%WAR_VERSION% STARTED >> "%LOG_FILE%"
    )

    echo.
    echo Tailing log ^(Ctrl+C to stop tailing, server continues running^):
    echo.
    timeout /t 1 /nobreak >nul
    type "%LOG_FILE%"
    goto :eof

:cmd_stop
    echo ==== Stopping %APP%

    if not exist "%PID_FILE%" (
        echo No PID file found -- %APP% already stopped?
        for /f %%p in ('powershell -NoProfile -Command "(Get-Process java -ErrorAction SilentlyContinue | Where-Object {$_.CommandLine -like '*suramadu-server*'}).Id" 2^>nul') do (
            echo Found running process with PID [%%p], stopping...
            taskkill /pid %%p /t >nul 2>&1
        )
        goto :eof
    )

    set /p SERVER_PID=<"%PID_FILE%"
    for /f "tokens=*" %%p in ("%SERVER_PID%") do set "SERVER_PID=%%p"

    echo Stopping PID [%SERVER_PID%]...
    taskkill /pid %SERVER_PID% >nul 2>&1

    set "COUNT=0"
:stop_wait
    tasklist /fi "pid eq %SERVER_PID%" 2>nul | findstr /i "java" >nul 2>&1
    if %errorlevel% neq 0 goto :stop_done
    if %COUNT% geq 30 goto :stop_force
    timeout /t 1 /nobreak >nul
    set /a COUNT+=1
    goto :stop_wait

:stop_force
    echo Process did not stop gracefully -- force killing
    taskkill /pid %SERVER_PID% /f /t >nul 2>&1

:stop_done
    del /f "%PID_FILE%" >nul 2>&1
    echo %date% %time%: %APP% STOPPED >> "%LOG_FILE%"
    echo %APP% stopped.
    goto :eof

:cmd_restart
    call :cmd_stop
    echo Sleeping...
    timeout /t 3 /nobreak >nul
    goto :cmd_start

:cmd_status
    echo.
    echo ==== Status of %APP%
    call :check_running
    if %errorlevel% equ 0 (
        set /p SERVER_PID=<"%PID_FILE%"
        echo.
        echo %APP% is running with PID [!SERVER_PID!]
        tasklist /fi "pid eq !SERVER_PID!" 2>nul
    ) else if exist "%PID_FILE%" (
        echo.
        echo %APP% is NOT running, but stale PID file found
        del /f "%PID_FILE%" >nul 2>&1
    ) else (
        echo.
        echo %APP% is NOT running
    )
    goto :eof

rem ── Helpers ────────────────────────────────────────────────────────────

:check_running
    if not exist "%PID_FILE%" exit /b 1
    set /p CHECK_PID=<"%PID_FILE%"
    for /f "tokens=*" %%p in ("%CHECK_PID%") do set "CHECK_PID=%%p"
    tasklist /fi "pid eq %CHECK_PID%" 2>nul | findstr /i "java" >nul 2>&1
    exit /b %errorlevel%

:show_help
    echo.
    echo %APP% Server
    echo.
    echo Usage:
    echo     %~nx0 { start ^| stop ^| restart ^| status ^| list }
    echo     %~nx0 -v VERSION { start ^| restart }
    echo     %~nx0 -h ^| --help
    echo.
    echo Options:
    echo     -v VERSION    Use a specific WAR version ^(e.g. -v 26.0^)
    echo                   Default: auto-detect latest version
    echo.
    goto :eof

:usage
    echo No action specified.
    call :show_help
    exit /b 1
