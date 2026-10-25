# Suramadu DirectDraw Benchmark

## App entry (`suramadu.config`)

```json
"/benchmark" : {
  "path" : "/benchmark",
  "webFolder" : "${webswing.rootDir}/webroot",
  "security" : {
    "module" : "NONE",
    "classPath" : [ ]
  },
  "icon" : "${webswing.rootDir}/dist/icon.png",
  "langFolder" : "${webswing.rootDir}/lang",
  "swingConfig" : {
    "jreExecutable" : "${java.home}/bin/java",
    "javaVersion" : "${java.version}",
    "classPathEntries" : [ "${webswing.rootDir}/dist/benchmark-lib/*" ],
    "vmArgs": "-Xmx512m -Dwebswing.benchmarkStatsFile=${webswing.rootDir}/logs/benchmark.csv -Dbenchmark.sceneSeconds=30",
    "fontConfig" : {
      "monospace" : "${webswing.rootDir}/fonts/RobotoMono-Regular.ttf",
      "DejaVu Sans Mono" : "${webswing.rootDir}/fonts/DejaVuSansMono.ttf",
      "DejaVu Sans Mono Oblique" : "${webswing.rootDir}/fonts/DejaVuSansMono-Oblique.ttf",
      "DejaVu Sans Mono Bold" : "${webswing.rootDir}/fonts/DejaVuSansMono-Bold.ttf",
      "DejaVu Sans Mono Bold Oblique" : "${webswing.rootDir}/fonts/DejaVuSansMono-BoldOblique.ttf",
      "DejaVu Sans" : "${webswing.rootDir}/fonts/DejaVuSans.ttf",
      "DejaVu Sans Oblique" : "${webswing.rootDir}/fonts/DejaVuSans-Oblique.ttf",
      "DejaVu Sans Bold" : "${webswing.rootDir}/fonts/DejaVuSans-Bold.ttf",
      "DejaVu Sans Bold Oblique" : "${webswing.rootDir}/fonts/DejaVuSans-BoldOblique.ttf",
      "dialog" : "${webswing.rootDir}/fonts/Roboto-Regular.ttf",
      "dialoginput" : "${webswing.rootDir}/fonts/RobotoMono-Regular.ttf",
      "serif" : "${webswing.rootDir}/fonts/RobotoSlab-Regular.ttf",
      "Roboto Black" : "${webswing.rootDir}/fonts/Roboto-Black.ttf",
      "Roboto Black Italic" : "${webswing.rootDir}/fonts/Roboto-BlackItalic.ttf",
      "Roboto Bold" : "${webswing.rootDir}/fonts/Roboto-Bold.ttf",
      "Roboto Bold Italic" : "${webswing.rootDir}/fonts/Roboto-BoldItalic.ttf",
      "Roboto Italic" : "${webswing.rootDir}/fonts/Roboto-Italic.ttf",
      "Roboto Light" : "${webswing.rootDir}/fonts/Roboto-Light.ttf",
      "Roboto Light Italic" : "${webswing.rootDir}/fonts/Roboto-LightItalic.ttf",
      "Roboto Medium" : "${webswing.rootDir}/fonts/Roboto-Medium.ttf",
      "Roboto Medium Italic" : "${webswing.rootDir}/fonts/Roboto-MediumItalic.ttf",
      "Roboto Regular" : "${webswing.rootDir}/fonts/Roboto-Regular.ttf",
      "Roboto Mono Regular" : "${webswing.rootDir}/fonts/RobotoMono-Regular.ttf",
      "Roboto Mono Bold" : "${webswing.rootDir}/fonts/RobotoMono-Bold.ttf",
      "Roboto Mono Italic" : "${webswing.rootDir}/fonts/RobotoMono-Italic.ttf",
      "Roboto Mono Bold Italic" : "${webswing.rootDir}/fonts/RobotoMono-BoldItalic.ttf"
    },
    "swingSessionTimeout" : 1800,
    "isolatedFs" : false,
    "debug" : true,
    "javaFx" : true,
    "directdraw" : true,
    "allowDelete" : true,
    "allowDownload" : true,
    "allowAutoDownload" : true,
    "allowUpload" : true,
    "allowJsLink" : false,
    "launcherType" : "Desktop",
    "launcherConfig" : {
      "mainClass" : "org.webswing.benchmark.Benchmark",
      "args" : ""
    },
    "allowedCorsOrigins" : [ "*" ],
    "userDir" : "${user}",
    "transparentFileSave" : false,
    "clearTransferDir" : true,
    "transparentFileOpen" : false,
    "transferDir" : "${user}/upload",
    "timeoutIfInactive" : true,
    "allowLocalClipboard" : true,
    "homeDir" : "dist"
  },
  "enabled" : true,
  "maxClients" : 5,
  "allowStealSession" : false,
  "goodbyeUrl" : "/",
  "name" : "Benchmark",
  "autoLogout" : false,
  "sessionMode" : "CONTINUE_FOR_BROWSER",
  "uploadMaxSize" : 100,
  "webHomeDir" : "dist"
}
```

(Drop `suramadu-benchmark.jar` into `benchmark-lib/`.)

## Running

1. Start the server via script `org/webswing/benchmark/bench-browser.fish` to ensure same screen size. The app drives itself through
   TEXT_TABLE → ICON_TABLE → VECTOR → IMAGE_FLOOD → IDLE (30 s each), prints
   `BENCHMARK DONE`, then idles. Do not minimise the browser tab — a hidden tab
   throttles rAF-driven acks and poisons the client numbers.
2. `python analyze_benchmark.py logs/benchmark.csv`
3. Saturation variant (max pipeline throughput instead of latency at 30 fps):
   add `-Dwebswing.drawDelayMs=1` to vmArgs and rerun — fps becomes the headline
   number; latency percentiles become meaningless (always saturated).

## What the columns mean

- `extract_ms` — under AWT treeLock + webPaintLock: what paints block on.
- `encode_ms` — ZPNG + protobuf on the sender thread (off-lock).
- `render_ms` — client-measured decode + canvas replay (duration on the client
  clock; no clock-sync involvement).
- `rtt_ms` — sendTimestamp → timestamp-echo arrival, both ends on the SERVER
  clock. Includes network both ways and client processing;
  `rtt − render ≈ network + queueing`.
- `kb/frame` — DirectDraw payload bytes.
- `fps` — achieved frames in the scene window (ack-gated; capped by
  `webswing.drawDelayMs`, default 33 ms → ~30).

## Caveats

- One frame in flight by protocol design: fps and rtt are coupled. For latency,
  read percentiles at default cadence; for throughput, use the saturation run.
- `render_ms`/ACK rows depend on the frontend actually populating the
  `timestamps` echo (`TimestampsMsgIn`). FRAME rows appearing without ACK rows
  means the client never fills it — then the frontend needs the echo restored
  before metrics 2 and 3 exist.
- Instrumentation cost is one `System.nanoTime()` pair per frame when disabled
  is wrong — when DISABLED it is one static null check; when enabled, two
  nanoTime calls, one CSV line, flush every 64 frames.
- Compare runs only with identical scenes, screen size and browser; DPR changes
  the pixel volume.
