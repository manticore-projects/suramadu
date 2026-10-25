#!/usr/bin/env fish
# Open the Suramadu benchmark in a browser with a REPRODUCIBLE window:
# fixed size, device-scale-factor 1, dedicated clean profile (no extensions).
#
# Usage:
#   ./bench-browser.fish chromium [width height]
#   ./bench-browser.fish firefox  [width height]
#
# Compare benchmark CSVs only across runs made with the SAME browser and the
# same size. The effective canvas size is what the child JVM was told — verify
# in the server log's child launch line: -Dwebswing.screenWidth / screenHeight
# must match between the runs you compare.

set -l url "http://localhost:8080/benchmark/"
set -l browser $argv[1]
set -l w 1600
set -l h 1000
if test (count $argv) -ge 3
    set w $argv[2]
    set h $argv[3]
end

switch "$browser"
    case chromium
        # Dedicated user-data-dir => own process, so --window-size is honored
        # even when another Chromium is already running; also: no extensions.
        set -l profile /tmp/suramadu-bench-chromium
        chromium \
            --user-data-dir=$profile \
            --no-first-run \
            --disable-extensions \
            --force-device-scale-factor=1 \
            --window-position=0,0 \
            --window-size=$w,$h \
            --new-window "$url"

    case firefox
        # Dedicated profile; create it on first use. DPR is pinned via user.js.
        set -l profile /tmp/suramadu-bench-firefox
        if not test -d $profile
            mkdir -p $profile
            printf '%s\n' \
                'user_pref("layout.css.devPixelsPerPx", "1.0");' \
                'user_pref("browser.sessionstore.resume_from_crash", false);' \
                'user_pref("browser.shell.checkDefaultBrowser", false);' \
                'user_pref("datareporting.policy.firstRunURL", "");' \
                > $profile/user.js
        end
        firefox \
            --profile $profile \
            --new-instance \
            --width $w --height $h \
            "$url"

    case '*'
        echo "usage: bench-browser.fish chromium|firefox [width height]" >&2
        exit 2
end
