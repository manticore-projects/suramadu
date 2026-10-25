<div align="center">

# Suramadu 27.1

### Swing in the Browser

[![Build](https://img.shields.io/github/actions/workflow/status/manticore-projects/suramadu/Gradle.yml?branch=master&style=for-the-badge&logo=githubactions&logoColor=white&label=Build)](https://github.com/manticore-projects/suramadu/actions/workflows/Gradle.yml)
[![Version](https://img.shields.io/badge/Version-27.1-orange?style=for-the-badge)](https://github.com/manticore-projects/suramadu/releases)
[![JDK 17+](https://img.shields.io/badge/JDK-17%2B%20%7C%2021%20%7C%2025%20%7C%2027-blue?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Node.js 24](https://img.shields.io/badge/Node.js-24%20LTS-339933?style=for-the-badge&logo=nodedotjs&logoColor=white)](https://nodejs.org/)
[![License: AGPL v3](https://img.shields.io/badge/License-AGPL%20v3-red?style=for-the-badge)](https://www.gnu.org/licenses/agpl-3.0)
[![Build](https://img.shields.io/badge/Build-Gradle%20%7C%20Maven-02303A?style=for-the-badge&logo=gradle&logoColor=white)](https://gradle.org/)
[![GitHub](https://img.shields.io/github/stars/manticore-projects/suramadu?style=for-the-badge&logo=github)](https://github.com/manticore-projects/suramadu)

<br/>

*Run any Java Swing application inside a modern web browser — pure HTML5, zero plugins, zero client-side installation.*

<br/>

[Getting Started](#getting-started) · [Build](#build-instructions) · [Upgrading](#upgrading-from-the-webswing-named-releases) · [What's New](#whats-new) · [Architecture](#architecture)

---

</div>

## Overview

**Suramadu** renders Java Swing applications into an HTML5 Canvas and delivers them to any browser over a WebSocket. Your application runs unchanged on the server; the browser only draws the window.

The project is a fork of **WebSwing v20.2.5**, the last release published under the GNU AGPL v3. It is maintained by [Manticore Projects](https://manticore-projects.com) to keep that codebase running on current JDKs, with a focus on modern JDK compatibility, security hardening and build toolchain modernization.

> **Suramadu is not affiliated with, endorsed by, or supported by Webswing Limited or Webswing s.r.o.**

### About the name

The [Suramadu](https://en.wikipedia.org/wiki/Suramadu_Bridge) is the bridge that crosses from Java to the island of Madura — 5.4 km of cable-stayed span across the Madura Strait, first proposed in the 1960s and finally opened in 2009. It seemed a fitting name for software whose whole job is carrying Java across to somewhere else.

### Source availability

Suramadu is licensed under the GNU AGPL v3. If you interact with a running instance over a network, you are entitled to the complete corresponding source of that instance under section 13 of the licence.

The complete corresponding source for every released version is in this repository. Releases are tagged; deployed instances link back here.

---

## What's New

### Rendering Reliability (27.1)

- **No more UI corruption on slow connections.** On a slow or congested link, the browser could
  draw parts of the UI with the wrong colours, shapes or text. Cause: the server's DirectDraw
  constant cache recycled ids *within* a single large frame, while the browser applies a frame's
  constants in a different order than the server allocated them. The cache now never evicts an
  entry used in the current frame and grows instead (`LRUDrawConstantPoolCache`).
- **Slow sessions no longer collapse.** After 5 s without a paint acknowledgement the server used
  to reset its cache and re-send everything, fonts included, to exactly the client that was
  already too slow: a feedback loop down to under 1 frame per second. The timeout now resumes
  sending without a reset.
- **Cache reset race fixed.** A browser resize, reconnect or recording start could reset the
  cache while a frame was being encoded. Resets now happen only between frames.

### JDK Compatibility

Recommended standard OpenJDK distributions: [Eclipse Temurin](https://adoptium.net/), [BellSoft Liberica](https://bell-sw.com/pages/downloads/), or [Amazon Corretto](https://aws.amazon.com/corretto/).

| JDK |    Status    | Temurin Support | Notes                                                     |
|:---:|:------------:|:---------------:|:----------------------------------------------------------|
| 17 (LTS) | 🔶 Supported | Oct 2027 | Toolchain of the build                                    |
| 21 (LTS) | ✅ Supported  | Dec 2029 |                                       |
| 23 | ✅ Supported  | ⛔ EOL (Mar 2025) | Non-LTS, 6-month lifecycle                                |
| 25 (LTS) | ✅ Supported  | Sep 2031 | Current LTS                                               |
| 26 | ✅ Supported  | ⛔ EOL (Sep 2026) | Non-LTS; requires `--sun-misc-unsafe-memory-access=allow` |
| **27** | ✅ **Supported** | Mar 2027 | Non-LTS; dedicated `*27` toolkit variant, selected automatically |

All internal APIs adapted for the post-JDK-11 module system — no `--illegal-access=permit`, no `-noverify` required.

JavaFX applications require JDK ≤ 26 — there is no JavaFX toolkit variant for JDK 27 yet; the launcher refuses the combination explicitly.

### JDK 27 Support

Suramadu 27.0 adds a dedicated toolkit variant (`suramadu-app-toolkit-java27`) for JDK 27,
where the applet-era `sun.awt.AppContext` was removed from the JDK: `SunToolkit.postEvent()`
lost its `AppContext` parameter, `SunToolkit.getDataTransferer()` moved to
`sun.awt.ComponentFactory`, the `jdk.jsobject` module is gone, and
`java.awt.peer.WindowPeer` gained `getAppropriateGraphicsConfiguration()`.

Nothing to configure: the right variant is chosen per application JVM — the launcher puts
exactly one of `suramadu-app-toolkit-java11` / `suramadu-app-toolkit-java27` on the boot
classpath, and the patched `sun.awt.PlatformGraphicsInfo` selects by `Runtime.version()`.
A JDK 27 session announces itself in the application log:

```
Loaded Webtoolkit org.webswing.toolkit.WebToolkit27
Toolkit.toolkit field has been set to: org.webswing.toolkit.WebToolkit27@5a8e6209
GraphicsEnvironment is org.webswing.toolkit.ge.WebGraphicsEnvironment27 (headless, JDK patch active)
```

On JDK 27 the launcher additionally passes `--enable-final-field-mutation=ALL-UNNAMED`
([JEP 500](https://openjdk.org/jeps/500)) and serves `netscape.javascript.JSObject` from
the boot classpath instead of patching the removed `jdk.jsobject` module. Server and
application JVMs are independent — the Jetty server itself also runs on JDK 17–27.

**Beware:** JetBrains Runtime modifies AWT's internal keyboard focus dispatch, which breaks when the toolkit is replaced. Mouse events are unaffected because they bypass the patched focus path.

### Demo

Try the [Online JSQLFormatter running on Suramadu](http://jsqlformatter.manticore-projects.com/jsqlformatter/demo.html?args=-c%20OoSQKgEgBApgHlAggZSgCgFBWz7yCiAMvgMJhQBUWuOAYgEoDyAslAMYBmMAdAE4wAHAPrwoASmrYANFA5xeSVJho4CxMtgCWAEyFsArr34A7NgE8hHXgHsAtpJWOZc3gEMALjAe4GLdndtrY24XDxghAAsAZ3dvHGAIfHp8dCgdPUMTc0sbWxl0gyMYUwtNY3drGQA3VwAbfXDtMPEoEAA5VLwiUnI03UKsiys7OMcx8YnJqencfP7M4uyyitGZtfWNyZlmVzg0WXluGvrG5rEoY4ahJs9VzfuHx19WNgCgkPkwyJiD3jvHgGA1ptNpJKAAKUY7XYXD4ghECHg-yBKMejA6LiOdSuNxgUAAPABeWBwLEna5hZGo6ljADiTAAqgAFKAAIQAmrgCgsSjkRjSBdS5hkirzltZxFTEG0ACJ9EWDITiqDEgBcrMYjAA0kISAz6Mk2iR2asGW0QOikIRCKs1D1sOrNTq9Qb8EaTesZABGVbPKDafR1SWzfy1WpCdxmAQwKKKdC27oaAD0FAA1FAZfQQAA1dq0oTIcD4NCIc4UJNUqhjP2vWyBYKvMNhNzhyPRqCuKntUH0CFQjr7VSJ8g7Pbyxu1Zt1CNR8L8DgtArWJueFsz6NCedU9Y1t4N5eT1fTtteGkJJIpdIxDz6WPQ-YAchID5kD4gD+D1PpjGZbM5S5XGA1xPT9NgZQs2lpVIAMPIDj1nTcYAXc4JDtDRQ1qbgl30cogIEVxeEjVYZDYARuFeHDV3wwiLG0GM2GIjCsP6A8p1qRiJ2YvRWKPVtZygAAfASoA-NAIBIVURME4SwGsXUIgI-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-57mNee-iL14DdmKsrqc+uwP2BJucj1+8+NBuoCWjjzo9gby2KJ9wK8sHtc0-m7gJbkXQItdsbvfrL06feKZ-uH5CvOfrdR+flAarugHf1aSllFKE8A9R7F-bzvJVHerMb-LFuGc25u-j1dYfI+rn9wr3x09aSF9q++57v5rgV9vQG7eezIkRtrrmy1mzW-Q-SfD6NyPUeY+4Gn5Xjy1eEeb4VWcf0ifLTaHv3rVX2nAMCk0Z-vXu7NwPnoXoqoKAAR9gPl9sPHXFAVFIPmvusBvlxGfsNqumNufFlNklrlvECMbHDBjIMIfIcCTtUqfGettnomrlbENFZjgGdswnVLOETnPuCqviBJ8mKl-vBMGgANxAA$$) <img width="33%" alt="JSQLFormatter Screenshot" align="right" src="https://github.com/user-attachments/assets/93786995-983b-4b94-93a5-a3499f084e70" />


### Features

- **Truly headless operation on Linux** — no X server, no Xvfb, no virtual framebuffer. AWT initialises against an `--patch-module`–injected `GraphicsEnvironment` + `FontManagerFactory` that bypass `libawt_xawt.so` entirely. A `*-jre-headless` JDK package is sufficient; container images shrink accordingly.
- **Shiro Security Plugin** with hot configuration reloading provides authorization via AD/LDAP/Database/Text Files with or without 2FA (Second Factor authorization)
- **SVG Application Icons**
- better server start scripts supporting SSL certificate registration and WAR version numbers
- Deduplication of the java libraries in the WAR file, reducing the size drastically
- **Network resilience** for flaky corporate VPNs (Netskope, Zscaler) — HTTP/2 via ALPN, Conscrypt TLS (tolerates underscored SNI hostnames), per-socket TCP keepalive, 5-minute idle timeouts, TLS session resumption, tuned thread pool with `LowResourceMonitor`

### Build & Runtime Modernization

- **Node.js 24 LTS** — migrated from Node 10; Webpack 5, TypeScript 5, Dart Sass
- **Gradle build system** — fast, incremental builds
- **All dependencies updated** — Jetty 12, Jackson 3, Guava, Log4j2, SLF4J 2.0, Apache Commons, Protocol Buffers, LZ4, and more

### Performance

- **SSE/AVX-optimized PNG encoding** via [zpng-java](https://manticore-projects.com/FPNG-Java/index.html) — hardware-accelerated image compression in the DirectDraw rendering pipeline
- **Browser-side font rendering** — text is rendered as font names + coordinates instead of server-side glyph bitmaps, reducing WebSocket bandwidth by up to 80%
- **GZIP/Brotli pre-compression** for all static assets (reducing the JS size from 5 MB to less than 1 MB), GZIP compression for all content (JSON)
- **Faster text drawing** *(27.1)* — runs of characters are drawn with one `fillText` call instead of one per character, with cached text measurements; per-character fallback only where browser and server metrics disagree. Text-heavy redraws render about twice as fast (benchmark: 17 → 9 ms median, 95th percentile 30 → 13 ms)
- **Faster image decoding** *(27.1)* — images are decoded with `createImageBitmap` straight from the binary data, off the main thread, instead of building base64 data URLs
- **Less work on the Swing event thread** *(27.1)* — PNG encoding of images happens lazily on the sender thread instead of while painting; image identity is hashed directly from the raster; fewer allocations per encoded frame
- **Built-in DirectDraw benchmark** *(27.1)* — a self-driving Swing application (`/benchmark`: table scrolling, icons, vector graphics, image flood), per-frame server and client timings written to CSV (`-Dwebswing.benchmarkStatsFile`), an analyzer and a reproducible browser launcher. See [the benchmark README](suramadu-benchmark/src/main/resources/org/webswing/benchmark/README.md)
- **Experimental Web Worker rendering** *(27.1, off by default)* — see [the dedicated section](#experimental-directdraw-rendering-in-a-web-worker-off-by-default) for measurements and why it stays disabled

### Security

The fork is hardened for the deployments it was built for.

[![Known Vulnerabilities](https://snyk.io/test/github/manticore-projects/suramadu/badge.svg?style=flat-square)](https://snyk.io/test/github/manticore-projects/suramadu)
[![Semgrep](https://github.com/manticore-projects/suramadu/actions/workflows/semgrep.yml/badge.svg)](https://github.com/manticore-projects/suramadu/actions/workflows/semgrep.yml)
[![Semgrep](https://img.shields.io/badge/semgrep-scanning-green?style=flat-square&logo=semgrep)](https://semgrep.dev)

**Continuous monitoring** via Snyk (dependency CVEs), Semgrep (SAST/OWASP Top 10), SpotBugs (bytecode analysis), and Codacy (code quality). CycloneDX SBOM generated with every release.

**Proactive hardening** against entire vulnerability classes — not just known CVEs: deserialization allowlists (CWE-502), SSRF scheme validation (CWE-918), XSS content-type enforcement (CWE-79), Zip Slip / path traversal protection (CWE-22), log injection sanitisation (CWE-117), AES-CBC→AES-GCM migration, and HMAC-signed file identifiers.

**Runtime** — non-root Docker container on Eclipse Temurin JRE 21 (Ubuntu Noble), multi-stage build excluding source and build tools from the image.

**Supply chain** — CycloneDX SBOM output supports transparency requirements under NIST SP 800-218 and the EU Cyber Resilience Act.

---

## Getting Started

### Prerequisites

| Component | Version |
|-----------|---------|
| JDK | 17 or later, up to and including **JDK 27** (21+ recommended; [Eclipse Temurin](https://adoptium.net/)). A **headless** JDK distribution is sufficient on Linux servers — see below. |

**No X server, no Xvfb, no virtual framebuffer required.** Suramadu runs truly headless on Linux via `--patch-module java.desktop` replacements for `GraphicsEnvironment`, `PlatformGraphicsInfo`, and `FontManagerFactory`. The standard `*-jre-headless` package from your distribution (or any JRE/JDK without the GUI dependencies) is enough. `DISPLAY` is explicitly unset by the startup script; nothing in the child Swing JVM ever calls into `libawt_xawt.so` or attempts a display connection.

### Quick Start

```bash
# Download the latest release
curl -LO https://github.com/manticore-projects/suramadu/releases/latest/download/suramadu-27.1.0.zip
unzip suramadu-27.1.0.zip
cd suramadu-27.1.0

# Start the server
./run.sh start

# Open in your browser
open http://localhost:8080
```

### Management

```bash
./run.sh start      # Start the server (background, with log tailing)
./run.sh stop       # Graceful shutdown (30s timeout, then SIGKILL)
./run.sh restart    # Stop + Start
./run.sh status     # Check if the server is running
```

---

## Build Instructions

### Prerequisites

| Tool   | Version |
|--------|---------|
| JDK    | 17+ to run the build, **plus an installed JDK 27** — the `suramadu-app-toolkit-java27` module compiles with a JDK 27 toolchain (auto-detected from installed JDKs) |
| Gradle | 9.+     |
| Git    | 2.x     |

> Node.js and npm are **automatically downloaded** during the build — no manual installation needed.

### Build with Gradle

```bash
# Clone the repository
git clone https://github.com/manticore-projects/suramadu.git
cd suramadu

# Full build
./gradlew clean build

# Build specific modules
./gradlew :suramadu-directdraw:suramadu-directdraw-javascript:build
./gradlew :suramadu-server:suramadu-server-frontend:build
```

### Deploy

```bash
# Extract to your deployment directory
unzip build/dist/suramadu-27.1.0.zip -d /opt/suramadu

# Configure your Swing application
vim /opt/suramadu/suramadu.config

# Start
cd /opt/suramadu && ./run.sh start
```

---

## Upgrading from the WebSwing-named releases

Everything a user or administrator touches now carries the `suramadu` name. Everything that would
break a running deployment — system properties, Java packages, the public API — is unchanged.

| | Before | Now |
|---|---|---|
| Distribution archive | `webswing-<version>.zip` | `suramadu-<version>.zip` |
| Web application | `webswing-server-<version>.war` | `suramadu-server-<version>.war` |
| Application configuration | `webswing.config` | `suramadu.config` |
| Server properties | `webswing.properties` | `suramadu.properties` |
| Gradle modules and jars | `webswing-*` | `suramadu-*` |
| Docker image | — | `manticore-projects/suramadu` |
| System properties | `-Dwebswing.*` | **unchanged** |
| Jetty properties keys | `org.webswing.server.*` | **unchanged** |
| Java packages and API | `org.webswing.*` | **unchanged** |

**No action is required to upgrade.** Existing `webswing.config` and `webswing.properties` files are
still read, so an installation created before the rename keeps working untouched. Rename them when
convenient, or leave them. Where both names are present, the `suramadu.*` files take precedence.

Java packages keep the `org.webswing` namespace deliberately: renaming them would break every
deployment that references a class by name, and would require altering the copyright and licence
notices the AGPL obliges us to preserve. They are internal identifiers, not product names.

---

## Architecture

<p align="center">
  <img src="architecture.svg" alt="Suramadu Architecture" width="800"/>
</p>

**How it works:** The server intercepts Java2D `Graphics2D` paint operations in the child JVM, serializes them via Protocol Buffers, and streams them over WebSocket to the browser. The browser's JavaScript engine deserializes and replays the draw commands on an HTML5 Canvas. User input (mouse, keyboard) flows back over the same WebSocket.

---

## Dependency Overview

| Component | Version | Purpose |
|-----------|---------|---------|
| Jetty | 12.1.+  | Embedded HTTP/WebSocket server |
| Jackson | 3.+     | JSON serialization |
| Protocol Buffers | 3.+     | Binary wire format (DirectDraw) |
| Apache Shiro | 3.+     | Authentication & authorization |
| Guava | 33.+    | Core utilities |
| Log4j 2 | 2.+     | Logging framework |
| SLF4J | 2.+     | Logging facade |
| Webpack | 5.+     | JavaScript bundling |
| TypeScript | 5.+     | Type-safe frontend code |

---

## Attribution

Suramadu is derived from WebSwing v20.2.5, the last release published under the GNU AGPL v3. All credit for the original architecture and implementation belongs to its authors.

Suramadu is not affiliated with, endorsed by, or supported by Webswing Limited or Webswing s.r.o.

## License

This project is licensed under the [GNU Affero General Public License v3.0](https://www.gnu.org/licenses/agpl-3.0.en.html).

---

## Experimental: DirectDraw rendering in a Web Worker (off by default)

Suramadu contains an optional render path that moves DirectDraw decoding and canvas replay off
the browser's main thread into a dedicated Web Worker with an `OffscreenCanvas`. It is
**disabled by default** and should stay that way until the findings below change. This section
records what was measured, what was ruled out, and why it is switched off, so the next attempt
does not start from zero.

### How it works

The worker owns the DirectDraw interpreter, the cumulative constant pool and one `OffscreenCanvas`
per Swing window. Each frame is replayed in the worker and handed back as a transferred
`ImageBitmap`, which the main thread blits into the regular window canvas. Everything that reads
window pixels on the main thread (dirty-rect blits, `moveAction`, `getPixels`, undocking, mirror
view) is unaffected.

| | |
|---|---|
| Enable | append `?ddWorker=true` to the application URL |
| Benchmark | `bench-browser.fish chromium\|firefox 1600 1000 worker` |
| Code | `suramadu-dd-worker.ts`, `suramadu-dd-client.ts`, routing in `suramadu-base.ts` (`initDirectDraw`, `renderInWorker`) |
| Fallback | any worker failure switches the session to main-thread rendering |

Implementation constraints that took effort to find and must be kept:

- **One constant pool.** The server sends every draw constant exactly once. All DirectDraw frames,
  base64 and binary alike, must go to the same interpreter; splitting them between main thread
  and worker corrupts rendering.
- **Copy frame bytes at call time.** `win.directDraw` is a view into the WebSocket buffer; a
  deferred copy can capture a later frame.
- **Serial rendering.** The interpreter is not re-entrant; the worker processes one frame at a time.
- **Start the worker from a Blob URL.** The page CSP allows `worker-src blob:` only.

### Results (October 2026, Linux/X11, AMD RX 570, Mesa 26.2)

Benchmark application `/benchmark`, same window and geometry for every run (controls VECTOR and
IMAGE_FLOOD identical in bytes per frame):

| | Chromium main thread | Chromium worker | Firefox 158 main thread | Firefox 158 worker |
|---|---|---|---|---|
| TEXT_TABLE fps | 18.5 | 19.5 | **27.2** | 0.4 (collapse) |
| TEXT render p50 per KB | 0.53 ms | 0.54 ms | **0.25 ms** | n/a |
| ICON_TABLE fps | 27.3 | 26.8 | 27.8 | 0.3 (collapse) |
| ICON render p95 | 23 ms | 31 ms | 14 ms | n/a |

Firefox worker figures were measured before the ack-timeout fix below; with the fix the session
no longer collapses completely, but it still stalls repeatedly and stays far behind the main thread.

- **Chromium:** no measurable gain. The work moves to another thread but costs the same per KB;
  tails are equal or slightly worse (extra bitmap round trip).
- **Firefox:** the worker is CPU-bound. A profile showed the worker thread **97.8 % busy** in
  software rasterization (`fill`, `fillText`, `drawImage`, plus large `memcpy`), while Firefox's
  accelerated canvas thread (`CanvasRenderer`) sat at **0.6 %**. On the main thread Firefox uses
  accelerated Canvas2D and is the fastest browser; inside the worker the same canvas work runs in
  software, several times slower.

`about:support` reports *Accelerated Canvas2D: available* and `gfx.canvas.remote.allow-offscreen`
is `true`, yet the worker's canvas was not accelerated. Ruled out by experiment, each without
effect:

- `gfx.canvas.accelerated.min-size = 1` (our canvases are created at 1×1 before resizing)
- `gfx.canvas.accelerated.profile-fallback-ratio = 1.0` and `profile-cache-miss-ratio = 1.0`
  (Firefox's per-canvas demotion heuristic)
- font loading in the worker (0.6 % of worker time in the profile)

Why Firefox renders a worker `OffscreenCanvas` in software in this configuration is **not
known**. The machine has no Firefox GPU process (`FEATURE_FAILURE_NO_GPU_PROCESS`), which may be
related; no documentation was found that confirms it.

### Why it is off

1. No gain in Chromium.
2. In Firefox it trades GPU rendering for software rendering: strictly worse.
3. The only remaining argument for it, a free main thread for input during heavy redraws, has not
   been measured; the benchmark scrolls server-side without user input.

### Fixed while investigating (apply to every session, worker or not)

The worker made the client slower, and that exposed three server-side bugs that also hit real
users on slow connections:

- **Constant-id recycling inside a frame** (`LRUDrawConstantPoolCache`, `DrawConstantPool`,
  `WebImage`): nested `DRAW_WEBIMAGE` encodes reset the per-frame overflow guard, so large
  accumulated frames recycled constant ids mid-frame. The browser applies a frame's outer
  constants first and nested ones lazily, so ids resolved to the wrong constant: the UI
  corruption seen on slow connections. Fixed with a per-frame stamp on cache entries: an entry
  used in the current frame is never evicted; the cache grows instead.
- **Ack-timeout cache reset** (`AbstractPaintDispatcher`): after 5 s without a paint ack the
  server reset its constant cache and re-sent everything (fonts included, frames of several MB)
  to the client that was already too slow, a feedback loop that collapsed slow sessions to under
  1 fps. The timeout now re-enables sending without a reset.
- **Cache reset race** (`AbstractPaintDispatcher`): a client `repaint`, resize handshake or
  recording start reset the cache on the event thread, possibly in the middle of an encode on the
  sender thread. Resets are now deferred to the start of the next sender cycle.

### Revisiting

Worth another look when a browser accelerates `OffscreenCanvas` in workers on the target
platform, or when input latency during heavy redraws becomes a measured problem. Verify first
with a Firefox Profiler capture including the *DOM Worker* threads: the `CanvasRenderer` thread
must be busy and the worker must not spend its time in raster code. Then compare
`?ddWorker=true` against the default with `bench-browser.fish`, alternating runs in the same
window, and accept the worker only if it wins in both browsers.

---

<div align="center">

**Maintained by [Manticore Projects](https://manticore-projects.com)**

*Building enterprise financial software for banks and insurances since 2011.*

</div>