# suramadu-app-toolkit-java27

JDK 27 variant of the Suramadu app toolkit (successor to `suramadu-app-toolkit-java11`).

## What changed vs. java11

JDK 27 removed `sun.awt.AppContext` (applet-era cleanup):

- `SunToolkit.postEvent(AppContext, AWTEvent)` → `SunToolkit.postEvent(AWTEvent)`;
  `targetToAppContext()` is gone. `WebToolkit27.deliverFocus()` uses the 1-arg form.
- `WebComponentPeer.postEvent()` in the **base** toolkit still calls
  `targetToAppContext()` — would throw `NoSuchMethodError` on first event. All five
  JDK-27 peers (`Web*Peer27`) override the package-private `postEvent(AWTEvent)` so the
  inherited body is never resolved. Works because the five versioned peers are the only
  concrete `WebComponentPeer` subclasses the toolkit instantiates, and both jars share
  the swing-boot classloader (same runtime package).
- `SunToolkit.getDataTransferer()` is gone; `WebToolkit27.getDataTransferer()` now
  overrides the `sun.awt.ComponentFactory` default method, which is what
  `DataTransferer.getInstance()` calls on JDK 27.

Verified unchanged against Temurin 27+35: `sun.awt.FontConfiguration` (ctor +
abstract set), `sun.font.SunFontManager`, `sun.awt.datatransfer.DataTransferer`
(full abstract surface), `sun.nio.cs.UTF_32`, `SurfaceManager.getPrimarySurfaceData()`,
`ComponentPeer.requestFocus(..., FocusEvent.Cause)`, and the private
`KeyboardFocusManager.shouldNativelyFocusHeavyweight(..., FocusEvent.Cause)` used
reflectively. `WebDataTransfer`, `WebFontManager`, `WebFontConfiguration` are
byte-identical to the java11 module.

## Build constraints

- This module: JDK 27 toolchain (set in `build.gradle`), with compile-time
  `--add-exports` (JDK 17+ javac makes internal-API access a hard error).
- Base `:suramadu-app-toolkit` and `:suramadu-app-toolkit-java11` must **stay on their
  current (≤ JDK 25) toolchain** — they compile against `targetToAppContext`, which no
  longer exists. The base bytecode runs fine on a JDK 27 JVM as long as the overridden
  `postEvent` body is never executed.

## Runtime flags (app JVM on JDK 27)

```
--add-opens   java.desktop/java.awt=ALL-UNNAMED
--add-exports java.desktop/java.awt.peer=ALL-UNNAMED
--add-exports java.desktop/sun.awt=ALL-UNNAMED
--add-exports java.desktop/sun.awt.datatransfer=ALL-UNNAMED
--add-exports java.desktop/sun.awt.image=ALL-UNNAMED
--add-exports java.desktop/sun.font=ALL-UNNAMED
--add-exports java.desktop/sun.java2d=ALL-UNNAMED
--add-exports java.base/sun.nio.cs=ALL-UNNAMED
```

Plus whatever the java11 launch profile already passes for the base toolkit
(e.g. `sun.awt.shell`, `sun.java2d.pipe`, `sun.print` if used at runtime).

## Still needed outside this module

- `settings.gradle`: include `':suramadu-app-toolkit-java27'`.
- The toolkit selection (launcher/bootstrap/modpatch) must map Java ≥ 27 (or ≥ the
  AppContext-removal release) to `org.webswing.toolkit.WebToolkit27` /
  `org.webswing.toolkit.ge.WebGraphicsEnvironment27`.
- `:suramadu-modpatch:modpatch-java-desktop` must be validated against the JDK 27
  `java.desktop` — not covered by this package.
