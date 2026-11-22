#!/bin/sh

set -eu
unset DISPLAY XAUTHORITY   # headless: no X11 needed; don't leak inherited state into child JVMs

APP="Suramadu"
WAR_PREFIX="suramadu-server"
BASE="$(cd "$(dirname "$0")" && pwd -P)"

# Config directory (always absolute): ~/.manticore if it holds a Suramadu config
# (legacy layout), otherwise the installation directory.
CONFIG_DIR="${BASE}"
if [ -f "${HOME}/.manticore/suramadu.config" ] || [ -f "${HOME}/.manticore/webswing.config" ]; then
    CONFIG_DIR="${HOME}/.manticore"
fi

# JDK: first of bundled (next to the installation), distro default runtime, distro default.
# If none exists, check_java falls back to whatever "java" is on PATH.
JAVA_HOME=""
for cand in "${BASE}/../java" /usr/lib/jvm/default-runtime /usr/lib/jvm/default; do
    if [ -x "${cand}/bin/java" ]; then
        JAVA_HOME="${cand}"
        break
    fi
done
JAVA_HOME="${JAVA_HOME:-${BASE}/../java}"
java_bin="${JAVA_HOME}/bin/java"   # may be overridden by check_java

# JUL config: next to the Suramadu config, else ~/.manticore; none = JDK defaults
LOGGING=""
for cand in "${CONFIG_DIR}/logging.properties" "${HOME}/.manticore/logging.properties"; do
    if [ -f "${cand}" ]; then
        LOGGING="-Djava.util.logging.config.file=${cand}"
        break
    fi
done

MIN_JAVA_VERSION=17
PRELOAD="LD_PRELOAD=${BASE}/dist/ext-lib/libjemalloc.so.2"
MALLOC_CONF="MALLOC_CONF=background_thread:true,metadata_thp:auto,dirty_decay_ms:5000,muzzy_decay_ms:5000,tcache_max:16384,narenas:4"

# SSL certificates to import into the JVM truststore on startup.
# Format: alias|host|port (one per line)
# Add new certificates by appending lines.
SSL_CERTS="
"
# Empty = cacerts of the resolved JAVA_HOME (also valid after the system-java fallback)
SSL_KEYSTORE=""
SSL_KEYSTORE_PASS="changeit"

# Server JVM flags (GC flags are added by check_java). -Xms is the floor idle uncommit can reach.
JAVA_OPTS="\
 -server -Xmx2G -Xms64m -Xss228k \
 -XX:+ExitOnOutOfMemoryError \
 -XX:+HeapDumpOnOutOfMemoryError \
 -XX:HeapDumpPath=${BASE}/logs/ \
 -XX:ErrorFile=${BASE}/logs/hs_err_%p.log \
 -Dcom.sun.jndi.ldap.object.disableEndpointIdentification=true \
 -Dwebswing.websocketMessageSizeLimit=1024000 \
 -Djsse.enableSNIExtension=false \
 --add-modules=java.desktop \
 --add-exports=java.desktop/sun.awt=ALL-UNNAMED \
 --add-exports=java.desktop/sun.awt.dnd=ALL-UNNAMED \
 --add-exports=java.desktop/sun.awt.datatransfer=ALL-UNNAMED \
 --add-exports=java.desktop/sun.awt.image=ALL-UNNAMED \
 --add-exports=java.desktop/sun.java2d=ALL-UNNAMED \
 --add-exports=java.desktop/sun.java2d.pipe=ALL-UNNAMED \
 --add-exports=java.desktop/sun.java2d.loops=ALL-UNNAMED \
 --add-exports=java.desktop/sun.font=ALL-UNNAMED \
 --add-exports=java.desktop/sun.print=ALL-UNNAMED \
 --add-exports=java.desktop/java.awt.peer=ALL-UNNAMED \
 --add-exports=java.desktop/java.awt.dnd=ALL-UNNAMED \
 --add-exports=java.base/sun.nio.cs=ALL-UNNAMED \
 --add-opens=java.desktop/sun.awt.image=ALL-UNNAMED \
"

# Window theme. Set to "" for the neutral default theme.
BRANDING_OPTS="\
 -Dorg.webswing.window.theme.primaryColor=0xd70900 \
 -Dorg.webswing.window.theme.secondaryColor=0xd70900 \
 -Dorg.webswing.window.theme.contentBgColor=0xffffff \
"

# Prefer the current configuration file names; fall back to the legacy ones so that
# installations created before the rename keep working untouched.
CONFIG_FILE="${CONFIG_DIR}/suramadu.config"
[ -f "${CONFIG_FILE}" ] || CONFIG_FILE="${CONFIG_DIR}/webswing.config"
PROPS_FILE="${CONFIG_DIR}/suramadu.properties"
[ -f "${PROPS_FILE}" ] || PROPS_FILE="${CONFIG_DIR}/webswing.properties"
JETTY_FILE="${CONFIG_DIR}/jetty.properties"

SURAMADU_OPTS="-h 0.0.0.0 -j ${JETTY_FILE} -c ${CONFIG_FILE} -pf ${PROPS_FILE}"
NICE_LEVEL=10
LOG_RETENTION_DAYS=10

PID_FILE="${BASE}/logs/${APP}.pid"
TAIL_PID_FILE="${BASE}/logs/${APP}-tail.pid"
LOG_FILE="${BASE}/logs/${APP}.log"
DETACH_LOG="${BASE}/logs/${APP}-restart.log"

USAGE="$(basename "$0") -- Suramadu Server

Usage:
    $(basename "$0") { start | stop | restart | status | list }
    $(basename "$0") -v VERSION { start | restart }
    $(basename "$0") -d { stop | restart }
    $(basename "$0") -h | --help

Options:
    -v VERSION    Use a specific WAR version (e.g. -v 26.0)
                  Default: auto-detect latest version
    -d            Force detached mode for stop/restart (see below)

Commands:
    start         Start the server
    stop          Stop the server
    restart       Stop + Start
    status        Check if the server is running
    list          List all available WAR versions

Detached mode:
    stop/restart are automatically detached when invoked from inside a
    ${APP} session (e.g. the Jediterm shell). The script re-launches itself
    as an orphaned daemon with a clean environment, so it survives the
    server going down. Your session WILL drop; reconnect after ~30-60 s.
    Output goes to logs/${APP}-restart.log"

# ── WAR Version Detection ───────────────────────────────────────────────

# Compare two version strings: returns 0 if $1 >= $2, 1 otherwise
# Handles: 20.2.5 vs 26.0, 26.0 vs 26.0.1, etc.
version_ge() {
    v1="$1"
    v2="$2"

    # Compare segment by segment
    while true; do
        # Extract first segment
        seg1="${v1%%.*}"
        seg2="${v2%%.*}"

        # Default empty to 0
        seg1="${seg1:-0}"
        seg2="${seg2:-0}"

        if [ "${seg1}" -gt "${seg2}" ] 2>/dev/null; then
            return 0
        elif [ "${seg1}" -lt "${seg2}" ] 2>/dev/null; then
            return 1
        fi

        # Move to next segment
        case "${v1}" in
            *.*) v1="${v1#*.}" ;;
            *)   v1="" ;;
        esac
        case "${v2}" in
            *.*) v2="${v2#*.}" ;;
            *)   v2="" ;;
        esac

        # Both exhausted — equal
        if [ -z "${v1}" ] && [ -z "${v2}" ]; then
            return 0
        fi
    done
}

# Find all WAR files and extract versions
list_wars() {
    found=0
    for war in "${BASE}/${WAR_PREFIX}"-*.war; do
        [ -f "${war}" ] || continue
        filename=$(basename "${war}")
        ver="${filename#"${WAR_PREFIX}"-}"
        ver="${ver%.war}"
        echo "${ver}"
        found=1
    done

    # Also check for unversioned WAR
    if [ -f "${BASE}/${WAR_PREFIX}.war" ]; then
        echo "(unversioned)"
        found=1
    fi

    if [ "${found}" -eq 0 ]; then
        return 1
    fi
    return 0
}

# Find the latest versioned WAR file
find_latest_war() {
    latest_ver=""
    latest_war=""

    for war in "${BASE}/${WAR_PREFIX}"-*.war; do
        [ -f "${war}" ] || continue
        filename=$(basename "${war}")
        ver="${filename#"${WAR_PREFIX}"-}"
        ver="${ver%.war}"

        # Skip non-numeric versions (e.g. SNAPSHOT)
        case "${ver}" in
            *[!0-9.]*) continue ;;
        esac

        if [ -z "${latest_ver}" ] || version_ge "${ver}" "${latest_ver}"; then
            latest_ver="${ver}"
            latest_war="${war}"
        fi
    done

    # Fallback to unversioned WAR
    if [ -z "${latest_war}" ] && [ -f "${BASE}/${WAR_PREFIX}.war" ]; then
        latest_war="${BASE}/${WAR_PREFIX}.war"
        latest_ver="(unversioned)"
    fi

    if [ -z "${latest_war}" ]; then
        echo "ERROR: No WAR files found matching ${WAR_PREFIX}-*.war in ${BASE}" >&2
        echo "" >&2
        echo "  Expected files like:" >&2
        echo "    ${WAR_PREFIX}-26.0.war" >&2
        echo "    ${WAR_PREFIX}-20.2.5.war" >&2
        echo "    ${WAR_PREFIX}.war" >&2
        return 1
    fi

    WAR_FILE="${latest_war}"
    WAR_VERSION="${latest_ver}"
    return 0
}

# Resolve a specific version to a WAR file
find_war_by_version() {
    target_ver="$1"
    target_war="${BASE}/${WAR_PREFIX}-${target_ver}.war"

    if [ -f "${target_war}" ]; then
        WAR_FILE="${target_war}"
        WAR_VERSION="${target_ver}"
        return 0
    fi

    echo "ERROR: WAR file not found: ${target_war}" >&2
    echo "" >&2
    echo "  Available versions:" >&2
    list_wars | sed 's/^/    /' >&2
    return 1
}

# ── Dependency Checks ────────────────────────────────────────────────────

detect_distro() {
    if [ -f /etc/os-release ]; then
        # shellcheck disable=SC1091
        . /etc/os-release
        echo "${ID:-unknown}"
    elif [ -f /etc/redhat-release ]; then
        echo "rhel"
    elif [ -f /etc/debian_version ]; then
        echo "debian"
    else
        echo "unknown"
    fi
}

check_java() {
    # Sets the globals java_bin and (on fallback) JAVA_HOME; cmd_start and
    # import_ssl_certs use them so the fallback is actually honoured.
    # A detached child inherits the JDK already validated by its parent
    # (its PATH is minimal, so re-resolving "java" could differ).
    if [ -n "${SURAMADU_JAVA_HOME:-}" ] && [ -x "${SURAMADU_JAVA_HOME}/bin/java" ]; then
        JAVA_HOME="${SURAMADU_JAVA_HOME}"
    fi
    java_bin="${JAVA_HOME}/bin/java"

    if [ ! -x "${java_bin}" ]; then
        if command -v java > /dev/null 2>&1; then
            java_real=$(readlink -f "$(command -v java)" 2>/dev/null || true)
            java_bin="${java_real:-$(command -v java)}"
            echo "WARN: JAVA_HOME (${JAVA_HOME}) not found, using system java (${java_bin})."
            # Derive the JDK home so keytool / cacerts resolve to the same install
            fallback_home="${java_bin%/bin/java}"
            if [ "${fallback_home}" != "${java_bin}" ] && [ -d "${fallback_home}" ]; then
                JAVA_HOME="${fallback_home}"
            fi
        else
            distro=$(detect_distro)
            echo "ERROR: Java not found."
            echo ""
            echo "  JAVA_HOME is set to: ${JAVA_HOME}"
            echo "  Neither ${java_bin} nor 'java' on PATH exist."
            echo ""
            echo "  Install a JDK 21+ (recommended) for your distribution:"
            echo ""
            case "${distro}" in
                arch|manjaro)
                    echo "    sudo pacman -S jdk21-openjdk" ;;
                ubuntu|debian|linuxmint|pop)
                    echo "    sudo apt update && sudo apt install openjdk-21-jdk" ;;
                fedora)
                    echo "    sudo dnf install java-21-openjdk-devel" ;;
                rhel|centos|rocky|alma|ol)
                    echo "    sudo yum install java-21-openjdk-devel" ;;
                opensuse*|sles)
                    echo "    sudo zypper install java-21-openjdk-devel" ;;
                alpine)
                    echo "    apk add openjdk21" ;;
                *)
                    echo "    Visit https://adoptium.net/ to download Eclipse Temurin JDK 21+" ;;
            esac
            echo ""
            exit 1
        fi
    fi

    java_version_output=$("${java_bin}" -version 2>&1 | grep -m1 ' version "' || true)
    java_version=$(echo "${java_version_output}" | sed -n 's/.*version "\([0-9]*\).*/\1/p')

    if [ -z "${java_version}" ]; then
        echo "WARN: Could not determine Java version from: ${java_version_output}"
    elif [ "${java_version}" -lt "${MIN_JAVA_VERSION}" ] 2>/dev/null; then
        echo "ERROR: Java ${java_version} detected, but JDK ${MIN_JAVA_VERSION}+ is required."
        echo "  Detected: ${java_version_output}"
        exit 1
    else
        echo "Java ${java_version} detected."
    fi

    # GC: Shenandoah (very low pauses, compressed oops, uncommits idle memory) where the
    # JDK includes it (not in Oracle builds); otherwise G1, told to shrink the heap when idle.
    # Single-generation Shenandoah on purpose: generational mode is not wanted yet.
    if "${java_bin}" -XX:+UseShenandoahGC -version > /dev/null 2>&1; then
        GC_OPTS="-XX:+UseShenandoahGC"
    else
        echo "WARN: this JDK has no Shenandoah, falling back to G1."
        GC_OPTS="-XX:+UseG1GC -XX:G1PeriodicGCInterval=60000 -XX:MinHeapFreeRatio=10 -XX:MaxHeapFreeRatio=30"
    fi
    FULL_OPTS="${JAVA_OPTS} ${GC_OPTS} ${BRANDING_OPTS}"
}

# Everything that can make a (re)start fail, checked BEFORE the running
# instance is touched. Exits non-zero (and prints why) if the start would fail.
preflight() {
    check_java

    if [ -n "${REQUESTED_VERSION}" ]; then
        find_war_by_version "${REQUESTED_VERSION}" || exit 1
    else
        find_latest_war || exit 1
    fi

    # Dry-run the JVM with the exact start flags: catches options this JDK rejects.
    # shellcheck disable=SC2086
    if ! jvm_out=$("${java_bin}" ${FULL_OPTS} -version 2>&1); then
        echo "ERROR: the JVM rejects its start options -- NOT touching the running instance."
        echo "${jvm_out}" | sed 's/^/  /'
        exit 1
    fi

    for f in "${CONFIG_FILE}" "${PROPS_FILE}" "${JETTY_FILE}"; do
        [ -f "${f}" ] || echo "WARN: ${f} not found"
    done

    PREFLIGHT_DONE=1
    if [ "${action}" = "restart" ]; then
        echo "Preflight OK: $(basename "${WAR_FILE}"), ${java_bin}, GC: ${GC_OPTS:-JDK default}"
    fi
}

import_ssl_certs() {
    if [ -z "${SSL_CERTS}" ]; then
        return 0
    fi

    if ! command -v openssl > /dev/null 2>&1; then
        echo "WARN: openssl not found, skipping SSL certificate import."
        return 0
    fi

    keytool_bin="${JAVA_HOME}/bin/keytool"
    ssl_keystore="${SSL_KEYSTORE:-${JAVA_HOME}/lib/security/cacerts}"
    if [ ! -x "${keytool_bin}" ]; then
        echo "WARN: keytool not found at ${keytool_bin}, skipping SSL certificate import."
        return 0
    fi

    tmp_cert_dir=$(mktemp -d)
    trap_cleanup() { rm -rf "${tmp_cert_dir}"; }
    trap trap_cleanup EXIT

    echo "${SSL_CERTS}" | while IFS='|' read -r alias host port; do
        # Skip empty lines
        [ -z "${alias}" ] && continue

        echo "  Importing SSL cert: ${alias} (${host}:${port})"

        cert_bundle="${tmp_cert_dir}/${alias}-bundle.pem"

        # Download the full certificate chain
        if ! openssl s_client -connect "${host}:${port}" -showcerts </dev/null 2>/dev/null \
            | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > "${cert_bundle}" \
            || [ ! -s "${cert_bundle}" ]; then
            echo "    WARN: Failed to retrieve certificate from ${host}:${port}, skipping."
            continue
        fi

        # Split the chain into individual certs and import each one
        cert_index=0
        current_cert=""
        while IFS= read -r line; do
            case "${line}" in
                "-----BEGIN CERTIFICATE-----")
                    current_cert="${line}"
                    ;;
                "-----END CERTIFICATE-----")
                    current_cert="${current_cert}
${line}"
                    cert_file="${tmp_cert_dir}/${alias}-${cert_index}.pem"
                    echo "${current_cert}" > "${cert_file}"

                    cert_alias="${alias}-${cert_index}"

                    # Remove old entry (ignore errors if it doesn't exist)
                    "${keytool_bin}" -delete \
                        -keystore "${ssl_keystore}" \
                        -storepass "${SSL_KEYSTORE_PASS}" \
                        -alias "${cert_alias}" 2>/dev/null || true

                    # Import the certificate
                    if "${keytool_bin}" -import -noprompt -trustcacerts \
                        -alias "${cert_alias}" \
                        -file "${cert_file}" \
                        -keystore "${ssl_keystore}" \
                        -storepass "${SSL_KEYSTORE_PASS}" 2>/dev/null; then
                        echo "    Imported: ${cert_alias}"
                    else
                        echo "    WARN: Failed to import ${cert_alias}"
                    fi

                    cert_index=$((cert_index + 1))
                    current_cert=""
                    ;;
                *)
                    if [ -n "${current_cert}" ]; then
                        current_cert="${current_cert}
${line}"
                    fi
                    ;;
            esac
        done < "${cert_bundle}"

        echo "    Imported ${cert_index} certificate(s) for ${alias}."
    done

    rm -rf "${tmp_cert_dir}"
}

# ── Suramadu server cert → CHILD app JVM truststore (optional, per host) ─
# Exports the listed aliases from the server keystore and imports them into the
# truststore of the JDK Suramadu launches the Swing apps with (NOT necessarily
# $JAVA_HOME). Disabled while SERVER_CERT_ALIASES is empty.
APP_JDK_HOME=""             # e.g. /home/manticore/jdk-25.0.3-lite
APP_TRUSTSTORE="${APP_JDK_HOME}/lib/security/cacerts"
APP_TRUSTSTORE_PASS="changeit"
KEYSTORE=""                 # e.g. /vboxapp/vbox/ssl/keystore.jks
KEYSTORE_PASS=""
SERVER_CERT_ALIASES=""      # space-separated; add the leaf if it doesn't chain to root

import_server_cert() {
    [ -n "${SERVER_CERT_ALIASES}" ] || return 0

    keytool_bin="${JAVA_HOME}/bin/keytool"
    [ -x "${keytool_bin}" ] || keytool_bin="keytool"

    [ -f "${KEYSTORE}" ]       || { echo "  WARN: keystore '${KEYSTORE}' missing, skip server-cert import"; return 0; }
    [ -w "${APP_TRUSTSTORE}" ] || { echo "  WARN: ${APP_TRUSTSTORE} not writable, skip server-cert import"; return 0; }

    tmp_dir=$(mktemp -d)
    for alias in ${SERVER_CERT_ALIASES}; do
        crt="${tmp_dir}/${alias}.crt"
        if ! "${keytool_bin}" -exportcert -rfc \
                -keystore "${KEYSTORE}" -storepass "${KEYSTORE_PASS}" \
                -alias "${alias}" -file "${crt}" 2>/dev/null; then
            echo "  WARN: alias '${alias}' not in keystore, skip"; continue
        fi
        "${keytool_bin}" -delete \
            -keystore "${APP_TRUSTSTORE}" -storepass "${APP_TRUSTSTORE_PASS}" \
            -alias "suramadu-${alias}" 2>/dev/null || true
        if "${keytool_bin}" -importcert -noprompt -trustcacerts \
                -keystore "${APP_TRUSTSTORE}" -storepass "${APP_TRUSTSTORE_PASS}" \
                -alias "suramadu-${alias}" -file "${crt}" 2>/dev/null; then
            echo "  Imported '${alias}' into app truststore"
        else
            echo "  WARN: failed to import '${alias}'"
        fi
    done
    rm -rf "${tmp_dir}"
}

# ── Helpers ──────────────────────────────────────────────────────────────

log_msg() {
    echo "$(date '+%Y-%m-%d %X'): $1" >> "${LOG_FILE}"
}

is_running() {
    [ -f "${PID_FILE}" ] && kill -0 "$(cat "${PID_FILE}")" 2>/dev/null
}

rotate_logs() {
    find "${LOG_FILE}"* -type f -mtime "+${LOG_RETENTION_DAYS}" -delete 2>/dev/null || true
    if [ -f "${LOG_FILE}" ]; then
        mv "${LOG_FILE}" "${LOG_FILE}_$(date '+%Y%m%d%H%M%S')"
        touch "${LOG_FILE}"
    fi
}

kill_tail() {
    if [ -f "${TAIL_PID_FILE}" ]; then
        kill "$(cat "${TAIL_PID_FILE}")" 2>/dev/null || true
        rm -f "${TAIL_PID_FILE}"
    fi
    pkill -f "tail -f ${LOG_FILE}" 2>/dev/null || true
}

# Detect a log-colorizing program and return a pipe expression, or empty string.
# Probed in preference order:
#   logalize  – Rust-based, regex-driven colorizer
#   grc       – Generic Colouriser (grcat conf.log)
#   ccze      – classic syslog/apache colorizer
#   lnav      – log navigator (tailing mode)
#   colorize  – Python colorize
#   hl        – hl (highlight)
detect_log_colorizer() {
    if command -v logalize > /dev/null 2>&1; then
        echo "logalize"
    elif command -v grc > /dev/null 2>&1; then
        echo "grc --colour=on cat"
    elif command -v ccze > /dev/null 2>&1; then
        echo "ccze -A"
    elif command -v lnav > /dev/null 2>&1; then
        echo "lnav -q"
    elif command -v colorize > /dev/null 2>&1; then
        echo "colorize"
    elif command -v hl > /dev/null 2>&1; then
        echo "hl"
    else
        echo ""
    fi
}

# ── Self-Detach (restart from inside a running session) ──────────────────

# True if this script runs as a descendant of the server JVM (PID_FILE),
# i.e. from a Jediterm shell opened inside a Suramadu session.
inside_server_tree() {
    [ -f "${PID_FILE}" ] || return 1
    srv=$(cat "${PID_FILE}" 2>/dev/null) || return 1
    [ -n "${srv}" ] || return 1
    p=$$
    while [ -n "${p}" ] && [ "${p}" -gt 1 ] 2>/dev/null; do
        [ "${p}" = "${srv}" ] && return 0
        p=$(ps -o ppid= -p "${p}" 2>/dev/null | tr -d ' ')
    done
    return 1
}

# Re-launch this script fully detached from the current process tree:
#   - double fork (subshell + background) -> orphaned, re-parented to init,
#     so it is no longer a descendant that the server could tear down
#   - setsid -> new session, no SIGHUP / process-group kill from the terminal
#   - env -i -> clean environment; the Jediterm shell inherits the app JVM's
#     environment (CLASSPATH, JAVA_TOOL_OPTIONS, ...) which must not leak
#     into the new server JVM
#   - stdin/stdout/stderr redirected, nothing references the dying terminal
detach_self() {
    mkdir -p "${BASE}/logs"
    self="${BASE}/$(basename "$0")"

    if [ -n "${REQUESTED_VERSION}" ]; then
        set -- -v "${REQUESTED_VERSION}" "${action}"
    else
        set -- "${action}"
    fi

    runner=""
    if command -v setsid > /dev/null 2>&1; then
        runner="setsid"
    fi

    : > "${DETACH_LOG}"
    (
        trap '' HUP
        # shellcheck disable=SC2086
        ${runner} env -i \
            HOME="${HOME}" \
            USER="${USER:-$(id -un)}" \
            LOGNAME="${USER:-$(id -un)}" \
            PATH="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin" \
            LANG="${LANG:-C.UTF-8}" \
            ${TZ:+TZ="${TZ}"} \
            SURAMADU_DETACHED=1 \
            SURAMADU_JAVA_HOME="${JAVA_HOME}" \
            nohup sh "${self}" "$@" >> "${DETACH_LOG}" 2>&1 < /dev/null &
    )

    echo ""
    echo "==== ${action} of ${APP} handed over to a detached process"
    echo "  Log:  ${DETACH_LOG}"
    echo "  This session will drop in ~2 s. Reconnect after 30-60 s."
}

# ── Commands ─────────────────────────────────────────────────────────────

cmd_list() {
    echo ""
    echo "==== Available ${APP} versions in ${BASE}"
    echo ""
    if ! list_wars; then
        echo "  (none found)"
    fi

    # Show which is latest
    if find_latest_war 2>/dev/null; then
        echo ""
        echo "  Latest: ${WAR_VERSION} -> $(basename "${WAR_FILE}")"
    fi
    echo ""
}

cmd_status() {
    echo ""
    echo "==== Status of ${APP}"

    if is_running; then
        pid=$(cat "${PID_FILE}")
        echo ""
        echo "${APP} is running with PID [${pid}]"
        ps -fp "${pid}" 2>/dev/null || true
    elif [ -f "${PID_FILE}" ]; then
        echo ""
        echo "${APP} is NOT running, but stale PID file found"
        rm -f "${PID_FILE}"
    else
        echo ""
        echo "${APP} is NOT running"
    fi
}

cmd_start() {
    if is_running; then
        echo "${APP} already running with PID [$(cat "${PID_FILE}")]"
        return 0
    fi

    check_java
    import_ssl_certs
    import_server_cert

    # Resolve WAR file
    if [ -n "${REQUESTED_VERSION}" ]; then
        find_war_by_version "${REQUESTED_VERSION}" || exit 1
    else
        find_latest_war || exit 1
    fi

    echo "==== Starting ${APP} v${WAR_VERSION}"
    echo "  WAR: $(basename "${WAR_FILE}")"
    mkdir -p "${BASE}/logs"

    rotate_logs

    # Verify jemalloc is present; warn and skip LD_PRELOAD rather than silently misconfiguring
    if [ ! -f "${BASE}/dist/ext-lib/libjemalloc.so.2" ]; then
        echo "WARN: libjemalloc.so.2 not found at ${BASE}/dist/ext-lib/ -- starting without jemalloc"
        cmd="${java_bin} ${FULL_OPTS} ${LOGGING} -jar ${WAR_FILE} ${SURAMADU_OPTS}"
    else
        cmd="${MALLOC_CONF} ${PRELOAD} ${java_bin} ${FULL_OPTS} ${LOGGING} -jar ${WAR_FILE} ${SURAMADU_OPTS}"
    fi

    # Raise fd limit -- one fd per session plus Jetty workers; default 1024 is easily exhausted
    ulimit -n 65535 2>/dev/null || echo "WARN: Could not raise open file limit (ulimit -n 65535)"

    # Prefer ionice + nice for CPU and I/O scheduling; fall back to nice-only
    if command -v ionice > /dev/null 2>&1; then
        launcher="ionice -c 2 -n 4 nice -n${NICE_LEVEL}"
    else
        launcher="nice -n${NICE_LEVEL}"
    fi

    # shellcheck disable=SC2086
    if nohup ${launcher} sh -c \
        "${cmd}; sleep 1; echo \"\$(date '+%Y-%m-%d %X'): ${APP} STOPPED\" >> '${LOG_FILE}'; rm -f '${PID_FILE}'" \
        >> "${LOG_FILE}" 2>&1 &
    then
        sleep 1
        pgrep -P $! > "${PID_FILE}" 2>/dev/null || echo $! > "${PID_FILE}"
        echo "Started with PID [$(cat "${PID_FILE}")]"
        log_msg "${APP} v${WAR_VERSION} STARTED ($(basename "${WAR_FILE}"))"

        # No terminal to tail into when running detached
        if [ "${SURAMADU_DETACHED:-0}" != "1" ]; then
            kill_tail
            colorizer=$(detect_log_colorizer)
            if [ -n "${colorizer}" ]; then
                # shellcheck disable=SC2086
                tail -f "${LOG_FILE}" | ${colorizer} &
                # Save the tail PID so kill_tail can stop it cleanly
                tail_pid=$( jobs -l | awk '/tail -f/ { print $2 }' | tail -1 )
                echo "${tail_pid:-$!}" > "${TAIL_PID_FILE}"
            else
                tail -f "${LOG_FILE}" &
                echo $! > "${TAIL_PID_FILE}"
            fi
        fi
    else
        echo "Error starting ${APP}"
        rm -f "${PID_FILE}"
        return 1
    fi
}

cmd_stop() {
    echo "==== Stopping ${APP}"

    if [ ! -f "${PID_FILE}" ]; then
        echo "No PID file found -- ${APP} already stopped?"
        return 0
    fi

    pid=$(cat "${PID_FILE}")

    if kill "${pid}" 2>/dev/null; then
        echo "Sent SIGTERM to PID [${pid}]"
        count=0
        while kill -0 "${pid}" 2>/dev/null && [ "${count}" -lt 30 ]; do
            sleep 1
            count=$((count + 1))
        done
        if kill -0 "${pid}" 2>/dev/null; then
            echo "Process did not stop gracefully -- sending SIGKILL"
            kill -9 "${pid}" 2>/dev/null || true
        fi
        log_msg "${APP} STOPPED"
    else
        echo "Process [${pid}] not running"
    fi

    rm -f "${PID_FILE}"
    kill_tail
}

cmd_restart() {
    # Never kill the old instance unless the new one can actually start
    # (already done in main when not detached; a detached child does its own)
    [ "${PREFLIGHT_DONE:-0}" = "1" ] || preflight
    cmd_stop
    echo "Sleeping..."
    sleep 3
    cmd_start
}

# ── Main ─────────────────────────────────────────────────────────────────

REQUESTED_VERSION=""
action=""
DETACH=0

while [ $# -gt 0 ]; do
    case "$1" in
        -h|--help)
            echo "${USAGE}"
            exit 0
            ;;
        -v|--version)
            if [ $# -lt 2 ]; then
                echo "ERROR: -v requires a version argument"
                exit 1
            fi
            REQUESTED_VERSION="$2"
            shift
            ;;
        -d|--detach)
            DETACH=1
            ;;
        start|stop|restart|status|list)
            action="$1"
            ;;
        *)
            echo "Unknown argument: $1"
            echo "${USAGE}"
            exit 1
            ;;
    esac
    shift
done

if [ -z "${action}" ]; then
    echo "No action specified."
    echo "${USAGE}"
    exit 1
fi

cd "${BASE}"

case "${action}" in
    stop|restart)
        if [ "${action}" = "restart" ] && [ "${SURAMADU_DETACHED:-0}" != "1" ]; then
            preflight
        fi
        if [ "${SURAMADU_DETACHED:-0}" = "1" ]; then
            # Detached child: give the launching session time to print and exit
            sleep 2
        elif [ "${DETACH}" -eq 1 ] || inside_server_tree; then
            detach_self
            exit 0
        fi
        ;;
esac

case "${action}" in
    list)    cmd_list    ;;
    status)  cmd_status  ;;
    start)   cmd_start   ;;
    stop)    cmd_stop    ;;
    restart) cmd_restart ;;
esac