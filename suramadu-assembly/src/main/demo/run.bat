@echo off
rem Thin launcher for run.ps1. All logic (version detection, preflight, detached
rem restart, GC selection, ...) lives in run.ps1; a batch file cannot do it reliably.
rem Same command line as run.sh:
rem     run.bat { start ^| stop ^| restart ^| status ^| list }  [-v VERSION]  [-d]  [-h]
setlocal

set "PS=%SystemRoot%\System32\WindowsPowerShell\v1.0\powershell.exe"
if not exist "%PS%" (
    where pwsh.exe >nul 2>&1
    if errorlevel 1 (
        echo ERROR: PowerShell not found. Windows PowerShell 5.1 ^(Windows 10 / Server 2016 or later^) or PowerShell 7 is required.
        exit /b 1
    )
    set "PS=pwsh.exe"
)

"%PS%" -NoProfile -ExecutionPolicy Bypass -File "%~dp0run.ps1" %*
exit /b %errorlevel%
