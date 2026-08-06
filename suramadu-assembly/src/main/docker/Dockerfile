# ── How to refresh pinned digests ─────────────────────────────────────────────
# Run the following after each upstream release:
#
#   docker pull eclipse-temurin:21-jdk-noble
#   docker inspect eclipse-temurin:21-jdk-noble \
#       --format='{{index .RepoDigests 0}}'
#
#   docker pull eclipse-temurin:21-jre-noble
#   docker inspect eclipse-temurin:21-jre-noble \
#       --format='{{index .RepoDigests 0}}'
#
# Paste the resulting sha256 values into the FROM lines below.
# Dependabot or Renovate can automate this update if enabled.
# ──────────────────────────────────────────────────────────────────────────────

# FIX (supply-chain / CVE): pin base images to immutable SHA-256 digests.
# Floating tags (e.g. "21-jdk-noble") can silently change to a layer that
# introduces new vulnerabilities or, in a worst case, is tampered with.
# The tag is kept for human readability; the digest is what Docker actually
# pulls.

# ── Stage 1: Build ────────────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-noble@sha256:82ab4a70ae46a1f1dc6d607e1d5826a37410b7eded2fe9a52e2394e760fb6aad AS builder

# FIX (silent pipe failures): set pipefail so any command in a pipeline that
# fails causes the RUN step to fail immediately rather than being swallowed.
SHELL ["/bin/bash", "-eo", "pipefail", "-c"]

# FIX (unpatched OS packages in build stage): upgrade before installing to
# pick up any security patches published since the base image was built.
RUN apt-get update \
    && apt-get upgrade -y \
    && apt-get install -y --no-install-recommends \
        git \
        unzip \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /build

# Cache Gradle wrapper + dependencies before copying full source
COPY gradlew gradlew.bat ./
COPY gradle/ gradle/
RUN chmod +x gradlew && ./gradlew --version --no-daemon

# Copy build definitions next (changes less often than source)
COPY build.gradle settings.gradle gradle.properties ./
COPY buildSrc/ buildSrc/

# Copy full source and build the distribution
COPY . .
RUN ./gradlew :suramadu-assembly:distZip --no-daemon -x test -x spotbugsMain -x checkstyleMain -x pmdMain

# Unpack the distribution
RUN mkdir -p /opt/suramadu \
    && unzip suramadu-assembly/build/dist/suramadu-*.zip -d /tmp/dist \
    && mv /tmp/dist/suramadu-*/* /opt/suramadu/ \
    && chmod +x /opt/suramadu/*.sh

# ── Stage 2: Runtime ──────────────────────────────────────────────────
# FIX (supply-chain / CVE): pinned digest — see refresh instructions above.
FROM eclipse-temurin:21-jre-noble@sha256:b4f73e2d0428d029d934aa911baad025f88a921ee9ce01b0d5f9ad4366180aac

LABEL maintainer="Manticore Projects <info@manticore-projects.com>" \
      org.opencontainers.image.title="Suramadu" \
      org.opencontainers.image.description="Java Swing applications in the browser" \
      org.opencontainers.image.source="https://github.com/manticore-projects/suramadu"

SHELL ["/bin/bash", "-eo", "pipefail", "-c"]

# Runtime dependencies for truly headless Swing rendering.
#
# Suramadu 26.4+ runs without any X server: java.desktop is patched via
# --patch-module so GraphicsEnvironment, PlatformGraphicsInfo, and
# FontManagerFactory all bypass X11GraphicsEnvironment / X11FontManager.
# The native library libawt_xawt.so is never loaded.  This means no Xvfb,
# no libxext6 / libxi6 / libxtst6 / libxrender1, no /tmp/.X11-unix.
#
# What we DO still need:
#   - libfontconfig1   font discovery via the fontconfig API (called by
#                      WebFontManager's native font registration path)
#   - fonts-dejavu-core  fallback fonts for Java's logical Dialog / Serif /
#                        Sans Serif / Monospaced families when an app does
#                        not ship its own
#
# apt-get upgrade picks up OS-level security patches since the base image build.
# curl is intentionally absent — wget (already in the temurin base image) is
# used for the HEALTHCHECK instead, removing curl's CVE surface entirely.
# FIX (gnupg2/dirmngr/libgcrypt20 CVEs): gnupg2 and its dependency chain are
# pulled in transiently by apt itself but are not needed at container runtime.
# Purging them in the same RUN layer removes those packages — and their CVEs —
# from the final image.  apt-get update is not re-run after the purge because
# package-list state is discarded via rm -rf /var/lib/apt/lists/* anyway.
RUN apt-get update \
    && apt-get upgrade -y \
    && apt-get install -y --no-install-recommends \
        libfontconfig1 \
        fonts-dejavu-core \
    && apt-get purge -y --auto-remove \
        gnupg2 \
        gpg \
        gpg-agent \
        gpgsm \
        dirmngr \
        wget \
    && rm -rf /var/lib/apt/lists/*

# FIX (shadow/login CVE-2024-56433): shadow login utilities are only needed
# for the useradd/groupadd commands below.  They are purged in the same RUN
# layer immediately after user creation so they are absent from the runtime image.
RUN groupadd -r suramadu \
    && useradd -r -g suramadu -m -s /bin/false suramadu \
    && apt-get purge -y --auto-remove login \
    && rm -rf /var/lib/apt/lists/*

# Copy distribution from builder
COPY --from=builder --chown=suramadu:suramadu /opt/suramadu /opt/suramadu

WORKDIR /opt/suramadu

# Logging and data directories (writable by the suramadu user).
# No /tmp/.X11-unix — the container runs truly headless.
RUN mkdir -p logs tmp \
    && chown -R suramadu:suramadu logs tmp

ENV JAVA_HOME="/opt/java/openjdk" \
    SURAMADU_HOME="/opt/suramadu"

# JVM flags — mirrors run.sh but without daemonization
ENV JAVA_OPTS="\
 -server -Xmx2G -Xms256m -Xss228k \
 -Djava.security.egd=file:/dev/urandom \
 -Djava.io.tmpdir=/opt/suramadu/tmp \
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

# They are user configuration read by existing deployments; renaming them would
# break upgrades for no benefit.
ENV SURAMADU_OPTS="-h 0.0.0.0 -j jetty.properties -c suramadu.config -pf suramadu.properties"

EXPOSE 8080 8443

# FIX (wget CVE-2021-31879 — Open Redirect, no Ubuntu fix): wget is purged
# above and replaced here with a pure bash built-in TCP health check.
# bash's /dev/tcp pseudo-device opens a TCP connection without any external
# binary, sends a minimal HTTP/1.0 request, and verifies the response begins
# with "HTTP".  No curl, no wget, no external dependencies.
HEALTHCHECK --interval=30s --timeout=5s --start-period=15s --retries=3 \
    CMD bash -c 'exec 3<>/dev/tcp/localhost/8080 \
        && printf "GET / HTTP/1.0\r\nHost: localhost\r\nConnection: close\r\n\r\n" >&3 \
        && head -1 <&3 | grep -qF HTTP' || exit 1

USER suramadu

# Start the server directly — no Xvfb, no X server, no DISPLAY required.
# WAR filename is resolved at runtime so versioned names work.
CMD sh -c '\
    WAR=$(ls ${SURAMADU_HOME}/suramadu-server*.war 2>/dev/null | head -1) && \
    exec java ${JAVA_OPTS} -jar ${WAR} ${SURAMADU_OPTS}'