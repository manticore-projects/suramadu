# suramadu changelog

Changelog of suramadu.

## 26.4.7 (2026-08-06)

## 26.4.6 (2026-08-06)

### Features

-  present the source offer on every entry point ([f8347](https://github.com/manticore-projects/suramadu/commit/f83470508695d75) Andreas Reichel)  

### Dependency updates

- update node packages ([9d426](https://github.com/manticore-projects/suramadu/commit/9d426aa4882593e) Andreas Reichel)  
### Other changes


## 26.4.5 (2026-07-23)

### Bug Fixes

-  **security**  javascript module updates ([12a45](https://github.com/manticore-projects/suramadu/commit/12a455724050a4a) Andreas Reichel)  
-  **security**  javascript module updates ([a3c5c](https://github.com/manticore-projects/suramadu/commit/a3c5c1834dad771) Andreas Reichel)  
-  **security/shiro**  fully dynamic fail-closed role discovery ([8beb7](https://github.com/manticore-projects/suramadu/commit/8beb788e1027992) Andreas Reichel)  
-  **security**  resolve open code-scanning alerts (path traversal, open redirect, response splitting, regex sanitization) ([9332e](https://github.com/manticore-projects/suramadu/commit/9332eac7eba1977) Andreas Reichel)  
-  **security**  update node packages ([87b6b](https://github.com/manticore-projects/suramadu/commit/87b6bc0b6dd8351) Andreas Reichel)  
-  **security**  Update Node modules ([10c65](https://github.com/manticore-projects/suramadu/commit/10c656c24c0e501) Andreas Reichel)  

## 26.4.4 (2026-06-17)

### Bug Fixes

-  **security**  Update node modules, JUnit6 ([28c3c](https://github.com/manticore-projects/suramadu/commit/28c3cf5b2bc4409) Andreas Reichel)  

### Other changes


## 26.4.3 (2026-06-04)

### Features

-  render a subtle outline around the logo ([89749](https://github.com/manticore-projects/suramadu/commit/897491b9c14d810) Andreas Reichel)  
-  avoid UNSAFE ([ad336](https://github.com/manticore-projects/suramadu/commit/ad3363f47d489e6) Andreas Reichel)  
-  use ZGC, JEMALLOC and colorise log file ([7e06c](https://github.com/manticore-projects/suramadu/commit/7e06cbf326dc9ec) Andreas Reichel)  

### Bug Fixes

-  **deps**  override uuid ^11.1.1 to clear sockjs/webpack-dev-server advisory ([8f331](https://github.com/manticore-projects/suramadu/commit/8f331f9e085bcb9) Andreas Reichel)  
-  **directdraw**  prevent EDT freeze when scrolling long text ([50074](https://github.com/manticore-projects/suramadu/commit/500749134bb60bf) Andreas Reichel)  
-  NPM TMP has Path Traversal enabling directory escape ([b76cf](https://github.com/manticore-projects/suramadu/commit/b76cf69b65f832c) Andreas Reichel)  
-  **classloader**  strip LocalVariableTypeTable attributes on ClassFormatError to handle JDK 25 strict verifier ([80b82](https://github.com/manticore-projects/suramadu/commit/80b82d20f8abde9) Andreas Reichel)  
-  **classloader**  strip LocalVariableTypeTable attributes on ClassFormatError to handle JDK 25 strict verifier ([17749](https://github.com/manticore-projects/suramadu/commit/17749ca915d82b8) Andreas Reichel)  
-  **security**  wire isOriginAllowed() to SecuredPathConfig.getAllowedCorsOrigins(), warn on wildcard ([c465e](https://github.com/manticore-projects/suramadu/commit/c465e41e499ba01) Andreas Reichel)  

### Other changes

**release 26.4.1**

* Signed-off-by: Andreas Reichel &lt;andreas@manticore-projects.com&gt; 

[c8465](https://github.com/manticore-projects/suramadu/commit/c84653892f10825) Andreas Reichel *2026-05-19 07:32:49*

**release 26.4.1**

* Signed-off-by: Andreas Reichel &lt;andreas@manticore-projects.com&gt; 

[5c220](https://github.com/manticore-projects/suramadu/commit/5c2202e40ee2cb9) Andreas Reichel *2026-05-19 07:29:39*


## 26.4.1 (2026-05-19)

### Bug Fixes

-  **security**  make Secure cookie flag conditional on HTTPS, add frame-src 'self' to CSP ([29fcc](https://github.com/manticore-projects/suramadu/commit/29fccc295b60224) Andreas Reichel)  
-  consolidate `--patch-module java.desktop` and avoid duplicates ([cec18](https://github.com/manticore-projects/suramadu/commit/cec1856440bb500) Andreas Reichel)  
-  **security**  avoid old Jackson libraries ([31d60](https://github.com/manticore-projects/suramadu/commit/31d60a4942ab41f) Andreas Reichel)  
-  **security**  guard listFiles() null return and canonicalize children against symlink escape ([0d0cf](https://github.com/manticore-projects/suramadu/commit/0d0cff54d74d0ab) Andreas Reichel)  
-  **security**  add Secure and SameSite=Strict to checkCookie probe cookie ([3d10c](https://github.com/manticore-projects/suramadu/commit/3d10cbb1162a367) Andreas Reichel)  
-  **security**  sanitize Origin header at CORS sink to resolve CRLF taint warning ([558e7](https://github.com/manticore-projects/suramadu/commit/558e79bedd93f26) Andreas Reichel)  

## 26.4 (2026-05-17)

### Features

-  **headless**  truly headless on JDK 21+ via java.desktop patch ([61409](https://github.com/manticore-projects/suramadu/commit/61409b424bfc166) Andreas Reichel)  

### Bug Fixes

-  Warn about JetBrains JDK and accept fonts from WebSwing ROOT ([5f196](https://github.com/manticore-projects/suramadu/commit/5f1960a485e6e9b) Andreas Reichel)  

### Other changes

**`fix(docker): pin base image digests, purge gnupg2/wget/shadow after use, replace healthcheck with bash /dev/tcp, suppress unfixable OS CVEs in .snyk`**

* Signed-off-by: Andreas Reichel &lt;andreas@manticore-projects.com&gt; 

[65d77](https://github.com/manticore-projects/suramadu/commit/65d774947c01939) Andreas Reichel *2026-05-14 01:00:52*

**`fix(path-traversal): canonicalise font config and USERPROFILE paths, bound font files to trusted directory`**

* Signed-off-by: Andreas Reichel &lt;andreas@manticore-projects.com&gt; 

[d00c5](https://github.com/manticore-projects/suramadu/commit/d00c569843c6631) Andreas Reichel *2026-05-14 00:50:07*

**`fix(cookies): add Secure and SameSite=Strict attributes, replace deprecated escape() with encodeURIComponent`**

* Signed-off-by: Andreas Reichel &lt;andreas@manticore-projects.com&gt; 

[d8b35](https://github.com/manticore-projects/suramadu/commit/d8b35f05f859589) Andreas Reichel *2026-05-14 00:42:25*

**`fix(cors): reject CRLF-tainted Origin headers and fix scheme-stripping in isSameOrigin`**

* Signed-off-by: Andreas Reichel &lt;andreas@manticore-projects.com&gt; 

[9cd73](https://github.com/manticore-projects/suramadu/commit/9cd73607d9c4818) Andreas Reichel *2026-05-14 00:38:53*

**`fix(xss): sanitise webswing version string and redirect URLs against DOM-based XSS`**

* Signed-off-by: Andreas Reichel &lt;andreas@manticore-projects.com&gt; 

[d12ee](https://github.com/manticore-projects/suramadu/commit/d12ee3fd6ed17a1) Andreas Reichel *2026-05-14 00:34:45*

**Release 26.3**

* Signed-off-by: Andreas Reichel &lt;andreas@manticore-projects.com&gt; 

[30979](https://github.com/manticore-projects/suramadu/commit/309795de3d1e5f7) Andreas Reichel *2026-05-09 05:06:31*


## 26.3 (2026-05-09)

### Features

-  **theme**  replace XFWM4 bitmap decorator with pure Java2D vector chrome ([b3c80](https://github.com/manticore-projects/suramadu/commit/b3c800344d7b7d5) Andreas Reichel)  
-  **theme**  replace XFWM4 bitmap decorator with pure Java2D vector chrome ([717fa](https://github.com/manticore-projects/suramadu/commit/717fa9d90cb19b1) Andreas Reichel)  
-  **theme**  replace XFWM4 bitmap decorator with pure Java2D vector chrome ([75861](https://github.com/manticore-projects/suramadu/commit/7586109c3d0cd78) Andreas Reichel)  
-  **server**  harden Jetty for flaky VPN and high concurrency ([a4544](https://github.com/manticore-projects/suramadu/commit/a45446417f1d161) Andreas Reichel)  

### Bug Fixes

-  Uncontrolled Recursion in BCEL ([012fd](https://github.com/manticore-projects/suramadu/commit/012fd7b45e274a3) Andreas Reichel)  

### Other changes


## 26.2 (2026-04-27)

### Breaking changes

-  **security**  regenerate protobuf gencode to fix GHSA-h4h5-3hr4-j3g2 ([74096](https://github.com/manticore-projects/suramadu/commit/74096068e4c2aa1) Andreas Reichel)  

### Features

-  use Conscrypt to work around overly strict SNI Hostname checks ([d5ece](https://github.com/manticore-projects/suramadu/commit/d5eceb19f1ffae5) manticore-projects)  
-  use Java 17 language features ([54abb](https://github.com/manticore-projects/suramadu/commit/54abb2cb6e89546) manticore-projects)  
-  import any SSL certificates (e.g. AD) into the JVM's keystore ([dd97f](https://github.com/manticore-projects/suramadu/commit/dd97f9a6ecfdfaf) Andreas Reichel)  
-  run `npm audit fix` automatically when security issues found ([85c03](https://github.com/manticore-projects/suramadu/commit/85c03d31d2539b8) Andreas Reichel)  
-  **security**  modernize shiro api and implement dynamic hot-reload ([93437](https://github.com/manticore-projects/suramadu/commit/934379831f165d7) Andreas Reichel)  
-  add pre-compressed resource serving (brotli/gzip) ([4199f](https://github.com/manticore-projects/suramadu/commit/4199fcbfa7b76c2) Andreas Reichel)  
-  SVG icon support via jsvg, fpng-java encoding, 96px cap ([59fe3](https://github.com/manticore-projects/suramadu/commit/59fe38fc13240a8) Andreas Reichel)  

### Bug Fixes

-  **security**  regenerate protobuf gencode to fix GHSA-h4h5-3hr4-j3g2 ([74096](https://github.com/manticore-projects/suramadu/commit/74096068e4c2aa1) Andreas Reichel)  
-  resolve remaining SpotBugs findings ([e01c0](https://github.com/manticore-projects/suramadu/commit/e01c007bfdaa7f1) manticore-projects)  
-  resolve all 8 Semgrep SAST findings ([6eeaf](https://github.com/manticore-projects/suramadu/commit/6eeafe05c00452a) Andreas Reichel)  
-  resolve all 8 Semgrep SAST findings ([e0489](https://github.com/manticore-projects/suramadu/commit/e0489fb53dcf733) Andreas Reichel)  
-  resolve 7 of 8 Semgrep SAST findings from initial scan ([4b873](https://github.com/manticore-projects/suramadu/commit/4b8734c1ebe9982) Andreas Reichel)  
-  **security**  prevent SSRF via URL scheme allowlist on openConnection calls ([197be](https://github.com/manticore-projects/suramadu/commit/197be9a4dbaa62b) Andreas Reichel)  
-  **security**  Harden input sanitization and output encoding ([95ba2](https://github.com/manticore-projects/suramadu/commit/95ba2aaa88e246a) Andreas Reichel)  
-  typescript, not javascript ([a1fa6](https://github.com/manticore-projects/suramadu/commit/a1fa615e4f5d577) manticore-projects)  
-  **security**  npm audit fix ([cba0b](https://github.com/manticore-projects/suramadu/commit/cba0b91cebd21b1) manticore-projects)  
-  **security**  npm audit fix ([5b634](https://github.com/manticore-projects/suramadu/commit/5b63434de7d2007) manticore-projects)  
-  harden Main and WebToolkit bootstrap against path traversal and resource leaks ([29513](https://github.com/manticore-projects/suramadu/commit/2951302bcd08d45) manticore-projects)  
-  accept legacy 3-segment file IDs for backward compatibility ([d6358](https://github.com/manticore-projects/suramadu/commit/d635878770b572b) manticore-projects)  
-  harden JwtUtil against multiple cryptographic and validation vulnerabilities ([ab89e](https://github.com/manticore-projects/suramadu/commit/ab89e19cb1c9e5a) manticore-projects)  
-  harden AbstractResourceHandler against path traversal and related vulnerabilities ([9bf9e](https://github.com/manticore-projects/suramadu/commit/9bf9e2102a3a25c) manticore-projects)  
-  harden security module chain against multiple vulnerabilities ([6cd67](https://github.com/manticore-projects/suramadu/commit/6cd673faa60baeb) manticore-projects)  
-  harden FileTransferHandlerImpl against multiple security vulnerabilities ([92f7f](https://github.com/manticore-projects/suramadu/commit/92f7fdca2268558) Andreas Reichel)  
-  replace insecure AES/ECB encryption with AES/CBC in JwtUtil ([d4eea](https://github.com/manticore-projects/suramadu/commit/d4eea33d241e6c7) Semgrep Autofix)  
-  engage `DOMPurify` to prevent DOM-based Cross-site Scripting (XSS) ([702fe](https://github.com/manticore-projects/suramadu/commit/702fe64eba1e768) Andreas Reichel)  

### Other changes

**Update README.md**

* Signed-off-by: manticore-projects &lt;andreas@manticore-projects.com&gt; 

[0b63e](https://github.com/manticore-projects/suramadu/commit/0b63e81f475c78d) manticore-projects *2026-03-30 09:21:43*

**Fix XSS vulnerability in FileTransferHandlerImpl file upload error message**

* Sanitize user-controlled filename before writing it to HTTP response to prevent XSS attacks. 
* ## Changes 
* - Added &#x60;escapeHtml()&#x60; helper method to sanitize HTML special characters (&#x60;&amp;&#x60;, &#x60;&lt;&#x60;, &#x60;&gt;&#x60;, &#x60;&quot;&#x60;, &#x60;&#x27;&#x60;) 
* - Applied HTML escaping to the &#x60;filename&#x60; variable in the file size error message on line 154 
* ## Why 
* The &#x60;filename&#x60; parameter originates from user-uploaded file metadata and was being written directly to the HTTP response without sanitization. An attacker could craft a malicious filename containing JavaScript (e.g., &#x60;&lt;script&gt;alert(&#x27;XSS&#x27;)&lt;/script&gt;.txt&#x60;) that would execute in the victim&#x27;s browser when the file size error is displayed. HTML-escaping the filename ensures that any special characters are rendered as text rather than interpreted as HTML/JavaScript. 
* ## Semgrep Finding Details 
* Untrusted input could be used to tamper with a web page rendering, which can lead to a Cross-site scripting (XSS) vulnerability. XSS vulnerabilities occur when untrusted input executes malicious JavaScript code, leading to issues such as account compromise and sensitive information leakage. To prevent this vulnerability, validate the user input, perform contextual output encoding or sanitize the input. 
* @18080123 requested Semgrep Assistant generate this pull request to fix [a finding](https://semgrep.dev/orgs/andreas_manticore_projects_com/findings/733627377) from the detection rule [java.servlets.security.servletresponse-writer-xss-deepsemgrep.servletresponse-writer-xss-deepsemgrep](https://semgrep.dev/r/java.servlets.security.servletresponse-writer-xss-deepsemgrep.servletresponse-writer-xss-deepsemgrep). 

[5212d](https://github.com/manticore-projects/suramadu/commit/5212d1776717340) Semgrep Autofix *2026-03-27 13:38:55*

**Update README.md**

* Signed-off-by: manticore-projects &lt;andreas@manticore-projects.com&gt; 

[63076](https://github.com/manticore-projects/suramadu/commit/630767662ec1142) manticore-projects *2026-03-26 14:58:28*


## 26.1 (2026-03-23)

### Features

-  drop Maven build support ([7f3dc](https://github.com/manticore-projects/suramadu/commit/7f3dcad8d0d0ff7) manticore-projects)  
-  migrate to Jetty 12 ([90df4](https://github.com/manticore-projects/suramadu/commit/90df491c14dc8d2) manticore-projects)  

### Other changes


## 26.0 (2026-03-22)

### Features

-  remove JDK8 stuff and improve the log output ([64bd4](https://github.com/manticore-projects/suramadu/commit/64bd4d10887e598) manticore-projects)  
-  Migrate WebSwing from JDK 11 to JDK 21 ([72884](https://github.com/manticore-projects/suramadu/commit/72884748fda141b) Andreas Reichel)  
-  envelope password when 2FA provided ([a3422](https://github.com/manticore-projects/suramadu/commit/a342272287844a4) manticore-projects)  
-  envelope password when 2FA provided ([84731](https://github.com/manticore-projects/suramadu/commit/84731af5b3a2c49) Andreas Reichel)  
-  Modernize WebSwing 20.2 branch ([b0a5e](https://github.com/manticore-projects/suramadu/commit/b0a5e88a8855da0) Andreas Reichel)  

### Bug Fixes

-  fetch all roles at once ([cccb2](https://github.com/manticore-projects/suramadu/commit/cccb2827b785a48) manticore-projects)  
-  increase timeout ([95661](https://github.com/manticore-projects/suramadu/commit/956619217eb2dd0) manticore-projects)  
-  increase timeout ([102b4](https://github.com/manticore-projects/suramadu/commit/102b464c5c14f5d) manticore-projects)  
-  catch Exception when PW is not encoded ([3fa0b](https://github.com/manticore-projects/suramadu/commit/3fa0bd32817b2f5) manticore-projects)  
-  scrollable application chooser ([1004f](https://github.com/manticore-projects/suramadu/commit/1004f366b4d4a2f) Andreas Reichel)  

### Other changes

**remove lock file**


[64880](https://github.com/manticore-projects/suramadu/commit/648808d7f2ee4b8) manticore-projects *2025-09-22 02:08:14*

**Create maven-publish.yml**


[cca4f](https://github.com/manticore-projects/suramadu/commit/cca4f7dd254a60c) manticore-projects *2023-11-26 05:30:15*

**Revert to latest released version**


[439d7](https://github.com/manticore-projects/suramadu/commit/439d720baa8558c) Andreas Reichel *2023-11-26 05:26:13*

**Update Log4J2 to 2.17.0**


[4387d](https://github.com/manticore-projects/suramadu/commit/4387d672739c9c3) Andreas Reichel *2021-12-18 12:56:16*

**Rewrite ShiroSecurityModule in a way that the WebSwingUser can be serialized.**


[cd02d](https://github.com/manticore-projects/suramadu/commit/cd02d97a60ea38f) Andreas Reichel *2021-12-18 07:30:42*

**Update dependecies/3rd part libraries**


[cff4d](https://github.com/manticore-projects/suramadu/commit/cff4d4cc470ccea) Andreas Reichel *2021-12-14 08:51:59*

**Fix Shiro Dependency**


[aa778](https://github.com/manticore-projects/suramadu/commit/aa7783e7ad0233e) Andreas Reichel *2021-12-13 15:02:54*

**VBox customization**

* set manticore-projects&#x27; colours, fonts and icons 
* set VBox title 
* add ShiroSecurityModule 
* enhance default webswing.config 

[ca96b](https://github.com/manticore-projects/suramadu/commit/ca96b37c999ad94) Andreas Reichel *2021-12-13 14:57:25*

**release 20.2.5**

* 3rd party security fixes (CVE-2021-28164 CVE-2020-13936 CVE-2021-28165) 
* resolve @342: ssl default setup, 
* resolve @341: Recording &amp; Mirroring user consent 
* resolve @340: Responsive support for login dialog 
* resolve @339: sso logout redirect from selector page, OIDC fix, logging fix 
* resolve @337: Failed to send msg to application: The remote endpoint was in state [BINARY_FULL_WRITING] which is an invalid state for called method [SOF-000603] 
* resolve @336: Logs cannot be downloaded in admin console; + fixed playback and mirror event handling; close recoding file stream when stopped 
* resolve @335: Session logs not working in admin console; 
* resolve @333: Disconnected client should try reconnecting to the same instance 
* resolve @328: Update /rest/activeSessionsCount to distinguish between connected/disconnected 
* resolve @327: HtmlPanel Bleeding Outside of Frame [QUA-000584] - fixed overflow of HtmlPanel inside JInternalFrame inside JDesktopPane inside JScrollPane; added POC to SwingSet3 InternalFrameDemo 
* resolve @322: Make dialogs more responsive 
* resolve @309: Touch - hidden keyboard leaves canvas part white - various fixes related to touch scaling and keyboard 
* resolve @308: Touch scaling with CWM - keyboard fixes for iOS 
* resolve @288: Webswing OpenAPI optimization - changed appPath from path parameter to query parameter 
* resolve @269: Added drainMode switch for session pools 
* resolve @179: CWM: 2html panels in a internal frame 

[d3a05](https://github.com/manticore-projects/suramadu/commit/d3a05286385ec24) Viktor Meszaros *2021-04-05 17:57:46*

**release 20.2.4**

* 3rd party vulnerability (CVE-2020-27223, CVE-2021-25122, CVE-2021-25329) 
* resolves #295: Cluster resilience improvements 
* resolves #307: Webswing Session Pool Not Working With SSL Cert [QUA-000542] [D4T-000561] 
* resolves #308: Touch scaling with CWM - use CSS zoom instead of viewport 
* resolves #310: Clipboard and DnD operations don&#x27;t create a copy of Transferable data [D4T-000556], reflective access warning fix, -h 0.0.0.0 app connection fix 
* resolves #311: Export javascript function for logout [D4T-000570] 
* resolves #312: Remove Logout buttons from dialogs if using NONE security module 
* resolves #314: Optimize Caret switch causing infinite cycle on EDT [A-D-000534] 
* resolves #315: fixed &quot;Start again&quot; and &quot;autoLogout&quot; issues 
* resolves #316: admin console permissions handling 
* resolves #319: Customization to allow canvas to be visible while reconnecting 
* resolves #324: readonly fix  [VER-000600] 
* resolves #325 fix of remove method [VER-000602] 
* resolves #326: Memory allocation increase in 20.2 compared to 20.1, increase default websocket buffer size 

[56417](https://github.com/manticore-projects/suramadu/commit/56417b424e9d075) Viktor Meszaros *2021-03-07 12:59:24*

**release 20.2.3**

* 3rd party security fix (CVE-2020-17523) 
* resolves @295: resilience improvements 
* resolves @230: fixed mirror and playback (unrelated to jquery) 
* resolves @302: show selector.lang 
* resolves @298: switching all ws clients to tyrus jdk container, log improvements 
* resolves @304: Log info when max client reached [ALL-000509] 
* resolves @303: Optimize frontend reconnect - set timeout of 7000ms for util 
* resolves @302: when 1 or less langs - define selector.lang to empty string 
* resolves @287: Inactive window canvas brought to front on tooltip show 
* resolves @301: Robot getRGBPixels fix [D4T-000503] 
* resolves @300: Admin server websocket truststore and hostVerifier config 
* resolves @298: admin console infinite rest looping when server stops 
* resolves @297: Tomcat: messages related to memory leak when app undeployed/stopped [CA,-000521] 
* resolves @294: Websocket connections lingering after reconnect [ALL-000510] 

[244fa](https://github.com/manticore-projects/suramadu/commit/244fabaf8956fe0) Viktor Meszaros *2021-02-06 00:21:53*

**release 20.2.2**

* 3rd party security fix (CVE-2020-35490,CVE-2020-25649,CVE-2020-17527) 
* resolves @279: Mirror view for cluster fix 
* resolves @274: Reconnect issues with unstable internet connection [FLU-000436] 
* resolves @270: Permission handling improvements in admin console 
* resolves @281: Autologout doesn&#x27;t log out on &quot;start again&quot; [D4T-000485] 
* resolves @172: Audio improvements - notify listeners about audio playback STOP 
* resolves @276: Prevent firefox password manager from popping-up 
* resolves @280: java.lang.IllegalStateException: Cipher not initialized 
* resolves @265: Make webswing/admin console/session pool property files optional 
* resolves @278: Applet: fails to register applet instance using jslink 
* resolves @271: Make wheel event on canvas configurable 
* resolves @273: fix access mapping security extension 
* resolves @272: App process waits for swingSessionTimeout to exit when using custom shutdown listener 
* resolves @262: Strict-Transport-Security header has wrong value 
* resolves @255: Copy/paste not working for non-input components with accessibility 

[f3639](https://github.com/manticore-projects/suramadu/commit/f36390eef9329e1) Viktor Meszaros *2021-01-11 09:40:46*

**release 20.2.1**

* fix CVE-2020-27218, CWE-200, CVE-2020-11979, CVE-2020-1945 
* resolves @259: Relative paths for properties file don&#x27;t work 
* resolves @258: Application shuts down if timeoutIfInactive is false and user is inactive 
* resolves @234: IllegalStateException: KeyStores with multiple certificates 
* resolves @240: Prevent rename folder with .. 
* resolves @250: Fixed inputting decimal separators from the numeric pad 
* resolves @238: Added better support for different virtual keyboards on touch devices 
* resolves @254: Accessibility not working when server running on Linux/Mac platform 
* resolves @251: Fixed pinch to zoom zooming both the Webswing content and the browser; Wheel event ignored in undocked window; JS error when focusing undocked window 
* resolves @253: Fixed admin console number field saving 0 when field is saved as empty 
* resolves @238: Enable static scaling using viewport meta for touch view; Fixed screen height when touch bar is disabled 
* resolves @219: Use variable substitutor for adminConsoleUrl 
* resolves @248: Class loader issue with AccessController 
* resolves @245: Fixed download on iOS using embedded Webswing 
* resolves @244: Properly escape connection secret on application instance startup 
* resolves @219: Fixed Linux startup scripts 
* resolves @243: Relative connection URL in embedded setup not working 
* resolves @219: Fixed instance not allowed to be created when maxClients &#x3D; -1 
* resolves @220: Test tool improvements 
* resolves @228: Fixed 3rd party vulnerabilities in Test tool 
* resolves @219: Fixed session log global pattern for deleting old files when having PID in log file name 

[d7745](https://github.com/manticore-projects/suramadu/commit/d7745eb1d28506f) Branislav Kuliha *2020-12-05 16:48:11*

**release 20.2**

* Cluster Support (not included in AGPL version) 
* Standalone Admin console (not included in AGPL version) 
* Bug fixes 
* Security fixes 

[ea4e5](https://github.com/manticore-projects/suramadu/commit/ea4e5714b07c879) Viktor Meszaros *2020-11-20 10:52:03*

**release 20.1.8**

* fix CVE-2020-27216, CWE-200, CVE-2020-13956, CVE-2020-26939, CVE-2020-13956 
* resolves @226: Fix deletion of jar files in temp 
* resolves @223: EDT monitoring thread dump timeout configurable 
* resolves @222: CTRL + ALT + KEY are not sending correctly from browser 
* resolves @218: IntelliJ IDEA not loading icons 
* resolves @215: Exception from IntelliJ IDEA 
* resolves @214: The bottom border in JFrame and JOptionPane is not rendered correctly when DirectDraw is activated 
* resolves @213: Admin console config field auto-filling list fix 
* resolves @212: Admin console variable resolution fix 

[e92b1](https://github.com/manticore-projects/suramadu/commit/e92b12ee090abc4) Branislav Kuliha *2020-11-04 16:46:05*

**release 20.1.7**

* fix CVE-2020-11023, CVE-2020-13920, CVE-2020-11998 
* resolves @210: WebService Problem when using WebSwing: No &lt;tubelines&gt; section found in the default [ jaxws-tubes-default.xml ] 
* resolves @209: DirectDraw: xor mode performance fix 
* resolves @208: Resize browser while uploading file cause progress bar to disappear 
* resolves @206: Download file with space in name changes space to plus sign 
* resolves @205: JsLink: passing null argument causes exception 
* resolves @204: Font size of title in window decoration can be changed by LaF to be too large 

[a43e2](https://github.com/manticore-projects/suramadu/commit/a43e268ca889843) Viktor Meszaros *2020-10-06 15:46:49*

**release 20.1.6**

* update 3rd party CWE-776, CVE-2020-13933 
* resolve @202: Directdraw: preload and cache large font files 
* resolve @200: Screen resolution change to 96 
* resolve @201: Caret rendering optimization - optional force no blink 
* resolve @199: Backspace Key Event is not processed correctly 
* resolve @198: Undocked Dialog not showing title 
* resolve @197: Window auto-undocks in IE 11 
* resolve @196: Javafx: window focus infinite cycle when modal dialog closed [VOL-7891] 

[1bde8](https://github.com/manticore-projects/suramadu/commit/1bde87baefef09e) Viktor Meszaros *2020-09-04 14:17:17*

**release 20.1.5**

* resolves @193: fix of session hangs on caret position calculation 
* resolves @194: Dropdowns not selectable by mouse [TRI-000307] 
* resolves @192: Undock resize does not work if triggered from embedded page [SOF-000253] 
* resolves @190: IllegalArgumentException in 20.1.4 if frame.setSize is called before setVisible 

[e6055](https://github.com/manticore-projects/suramadu/commit/e6055d2e2892f55) Viktor Meszaros *2020-08-14 16:34:33*

**release 20.1.4**

* fix CVE-2020-7692, CVE-2020-11989, CVE-2019-14900 
* resolves @171: undock improvements 
* resolves @189: AWT Robot mouse motion interpolation issue 
* resolves @188: Directdraw: gradient painting fallback flag 
* resolves @187: Directdraw: Flag to render text as path to force server side fonts 
* resolves @186: Combobox Popup misplaced if window is moved 
* resolves @184: setModalExclusionType handling 
* resolves @182: iOS 13 various touch issues [EMR-000279] 
* resolves @178: jetty.properties can&#x27;t handle absolute paths 
* resolves @177: fast key entry causing input upper-case char in firefox (jediterm) 

[c3535](https://github.com/manticore-projects/suramadu/commit/c35352b562a67ee) Viktor Meszaros *2020-08-05 12:49:24*

**release 20.1.3**

* resolves @176: Admin console cannot load application with name containedin base URL [D4T-000265] 
* resolves @86: Unhandled Exception in IBM JRE 
* resolves @175: WebPaintDispatcher#requestBrowserClipboard focus issue 
* resolves @174: Download file name spaces changed to plus, error message save dialog sticks around 
* resolves @173: DirectDraw Texture paint ignores alpha 
* resolves @172: Audio improvements - fixed opening audio stream from File; 
* resolves @171: Undock improvements 
* resolves @170: toolkit.getMaximumCursorColors() notifyCursorUpdateNPE (#395) 
* resolves @161: Javafx drag and drop does not work 
* resolves @169: Undock warning if javafx:false [SOF-000260] 
* resolves @158: Create a switch to disable logging stats 
* resolves @168: EventDispatcher: enter does not trigger keytyped event 
* resolves @167: Introduce new session mode continue_for_tab 
* resolves @164: Copy not working in Chrome [CHA-000257] 
* resolves @165: Button doesn&#x27;t exist 0 - special mouse buttons 
* resolves @163: Undocking window resize loops hard [SOF-000253] 
* resolves @162: JFileChooser shown with Synthetica LAF 
* resolves @143: JFileChooser dialog integration if JFilechooser embeded in wizard dialog + per dialog permissions 

[9ebab](https://github.com/manticore-projects/suramadu/commit/9ebab292f29ce3f) Viktor Meszaros *2020-07-06 10:33:09*

**release 20.1.2**

* updated log4j to version 2.13.2 [CVE-2020-9488] 
* resolves @149: Admin Console Logs me out when Removing App 
* resolves @156: fix resize after webswing-element is inits with 0 size 
* resolves @157: Allow using complex custom args as variables in webswing.config 
* resolves @144: REST API for number of active sessions 
* resolves @154: Application stops responding if WebWindow.handleWebActionEvent blocks 
* resolves @155: CORS protection not in effect when using local admin console 
* resolves @151: Session recording not recording all frames 
* resolves @152: Mouse over open combobox closes the combobox in CWM 
* resolves @150: Unclosed directory streams of screenshot folders in automated testing 
* resolves @137: for the specific use case 
* resolves @147: system properties for graphs customization 
* resolves @148: session view not showing application correctly(resolves #382) 
* resolves @140: Java 11 module system warnings 
* resolves @146: Regression: IME supports Chinese&lt;EF&gt;&lt;BC&gt;&lt;9F&gt;(resolves #390) 
* resolves @136: button doesn&#x27;t exist 0 - again 
* resolves @142: Exceptions when undocking window 

[b8aed](https://github.com/manticore-projects/suramadu/commit/b8aed0c07228165) Viktor Meszaros *2020-06-03 15:33:53*

**release 20.1.1**

* resolves @116: Parameterized test execution + other improvements 
* resolves @135: Vertex and Vertex-Light themes missing shade button 
* resolves @132: file upload message to notify the user file has been uploaded successfully 
* resolves @133: Log4j not closing properly on Tomcat shutdown 
* resolves @129 Javafx: texture lookup memory leak 
* resolves @127 Directdraw: rendering right-to-left text is not ordered correctly 
* resolves @126: Webswing continuously grabbing focus 

[58b66](https://github.com/manticore-projects/suramadu/commit/58b667c3c9996c6) Viktor Meszaros *2020-05-06 08:16:54*

**release 20.1**

* Accessibility support 
* Undocking Swing windows to browser windows 
* Improved support for mixing Swing with native HTML content 
* Audio support 
* Improved Admin console 
* New OpenAPI based REST interface 
* Bug fixes 
* Security fixes 
* (CVE-2020-11103, CVE-2020-11112, CVE-2020-11111, CVE-2020-11113, CVE-2020-10968, CVE-2020-10969, CVE-2020-10672, CWE-200, CVE-2020-7226) 

[e0067](https://github.com/manticore-projects/suramadu/commit/e00672238207a61) Viktor Meszaros *2020-04-03 22:10:39*

**release 2.7.5**

* fix CVE-2020-8840 - updated jackson-databind 
* re @107: fix No keyboard events from Webswing on tablets [CAN-000114] 
* re @106: support copy png images to clipboard in chrome 
* re @105: fix updating file filter in upload dialog 
* re @104 firefox directdraw rendering fix 

[f373f](https://github.com/manticore-projects/suramadu/commit/f373f55d2f1329a) Viktor Meszaros *2020-03-03 15:11:58*

**release 2.7.4**

* update jackson to fix CVE-2019-20330 
* resolves @100: Assembly: include security modules in minimal build 
* resolves @99: Feature: trigger pdf file opening in browser preview 
* resolves @98: Add option to hide touch toolbar 
* resolves @95: Jetty: remove &quot;Server&quot; header from all requests 
* resolves @92: fix prevent scroll in IE11 
* resolves @91: JavaFX fxml scene can not be loaded with java 11+ 

[cbf95](https://github.com/manticore-projects/suramadu/commit/cbf95bd5448e86d) Viktor Meszaros *2020-02-03 14:33:00*

**release 2.7.3**

* resolves @88 Directdraw: automatic fallback to png in case of exceeding max constant cache size 
* resolves @87: Enter key of Android/Chrome clients does not trigger ActionEvent in JTextFields 
* resolves @86 Unhandled Exception in IBM JRE 
* resolves @85 Files DnD not working 

[ba442](https://github.com/manticore-projects/suramadu/commit/ba4420e72faee13) Viktor Meszaros *2020-01-06 10:37:38*

**release 2.7.2**

* re @73: CWM: fixed resize of maximized window when screen size changes 
* re @83 handle float width/height values in chrome 
* resolves @83 continuous handshake in zoom mode 
* resolves @82: download filename fix 
* re @73: added missing handshake to fix canvas resizing after window changes size 
* resolves @81: flag to prevent user.home modification 
* resolves @79: CWM: direct draw rendering fails for new window with 0 size 
* re @80: Choose correct file filter when saving file 

[0b151](https://github.com/manticore-projects/suramadu/commit/0b1513aee4ce824) Viktor Meszaros *2019-12-02 19:06:40*

**release 2.7.1**

* update 3rd party to fix CVE-2019-17531, CVE-2019-10755 
* fix minimal distro admin console 404 
* resolves @78: Error when shutdown can result in &#x27;zombie&#x27; session + notifyshutdown api fix 
* resolves @77: Allow overriding ping parameters in javascript 
* re @55: increase directdraw cache max size 
* resolves @76: NPE if global webfolder can not be resolved 
* resolves  @72: Cursor occasionally stuck on &quot;resize&quot; if using LAF window style decorations (Metal LaF) 
* re @57: Shutdown api callback handling improvements 
* re @62: javafx fix 
* re @61: mirror view fixed for CWM 
* resolves @67 : security issue fix 
* resolves @66 : directdraw rendering issue 

[b735a](https://github.com/manticore-projects/suramadu/commit/b735a9e785bef7b) Viktor Meszaros *2019-11-04 13:14:22*

**release 2.7**

* Compositing window manager - allows step-by-step migration to native web technology 
* Test tool for easy performance and integration testing 
* Improved support for use on touch devices (Android &amp; iOS) 
* Added support for IME input 
* New improved SAML2 security module with SLO and SP-metadata support. 
* Admin console is now standalone and can be removed. 
* Replaced RequireJs with Webpack and partial conversion to Typescript 
* Reduced size of webswing-server.war binary 
* More precise text rendering in DirectDraw mode 
* Other small improvements &amp; bug fixes 

[09e81](https://github.com/manticore-projects/suramadu/commit/09e810c9c368534) Viktor Meszaros *2019-10-03 20:31:11*

**upgrade netbeans demo to v9.0 due to broken mvn repo**


[99094](https://github.com/manticore-projects/suramadu/commit/99094c8e8f1b374) Viktor Meszaros *2019-09-03 06:20:29*

**release 2.6.5**

* Merge branch &#x27;hotfix/2.6.5&#x27; 
* resolves 3rd party vulnerabilities: CVE-2019-12400 CVE-2018-11775 CVE-2019-10086 CVE-2019-14379  CVE-2019-14439 
* resolves #x47: Directdraw IllegalArgumentException fix 
* resolves #x44: Directdraw animated gif rendering 
* resolves #x42 Directdraw - precise text layout 
* updating poms for 2.6.5 branch with snapshot versions 

[f5719](https://github.com/manticore-projects/suramadu/commit/f57192cd53ba5f0) Viktor Meszaros *2019-09-02 22:10:17*

**release 2.6.4**

* Merge branch &#x27;hotfix/2.6.4&#x27; 
* fix CVE-2019-12814 
* updating poms for branch&#x27;hotfix/2.6.4&#x27; with non-snapshot versions 
* resolves #40: fix dragging JSplitPane (when released on window decoration) 
* resolves #33 : fix performance of animated button focus and indefinite progress bar 
* resolves #35: Provide browser timezone to webswing instance 
* resolves #36 : fix resolution of best cursor size 
* resolves #30 : directdraw hi-dpi bug fixes 
* updating poms for 2.6.4 branch with snapshot versions 

[094b2](https://github.com/manticore-projects/suramadu/commit/094b2a542ec72b7) Viktor Meszaros *2019-08-01 10:14:54*

**release 2.6.3**

* Merge branch &#x27;hotfix/2.6.3&#x27; 
* updating poms for branch&#x27;hotfix/2.6.3&#x27; with non-snapshot versions 
* fix CVE-2018-10237: update guava 
* resolves #25 : OIDC security module - access token and refresh token as user attrib 
* resolves #18 : fix app termination on IE11 when native filechchooser is open 
* updating poms for 2.6.3 branch with snapshot versions 

[e0031](https://github.com/manticore-projects/suramadu/commit/e0031294aa3b74b) Viktor Meszaros *2019-07-01 16:14:48*

**release 2.6.2**

* 52efad77 updating poms for branch&#x27;hotfix/2.6.2&#x27; with non-snapshot versions 
* a0aab663 resolves #16 : thread cleanup on undeploy 
* 3fc29fd4 CVE-2019-12086 : update jackson lib 
* 32bf1669 resolve #338 : directdraw render high-res image - merge fix 
* cf30246a see #342 : allow default unbound/unshared websocket threadpool 
* 118a4638 resolves #15 : printing misalignment fix 
* 546b2a07 resolves #342 : increase default websocket thread pool 
* a48c4e00 jetty server version update, see #340 : openid roles claim reading improvement 
* 0124b625 resolves #340 : openid roles claim reading improvement 
* 32fded1d resolves #339 : focus fix after requestBrowserClipboard 
* 7d9ffc7b resolve #338 : directdraw render high-res image 
* 9db4af11 resolve #337 :fix problem with multi-file upload 
* 7c733467 resolves #336 : fix decoration buttons for dialogs 
* 871fde6b updating poms for 2.6.2 branch with snapshot versions 

[65402](https://github.com/manticore-projects/suramadu/commit/65402b1327c3505) Viktor Meszaros *2019-06-01 07:51:49*

**updating poms for branch'hotfix/2.6.1' with non-snapshot versions**


[00ef4](https://github.com/manticore-projects/suramadu/commit/00ef455ab54af8b) Viktor Meszaros *2019-05-01 01:34:26*

**jetty security update**


[43897](https://github.com/manticore-projects/suramadu/commit/438974c2aed74a7) Viktor Meszaros *2019-05-01 01:21:07*

**fix file chooser dialog disapearing on focus lost**


[32af4](https://github.com/manticore-projects/suramadu/commit/32af49c1bf11e54) Viktor Meszaros *2019-05-01 01:06:54*

**fix of xor mode text rendering in ie, fix web.xml validity**


[c9c25](https://github.com/manticore-projects/suramadu/commit/c9c2577841ef749) Viktor Meszaros *2019-05-01 01:06:14*

**fix of security module login page content type**


[66412](https://github.com/manticore-projects/suramadu/commit/664124b988e7d30) Viktor Meszaros *2019-05-01 01:05:47*

**updating poms for 2.6.1 branch with snapshot versions**


[ce8ac](https://github.com/manticore-projects/suramadu/commit/ce8ac9d8c5c1b06) Viktor Meszaros *2019-05-01 01:04:48*

**updating poms for branch'release/2.6' with non-snapshot versions**


[13a4f](https://github.com/manticore-projects/suramadu/commit/13a4fd70a366c15) Viktor Meszaros *2019-03-27 15:35:31*

**updating poms for 2.6 branch with snapshot versions**


[7cca5](https://github.com/manticore-projects/suramadu/commit/7cca5fdf0bec919) Viktor Meszaros *2019-03-27 15:16:05*

**no squash on release**


[42a84](https://github.com/manticore-projects/suramadu/commit/42a84ae46b31b39) Viktor Meszaros *2019-03-27 15:14:53*

**added config for edt monitor busy animation delay, security modules extension configuration fix**


[24b3a](https://github.com/manticore-projects/suramadu/commit/24b3aba8910834d) Viktor Meszaros *2019-03-27 14:53:40*

**admin console improvements and bug fixes**


[84f13](https://github.com/manticore-projects/suramadu/commit/84f13e39aac43ac) Viktor Meszaros *2019-03-26 20:53:51*

**add state param verification to openid connect flow**


[2a8f3](https://github.com/manticore-projects/suramadu/commit/2a8f3645919a736) Viktor Meszaros *2019-03-25 14:28:12*

**fix j11 reflective access warning**


[f6318](https://github.com/manticore-projects/suramadu/commit/f63185686980452) Viktor Meszaros *2019-03-25 10:42:09*

**update netbeans demo version**


[a2909](https://github.com/manticore-projects/suramadu/commit/a2909950f5ff7c7) Viktor Meszaros *2019-03-25 10:41:01*

**javafx demo fix**


[9f9b9](https://github.com/manticore-projects/suramadu/commit/9f9b9721f2be3b3) Viktor Meszaros *2019-03-22 12:43:37*

**ping webworker termination fix**


[ad17b](https://github.com/manticore-projects/suramadu/commit/ad17bb91578067c) Viktor Meszaros *2019-03-21 18:06:18*

**fix Windows LaF platform icon loading in java11**


[bcf4c](https://github.com/manticore-projects/suramadu/commit/bcf4c8b2b9f7857) Viktor Meszaros *2019-03-20 22:39:29*

**transparent upload/download fix**


[4ebe7](https://github.com/manticore-projects/suramadu/commit/4ebe733f6cde5ab) Viktor Meszaros *2019-03-20 14:13:37*

**don't open session view in new window**


[d0749](https://github.com/manticore-projects/suramadu/commit/d074963b636419c) Branislav Kuliha *2019-03-20 10:55:54*

**show logs for finished sessions**


[5007e](https://github.com/manticore-projects/suramadu/commit/5007e58896534b8) Branislav Kuliha *2019-03-19 13:02:48*

**fix canvas resizing when zoom**


[b3082](https://github.com/manticore-projects/suramadu/commit/b30823ce333eb6f) Viktor Meszaros *2019-03-19 12:21:29*

**added system variable defaults webswing.sessionLog.maxSize and**

* webswing.sessionLog.size for session logs 

[970d8](https://github.com/manticore-projects/suramadu/commit/970d89d8eedc580) Branislav Kuliha *2019-03-19 12:03:26*

**fixed closed appender on default log when session logging is turned off**


[87d20](https://github.com/manticore-projects/suramadu/commit/87d206ca7706ed5) Branislav Kuliha *2019-03-19 08:25:29*

**use USERPROFILE instead of user.home to create Desktop folder**


[ae1f4](https://github.com/manticore-projects/suramadu/commit/ae1f439bc8e845a) Branislav Kuliha *2019-03-18 13:43:11*

**updated default logging folder, log destination of session log file**


[9fd2b](https://github.com/manticore-projects/suramadu/commit/9fd2b0b878900b9) Branislav Kuliha *2019-03-18 13:38:39*

**show server version in navbar**


[f716a](https://github.com/manticore-projects/suramadu/commit/f716a65f675a5b2) Branislav Kuliha *2019-03-18 09:38:01*

**show session logs in sessions overview**


[1b908](https://github.com/manticore-projects/suramadu/commit/1b9080df423baf2) Branislav Kuliha *2019-03-18 08:42:39*

**add jfx 11 deps to swingset3**


[7a368](https://github.com/manticore-projects/suramadu/commit/7a368790430058e) Viktor Meszaros *2019-03-15 12:52:37*

**proto version - fix build warning**


[f195e](https://github.com/manticore-projects/suramadu/commit/f195e57d039f4cc) Viktor Meszaros *2019-03-15 12:29:50*

**fix default directdraw webfont mapping**


[0da93](https://github.com/manticore-projects/suramadu/commit/0da932c82d41b6c) Viktor Meszaros *2019-03-15 09:15:56*

**sessions overview - layout changes, sorting, tabs,**


[dff34](https://github.com/manticore-projects/suramadu/commit/dff341d8f4204eb) Branislav Kuliha *2019-03-14 14:42:38*

**increase websocket max message size to 1mb**


[482fc](https://github.com/manticore-projects/suramadu/commit/482fc2bb54ac690) Viktor Meszaros *2019-03-14 12:50:09*

**update jetty version to 9.4.15**


[6f6b2](https://github.com/manticore-projects/suramadu/commit/6f6b294ecb41000) Viktor Meszaros *2019-03-14 10:28:46*

**session logging**


[74835](https://github.com/manticore-projects/suramadu/commit/74835cfffb3a6f9) Branislav Kuliha *2019-03-14 08:30:23*

**sessions overview**


[0cd3b](https://github.com/manticore-projects/suramadu/commit/0cd3b63129a09ae) Branislav Kuliha *2019-03-13 14:17:17*

**session logging**


[1951c](https://github.com/manticore-projects/suramadu/commit/1951c05013f0763) Branislav Kuliha *2019-03-13 12:47:11*

**session logging**


[59858](https://github.com/manticore-projects/suramadu/commit/598588fdc7e7447) Branislav Kuliha *2019-03-07 11:18:10*

**improved logging, test shutdown with confirmation dialog**


[d8080](https://github.com/manticore-projects/suramadu/commit/d80802fa6e0e45b) Viktor Meszaros *2019-02-28 18:33:42*

**jvm crash workaround for javafx 11 on windows, build cleanup, styles update**


[ca0fd](https://github.com/manticore-projects/suramadu/commit/ca0fd44dc6e3f77) Viktor Meszaros *2019-02-28 13:44:00*

**Updating develop poms back to snapshot version**


[e8e45](https://github.com/manticore-projects/suramadu/commit/e8e45158a6aa5db) Viktor Meszaros *2019-02-28 07:22:20*

**Updating develop poms back to snapshot version**


[9c2cd](https://github.com/manticore-projects/suramadu/commit/9c2cd3a42d8bd2d) Viktor Meszaros *2019-02-26 18:42:47*

**Updating develop poms to hotfix version to avoid merge conflicts**


[d2e06](https://github.com/manticore-projects/suramadu/commit/d2e06ed4f3568dd) Viktor Meszaros *2019-02-26 18:31:41*

**updating poms for branch'hotfix/2.5.12' with non-snapshot versions**


[f6f3b](https://github.com/manticore-projects/suramadu/commit/f6f3be4e1ddabd5) Viktor Meszaros *2019-02-26 18:07:54*

**fix demo**


[3f88c](https://github.com/manticore-projects/suramadu/commit/3f88cf7e11b5069) Viktor Meszaros *2019-02-26 17:46:10*

**WebToolkit - Desktop folder logic cleanup**


[8e84d](https://github.com/manticore-projects/suramadu/commit/8e84d6c60db2c3f) Andrej Zelman *2019-02-26 17:35:01*

**Admin Console application filter and sort**


[afe41](https://github.com/manticore-projects/suramadu/commit/afe4124219f6ea5) Andrej *2019-02-26 17:34:07*

**fix duration displaying**


[05701](https://github.com/manticore-projects/suramadu/commit/05701ed739fa662) Viktor Meszaros *2019-02-26 17:33:46*

**license fix**


[e3300](https://github.com/manticore-projects/suramadu/commit/e3300c69bd2659a) Viktor Meszaros *2019-02-26 17:33:17*

**text/plain flavor copy fix**


[599bb](https://github.com/manticore-projects/suramadu/commit/599bb9e92465f60) Viktor Meszaros *2019-02-26 17:30:21*

**window resize/move fix**


[9509e](https://github.com/manticore-projects/suramadu/commit/9509ef5ad63cc12) Viktor Meszaros *2019-02-26 17:30:09*

**updating poms for 2.5.12 branch with snapshot versions**


[5f82d](https://github.com/manticore-projects/suramadu/commit/5f82ddc27bbb305) Viktor Meszaros *2019-02-26 15:30:38*

**window resize/move fix**


[5e89b](https://github.com/manticore-projects/suramadu/commit/5e89b9931537b96) Viktor Meszaros *2019-02-26 15:24:58*

**text/plain flavor copy fix**


[9dbc7](https://github.com/manticore-projects/suramadu/commit/9dbc760504e41f3) Viktor Meszaros *2019-02-26 14:53:45*

**NPE fix**


[41613](https://github.com/manticore-projects/suramadu/commit/41613124a634bed) Viktor Meszaros *2019-02-22 18:02:54*

**javafx 11 classpath fix**


[efee7](https://github.com/manticore-projects/suramadu/commit/efee7eb242906aa) Viktor Meszaros *2019-02-21 14:38:27*

**brand colors and favicon update**


[74b74](https://github.com/manticore-projects/suramadu/commit/74b74a7c2aab454) Andrej *2019-02-19 07:18:34*

**java 11 build fix**


[9cf69](https://github.com/manticore-projects/suramadu/commit/9cf69c2133841c4) Viktor Meszaros *2019-02-18 18:04:50*

**java 11 build fix**


[5b3d6](https://github.com/manticore-projects/suramadu/commit/5b3d63899a1bb03) Viktor Meszaros *2019-02-18 16:55:45*

**java 11 build fix**


[68bca](https://github.com/manticore-projects/suramadu/commit/68bca145372d8f0) Viktor Meszaros *2019-02-18 13:49:01*

**fix duration displaying**


[b2de3](https://github.com/manticore-projects/suramadu/commit/b2de34de8c262a3) Viktor Meszaros *2019-02-18 12:30:31*

**jdk 11 build**


[8e571](https://github.com/manticore-projects/suramadu/commit/8e5717e54d7bfbc) Viktor Meszaros *2019-02-18 12:13:36*

**NPE fix**


[7caf1](https://github.com/manticore-projects/suramadu/commit/7caf1fc5c65860f) Viktor Meszaros *2019-02-18 10:25:19*

**Admin Console application filter and sort**


[71019](https://github.com/manticore-projects/suramadu/commit/71019d39c2f6969) Andrej *2019-02-15 08:19:35*

**javafx 11 update**


[d0d62](https://github.com/manticore-projects/suramadu/commit/d0d62d94fffb625) Viktor Meszaros *2019-02-15 06:33:23*

**javafx 11 build update**


[d0f48](https://github.com/manticore-projects/suramadu/commit/d0f485e620c21d6) Viktor Meszaros *2019-02-13 12:42:30*

**switch to jdk11 build, rename project**


[31645](https://github.com/manticore-projects/suramadu/commit/3164502ff95cb68) Viktor Meszaros *2019-02-12 03:39:33*

**removed java 7 support**


[8e24e](https://github.com/manticore-projects/suramadu/commit/8e24efa2a2dd97f) Viktor Meszaros *2019-02-11 11:43:57*

**WebToolkit - Desktop folder logic cleanup**


[b1b78](https://github.com/manticore-projects/suramadu/commit/b1b78d7041d831c) Andrej Zelman *2019-02-06 20:43:36*

**IOException import fix**


[fa54d](https://github.com/manticore-projects/suramadu/commit/fa54d581dfbd401) Andrej Zelman *2019-02-06 20:13:06*

**Updating develop poms back to snapshot version**


[dbe24](https://github.com/manticore-projects/suramadu/commit/dbe241d1f3c9572) Viktor Meszaros *2019-02-04 02:35:12*

**Updating develop poms to hotfix version to avoid merge conflicts**


[bc3d5](https://github.com/manticore-projects/suramadu/commit/bc3d5296ff546f1) Viktor Meszaros *2019-02-04 02:10:06*

**updating poms for branch'hotfix/2.5.11' with non-snapshot versions**


[81062](https://github.com/manticore-projects/suramadu/commit/81062d5ff804ca4) Viktor Meszaros *2019-02-04 02:04:41*

**autologout on timeout fix**

* (cherry picked ) 

[36535](https://github.com/manticore-projects/suramadu/commit/365353cc6197bfc) Viktor Meszaros *2019-02-04 02:03:51*

**File upload CORS fix**

* (cherry picked from commit 6d33704) 

[a5734](https://github.com/manticore-projects/suramadu/commit/a573418aaa1d5d1) Viktor Meszaros *2019-02-01 15:31:32*

**pom file license update**


[c5a1a](https://github.com/manticore-projects/suramadu/commit/c5a1a3684184df3) Viktor Meszaros *2019-02-01 15:22:19*

**right-click event handling fix**

* (cherry picked from commit 884dde4) 

[b63cd](https://github.com/manticore-projects/suramadu/commit/b63cd0cbb373d7e) Viktor Meszaros *2019-02-01 15:13:10*

**fix default shutdown procedure to run on EDT**

* (cherry picked from commit 715131c) 

[dcf21](https://github.com/manticore-projects/suramadu/commit/dcf2173e0e251d4) Viktor Meszaros *2019-02-01 15:11:40*

**filechooser fix for Windows service**

* (cherry picked from commit 6cbc4c7) 

[2c59b](https://github.com/manticore-projects/suramadu/commit/2c59ba1c5394b8c) Andrej Zelman *2019-02-01 15:09:46*

**resolves #289 on mouse move event MouseEvent.button is always 0**

* (cherry picked from commit fff23e1) 

[a2ccc](https://github.com/manticore-projects/suramadu/commit/a2cccf900f8220e) Viktor Meszaros *2019-02-01 15:05:23*

**updating poms for 2.5.11 branch with snapshot versions**


[d8cf1](https://github.com/manticore-projects/suramadu/commit/d8cf1ed96d6fe8f) Viktor Meszaros *2019-02-01 14:59:50*

**Desktop folder creation moved to Webtoolkit**


[6cbc4](https://github.com/manticore-projects/suramadu/commit/6cbc4c724152e88) Andrej Zelman *2019-02-01 13:21:11*

**resolves #289 on mouse move event MouseEvent.button is always 0**


[fff23](https://github.com/manticore-projects/suramadu/commit/fff23e16d82ef3a) Viktor Meszaros *2019-01-30 15:06:37*

**fix loading default-package classes**


[f60b9](https://github.com/manticore-projects/suramadu/commit/f60b9ff96028aad) Viktor Meszaros *2019-01-30 13:02:09*

**Dektop folder created in user.home system property**


[351bf](https://github.com/manticore-projects/suramadu/commit/351bf94270b43fa) Andrej Zelman *2019-01-23 11:13:20*

**Updating develop poms back to snapshot version**


[8b7e5](https://github.com/manticore-projects/suramadu/commit/8b7e51cbb4b2304) Viktor Meszaros *2019-01-22 21:33:40*

**Desktop Folder creation for Windows service**


[ff691](https://github.com/manticore-projects/suramadu/commit/ff691bee9139d3a) Andrej Zelman *2019-01-22 15:29:20*

**fix default shutdown procedure to run on EDT**


[71513](https://github.com/manticore-projects/suramadu/commit/715131c4a96bf6e) Viktor Meszaros *2019-01-22 03:03:42*

**fix authorization for metrics endpoint**


[8928d](https://github.com/manticore-projects/suramadu/commit/8928d424644ce66) Viktor Meszaros *2019-01-22 02:31:26*

**javafx DnD fix**


[75dce](https://github.com/manticore-projects/suramadu/commit/75dcec1b6dbbf2e) Viktor Meszaros *2019-01-22 01:10:22*

**right-click event handling fix**


[884dd](https://github.com/manticore-projects/suramadu/commit/884dde4cf0ae45d) Viktor Meszaros *2019-01-10 13:13:27*

**auto logout logging fix, recordings setup fix**


[3ebeb](https://github.com/manticore-projects/suramadu/commit/3ebeb604973af21) Viktor Meszaros *2019-01-09 23:51:50*

**admin console - fix resolve var in tomcat**


[683b6](https://github.com/manticore-projects/suramadu/commit/683b65123eab45b) Viktor Meszaros *2019-01-08 10:34:14*

**log rest api errors fix**


[4e8ca](https://github.com/manticore-projects/suramadu/commit/4e8cac6fe0c8d6e) Viktor Meszaros *2019-01-08 10:33:45*

**autologout on timeout fix**


[040c9](https://github.com/manticore-projects/suramadu/commit/040c9a30c77b60d) Viktor Meszaros *2019-01-07 22:33:29*

**update license in pom.xml**


[2cec2](https://github.com/manticore-projects/suramadu/commit/2cec2fa58a8683f) Viktor Meszaros *2019-01-04 12:00:48*

**Updating develop poms to hotfix version to avoid merge conflicts**


[61d2b](https://github.com/manticore-projects/suramadu/commit/61d2bfd6666c695) Viktor Meszaros *2019-01-03 15:29:57*

**jackson-databind security update**


[8ee6e](https://github.com/manticore-projects/suramadu/commit/8ee6edf150760fc) Viktor Meszaros *2019-01-03 15:10:36*

**updating poms for branch'hotfix/2.5.10' with non-snapshot versions**


[ea7ce](https://github.com/manticore-projects/suramadu/commit/ea7cea1ef4b2242) Viktor Meszaros *2019-01-03 14:33:16*

**limited access for anonymous security module**


[0e7c2](https://github.com/manticore-projects/suramadu/commit/0e7c25288927e2d) Viktor Meszaros *2019-01-03 14:32:28*

**cleanup temporary pdf files**


[0c8ff](https://github.com/manticore-projects/suramadu/commit/0c8ffcfddcb5376) Viktor Meszaros *2019-01-03 14:24:54*

**IE scroll fix**


[f00b7](https://github.com/manticore-projects/suramadu/commit/f00b7a00fafa30d) Viktor Meszaros *2019-01-03 13:42:41*

**stack overflow fix**


[0bd42](https://github.com/manticore-projects/suramadu/commit/0bd424c2a0d383f) Viktor Meszaros *2019-01-03 13:33:34*

**updating poms for 2.5.10 branch with snapshot versions**


[31727](https://github.com/manticore-projects/suramadu/commit/31727d6bd93a149) Viktor Meszaros *2018-12-28 18:16:31*

**update of protoc plugin, wildfly IoC fix**


[c5e74](https://github.com/manticore-projects/suramadu/commit/c5e74b625c9cc20) Viktor Meszaros *2018-12-20 12:25:23*

**File upload CORS fix**


[6d337](https://github.com/manticore-projects/suramadu/commit/6d33704623bbe9a) Viktor Meszaros *2018-12-19 12:03:46*

**session recording improvements**


[42499](https://github.com/manticore-projects/suramadu/commit/42499bb676e57c4) Viktor Meszaros *2018-12-14 17:19:15*

**Updating develop poms back to snapshot version**


[9a140](https://github.com/manticore-projects/suramadu/commit/9a140cac734a8a8) Viktor Meszaros *2018-12-07 00:36:02*

**Updating develop poms to hotfix version to avoid merge conflicts**


[48030](https://github.com/manticore-projects/suramadu/commit/48030580efac228) Viktor Meszaros *2018-12-07 00:31:56*

**updating poms for branch'hotfix/2.5.9' with non-snapshot versions**


[c218f](https://github.com/manticore-projects/suramadu/commit/c218fe40e424dcd) Viktor Meszaros *2018-12-07 00:21:07*

**image observer improvement, log msg**


[a59d7](https://github.com/manticore-projects/suramadu/commit/a59d774ec181cae) Viktor Meszaros *2018-12-07 00:20:37*

**jquery update**


[a46ea](https://github.com/manticore-projects/suramadu/commit/a46eace9549d701) Viktor Meszaros *2018-12-06 17:23:29*

**long-polling fallback fix**


[2ea4f](https://github.com/manticore-projects/suramadu/commit/2ea4f8469e48f2c) Viktor Meszaros *2018-12-06 14:45:37*

**option to link cookie to user IP**


[ac277](https://github.com/manticore-projects/suramadu/commit/ac2770327537aec) Viktor Meszaros *2018-12-04 00:04:40*

**admin console reload on logout**


[175b4](https://github.com/manticore-projects/suramadu/commit/175b45450b2b047) Viktor Meszaros *2018-12-04 00:02:58*

**error handler fix, waitForImage fix**


[e1599](https://github.com/manticore-projects/suramadu/commit/e1599a48f8d9c83) Viktor Meszaros *2018-11-30 01:21:13*

**updating poms for 2.5.9 branch with snapshot versions**


[e5b6e](https://github.com/manticore-projects/suramadu/commit/e5b6e8812bd196d) Viktor Meszaros *2018-11-29 23:41:56*

**Updating develop poms back to snapshot version**


[d71f4](https://github.com/manticore-projects/suramadu/commit/d71f431d9844f4e) Viktor Meszaros *2018-11-29 14:28:17*

**manual merge of 2.5.8 mediumweight popup focus fix**


[e42fa](https://github.com/manticore-projects/suramadu/commit/e42fab5a05d4d0a) Viktor Meszaros *2018-11-29 14:24:00*

**merge 2.5.8 fixes to develop branch**


[0bd7c](https://github.com/manticore-projects/suramadu/commit/0bd7c52f54e6c09) Viktor Meszaros *2018-11-28 22:56:05*

**Updating develop poms to hotfix version to avoid merge conflicts**


[f236f](https://github.com/manticore-projects/suramadu/commit/f236fa23e589ebe) Viktor Meszaros *2018-11-28 16:14:00*

**updating poms for branch'hotfix/2.5.8' with non-snapshot versions**


[3668c](https://github.com/manticore-projects/suramadu/commit/3668cf976bfaa5c) Viktor Meszaros *2018-11-28 16:07:54*

**fix shutdown after uncaught exception**


[f64a9](https://github.com/manticore-projects/suramadu/commit/f64a938366b0ee0) Viktor Meszaros *2018-11-27 13:44:42*

**mediumweight popup focus fix**


[de04d](https://github.com/manticore-projects/suramadu/commit/de04db34396d3e8) Viktor Meszaros *2018-11-26 07:22:47*

**improved customization support**


[741f2](https://github.com/manticore-projects/suramadu/commit/741f20eb0a97750) Viktor Meszaros *2018-11-24 11:56:44*

**fix LaF window decorations handling**


[84a1b](https://github.com/manticore-projects/suramadu/commit/84a1bb12f92c551) Viktor Meszaros *2018-11-23 01:07:30*

**fix handling modal child dialog of JFileChooser**


[f4232](https://github.com/manticore-projects/suramadu/commit/f4232fb10d346ff) Viktor Meszaros *2018-11-21 19:06:10*

**fix waiting image observer**


[27596](https://github.com/manticore-projects/suramadu/commit/275967a2916fc1f) Viktor Meszaros *2018-11-08 14:50:00*

**JavaFX SwingNode rendering fix**


[8e8bd](https://github.com/manticore-projects/suramadu/commit/8e8bd6a80cc8ad2) Viktor Meszaros *2018-10-26 11:20:41*

**resolves #301 admin console variables handling improvement**


[53143](https://github.com/manticore-projects/suramadu/commit/531433cdfafc13f) Viktor Meszaros *2018-10-24 00:27:11*

**SAML response validation fix**


[55fea](https://github.com/manticore-projects/suramadu/commit/55fea8840d0b5d3) Viktor Meszaros *2018-10-22 20:55:24*

**updating poms for 2.5.8 branch with snapshot versions**


[966e3](https://github.com/manticore-projects/suramadu/commit/966e3acd65b563a) Viktor Meszaros *2018-10-22 20:54:19*

**documentation update**


[0bf72](https://github.com/manticore-projects/suramadu/commit/0bf728e9909990a) Andrej Zelman *2018-10-17 10:14:12*

**jackson version update, HiDPI support**


[54e06](https://github.com/manticore-projects/suramadu/commit/54e06493ee3640d) Viktor Meszaros *2018-10-15 20:28:53*

**Updating develop poms back to snapshot version**


[3778d](https://github.com/manticore-projects/suramadu/commit/3778d4f6770862b) Viktor Meszaros *2018-10-15 10:08:40*

**close websocket on logout**


[65085](https://github.com/manticore-projects/suramadu/commit/65085ef8f6020c4) Viktor Meszaros *2018-10-15 09:50:14*

**Updating develop poms to hotfix version to avoid merge conflicts**


[3a00e](https://github.com/manticore-projects/suramadu/commit/3a00e483822a076) Viktor Meszaros *2018-10-15 09:12:07*

**updating poms for branch'hotfix/2.5.7' with non-snapshot versions**


[d2012](https://github.com/manticore-projects/suramadu/commit/d2012fc7a20da80) Viktor Meszaros *2018-10-15 09:06:29*

**guava and jackson update**


[9dedb](https://github.com/manticore-projects/suramadu/commit/9dedb52c8ab55fb) Viktor Meszaros *2018-10-12 22:23:07*

**close websocket on logout**


[42d96](https://github.com/manticore-projects/suramadu/commit/42d96de01621fc5) Viktor Meszaros *2018-10-12 14:06:59*

**atmosphere version update, close websocket on logout, security module integration fix**


[0a894](https://github.com/manticore-projects/suramadu/commit/0a89495f3ec43c3) Viktor Meszaros *2018-10-12 00:16:43*

**firefox DD rendering glitch on 0 size image crop**


[5f8ad](https://github.com/manticore-projects/suramadu/commit/5f8adf9d61b6005) Viktor Meszaros *2018-10-09 10:31:55*

**firefox DD rendering glitch on 0 size image crop**


[ac3be](https://github.com/manticore-projects/suramadu/commit/ac3be96b0bbca59) Viktor Meszaros *2018-10-05 16:24:17*

**dnd events mem-leak fix**


[049d9](https://github.com/manticore-projects/suramadu/commit/049d978a1584588) Viktor Meszaros *2018-10-05 15:24:39*

**dnd events fix**


[bb2c6](https://github.com/manticore-projects/suramadu/commit/bb2c64210ed9067) Viktor Meszaros *2018-10-03 00:55:36*

**fix firefox scroll**


[1343b](https://github.com/manticore-projects/suramadu/commit/1343b6677f75b50) Viktor Meszaros *2018-10-01 02:16:25*

**updating poms for 2.5.7 branch with snapshot versions**


[271eb](https://github.com/manticore-projects/suramadu/commit/271eb1c4ed1e07f) Viktor Meszaros *2018-10-01 01:53:49*

**isolated filesystem fix, java 11 support**


[11a04](https://github.com/manticore-projects/suramadu/commit/11a04ea856fa5a4) Viktor Meszaros *2018-10-01 01:31:45*

**removing JMX Server from client to simplify Java10 integration**


[73617](https://github.com/manticore-projects/suramadu/commit/736179fa7f209eb) Andrej Zelman *2018-09-17 08:09:01*

**Updating develop poms back to snapshot**


[3fa05](https://github.com/manticore-projects/suramadu/commit/3fa05d2c6c355a5) Viktor Meszaros *2018-08-27 04:51:09*

**Updating develop poms to hotfix version to avoid merge conflicts**


[c1d72](https://github.com/manticore-projects/suramadu/commit/c1d727b9234a4cb) Viktor Meszaros *2018-08-27 03:25:32*

**updating poms for branch'hotfix/2.5.6' with non-snapshot versions**


[a7387](https://github.com/manticore-projects/suramadu/commit/a7387be2cc7c27b) Viktor Meszaros *2018-08-27 03:20:52*

**3rd party libs security update (from 7c976ef)**


[58d38](https://github.com/manticore-projects/suramadu/commit/58d38969387ebe9) Viktor Meszaros *2018-08-27 03:20:37*

**updating poms for 2.5.6 branch with snapshot versions**


[dc0c1](https://github.com/manticore-projects/suramadu/commit/dc0c13d5911310e) Viktor Meszaros *2018-08-27 02:10:01*

**3rd party libs security update,**


[7c976](https://github.com/manticore-projects/suramadu/commit/7c976ef35bcbeb1) Viktor Meszaros *2018-08-27 02:04:25*

**NPE fix**


[3141a](https://github.com/manticore-projects/suramadu/commit/3141af28331ff85) Viktor Meszaros *2018-08-20 18:55:27*

**Updating develop poms to hotfix version to avoid merge conflicts**


[4643d](https://github.com/manticore-projects/suramadu/commit/4643dd7e21bc09e) Viktor Meszaros *2018-08-08 03:29:33*

**updating poms for branch'hotfix/2.5.5' with non-snapshot versions**


[00389](https://github.com/manticore-projects/suramadu/commit/003897cf538a856) Viktor Meszaros *2018-08-08 03:21:55*

**New Session Metrics for analytics and reporting (from d7461c6)**


[80049](https://github.com/manticore-projects/suramadu/commit/80049da46b9218b) Viktor Meszaros *2018-08-08 03:20:29*

**isolatedFs fix (from eaa87df)**


[6bb11](https://github.com/manticore-projects/suramadu/commit/6bb11f1deb9d62f) Viktor Meszaros *2018-08-08 02:32:55*

**fix occasional IllegalComponentStateException (from 32b3b5d)**


[3fd90](https://github.com/manticore-projects/suramadu/commit/3fd90b56f476250) Viktor Meszaros *2018-08-08 02:29:44*

**Fix atmosphere NPE in tomcat (from 2366ffd)**


[66d5e](https://github.com/manticore-projects/suramadu/commit/66d5e029f3e5025) Viktor Meszaros *2018-08-08 02:29:24*

**updating poms for 2.5.5 branch with snapshot versions**


[2cf46](https://github.com/manticore-projects/suramadu/commit/2cf46b482e0e7f2) Viktor Meszaros *2018-08-08 02:28:37*

**New Session Metrics for analytics and reporting - initial commit**


[d7461](https://github.com/manticore-projects/suramadu/commit/d7461c6f39c9724) Andrej Zelman *2018-07-23 09:00:13*

**Support for JDK10**


[7d1b6](https://github.com/manticore-projects/suramadu/commit/7d1b62ade5baf14) Jannis Pohl *2018-07-19 11:48:31*

**embeded security module - password hashed**


[dd04b](https://github.com/manticore-projects/suramadu/commit/dd04b2932597505) Viktor Meszaros *2018-07-15 07:06:15*

**rt-win-shell & jdk.jsobject.modpatch dependency propagation fix**


[6d663](https://github.com/manticore-projects/suramadu/commit/6d663aad2fa39b3) Andrej Zelman *2018-06-22 08:53:34*

**serverless build profile**


[2512b](https://github.com/manticore-projects/suramadu/commit/2512b53e7ed765e) Viktor Meszaros *2018-06-18 10:07:13*

**isolatedFs fix**


[eaa87](https://github.com/manticore-projects/suramadu/commit/eaa87df6a1da78b) Viktor Meszaros *2018-06-16 21:57:29*

**directdraw fallback improvements**


[fe4d6](https://github.com/manticore-projects/suramadu/commit/fe4d666fd6eb735) Viktor Meszaros *2018-06-09 18:17:26*

**fix occasional IllegalComponentStateException**


[32b3b](https://github.com/manticore-projects/suramadu/commit/32b3b5df53848bf) Viktor Meszaros *2018-06-09 17:51:48*

**Fix atmosphere NPE in tomcat**


[2366f](https://github.com/manticore-projects/suramadu/commit/2366ffd5f8c980a) Viktor Meszaros *2018-06-08 02:29:47*

**README.md updated license**


[04f62](https://github.com/manticore-projects/suramadu/commit/04f62c02fe6908f) Andrej Zelman *2018-06-05 12:09:34*

**Updating License to AGPLv3**


[30bd6](https://github.com/manticore-projects/suramadu/commit/30bd624a3f32bfc) Andrej Zelman *2018-06-05 10:01:34*

**Updating develop poms to hotfix version to avoid merge conflicts**


[8daa5](https://github.com/manticore-projects/suramadu/commit/8daa5635711483d) Viktor Meszaros *2018-06-04 10:59:38*

**updating poms for branch'hotfix/2.5.4' with non-snapshot versions**


[59ef4](https://github.com/manticore-projects/suramadu/commit/59ef4a668860b0f) Viktor Meszaros *2018-06-04 10:52:47*

**secure rest endpoints**


[470e4](https://github.com/manticore-projects/suramadu/commit/470e414ad1024b4) Viktor Meszaros *2018-06-04 10:35:39*

**disable activemq jmx**

* (cherry picked from commit bdd4233) 

[b0dcd](https://github.com/manticore-projects/suramadu/commit/b0dcd1941e496d3) Viktor Meszaros *2018-06-04 10:27:49*

**Netbeans startup fix (ClassCircularityError)**

* (cherry picked from commit 9078f42) 

[ef1f2](https://github.com/manticore-projects/suramadu/commit/ef1f2ff3ff0c780) Viktor Meszaros *2018-06-04 10:27:26*

**mouse scroll sensitivity fix**

* (cherry picked from commit 9ee5f9f) 

[5f0ad](https://github.com/manticore-projects/suramadu/commit/5f0ad8b885be37c) Viktor Meszaros *2018-06-04 10:26:50*

**updating poms for 2.5.4 branch with snapshot versions**


[b205e](https://github.com/manticore-projects/suramadu/commit/b205e06a1c3fbb6) Viktor Meszaros *2018-06-04 10:24:38*

**dependency propagation fix**


[7c0af](https://github.com/manticore-projects/suramadu/commit/7c0af946c98fdc8) Viktor Meszaros *2018-05-31 12:52:06*

**secure rest endpoints**


[43e99](https://github.com/manticore-projects/suramadu/commit/43e9964a1bf9953) Viktor Meszaros *2018-05-31 12:46:18*

**disable activemq jmx**


[bdd42](https://github.com/manticore-projects/suramadu/commit/bdd4233ed109d0e) Viktor Meszaros *2018-05-31 12:44:26*

**README.md edited online with Bitbucket**


[166f9](https://github.com/manticore-projects/suramadu/commit/166f97d5ceff698) Andrej Zelman *2018-05-30 07:56:52*

**Switched https default to true.**


[98f47](https://github.com/manticore-projects/suramadu/commit/98f475ed2bceae8) Andrew Serff *2018-05-18 15:15:53*

**Added default values for boolean properties in case they aren't set. Also updated formatting.**


[025ba](https://github.com/manticore-projects/suramadu/commit/025ba18930bf545) Andrew Serff *2018-05-18 15:08:37*

**Made SSL ClientAuth configurable for #297**


[26f5a](https://github.com/manticore-projects/suramadu/commit/26f5a7d68481ad2) Andrew Serff *2018-05-17 15:10:21*

**resolves #295 - OpenIdWebswingUser is not serializable**


[e823e](https://github.com/manticore-projects/suramadu/commit/e823ecc4290979b) Andrej Zelman *2018-05-09 06:58:15*

**Netbeans startup fix (ClassCircularityError)**


[9078f](https://github.com/manticore-projects/suramadu/commit/9078f42e7126bf8) Viktor Meszaros *2018-05-08 22:33:07*

**README.md edited online with Bitbucket**


[29eeb](https://github.com/manticore-projects/suramadu/commit/29eeb03f7ef00e3) Andrej Zelman *2018-05-07 11:23:56*

**Tab key_typed event fix**


[05398](https://github.com/manticore-projects/suramadu/commit/0539897235e02cc) Viktor Meszaros *2018-05-03 23:33:39*

**DirectDraw NPE fix**


[1dcc9](https://github.com/manticore-projects/suramadu/commit/1dcc9c34a504e87) Viktor Meszaros *2018-04-24 20:07:04*

**mouse scroll sensitivity fix**


[9ee5f](https://github.com/manticore-projects/suramadu/commit/9ee5f9fda07564f) Viktor Meszaros *2018-04-24 01:31:49*

**resolves #293 : awt.Desktop.open fix**


[1c82b](https://github.com/manticore-projects/suramadu/commit/1c82b5dde3437f8) Viktor Meszaros *2018-04-23 10:16:27*

**resolves #292 : updated jetty version**


[4bf66](https://github.com/manticore-projects/suramadu/commit/4bf66efe5f8eb20) Viktor Meszaros *2018-04-23 10:00:32*

**Updating develop poms to hotfix version to avoid merge conflicts**


[47469](https://github.com/manticore-projects/suramadu/commit/474696f12463142) Viktor Meszaros *2018-04-13 23:33:24*

**updating poms for branch'hotfix/2.5.3' with non-snapshot versions**


[98e82](https://github.com/manticore-projects/suramadu/commit/98e82ab9cc06396) Viktor Meszaros *2018-04-13 23:29:05*

**JavaFx event time fix**


[db8d8](https://github.com/manticore-projects/suramadu/commit/db8d850f1f6975e) Viktor Meszaros *2018-04-13 23:27:32*

**openid connect watchdog fix (cherry picked from commit bfc0bda)**


[7b45a](https://github.com/manticore-projects/suramadu/commit/7b45aa248ad9448) Viktor Meszaros *2018-04-13 23:25:06*

**security http headers, jetty jmx disabled, jetty dependency fix**


[0ed85](https://github.com/manticore-projects/suramadu/commit/0ed850df893b111) Viktor Meszaros *2018-04-13 23:23:32*

**Added applicationUrl to SwingSession**


[c891b](https://github.com/manticore-projects/suramadu/commit/c891bddc526e448) Andrej Zelman *2018-04-13 10:31:30*

**3rd party libs version update**


[0adb4](https://github.com/manticore-projects/suramadu/commit/0adb4e75a5440d8) Viktor Meszaros *2018-04-13 08:50:38*

**Added global /rest/sessions**


[cbc18](https://github.com/manticore-projects/suramadu/commit/cbc18d3734c315a) Andrej Zelman *2018-04-10 17:32:35*

**updating poms for 2.5.3 branch with snapshot versions**


[0b878](https://github.com/manticore-projects/suramadu/commit/0b878ab08125c33) Viktor Meszaros *2018-04-09 16:59:53*

**applet jslink reference fix**


[71ae5](https://github.com/manticore-projects/suramadu/commit/71ae53ca8bf3780) Viktor Meszaros *2018-04-06 11:36:13*

**openid connect watchdog fix**


[bfc0b](https://github.com/manticore-projects/suramadu/commit/bfc0bdaab94fc42) Viktor Meszaros *2018-04-03 14:51:39*

**Updating develop poms back to pre merge state**


[1d62b](https://github.com/manticore-projects/suramadu/commit/1d62b530486b8d4) Viktor Meszaros *2018-03-27 05:59:07*

**Updating develop poms to hotfix version to avoid merge conflicts**


[cd63d](https://github.com/manticore-projects/suramadu/commit/cd63db6df87eb29) Viktor Meszaros *2018-03-27 05:59:00*

**updating poms for branch'hotfix/2.5.2' with non-snapshot versions**


[9828c](https://github.com/manticore-projects/suramadu/commit/9828c098661e313) Viktor Meszaros *2018-03-27 05:52:26*

**rendering fix (cherry picked from commit c86d189)**


[77e6c](https://github.com/manticore-projects/suramadu/commit/77e6c489e08e6d9) Viktor Meszaros *2018-03-27 05:42:41*

**DD xor mode for IE11 fix (cherry picked from commit 5b79db3)**


[d068d](https://github.com/manticore-projects/suramadu/commit/d068d8cdc678f3d) Viktor Meszaros *2018-03-27 05:42:00*

**fileupload fix (websphere) (cherry picked from commit 1532637)**


[bffef](https://github.com/manticore-projects/suramadu/commit/bffef5e7ed97cb8) Viktor Meszaros *2018-03-27 05:37:47*

**NoClassDefFoundException from static init of main class fix (cherry picked from commit 6bfb164)**


[5057f](https://github.com/manticore-projects/suramadu/commit/5057fc91bd2dd52) Viktor Meszaros *2018-03-27 05:36:31*

**JavaFx rendering optimizations (cherry picked from commit a9b62b8)**


[338a4](https://github.com/manticore-projects/suramadu/commit/338a460fad6a9d6) Viktor Meszaros *2018-03-27 05:36:05*

**jackson lib update (cherry picked from commit 7865c59)**


[cc555](https://github.com/manticore-projects/suramadu/commit/cc5556851803f15) Viktor Meszaros *2018-03-27 05:33:16*

**updating poms for 2.5.2 branch with snapshot versions**


[3c3bc](https://github.com/manticore-projects/suramadu/commit/3c3bc600c3cd675) Viktor Meszaros *2018-03-27 05:27:51*

**JavaFx rendering optimizations**


[a9b62](https://github.com/manticore-projects/suramadu/commit/a9b62b8e4cdd238) Viktor Meszaros *2018-03-25 13:20:28*

**DD xor mode for IE11 fix**


[bf6db](https://github.com/manticore-projects/suramadu/commit/bf6db8d36bb09b2) Viktor Meszaros *2018-03-24 06:16:14*

**DD xor mode for IE11 fix**


[5b79d](https://github.com/manticore-projects/suramadu/commit/5b79db3852ccb88) Viktor Meszaros *2018-03-20 12:49:23*

**docs update**


[08907](https://github.com/manticore-projects/suramadu/commit/0890719d0e45634) Viktor Meszaros *2018-03-09 16:27:02*

**NoClassDefFoundException from static init of main class fix**


[6bfb1](https://github.com/manticore-projects/suramadu/commit/6bfb164120556a1) Viktor Meszaros *2018-02-16 01:18:24*

**fileupload fix (websphere)**


[15326](https://github.com/manticore-projects/suramadu/commit/15326371eb99867) Viktor Meszaros *2018-02-16 00:33:01*

**rendering fix**


[c86d1](https://github.com/manticore-projects/suramadu/commit/c86d1898155d936) Viktor Meszaros *2018-02-15 02:21:05*

**extension fix**


[0e378](https://github.com/manticore-projects/suramadu/commit/0e37807a4acdcd9) Viktor Meszaros *2018-02-12 14:48:04*

**class blacklist generator**


[be951](https://github.com/manticore-projects/suramadu/commit/be95139c21c2c8b) Viktor Meszaros *2018-02-08 15:55:53*

**PropertyFile security serialization fix**


[2d278](https://github.com/manticore-projects/suramadu/commit/2d278788279846d) Andrej Zelman *2018-02-08 14:24:10*

**jackson lib update**


[7865c](https://github.com/manticore-projects/suramadu/commit/7865c595a50d672) Viktor Meszaros *2018-02-06 23:30:23*

**startup time optimization**


[c3962](https://github.com/manticore-projects/suramadu/commit/c3962b6788d7839) Viktor Meszaros *2018-02-06 10:49:24*

**path.separator build fix**


[286f3](https://github.com/manticore-projects/suramadu/commit/286f3fa7f1ad99c) Viktor Meszaros *2018-02-05 07:54:52*

**Fixes Serialization Exception for Property File security mode**


[86a56](https://github.com/manticore-projects/suramadu/commit/86a564c61d6f4d9) Andrej Zelman *2018-02-03 15:09:52*

**response headers fix, test fix**


[de512](https://github.com/manticore-projects/suramadu/commit/de512687729c3f8) Viktor Meszaros *2018-02-01 23:42:59*

**startup time optimization - class modification blacklist**


[eb17c](https://github.com/manticore-projects/suramadu/commit/eb17caa68af6716) Viktor Meszaros *2018-01-31 23:18:47*

**directdraw fallback fix**


[0f4b5](https://github.com/manticore-projects/suramadu/commit/0f4b521378f91d1) Viktor Meszaros *2018-01-31 00:07:10*

**caret location tracking**


[8f8ea](https://github.com/manticore-projects/suramadu/commit/8f8ea20689c878e) Viktor.meszaros *2018-01-30 15:51:58*

**extendable rest service handler**


[3bccb](https://github.com/manticore-projects/suramadu/commit/3bccb5b63c4460b) Viktor Meszaros *2018-01-24 02:26:50*

**directdraw fallback, update maven profile**


[2bf6c](https://github.com/manticore-projects/suramadu/commit/2bf6c08ed1a2401) Viktor.meszaros *2018-01-23 17:53:01*

**Refactoring Computation of running instance count to InstanceHolder**


[4644c](https://github.com/manticore-projects/suramadu/commit/4644c58e49abc2c) Andrej Zelman *2018-01-23 13:38:00*

**enterprise module integrations, improved startup service, login fix**


[0e164](https://github.com/manticore-projects/suramadu/commit/0e164832e61afc1) Viktor Meszaros *2018-01-22 03:09:03*

**added serialization to SwingInstanceImpl**


[2c296](https://github.com/manticore-projects/suramadu/commit/2c296fc25c9487e) Andrej Zelman *2018-01-19 12:49:34*

**Instance Holder update**


[569c0](https://github.com/manticore-projects/suramadu/commit/569c06207afe8fc) Andrej Zelman *2018-01-19 11:58:02*

**Updating develop poms back to pre merge state**


[92c04](https://github.com/manticore-projects/suramadu/commit/92c04179c213537) Viktor.meszaros *2018-01-19 11:45:10*

**merge 2.5.1 hot fixes to develop**


[e07af](https://github.com/manticore-projects/suramadu/commit/e07af42dae5adbc) Viktor Meszaros *2018-01-19 00:45:08*

**Updating develop poms to hotfix version to avoid merge conflicts**


[6fa10](https://github.com/manticore-projects/suramadu/commit/6fa105b6dcae25e) Viktor Meszaros *2018-01-19 00:21:03*

**updating poms for branch'hotfix/2.5.1' with non-snapshot versions**


[c9cc3](https://github.com/manticore-projects/suramadu/commit/c9cc3a0686568db) Viktor Meszaros *2018-01-19 00:15:48*

**directdraw xor mode - status fix**


[2e0a5](https://github.com/manticore-projects/suramadu/commit/2e0a5a8fa095fa1) Viktor Meszaros *2018-01-19 00:15:09*

**directdraw precision fix**


[27f91](https://github.com/manticore-projects/suramadu/commit/27f91ce77be2959) Viktor Meszaros *2018-01-19 00:09:54*

**disable release squash**


[977a1](https://github.com/manticore-projects/suramadu/commit/977a16e92e9f920) Viktor Meszaros *2018-01-18 23:48:19*

**PrinterJob.setPrintService fix (0663099), resolves #273 (f94cbf7), multi-window dnd fix (3d4ca4b), JavaFx multi-window DnD fix(7b20528)**


[4465c](https://github.com/manticore-projects/suramadu/commit/4465c42cea7a4d5) Viktor Meszaros *2018-01-18 23:23:12*

**updating poms for 2.5.1 branch with snapshot versions**


[e6183](https://github.com/manticore-projects/suramadu/commit/e6183643ccc34ec) Viktor Meszaros *2018-01-18 23:01:33*

**JavaFx multi-window DnD fix**


[7b205](https://github.com/manticore-projects/suramadu/commit/7b20528cab24023) Viktor Meszaros *2018-01-18 22:50:15*

**session mode handling refactored**


[822b3](https://github.com/manticore-projects/suramadu/commit/822b3a5dbe398fd) Viktor.meszaros *2018-01-17 18:53:15*

**Adding possibility to override SessionManager**


[7ba0b](https://github.com/manticore-projects/suramadu/commit/7ba0b4dde08aec9) Andrej Zelman *2018-01-16 20:59:16*

**instance holder refactoring**


[cbf24](https://github.com/manticore-projects/suramadu/commit/cbf24b4cd00ce62) Viktor.meszaros *2018-01-16 11:48:19*

**adding CacheManager override possibility**


[1444f](https://github.com/manticore-projects/suramadu/commit/1444f9b603c8a90) Andrej Zelman *2018-01-16 10:09:26*

**simultaneous multiple file download**


[d8ab4](https://github.com/manticore-projects/suramadu/commit/d8ab43db910be66) Viktor.meszaros *2018-01-15 16:09:52*

**InstanceHolder factor-out from Manager**


[d1aee](https://github.com/manticore-projects/suramadu/commit/d1aee1de9705468) Andrej Zelman *2018-01-15 14:07:55*

**multi-window dnd fix**


[3d4ca](https://github.com/manticore-projects/suramadu/commit/3d4ca4bdc92f09c) Viktor.meszaros *2018-01-15 13:30:33*

**methods visibility update**


[734c7](https://github.com/manticore-projects/suramadu/commit/734c7ebeb7a6cd8) Andrej Zelman *2018-01-13 19:36:25*

**Refactored Rest services, cleanup**


[50b75](https://github.com/manticore-projects/suramadu/commit/50b75bca9b16c9b) Viktor.meszaros *2018-01-12 16:11:28*

**resolves #273 Merged in YannisBres/webswing (pull request #22)**


[f94cb](https://github.com/manticore-projects/suramadu/commit/f94cbf7b3fea456) Viktor.meszaros *2018-01-11 16:51:12*

**rest api fix**


[311fc](https://github.com/manticore-projects/suramadu/commit/311fca769f7cf8e) Viktor.meszaros *2018-01-11 16:49:57*

**JavaFx build lib**


[00f0a](https://github.com/manticore-projects/suramadu/commit/00f0ad4d1de274e) Viktor Meszaros *2018-01-11 16:20:32*

**java9 compatibility lib**


[6c87b](https://github.com/manticore-projects/suramadu/commit/6c87b6a5d067c94) Viktor.meszaros *2018-01-11 15:47:35*

**Java9 compatibility improvements and bug fixes (known java9 issues: isolatedFS and javafx are broken)**


[3df5b](https://github.com/manticore-projects/suramadu/commit/3df5bebeb947645) Viktor.meszaros *2018-01-11 15:41:20*

**js initialization function**


[6068e](https://github.com/manticore-projects/suramadu/commit/6068e33babbbd23) Viktor.meszaros *2018-01-09 11:50:59*

**Adding Serializable for shared cache purposes**


[43723](https://github.com/manticore-projects/suramadu/commit/43723bca65afa3c) Andrej Zelman *2018-01-06 09:31:48*

**NPE fix for non provided bindingModule**


[26e44](https://github.com/manticore-projects/suramadu/commit/26e442365bcb3f0) Andrej Zelman *2018-01-05 08:42:06*

**Service Injection update, EnterpriseExtension revert**


[b2043](https://github.com/manticore-projects/suramadu/commit/b204387023fca37) Andrej Zelman *2018-01-05 07:42:19*

**PrinterJob.setPrintService fix**


[06630](https://github.com/manticore-projects/suramadu/commit/0663099fed34669) Viktor Meszaros *2018-01-02 13:14:24*

**updates for enterprise extensions**


[1e4e1](https://github.com/manticore-projects/suramadu/commit/1e4e13cceff9a07) Andrej Zelman *2017-12-30 12:04:53*

**Updating develop poms back to pre merge state**


[63689](https://github.com/manticore-projects/suramadu/commit/63689658e460e83) Viktor Meszaros *2017-12-20 19:05:26*

**squashing 'master' into 'develop'**


[e16d1](https://github.com/manticore-projects/suramadu/commit/e16d1e8877871ef) Viktor Meszaros *2017-12-20 19:05:22*

**updating develop poms to master versions to avoid merge conflicts**


[52c2b](https://github.com/manticore-projects/suramadu/commit/52c2b3bb955678e) Viktor Meszaros *2017-12-20 19:05:18*

**squashing 'release/2.5' into 'master'**


[64901](https://github.com/manticore-projects/suramadu/commit/64901b6e6661faa) Viktor Meszaros *2017-12-20 19:04:54*

**updating poms for 2.6-SNAPSHOT development**


[1dfa8](https://github.com/manticore-projects/suramadu/commit/1dfa89ed88fe3e7) Viktor Meszaros *2017-12-20 18:57:32*

**git attrs**


[b0a30](https://github.com/manticore-projects/suramadu/commit/b0a302d439e54a9) Viktor Meszaros *2017-12-20 18:55:51*

**documentation update, publish build profile**


[5cde5](https://github.com/manticore-projects/suramadu/commit/5cde58783fc865f) Viktor Meszaros *2017-12-20 15:17:08*

**javafx mousemove event fix**


[8faca](https://github.com/manticore-projects/suramadu/commit/8faca6d1564513f) Viktor Meszaros *2017-12-18 23:48:53*

**javafx dnd fix (none_action drop)**


[a78c6](https://github.com/manticore-projects/suramadu/commit/a78c670330b7979) Viktor Meszaros *2017-12-15 22:40:27*

**pdf.js version update**


[9378c](https://github.com/manticore-projects/suramadu/commit/9378c4a4f139907) Viktor Meszaros *2017-12-11 17:20:43*

**upload fix for mac**


[bebd1](https://github.com/manticore-projects/suramadu/commit/bebd1ea4150ff13) RichardMszrs *2017-12-11 09:53:37*

**DD fix texture paint exception**


[9e4ad](https://github.com/manticore-projects/suramadu/commit/9e4addc04e5084a) Viktor Meszaros *2017-12-01 09:10:18*

**cleanup**


[73a3a](https://github.com/manticore-projects/suramadu/commit/73a3a111027ab86) Viktor Meszaros *2017-11-29 11:54:40*

**blocked event handling fix (close dialog while moving)**


[91cd2](https://github.com/manticore-projects/suramadu/commit/91cd22f8ddbbf7b) Viktor Meszaros *2017-11-24 13:51:53*

**doubled input events fix**


[d6625](https://github.com/manticore-projects/suramadu/commit/d66259603509792) Viktor Meszaros *2017-11-24 09:31:21*

**altgr chars fix**


[45298](https://github.com/manticore-projects/suramadu/commit/4529888faa28d8e) Viktor Meszaros *2017-11-24 08:16:09*

**EDT monitoring, thread dump tool, warning messages improved**


[67e9c](https://github.com/manticore-projects/suramadu/commit/67e9cee4448551c) Viktor Meszaros *2017-11-23 14:11:57*

**squashing 'feature/jfx_performance' into 'develop'**


[f8b56](https://github.com/manticore-projects/suramadu/commit/f8b566c48a87d57) Viktor Meszaros *2017-11-19 23:45:48*

**directdraw xormode composition**


[2c8d1](https://github.com/manticore-projects/suramadu/commit/2c8d11b719c65b6) Viktor Meszaros *2017-11-17 09:01:07*

**saml2 fix (cherry picked from commit 23cfe1e)**


[35557](https://github.com/manticore-projects/suramadu/commit/35557666181cf18) Viktor Meszaros *2017-11-17 08:19:05*

**saml2 fix**


[23cfe](https://github.com/manticore-projects/suramadu/commit/23cfe1e84bca127) Viktor Meszaros *2017-11-16 23:28:13*

**tomcat shutdown fix, tomcat url redirect fix, thread names**


[f1ef9](https://github.com/manticore-projects/suramadu/commit/f1ef940ec343acb) Viktor Meszaros *2017-11-15 23:31:16*

**font init fix**


[c67ce](https://github.com/manticore-projects/suramadu/commit/c67ce6ab5203807) Viktor Meszaros *2017-11-14 23:06:35*

**server side printing**


[da1d6](https://github.com/manticore-projects/suramadu/commit/da1d65fe34649d9) Viktor Meszaros *2017-11-13 23:31:19*

**messaging api fix**


[d63de](https://github.com/manticore-projects/suramadu/commit/d63ded8807cf94b) Viktor Meszaros *2017-11-03 23:08:00*

**gcloud build fix**


[f2332](https://github.com/manticore-projects/suramadu/commit/f2332947ccc0b90) Viktor Meszaros *2017-11-01 13:32:37*

**resolves #265 doubled touch events fix**


[f15cc](https://github.com/manticore-projects/suramadu/commit/f15cc1733ad182c) Viktor Meszaros *2017-11-01 01:33:05*

**gcloud build fix**


[4c66d](https://github.com/manticore-projects/suramadu/commit/4c66dd6516c6e4b) Viktor Meszaros *2017-10-31 20:46:21*

**gcloud build fix**


[5fbc2](https://github.com/manticore-projects/suramadu/commit/5fbc2eae7851d90) Viktor Meszaros *2017-10-31 20:33:18*

**gcloud build fix**


[1c6e2](https://github.com/manticore-projects/suramadu/commit/1c6e202685830dc) Viktor Meszaros *2017-10-31 20:19:52*

**release config**


[5258c](https://github.com/manticore-projects/suramadu/commit/5258ce432313bf3) Viktor Meszaros *2017-10-31 15:10:43*

**javafx fix**


[e396f](https://github.com/manticore-projects/suramadu/commit/e396f50a89ccabe) Viktor Meszaros *2017-10-30 10:38:50*

**maven central deployement requirements**


[31325](https://github.com/manticore-projects/suramadu/commit/313253f11b26a95) Viktor Meszaros *2017-10-28 00:13:42*

**removed java6 support**


[2f82a](https://github.com/manticore-projects/suramadu/commit/2f82af21d80806d) Viktor Meszaros *2017-10-27 15:23:37*

**rest features registration**


[fdd68](https://github.com/manticore-projects/suramadu/commit/fdd68a04b1def7f) Viktor Meszaros *2017-10-27 00:09:08*

**file upload - IE browse button fix**


[db8da](https://github.com/manticore-projects/suramadu/commit/db8da37022f64a8) Viktor Meszaros *2017-10-25 14:36:46*

**NPE fix (SunGraphics2D.validateColor at WebComponentPeer.createVolatileImage)**


[20a59](https://github.com/manticore-projects/suramadu/commit/20a599bbeb07eb8) Viktor Meszaros *2017-10-23 20:32:07*

**print file id fix**


[b6844](https://github.com/manticore-projects/suramadu/commit/b68449d96729827) Viktor Meszaros *2017-10-18 16:00:03*

**jersey rest handler**


[77d7c](https://github.com/manticore-projects/suramadu/commit/77d7ce0db44e8fc) Viktor Meszaros *2017-10-18 15:58:55*

**api fix**


[69420](https://github.com/manticore-projects/suramadu/commit/69420e603251d64) Viktor Meszaros *2017-10-15 12:40:14*

**configurable paint ack timeout**


[2ca69](https://github.com/manticore-projects/suramadu/commit/2ca69d6102537c3) Viktor Meszaros *2017-10-11 21:20:47*

**file upload fixes**


[e4929](https://github.com/manticore-projects/suramadu/commit/e49291b9e78fd77) Viktor Meszaros *2017-10-09 08:47:28*

**directdraw debug log**


[08d04](https://github.com/manticore-projects/suramadu/commit/08d04d42756f428) Viktor Meszaros *2017-10-04 13:59:44*

**server extensions**


[050e6](https://github.com/manticore-projects/suramadu/commit/050e69b3e42de1e) Viktor Meszaros *2017-10-04 13:56:53*

**root security module fallback fix**


[6b6be](https://github.com/manticore-projects/suramadu/commit/6b6be20c97db1bc) Viktor Meszaros *2017-10-03 12:23:00*

**browser cache fix**


[ff408](https://github.com/manticore-projects/suramadu/commit/ff4086be07cc694) Viktor Meszaros *2017-10-03 09:37:38*

**keyup char fix, mousedrag button fix**


[6412e](https://github.com/manticore-projects/suramadu/commit/6412e2fcf350530) Viktor Meszaros *2017-10-02 20:07:53*

**cookies disabled check**


[3d125](https://github.com/manticore-projects/suramadu/commit/3d1252363e0ce80) Viktor Meszaros *2017-10-02 14:19:21*

**keyup char fix, mousedrag button fix**


[a5681](https://github.com/manticore-projects/suramadu/commit/a56813d22bca134) Viktor Meszaros *2017-09-29 12:51:05*

**logout with cors fix**


[d7539](https://github.com/manticore-projects/suramadu/commit/d75394bac4b7586) Viktor Meszaros *2017-09-28 08:15:24*

**stats fix, ping warning calibration**


[713af](https://github.com/manticore-projects/suramadu/commit/713affa8050bf5c) Viktor Meszaros *2017-09-25 20:35:58*

**mouse move event fix for FF and IE**


[7e8f2](https://github.com/manticore-projects/suramadu/commit/7e8f26d693b08e4) Viktor Meszaros *2017-09-22 17:36:43*

**login with cors fix**


[d6c1d](https://github.com/manticore-projects/suramadu/commit/d6c1dbe01a8a8e7) Viktor Meszaros *2017-09-22 14:22:00*

**resolves #266 #267**


[1d721](https://github.com/manticore-projects/suramadu/commit/1d721ab5a074c5b) Andrej Zelman *2017-09-21 22:16:18*

**default enabled=true when creating new app**


[b0f31](https://github.com/manticore-projects/suramadu/commit/b0f3168edc26e48) Viktor Meszaros *2017-09-21 21:52:00*

**app path config variable, NPE fix**


[d79d5](https://github.com/manticore-projects/suramadu/commit/d79d5f04776f6fd) Viktor Meszaros *2017-09-21 18:50:11*

**Main cleanup**


[a061a](https://github.com/manticore-projects/suramadu/commit/a061a30134c8048) Andrej Zelman *2017-09-21 15:52:27*

**#267 cleanup**


[7cc69](https://github.com/manticore-projects/suramadu/commit/7cc6950005e37be) Andrej Zelman *2017-09-21 15:47:46*

**#267 removing unwanted CLI param**


[dc788](https://github.com/manticore-projects/suramadu/commit/dc7887b8a093921) Andrej Zelman *2017-09-21 15:38:45*

**NPE fix**


[92acc](https://github.com/manticore-projects/suramadu/commit/92acc39c64205c7) Viktor Meszaros *2017-09-21 13:09:05*

**#267 generic CLI flag for custom system properties file**


[4fcb5](https://github.com/manticore-projects/suramadu/commit/4fcb50722866119) Andrej Zelman *2017-09-21 08:20:05*

**#267 do not override system properties with default**


[2f33e](https://github.com/manticore-projects/suramadu/commit/2f33e300aed825a) Andrej Zelman *2017-09-20 19:12:17*

**#266 added CLI help**


[a639b](https://github.com/manticore-projects/suramadu/commit/a639b124481c1bc) Andrej Zelman *2017-09-20 19:10:27*

**#266 new flag for clean/keep temp folder during startup**


[9fbda](https://github.com/manticore-projects/suramadu/commit/9fbda77caea4d1d) Andrej Zelman *2017-09-20 07:57:05*

**#267 do not overwrite existing system properties**


[ec273](https://github.com/manticore-projects/suramadu/commit/ec2737e122d424d) Andrej Zelman *2017-09-20 07:21:46*

**#267 adding new startup variable for preserving system variables**


[416b7](https://github.com/manticore-projects/suramadu/commit/416b717f848104b) Andrej Zelman *2017-09-19 19:37:42*

**file upload logging improved**


[d8b66](https://github.com/manticore-projects/suramadu/commit/d8b66fffe850b2f) Viktor Meszaros *2017-09-19 17:10:33*

**click event tolerance**


[1c88a](https://github.com/manticore-projects/suramadu/commit/1c88a936ac069d2) Viktor Meszaros *2017-09-18 18:43:32*

**clipboard paste request (api)**


[150ab](https://github.com/manticore-projects/suramadu/commit/150abf43f6c58bc) Viktor Meszaros *2017-09-15 11:43:19*

**custom fonts install fix**


[b2e1a](https://github.com/manticore-projects/suramadu/commit/b2e1acad700299a) Viktor Meszaros *2017-09-13 10:25:40*

**DirectDraw rendering bias calculation fix**


[d56cd](https://github.com/manticore-projects/suramadu/commit/d56cd81c38f5e61) Viktor Meszaros *2017-09-12 22:11:12*

**resolves #257 : progress indicator for login dialog**


[41a0b](https://github.com/manticore-projects/suramadu/commit/41a0b0264cb9005) Viktor Meszaros *2017-09-11 11:15:06*

**win7 IE11 connection fix**


[29f4a](https://github.com/manticore-projects/suramadu/commit/29f4a477cc2752b) Viktor Meszaros *2017-09-07 11:48:37*

**fix double click (network latency)**


[0ca5e](https://github.com/manticore-projects/suramadu/commit/0ca5e564a7a20d8) Viktor Meszaros *2017-09-04 13:56:08*

**NPE fix in Clipboard**


[2339e](https://github.com/manticore-projects/suramadu/commit/2339eb8ab6864fb) Viktor Meszaros *2017-08-28 15:56:50*

**run shutdown listener in separate thread**


[6317b](https://github.com/manticore-projects/suramadu/commit/6317bd7ec9b1474) Viktor Meszaros *2017-08-28 10:45:29*

**directdraw chrome rendering fix**


[27222](https://github.com/manticore-projects/suramadu/commit/272222c5c17baed) Viktor Meszaros *2017-07-28 21:09:07*

**directdraw chrome rendering fix**


[959eb](https://github.com/manticore-projects/suramadu/commit/959eb86dedd9333) Viktor Meszaros *2017-07-28 20:55:24*

**fix disconnect dialog before connected**


[28c91](https://github.com/manticore-projects/suramadu/commit/28c91844412bd59) Viktor Meszaros *2017-07-28 15:30:35*

**ping url resolution fix**


[5c36a](https://github.com/manticore-projects/suramadu/commit/5c36a69f1094f68) Viktor Meszaros *2017-07-27 15:56:25*

**network connection monitor**


[1c357](https://github.com/manticore-projects/suramadu/commit/1c35760a63600f2) Viktor Meszaros *2017-07-26 10:24:39*

**remove ctrl+shift+v paste support (only works in chrome)**


[f9e07](https://github.com/manticore-projects/suramadu/commit/f9e07c3dbfd0e9c) Viktor Meszaros *2017-07-25 16:31:44*

**javafx focus fix**


[977e1](https://github.com/manticore-projects/suramadu/commit/977e113816f9e9e) Viktor Meszaros *2017-07-24 16:17:21*

**continue dialog improvement**


[09481](https://github.com/manticore-projects/suramadu/commit/09481b5b95ead45) Viktor Meszaros *2017-07-24 12:44:46*

**resolves #254: system properties fix**


[c33ea](https://github.com/manticore-projects/suramadu/commit/c33ea1aacbf469c) Viktor Meszaros *2017-07-23 09:07:32*

**resolves #217 : file dialog display fix if folder does not exist (Win LaF)**


[642d9](https://github.com/manticore-projects/suramadu/commit/642d9267b25dbab) Viktor Meszaros *2017-07-22 13:06:29*

**resolves #255 : focus issue fix2**


[d011d](https://github.com/manticore-projects/suramadu/commit/d011daa5303489d) Viktor Meszaros *2017-07-22 12:15:49*

**sds integration fix**


[cc264](https://github.com/manticore-projects/suramadu/commit/cc264a56cf2315c) Viktor Meszaros *2017-07-21 17:42:31*

**resolves #255 : focus issue fix**


[e5d1e](https://github.com/manticore-projects/suramadu/commit/e5d1e227cfc6437) Viktor Meszaros *2017-07-21 10:44:19*

**continue dialog improvements**


[0168a](https://github.com/manticore-projects/suramadu/commit/0168a417c214594) Viktor Meszaros *2017-07-20 14:33:02*

**resolves  #223 : custom clipboard integration api**


[b85a5](https://github.com/manticore-projects/suramadu/commit/b85a5ec2ce05099) Viktor Meszaros *2017-07-20 12:16:39*

**configuration reloading fix**


[785e7](https://github.com/manticore-projects/suramadu/commit/785e78e3a46749c) Viktor Meszaros *2017-07-19 17:47:10*

**chrome memory leak fix**


[32ebc](https://github.com/manticore-projects/suramadu/commit/32ebc8ecdee9f14) Viktor Meszaros *2017-07-12 22:41:48*

**event handling fix**


[0c9af](https://github.com/manticore-projects/suramadu/commit/0c9af59c1d58b0f) Viktor Meszaros *2017-07-04 21:18:33*

**messaging api**


[02862](https://github.com/manticore-projects/suramadu/commit/02862264e642875) Viktor Meszaros *2017-07-04 19:21:32*

**resolves #240 #236 touch events fix**


[21ccf](https://github.com/manticore-projects/suramadu/commit/21ccf3eda5dce4d) Viktor Meszaros *2017-07-03 12:45:10*

**see #222 : get connected user fix**


[ed3f0](https://github.com/manticore-projects/suramadu/commit/ed3f0a5583c36e0) Viktor Meszaros *2017-06-29 11:58:07*

**increased max websocket message size (fix copy-paste from outlook)**


[2798d](https://github.com/manticore-projects/suramadu/commit/2798de353941dae) Viktor Meszaros *2017-06-27 22:22:13*

**resolves #235 :focus back to most recent after window closed**


[fa092](https://github.com/manticore-projects/suramadu/commit/fa09291fc26adad) Viktor Meszaros *2017-06-27 20:52:27*

**javafx init fix**


[54b6d](https://github.com/manticore-projects/suramadu/commit/54b6d5ff6165e85) Viktor Meszaros *2017-06-27 20:32:45*

**resolves #232 : fix of session handling for CONTINUE_FOR_BROWSER mode**


[60db9](https://github.com/manticore-projects/suramadu/commit/60db9fb7c19da7f) Viktor Meszaros *2017-06-27 18:02:39*

**sds integration fixes**


[e2c3f](https://github.com/manticore-projects/suramadu/commit/e2c3f70b7019ba2) Viktor Meszaros *2017-06-27 13:44:00*

**javafx context menu fix**


[fee62](https://github.com/manticore-projects/suramadu/commit/fee62cdcf0c65cb) Viktor Meszaros *2017-06-23 17:25:08*

**resolves #252 : dnd exception**


[a8349](https://github.com/manticore-projects/suramadu/commit/a83495770644ddd) Viktor Meszaros *2017-06-22 20:49:12*

**disconnected dialog fix**


[13c69](https://github.com/manticore-projects/suramadu/commit/13c69db6863b6d5) Viktor Meszaros *2017-06-14 14:01:42*

**NPE fix**


[9bd5e](https://github.com/manticore-projects/suramadu/commit/9bd5e74f9c2dcf2) Viktor Meszaros *2017-06-13 09:44:57*

**SAML2 module encryption support**


[737c6](https://github.com/manticore-projects/suramadu/commit/737c6010ff96c58) Viktor Meszaros *2017-06-11 22:51:03*

**resolves #245 : stats aggregation fix**


[3dc41](https://github.com/manticore-projects/suramadu/commit/3dc418da15f3c2a) Viktor Meszaros *2017-06-09 11:13:22*

**resolves #222 NPE fix**


[6d1b0](https://github.com/manticore-projects/suramadu/commit/6d1b0a83b48d8fd) Viktor Meszaros *2017-06-09 09:30:34*

**see #242 fix git.properties generation**


[30d0e](https://github.com/manticore-projects/suramadu/commit/30d0e9cfa29da60) Viktor Meszaros *2017-06-08 21:42:07*

**resolves #244 #245 : NPE and stats aggregation fix**


[482e7](https://github.com/manticore-projects/suramadu/commit/482e755e4c21cfc) Viktor Meszaros *2017-06-08 11:46:03*

**resolves #227 #238 : AWT Robot support**


[012f7](https://github.com/manticore-projects/suramadu/commit/012f70f3ae1ea64) Viktor Meszaros *2017-06-06 21:42:11*

**force lower case URL's (SAML2 fix)**


[98bc9](https://github.com/manticore-projects/suramadu/commit/98bc976e29e0023) Viktor Meszaros *2017-06-05 15:23:42*

**openid redirect fix**


[02240](https://github.com/manticore-projects/suramadu/commit/022405e0be2024f) Viktor Meszaros *2017-05-31 23:44:02*

**watch configuration file changes**


[f2dcd](https://github.com/manticore-projects/suramadu/commit/f2dcdaa7327d4b0) Viktor Meszaros *2017-05-30 23:14:28*

**gce snapshot build fix**


[923af](https://github.com/manticore-projects/suramadu/commit/923af7b511298a4) Viktor Meszaros *2017-05-23 17:30:57*

**gce snapshot build fix**


[7aed2](https://github.com/manticore-projects/suramadu/commit/7aed2bdc9e5d052) Viktor Meszaros *2017-05-23 17:13:20*

**fix FileDialog integration**


[65753](https://github.com/manticore-projects/suramadu/commit/6575387b401e91d) Viktor Meszaros *2017-05-23 16:02:34*

**gce snapshot build**


[73c0c](https://github.com/manticore-projects/suramadu/commit/73c0c4fac8a7087) Viktor Meszaros *2017-05-17 22:16:44*

**gce snapshot build**


[653be](https://github.com/manticore-projects/suramadu/commit/653be40c9f887cf) Viktor Meszaros *2017-05-17 21:56:45*

**resolves #199: localization support**


[8063c](https://github.com/manticore-projects/suramadu/commit/8063c997e1889ed) Viktor Meszaros *2017-05-17 20:59:42*

**websphere fix (use PARENT_LAST classloading order)**


[ce45c](https://github.com/manticore-projects/suramadu/commit/ce45c25010cbe66) Viktor Meszaros *2017-05-11 04:19:52*

**properties SM path handling fix, selector page improvement**


[23956](https://github.com/manticore-projects/suramadu/commit/239564ef8e3ebec) Viktor Meszaros *2017-05-02 17:56:58*

**CSP header fix for FF**


[1a45a](https://github.com/manticore-projects/suramadu/commit/1a45a329a729046) Viktor Meszaros *2017-05-01 01:46:08*

**directdraw animated gif handling fix**


[01724](https://github.com/manticore-projects/suramadu/commit/017245be6aa5efb) Viktor Meszaros *2017-04-27 20:04:08*

**alt+key events  fix**


[4a586](https://github.com/manticore-projects/suramadu/commit/4a586e7ff0dda4a) Viktor Meszaros *2017-04-26 21:28:53*

**ie11 cache fix**


[44e4a](https://github.com/manticore-projects/suramadu/commit/44e4adc808c6d79) Viktor Meszaros *2017-04-26 15:44:46*

**multiple upload folders support**


[1d059](https://github.com/manticore-projects/suramadu/commit/1d059ed9f99ee1c) Viktor Meszaros *2017-04-26 01:24:01*

**user inactivity timeout**


[f3031](https://github.com/manticore-projects/suramadu/commit/f30315dcc0a908a) Viktor Meszaros *2017-04-25 21:28:01*

**resolves #214 : dialog with modal type DOCUMENT_MODAL fix**


[095e1](https://github.com/manticore-projects/suramadu/commit/095e14f221bd025) viktor *2017-04-20 17:42:14*

**auto-reload user.properties file**


[7b20a](https://github.com/manticore-projects/suramadu/commit/7b20a84e0651b19) Viktor Meszaros *2017-04-18 04:46:02*

**keycloak security module fallback option**


[66ecc](https://github.com/manticore-projects/suramadu/commit/66ecc1f148d6838) Viktor Meszaros *2017-04-17 19:41:50*

**resolves #208 : broken ssl**


[00cc3](https://github.com/manticore-projects/suramadu/commit/00cc3ab75451647) Viktor Meszaros *2017-04-17 16:40:10*

**websocket security fixes**


[ec675](https://github.com/manticore-projects/suramadu/commit/ec675b4ffedad53) Viktor Meszaros *2017-04-11 18:12:13*

**README.md edited online with Bitbucket**


[75856](https://github.com/manticore-projects/suramadu/commit/7585648d5463bde) Viktor Meszaros *2017-04-07 18:41:03*

**bitbucket-pipelines.yml fix 2**


[9580c](https://github.com/manticore-projects/suramadu/commit/9580cb46daab452) Viktor Meszaros *2017-04-07 17:03:01*

**bitbucket-pipelines.yml fix**


[b2cf0](https://github.com/manticore-projects/suramadu/commit/b2cf068493cabff) Viktor Meszaros *2017-04-07 16:45:08*

**bitbucket pipelines upload fix**


[b7d4d](https://github.com/manticore-projects/suramadu/commit/b7d4dea7a59ef1c) Viktor Meszaros *2017-04-07 16:24:19*

**bitbucket pipelines upload fix**


[09550](https://github.com/manticore-projects/suramadu/commit/095501e832e741d) Viktor Meszaros *2017-04-07 16:18:16*

**bitbucket pipelines upload result**


[c4f77](https://github.com/manticore-projects/suramadu/commit/c4f77c2aec2851b) Viktor Meszaros *2017-04-06 21:34:59*

**URL state API order fix**


[be47a](https://github.com/manticore-projects/suramadu/commit/be47adef68b8c39) Viktor Meszaros *2017-04-05 15:25:41*

**URL state API improvement**


[3df53](https://github.com/manticore-projects/suramadu/commit/3df53a85fa0f8e2) Viktor Meszaros *2017-04-03 20:31:23*

**right click drag event fix**


[0ee5b](https://github.com/manticore-projects/suramadu/commit/0ee5b5786abd8c2) Viktor Meszaros *2017-04-03 20:28:27*

**Messages and labels corections**


[80762](https://github.com/manticore-projects/suramadu/commit/80762c9f9a5e465) Viktor Meszaros *2017-03-30 16:08:03*

**IE custom cursor fix**


[91954](https://github.com/manticore-projects/suramadu/commit/91954d142610c5e) Viktor Meszaros *2017-03-25 11:47:25*

**IE custom cursor fix**


[be1db](https://github.com/manticore-projects/suramadu/commit/be1db4cd8f6b05b) Viktor Meszaros *2017-03-24 01:03:40*

**URL navigation - param fix**


[f4366](https://github.com/manticore-projects/suramadu/commit/f4366e4bf7eb398) Viktor Meszaros *2017-03-21 20:25:07*

**ie10 - fallback to png rendering**


[55cc2](https://github.com/manticore-projects/suramadu/commit/55cc2df6ca67832) Viktor Meszaros *2017-03-21 03:13:44*

**ie10 directdraw fix (setDashLine)**


[08870](https://github.com/manticore-projects/suramadu/commit/08870b8b2d849bd) Viktor Meszaros *2017-03-18 16:19:50*

**dirty area re-paint fix**


[1044d](https://github.com/manticore-projects/suramadu/commit/1044d410d49e5be) Viktor Meszaros *2017-03-16 20:41:10*

**see #172 : java fx file chooser integration**


[bf2ca](https://github.com/manticore-projects/suramadu/commit/bf2ca836c769acc) Viktor Meszaros *2017-03-16 19:02:53*

**clip repaint fix**


[81320](https://github.com/manticore-projects/suramadu/commit/813203440f566fa) Viktor Meszaros *2017-03-15 21:36:58*

**Clipboard - handle paste special  (ctrl+shift+v)**


[deefb](https://github.com/manticore-projects/suramadu/commit/deefbb0417352e9) Viktor Meszaros *2017-03-14 19:20:26*

**API for URL navigation in swing apps**


[039eb](https://github.com/manticore-projects/suramadu/commit/039ebdd4974b599) Viktor Meszaros *2017-03-14 15:54:02*

**DnD speed fix**


[d908e](https://github.com/manticore-projects/suramadu/commit/d908eba54665aae) Viktor Meszaros *2017-03-10 23:21:29*

**DnD speed fix, see #172 : JavaFx Tab key handling fix**


[93c31](https://github.com/manticore-projects/suramadu/commit/93c316da0e6b789) Viktor Meszaros *2017-03-10 21:29:51*

**JavaFx termination fix**


[e8a6f](https://github.com/manticore-projects/suramadu/commit/e8a6f1d11e54aa7) Viktor Meszaros *2017-03-10 20:51:28*

**resolves #211 #172 #185 : Native JavaFX application support (java8 only, has to be enabled in config)**


[94a81](https://github.com/manticore-projects/suramadu/commit/94a81def1de8d61) Viktor Meszaros *2017-03-10 19:38:25*

**bitbucket pipeline config update**


[9de78](https://github.com/manticore-projects/suramadu/commit/9de78606929bf34) Viktor Meszaros *2017-03-02 17:06:41*

**NPE fix**


[7aa28](https://github.com/manticore-projects/suramadu/commit/7aa28b2cab07e29) Viktor Meszaros *2017-03-02 16:50:06*

**fix2**


[442ce](https://github.com/manticore-projects/suramadu/commit/442ce458efc844e) Viktor Meszaros *2017-02-28 17:42:43*

**fix**


[5d481](https://github.com/manticore-projects/suramadu/commit/5d48117635b7935) Viktor Meszaros *2017-02-28 17:20:17*

**fix compile**


[d7d5f](https://github.com/manticore-projects/suramadu/commit/d7d5f82dcd414a0) Viktor Meszaros *2017-02-28 17:06:56*

**warn user when websocket failed**


[4097f](https://github.com/manticore-projects/suramadu/commit/4097fcf44c2f358) Viktor Meszaros *2017-02-28 16:57:25*

**secured cookie**


[99f43](https://github.com/manticore-projects/suramadu/commit/99f4374064c65d7) Viktor Meszaros *2017-02-25 05:37:26*

**session recording from admin console**


[123ee](https://github.com/manticore-projects/suramadu/commit/123ee778bae6a96) Viktor Meszaros *2017-02-24 16:28:58*

**resolves  #213 freeze on thread interupt, fix of mirror view (with multi security contexts)**


[ba13a](https://github.com/manticore-projects/suramadu/commit/ba13ace4146ecbd) Viktor Meszaros *2017-02-23 22:31:28*

**access to role mapping extension**


[57d79](https://github.com/manticore-projects/suramadu/commit/57d79439c927113) Viktor Meszaros *2017-02-17 18:15:17*

**resolves #210 fix NPE on dialog dispose**


[fd5de](https://github.com/manticore-projects/suramadu/commit/fd5dec273e6e4c0) Viktor Meszaros *2017-02-17 18:10:36*

**resolves #197 goodbye url option**


[619fe](https://github.com/manticore-projects/suramadu/commit/619fe7c6745d24e) Viktor Meszaros *2017-02-11 05:40:25*

**stop jetty on init error, variable resolution for authorized roles**


[4059e](https://github.com/manticore-projects/suramadu/commit/4059e497f0db17d) Viktor Meszaros *2017-02-10 22:58:18*

**+**


[2a7b3](https://github.com/manticore-projects/suramadu/commit/2a7b3b974cb9e6a) Viktor Meszaros *2017-02-10 00:20:38*

**openid login/logout fix**


[e8674](https://github.com/manticore-projects/suramadu/commit/e8674d01cf552e2) Viktor Meszaros *2017-02-10 00:17:44*

**openID+keycloak+embeded security modules, key char handling fix, url handling jsLink example**


[a0895](https://github.com/manticore-projects/suramadu/commit/a0895ce2b6c8e62) Viktor Meszaros *2017-02-08 18:38:27*

**proxy fix, saml improvements**


[05251](https://github.com/manticore-projects/suramadu/commit/05251bc4a2ca2e3) Viktor Meszaros *2017-01-13 22:15:21*

**reverse proxy fix**


[cb6ab](https://github.com/manticore-projects/suramadu/commit/cb6ab2f4bccb880) Viktor Meszaros *2017-01-11 22:41:46*

**memory stat fix, graph label rendering fix**


[f7198](https://github.com/manticore-projects/suramadu/commit/f71980671020f63) Viktor Meszaros *2017-01-09 15:58:09*

**config table performance fix**


[21944](https://github.com/manticore-projects/suramadu/commit/21944c66dd57a8b) Viktor Meszaros *2017-01-06 23:38:26*

**permission mapping extension fix, new table editor type**


[20e9d](https://github.com/manticore-projects/suramadu/commit/20e9da11b50b286) Viktor Meszaros *2017-01-06 19:29:00*

**authorization config, bug fixes**


[ee3d1](https://github.com/manticore-projects/suramadu/commit/ee3d1f8328746b0) Viktor Meszaros *2016-12-30 22:25:26*

**reverse proxy path fix, logs download link**


[28514](https://github.com/manticore-projects/suramadu/commit/285148eee8cc9e3) Viktor Meszaros *2016-12-28 21:41:14*

**JS memory leak fix, bootclasspath fix**


[1b2ac](https://github.com/manticore-projects/suramadu/commit/1b2ac153068a3b9) Viktor Meszaros *2016-12-23 16:47:51*

**user ip, browser info in admin; warning signs history**


[0a6b0](https://github.com/manticore-projects/suramadu/commit/0a6b0a767292197) Viktor Meszaros *2016-12-23 16:15:53*

**admin sessions order and time format**


[6b8f8](https://github.com/manticore-projects/suramadu/commit/6b8f8baab5c0a71) Viktor Meszaros *2016-12-20 20:01:00*

**resolves #204 , revert jetty version, duplicate file name fix**


[ea77c](https://github.com/manticore-projects/suramadu/commit/ea77c5af15ad447) Viktor Meszaros *2016-12-20 15:55:15*

**ctrl+space fix**


[5beca](https://github.com/manticore-projects/suramadu/commit/5beca67cd40e3d4) Viktor Meszaros *2016-12-15 15:12:53*

**jetty 9.3 ssl error (downgrade to 9.2)**


[9fe5d](https://github.com/manticore-projects/suramadu/commit/9fe5d5dc583b469) Viktor Meszaros *2016-12-15 14:50:41*

**draw delay conig**


[77599](https://github.com/manticore-projects/suramadu/commit/77599a310738c12) Viktor Meszaros *2016-12-15 09:54:25*

**resolves #171 : ctrl+alt+key combination fix**


[76639](https://github.com/manticore-projects/suramadu/commit/766393748cb79e2) Viktor Meszaros *2016-12-14 23:52:14*

**print document size fix, double click fix**


[b4294](https://github.com/manticore-projects/suramadu/commit/b4294774287c278) Viktor Meszaros *2016-12-14 23:15:08*

**copy paste support for mac/safari**


[3ed77](https://github.com/manticore-projects/suramadu/commit/3ed7710ce4109fd) Viktor Meszaros *2016-12-14 23:14:08*

**security module classloading fix**


[24410](https://github.com/manticore-projects/suramadu/commit/2441026f45d254f) Viktor Meszaros *2016-12-13 09:28:34*

**scroll panel rendering fix**


[2b8a3](https://github.com/manticore-projects/suramadu/commit/2b8a3c58814389d) Viktor Meszaros *2016-12-12 14:22:10*

**save dialog input border**


[5cd2b](https://github.com/manticore-projects/suramadu/commit/5cd2b8c37ca1a8b) Viktor Meszaros *2016-12-08 18:36:39*

**anonym security module logout fix**


[8e842](https://github.com/manticore-projects/suramadu/commit/8e842b0f48c5a2f) Viktor Meszaros *2016-12-08 00:08:33*

**clipboard in edge, Ctrl+shift+x/c/v key event, dd image render fix**


[0d77a](https://github.com/manticore-projects/suramadu/commit/0d77a8ff8482d88) Viktor Meszaros *2016-12-06 22:35:20*

**resolves #201: IE font fix**


[d3cc4](https://github.com/manticore-projects/suramadu/commit/d3cc422e14fe422) Viktor Meszaros *2016-11-29 23:53:57*

**css refactoring and cleanup**


[c869b](https://github.com/manticore-projects/suramadu/commit/c869bf022060a01) Viktor Meszaros *2016-11-28 18:20:19*

**simplified clipboard, url case fix, decoration fix, user attrs variables**


[52f62](https://github.com/manticore-projects/suramadu/commit/52f6273113f8cb7) Viktor Meszaros *2016-11-25 22:02:57*

**resizing of windows**


[510c6](https://github.com/manticore-projects/suramadu/commit/510c6482a21a32a) Viktor Meszaros *2016-11-21 12:24:42*

**bug fixes (dnd, cursor, directdraw)**


[19918](https://github.com/manticore-projects/suramadu/commit/19918e9367d0aab) Viktor Meszaros *2016-11-18 12:07:56*

**admin console log viewer**


[28de3](https://github.com/manticore-projects/suramadu/commit/28de38f0bd57d6c) Viktor Meszaros *2016-11-15 19:36:34*

**wildfly, glassfish, tomcat compatibility fixes**


[33d5e](https://github.com/manticore-projects/suramadu/commit/33d5ecc9416c901) Viktor Meszaros *2016-11-14 12:24:56*

**audit log**


[76e29](https://github.com/manticore-projects/suramadu/commit/76e296e422cde1c) Viktor Meszaros *2016-11-08 12:40:45*

**bug fixes**


[f4f7f](https://github.com/manticore-projects/suramadu/commit/f4f7f325c152302) Viktor Meszaros *2016-11-04 00:22:32*

**changes for integration support 3**


[0fd10](https://github.com/manticore-projects/suramadu/commit/0fd106a44ee8975) Viktor Meszaros *2016-11-03 12:44:44*

**bug fixes, styling for file upload bar**


[86610](https://github.com/manticore-projects/suramadu/commit/86610bff8d7fda9) Viktor Meszaros *2016-10-28 23:18:44*

**changes for integration support 2**


[7b7cb](https://github.com/manticore-projects/suramadu/commit/7b7cbb0a8232b49) Viktor Meszaros *2016-10-26 19:48:03*

**changes for integration support**


[e5b5d](https://github.com/manticore-projects/suramadu/commit/e5b5d1ce3a24d68) Viktor Meszaros *2016-10-26 11:06:43*

**bitbucket-pipelines.yml created online with Bitbucket**


[4f648](https://github.com/manticore-projects/suramadu/commit/4f6482d9eb8b2f5) Viktor Meszaros *2016-10-17 08:46:18*

**Classloading fix**


[918ad](https://github.com/manticore-projects/suramadu/commit/918ad43ba89c6ef) Viktor Meszaros *2016-10-13 10:54:43*

**clipboard minimized**


[2092f](https://github.com/manticore-projects/suramadu/commit/2092fd9ff94881d) Viktor Meszaros *2016-10-13 08:52:38*

**clipboard toolbar improvements**


[fe181](https://github.com/manticore-projects/suramadu/commit/fe181fd6c50c8df) Viktor Meszaros *2016-10-12 21:39:28*

**admin console User session fix, classloading hierarchy fix for javafx**


[36d19](https://github.com/manticore-projects/suramadu/commit/36d19bc9131eaca) Viktor Meszaros *2016-10-12 12:56:14*

**remove some duplicate CSS rules**


[2935c](https://github.com/manticore-projects/suramadu/commit/2935cf30c49af7d) Stephen Niemans *2016-10-12 09:45:41*

**add styling to the clipboard panel, additional SCSS streamlining**


[5b6d4](https://github.com/manticore-projects/suramadu/commit/5b6d46a3a22995f) Stephen Niemans *2016-10-12 09:09:50*

**CSS Overhaul**


[6ddda](https://github.com/manticore-projects/suramadu/commit/6dddae9bfb2fab2) Stephen Niemans *2016-10-03 07:46:41*

**chrome scroll lag fix**


[fdbe7](https://github.com/manticore-projects/suramadu/commit/fdbe7e777b543b2) viktor *2016-10-02 14:42:36*

**rendering performance fix, messages improvement**


[51291](https://github.com/manticore-projects/suramadu/commit/512919d79c68af1) Viktor Meszaros *2016-10-01 19:18:02*

**message dialog and file integration bug fixes**


[f51aa](https://github.com/manticore-projects/suramadu/commit/f51aaf4cf9c6b29) Viktor Meszaros *2016-09-30 11:38:55*

**styling optimizations**


[3431e](https://github.com/manticore-projects/suramadu/commit/3431ef39c7283a0) Viktor Meszaros *2016-09-29 20:54:11*

**style fixes**


[1fd48](https://github.com/manticore-projects/suramadu/commit/1fd481ee0cb4d18) Viktor Meszaros *2016-09-29 11:32:39*

**sass maven build, bug fixes**


[13670](https://github.com/manticore-projects/suramadu/commit/136700c09dd8182) Viktor Meszaros *2016-09-29 02:02:35*

**add styling to the autosave dialog**


[0396c](https://github.com/manticore-projects/suramadu/commit/0396cf513185712) Stephen Niemans *2016-09-28 17:31:48*

**make changes to upload progress bar**


[9bbcc](https://github.com/manticore-projects/suramadu/commit/9bbccdfb08135bf) Stephen Niemans *2016-09-28 15:35:45*

**small bug fixes**


[cc2a3](https://github.com/manticore-projects/suramadu/commit/cc2a3aa1044462c) Viktor Meszaros *2016-09-28 15:22:37*

**tidy css with csscomb**


[2f50b](https://github.com/manticore-projects/suramadu/commit/2f50b335f307aa5) Stephen Niemans *2016-09-28 09:20:40*

**transparent file chooser integration**


[77da6](https://github.com/manticore-projects/suramadu/commit/77da674d68507cb) Viktor Meszaros *2016-09-27 20:42:42*

**cleanup css**


[f9786](https://github.com/manticore-projects/suramadu/commit/f9786a6f6e993a7) Stephen Niemans *2016-09-27 20:04:27*

**additional styling changes**


[e382c](https://github.com/manticore-projects/suramadu/commit/e382c15cdd0578d) Stephen Niemans *2016-09-27 12:12:13*

**admin console fix**


[fa45e](https://github.com/manticore-projects/suramadu/commit/fa45e92decdd435) Viktor Meszaros *2016-09-27 10:40:23*

**overhaul webapp styling**

* Login and upload dialogs complete 

[adb14](https://github.com/manticore-projects/suramadu/commit/adb1436e0924b13) Stephen Niemans *2016-09-27 08:11:23*

**ajax loading animation**


[2778d](https://github.com/manticore-projects/suramadu/commit/2778da5dd8d8a22) Viktor Meszaros *2016-09-22 18:04:11*

**Database security module**


[ba9ea](https://github.com/manticore-projects/suramadu/commit/ba9ead1c2490f86) Viktor Meszaros *2016-09-22 01:41:26*

**api jars in distribution package, small bug fixes**


[88815](https://github.com/manticore-projects/suramadu/commit/88815ae2aece674) Viktor Meszaros *2016-09-21 22:54:51*

**customize dialog messages**


[98245](https://github.com/manticore-projects/suramadu/commit/98245ca16118d59) Viktor Meszaros *2016-09-21 02:40:53*

**version in api and manifests**


[2a865](https://github.com/manticore-projects/suramadu/commit/2a865d3034fc0eb) Viktor Meszaros *2016-09-20 15:48:33*

**DirectDraw fix**


[78a68](https://github.com/manticore-projects/suramadu/commit/78a68db0bd8e6e5) Viktor Meszaros *2016-09-20 14:20:11*

**variable replacement optimization, bug fixes**


[0770a](https://github.com/manticore-projects/suramadu/commit/0770a3fba0a5f62) Viktor Meszaros *2016-09-19 18:23:32*

**admin console stats improvements**


[2eaa9](https://github.com/manticore-projects/suramadu/commit/2eaa9f1fa5ef8ec) Viktor Meszaros *2016-09-15 03:46:54*

**admin console stats warnings**


[bc0ae](https://github.com/manticore-projects/suramadu/commit/bc0ae07b272c385) Viktor Meszaros *2016-09-14 21:09:01*

**admin console improvements**


[dc847](https://github.com/manticore-projects/suramadu/commit/dc847c3afec2c76) Viktor Meszaros *2016-09-14 03:18:46*

**bug fixes and improvements**


[70c47](https://github.com/manticore-projects/suramadu/commit/70c47f46fe8874c) Viktor Meszaros *2016-09-12 20:38:03*

**bug fixes**


[3b3ed](https://github.com/manticore-projects/suramadu/commit/3b3edbfe977abb2) Viktor Meszaros *2016-09-10 02:22:00*

**path with space fix**


[c82cd](https://github.com/manticore-projects/suramadu/commit/c82cd8531af7b3a) Viktor Meszaros *2016-09-09 02:10:54*

**jetty 9, tomcat fix, other bug fixes**


[7ed65](https://github.com/manticore-projects/suramadu/commit/7ed65b2880d26f5) Viktor Meszaros *2016-09-09 00:49:47*

**auto logout after swing finished**


[0636e](https://github.com/manticore-projects/suramadu/commit/0636e05f5effae1) Viktor Meszaros *2016-09-08 10:54:26*

**version 2.5-SNAPSHOT**


[716d0](https://github.com/manticore-projects/suramadu/commit/716d0362310230a) Viktor Meszaros *2016-09-06 19:20:52*

**upload folder, unlimited session timeout, deadlock fix**


[acfe5](https://github.com/manticore-projects/suramadu/commit/acfe5c484b38b97) Viktor Meszaros *2016-09-06 18:39:15*

**DirectDraw memory consumption optimization**


[872d3](https://github.com/manticore-projects/suramadu/commit/872d3d143e9c7a4) vikto *2016-09-06 15:21:42*

**DD memory optimization**


[eff4e](https://github.com/manticore-projects/suramadu/commit/eff4e9ea56ff9d5) vikto *2016-09-06 14:20:25*

**resolves #123 IllegalArgumentException if width=0/height=0 , bcel v6.0**


[16a88](https://github.com/manticore-projects/suramadu/commit/16a88d41d8574fd) vikto *2016-09-05 17:11:18*

**bug fixes**


[0e148](https://github.com/manticore-projects/suramadu/commit/0e1486141232c60) vikto *2016-09-05 16:48:44*

**bcel 6**


[29c9a](https://github.com/manticore-projects/suramadu/commit/29c9a470a38397b) vikto *2016-09-05 13:34:59*

**resolves #168 #158 : move and resize events from Window fix**


[7392f](https://github.com/manticore-projects/suramadu/commit/7392f60873cb8a2) vikto *2016-09-03 02:01:36*

**bug fixes**


[1f230](https://github.com/manticore-projects/suramadu/commit/1f230cc27c0dbe8) vikto *2016-09-03 01:40:31*

**native windows LaF fix**


[44cb1](https://github.com/manticore-projects/suramadu/commit/44cb1df6a1ae408) vikto *2016-09-02 12:41:11*

**transparent file open**


[2332a](https://github.com/manticore-projects/suramadu/commit/2332a836f017040) vikto *2016-09-01 17:48:26*

**admin console refactoring 9**


[3df7c](https://github.com/manticore-projects/suramadu/commit/3df7cb9b9496cb1) vikto *2016-08-30 18:47:46*

**admin console refactoring 8**


[a6f94](https://github.com/manticore-projects/suramadu/commit/a6f9499a945ad2a) vikto *2016-08-30 14:44:22*

**admin console refactoring 7**


[e278b](https://github.com/manticore-projects/suramadu/commit/e278bb32b8fb166) vikto *2016-08-28 22:07:47*

**admin console refactoring 6**


[168e7](https://github.com/manticore-projects/suramadu/commit/168e7c4ff13c899) vikto *2016-08-27 01:54:58*

**admin console refactoring 5**


[d7035](https://github.com/manticore-projects/suramadu/commit/d70350083352ba9) vikto *2016-08-26 15:42:21*

**admin console refactoring 4**


[35acd](https://github.com/manticore-projects/suramadu/commit/35acd8ba1120549) vikto *2016-08-23 21:58:24*

**admin console refactoring 3**


[7b812](https://github.com/manticore-projects/suramadu/commit/7b81206a11817fb) vikto *2016-08-22 22:23:43*

**admin console refactoring 2**


[ab0e6](https://github.com/manticore-projects/suramadu/commit/ab0e6a239ca9708) vikto *2016-08-22 14:16:06*

**admin console refactoring 1**


[0599f](https://github.com/manticore-projects/suramadu/commit/0599f175c3c239b) vikto *2016-08-20 15:49:32*

**server configuration refactoring 2**


[66dff](https://github.com/manticore-projects/suramadu/commit/66dff4e4b13305e) vikto *2016-08-16 13:08:35*

**server configuration refactoring**


[2d6c2](https://github.com/manticore-projects/suramadu/commit/2d6c274e941d6f8) vikto *2016-08-15 17:09:16*

**cleanup, javadoc, permission map extension**


[f460d](https://github.com/manticore-projects/suramadu/commit/f460d417a9dace0) vikto *2016-08-08 14:48:09*

**one time url fix**


[0877e](https://github.com/manticore-projects/suramadu/commit/0877e343e212de3) vikto *2016-08-05 10:13:11*

**tomcat websocket fix**


[a5372](https://github.com/manticore-projects/suramadu/commit/a5372917542d7c5) vikto *2016-08-03 16:04:05*

**see #175 : unlimited session timeout, resolves #179: deadlock**


[8b49c](https://github.com/manticore-projects/suramadu/commit/8b49cc278cfc824) vikto *2016-08-02 21:34:47*

**docker apache proxy**


[c3049](https://github.com/manticore-projects/suramadu/commit/c3049762cf0beb2) viktor *2016-08-02 16:24:10*

**server refactoring 13**


[eb258](https://github.com/manticore-projects/suramadu/commit/eb258fcbb51ae36) vikto *2016-08-02 07:36:20*

**server refactoring 14**


[06f6f](https://github.com/manticore-projects/suramadu/commit/06f6f30789d9b84) vikto *2016-07-29 16:16:41*

**server refactoring 13**


[43274](https://github.com/manticore-projects/suramadu/commit/43274a4fdca46c8) vikto *2016-07-29 15:02:04*

**server refactoring 12**


[58ce7](https://github.com/manticore-projects/suramadu/commit/58ce7760dba9577) vikto *2016-07-28 17:17:18*

**server refactoring 11**


[61751](https://github.com/manticore-projects/suramadu/commit/6175167bdd787dd) vikto *2016-07-28 13:31:46*

**server refactoring 10**


[e2f79](https://github.com/manticore-projects/suramadu/commit/e2f79685f7df633) vikto *2016-07-27 14:56:31*

**server refactoring 9**


[2e30b](https://github.com/manticore-projects/suramadu/commit/2e30b2f5b38275a) vikto *2016-07-27 13:57:19*

**server refactoring 8**


[82185](https://github.com/manticore-projects/suramadu/commit/82185683662572f) vikto *2016-07-26 06:54:03*

**server refactoring 7**


[029e5](https://github.com/manticore-projects/suramadu/commit/029e50cd0c8747f) vikto *2016-07-22 15:35:30*

**server refactoring 7**


[83919](https://github.com/manticore-projects/suramadu/commit/839192d84b9cc50) vikto *2016-07-20 15:38:50*

**resolves #174 : upload folder config**


[21cb8](https://github.com/manticore-projects/suramadu/commit/21cb83029e7250e) vikto *2016-07-14 13:05:00*

**server refactoring 6**


[35210](https://github.com/manticore-projects/suramadu/commit/3521046e28cc679) vikto *2016-07-13 17:21:21*

**server refactoring 5**


[8d944](https://github.com/manticore-projects/suramadu/commit/8d94469b9c5bfc1) vikto *2016-07-08 08:54:25*

**server refactoring 4**


[dfc2b](https://github.com/manticore-projects/suramadu/commit/dfc2b3050943946) vikto *2016-07-07 15:36:29*

**server refactoring 3**


[4843a](https://github.com/manticore-projects/suramadu/commit/4843af10cdcc3a6) vikto *2016-07-07 09:58:16*

**server refactoring 2**


[5a3fc](https://github.com/manticore-projects/suramadu/commit/5a3fc08cdeb3181) vikto *2016-07-05 22:27:47*

**server refactoring**


[9b447](https://github.com/manticore-projects/suramadu/commit/9b44709419b569c) vikto *2016-07-04 16:42:05*

**merge PR#16, see PR#17 fix**


[38e77](https://github.com/manticore-projects/suramadu/commit/38e777da8aff74d) V m *2016-06-23 10:29:50*

**resolves #86 : DirectDraw fonts, Merge of PR#15 and PR#16, other fixes**

* DD - Custom Paint classes, DD applet rendering fix, recording playback 
* fix 

[48fee](https://github.com/manticore-projects/suramadu/commit/48fee4fbf8a784b) vikto *2016-06-22 23:26:13*

**merge of PR #17**


[5dcb7](https://github.com/manticore-projects/suramadu/commit/5dcb718a35ee27e) vikto *2016-06-17 23:43:55*

**see #165 : all windows desktop properties, see #146 : repainting fix**


[ca3e4](https://github.com/manticore-projects/suramadu/commit/ca3e4c3410be77f) vikto *2016-06-17 23:25:17*

**see #164 : avoid double download same file, see #98 : pom fix**


[23923](https://github.com/manticore-projects/suramadu/commit/2392327157884e9) vikto *2016-06-17 12:58:38*

**resolves #170 : incorrect KeyStroke generated**


[93a4c](https://github.com/manticore-projects/suramadu/commit/93a4c7b308eb75f) vikto *2016-06-16 15:33:27*

**resolves #159 : fix of ALWAYS_NEW_SESSION mode on unstable connection**


[c33bd](https://github.com/manticore-projects/suramadu/commit/c33bdd094d2f59a) vikto *2016-06-16 11:41:37*

**resolves #98 : load new javascript when Webswing version changes**


[b267b](https://github.com/manticore-projects/suramadu/commit/b267b265e2aa61f) vikto *2016-06-16 08:35:20*

**resolves #169 : graceful swing shutdown when session times out**


[b3da2](https://github.com/manticore-projects/suramadu/commit/b3da2ff77d460eb) vikto *2016-06-15 14:13:36*

**resolves #156 : configure upload file size limit per application**


[f735a](https://github.com/manticore-projects/suramadu/commit/f735a8b9e3633d1) vikto *2016-06-13 13:47:30*

**resolves #159 : session resuming mode configuration + session stealing**


[b9c0c](https://github.com/manticore-projects/suramadu/commit/b9c0cf95322a9bd) vikto *2016-06-10 10:42:32*

**resolves #162 : added default Windows font desktop properties**


[70e0e](https://github.com/manticore-projects/suramadu/commit/70e0e7b20507ad3) vikto *2016-06-09 11:36:55*

**see #115 : focus fix for applets**


[66734](https://github.com/manticore-projects/suramadu/commit/66734347ed3de89) vikto *2016-06-09 11:12:41*

**resolves #161 : fix of char resolution on non-EN keyboards**


[ce336](https://github.com/manticore-projects/suramadu/commit/ce3365a7da7dadf) vikto *2016-06-09 11:09:44*

**see #165 : system colors fix**


[c3456](https://github.com/manticore-projects/suramadu/commit/c345640ad23ea89) vikto *2016-06-09 08:23:08*

**resolves #167 : DnD freeze when modal dialog opened in drop event**


[b8004](https://github.com/manticore-projects/suramadu/commit/b8004ccbb569279) vikto *2016-06-09 08:20:28*

**resolves #166 : clipboard behavior improvement ()**


[6d453](https://github.com/manticore-projects/suramadu/commit/6d4535119976682) vikto *2016-06-08 14:43:17*

**resolves #165 : missing system color definitions on windows**


[c60a0](https://github.com/manticore-projects/suramadu/commit/c60a0da28ea6689) vikto *2016-06-08 14:38:15*

**resolves #164 : automatic file download when save JFileChooser dialog is**

* used 

[3ff8b](https://github.com/manticore-projects/suramadu/commit/3ff8b686e9d3b00) vikto *2016-06-08 14:31:41*

**see #86 : DirectDraw server rendered fonts using GlyphList**


[c4397](https://github.com/manticore-projects/suramadu/commit/c43978a90e65d28) viktor *2016-05-06 00:40:53*

**empty icon field fix**


[8fe3e](https://github.com/manticore-projects/suramadu/commit/8fe3eec2aa2e876) viktor *2016-05-02 11:52:21*

**resolves #138 : html formated jlabel in DD fix**


[78c5f](https://github.com/manticore-projects/suramadu/commit/78c5f55bdb70da2) viktor *2016-04-21 14:51:46*

**SwingProcess log handling improvement**


[39ade](https://github.com/manticore-projects/suramadu/commit/39adee0dc91e245) viktor *2016-04-20 14:00:04*

**resolves #149: eclipse branding, fix of applet displaying issue**


[4e81e](https://github.com/manticore-projects/suramadu/commit/4e81e2f8d938601) viktor *2016-04-20 12:41:46*

**resolves #144 : printing improvements**


[088e9](https://github.com/manticore-projects/suramadu/commit/088e9fc05fafd02) viktor *2016-04-19 16:56:39*

**resolves #151 : initial focus fix**


[fbe6b](https://github.com/manticore-projects/suramadu/commit/fbe6b16324c7d95) viktor *2016-04-14 16:48:44*

**resolves #146 : medium weight popup rendering with directdraw**


[713c0](https://github.com/manticore-projects/suramadu/commit/713c085c622c01f) viktor *2016-04-13 15:26:43*

**Revert start.bat to webswing.bat.**


[d779a](https://github.com/manticore-projects/suramadu/commit/d779a2e3a128006) Todd Shoemaker *2016-04-07 13:17:49*

**resolves #148 : admin console on tomcat fix**


[3d929](https://github.com/manticore-projects/suramadu/commit/3d9290e75e14059) viktor *2016-04-07 11:03:55*

**resolves #146 : medium weight popup rendering**


[4edd2](https://github.com/manticore-projects/suramadu/commit/4edd28731906c89) viktor *2016-04-06 23:58:49*

**Restored webswing.bat/webswing.sh.**


[6b900](https://github.com/manticore-projects/suramadu/commit/6b9003f04ff375a) Todd Shoemaker *2016-04-06 20:40:42*

**install.md edited online with Bitbucket**

* Corrected spelling, changed webstart.sh with start.sh. 

[55b44](https://github.com/manticore-projects/suramadu/commit/55b441634877ca7) Todd Shoemaker *2016-04-05 01:26:40*

**mkdocs.yml edited online with Bitbucket**

* Corrected spelling of Installation and Embedded. 

[cb036](https://github.com/manticore-projects/suramadu/commit/cb036c5177f72fe) Todd Shoemaker *2016-04-05 01:20:33*

**index.md edited online with Bitbucket.  Fixed typos, improved grammar.**


[581a8](https://github.com/manticore-projects/suramadu/commit/581a81c36879efa) Todd Shoemaker *2016-04-05 01:13:41*

**resolves #145 : fix setLocationRelativeTo when window maximized**


[56eae](https://github.com/manticore-projects/suramadu/commit/56eaee408b223d7) viktor *2016-03-22 01:13:36*

**Do not put 'null' to the system properties of the forked JVM process**


[1ac58](https://github.com/manticore-projects/suramadu/commit/1ac5894ca5e1468) Vitaly Litvak *2016-02-22 15:35:39*

**Escape forked application bootclasspath entries with quotes so they can contain spaces**


[79498](https://github.com/manticore-projects/suramadu/commit/794980a44cf62ba) Vitaly Litvak *2016-02-18 17:07:11*

**resolves #140 : resizable fix**


[ecc81](https://github.com/manticore-projects/suramadu/commit/ecc81b51395266e) viktor *2016-02-13 16:56:09*

**resolves #114 #118 : NPE on closing application (Tomcat 8)**


[08ed1](https://github.com/manticore-projects/suramadu/commit/08ed1c71baf1bd2) viktor *2016-02-12 16:16:06*

**resolves #139 : non blocking log processing**


[e1b03](https://github.com/manticore-projects/suramadu/commit/e1b03d1ab65fa4d) viktor *2016-02-12 14:27:48*

**resolves #107: long-polling auto fall back fix**


[ec5e4](https://github.com/manticore-projects/suramadu/commit/ec5e4220f9a1e70) viktor *2016-02-04 00:43:44*

**resolves #106 : mirror view initial redraw fix**


[8831b](https://github.com/manticore-projects/suramadu/commit/8831b9c15bc7a22) viktor *2016-01-31 03:47:48*

**resolves #125 : classes directory loadin fix**


[4882c](https://github.com/manticore-projects/suramadu/commit/4882c60f5bd130f) viktor *2016-01-29 01:17:23*

**resolves #112 : java6 startup issue**


[2510f](https://github.com/manticore-projects/suramadu/commit/2510f4d1c248863) viktor *2016-01-29 00:47:10*

**resolves #136 : jre setting per swing application**


[66b93](https://github.com/manticore-projects/suramadu/commit/66b9307db7db62b) viktor *2016-01-27 02:31:48*

**see #110 : custom args documentation**


[e90a8](https://github.com/manticore-projects/suramadu/commit/e90a80ec8779af1) viktor *2016-01-21 15:28:39*

**resolves #105 : DefaultWindowDecoratorTheme NPE in openjdk**


[231fa](https://github.com/manticore-projects/suramadu/commit/231fa62dcca331a) viktor *2016-01-21 01:08:23*

**resolves #131 #132 : prevent session expiration while connected**


[19a90](https://github.com/manticore-projects/suramadu/commit/19a9036b4a5122a) viktor *2016-01-20 23:35:08*

**resolves #122 #130 : fix of isolatedFs on linux java8**


[2bbaa](https://github.com/manticore-projects/suramadu/commit/2bbaa215416dd7e) viktor *2016-01-20 15:37:04*

**resolves #104 #102 : improved shell start script**


[ad897](https://github.com/manticore-projects/suramadu/commit/ad897f013ee7a01) viktor *2016-01-19 16:37:25*

**resolves #29  #117  : awt filechooser support fixed**


[e1ef9](https://github.com/manticore-projects/suramadu/commit/e1ef9625a550d9b) Viktor Meszaros *2015-12-13 01:24:54*

**resolves #115 : focus issue fix and cleanup**


[82aff](https://github.com/manticore-projects/suramadu/commit/82affa7a7f2207b) Viktor Meszaros *2015-12-11 01:59:07*

**Docs Theme**


[f3c81](https://github.com/manticore-projects/suramadu/commit/f3c815e9946e982) Viktor Meszaros *2015-11-30 23:32:55*

**clean**


[2512f](https://github.com/manticore-projects/suramadu/commit/2512f8bd80be9a2) Rich *2015-11-30 23:30:50*

**clean up**


[1867a](https://github.com/manticore-projects/suramadu/commit/1867aa037ea1037) Rich *2015-11-30 23:28:09*

**fixed navbar**


[fb167](https://github.com/manticore-projects/suramadu/commit/fb167cb40c9b68b) Rich *2015-11-30 23:13:18*

**resolves #111 : temporary window focus handling. ( also see #94 )**


[98aa4](https://github.com/manticore-projects/suramadu/commit/98aa4e16aaad49c) Viktor Meszaros *2015-11-28 01:22:46*

**resolves #103 : incorrect classpath resolution in tomcat**


[f8ede](https://github.com/manticore-projects/suramadu/commit/f8edea1d9c1d866) Viktor Meszaros *2015-11-17 20:57:01*

**tasks about navbar, sidebar ...**


[39dd6](https://github.com/manticore-projects/suramadu/commit/39dd605dfcf5c65) Rich *2015-11-17 00:48:55*

**New custom bootstrap, Footer removed**


[9bb77](https://github.com/manticore-projects/suramadu/commit/9bb77e989a50af0) Rich *2015-11-08 19:53:19*

**resolves issue #80 : activate successor window when window closed**


[1b465](https://github.com/manticore-projects/suramadu/commit/1b465a0c335a672) Viktor Meszaros *2015-11-08 13:48:36*

**added custom theme**


[9050f](https://github.com/manticore-projects/suramadu/commit/9050f898094af37) Rich *2015-11-08 01:15:49*

**see issue #80 : modal dialog focus handling improvements**


[4fde5](https://github.com/manticore-projects/suramadu/commit/4fde57aa43c4a24) Viktor Meszaros *2015-11-03 01:28:19*

**resolves   #100 : PrintServiceLookup.lookupPrintServices call fix**


[4c02a](https://github.com/manticore-projects/suramadu/commit/4c02a7f81125771) Viktor Meszaros *2015-10-27 23:41:55*

**resolves #99 :  "swingSessionTimeout": 0 on relaod fix**


[0d973](https://github.com/manticore-projects/suramadu/commit/0d9738790555e94) Viktor Meszaros *2015-10-27 23:35:16*

**resolves  #91 : limit thread pool size**


[e9271](https://github.com/manticore-projects/suramadu/commit/e9271309991c794) Viktor Meszaros *2015-10-27 03:19:19*

**resolves  #80 : focus bug**


[57fa3](https://github.com/manticore-projects/suramadu/commit/57fa3a7f6d8cba9) Viktor Meszaros *2015-10-26 23:57:22*

**resolves #97 : NPE in theme impl**


[979e7](https://github.com/manticore-projects/suramadu/commit/979e71c5b8c937e) Viktor Meszaros *2015-10-14 21:58:49*

**resolves #93 : class loading of remote EJB stub**


[97ec6](https://github.com/manticore-projects/suramadu/commit/97ec6bdf66ad26c) Viktor Meszaros *2015-10-14 21:06:39*

**resolves  #96: classloader class cast exception**


[55e14](https://github.com/manticore-projects/suramadu/commit/55e145f6879ec54) Viktor Meszaros *2015-10-14 20:15:01*

**removed imagePoolCache from other places**


[e7d06](https://github.com/manticore-projects/suramadu/commit/e7d060df2765cad) Andrey Breskalenko *2015-10-05 17:47:31*

**added a test for line bias**


[50137](https://github.com/manticore-projects/suramadu/commit/50137ea74c725d5) Andrey Breskalenko *2015-10-02 12:48:03*

**fixed incorrect png encoding for some cases, added test**


[3c877](https://github.com/manticore-projects/suramadu/commit/3c877814ee05288) Andrey Breskalenko *2015-10-01 13:51:39*

**fixed hash computation methods**


[ff3a3](https://github.com/manticore-projects/suramadu/commit/ff3a365938b9c76) Andrey Breskalenko *2015-10-01 12:26:03*

**fixed background filling in images/webimages, added test cases**


[a6829](https://github.com/manticore-projects/suramadu/commit/a682937c2fcdf1b) Andrey Breskalenko *2015-10-01 09:50:55*

**directdraw test classpath fix**


[d9e0b](https://github.com/manticore-projects/suramadu/commit/d9e0b4be469e06d) Viktor Meszaros *2015-09-30 21:58:54*

**totally removed chunks as COPY_AREA operation now works correctly even without them**


[bd87c](https://github.com/manticore-projects/suramadu/commit/bd87c2c6da88c5f) Andrey Breskalenko *2015-09-30 14:22:04*

**fixed tests layout to include all tests, cleaned code**


[138a1](https://github.com/manticore-projects/suramadu/commit/138a1f661e0d864) Andrey Breskalenko *2015-09-30 13:17:36*

**changed images drawing approach, removed image holder; fixes tests 24 & 25**


[df9e6](https://github.com/manticore-projects/suramadu/commit/df9e69e9739d95a) Andrey Breskalenko *2015-09-30 12:24:10*

**added two tests with incorrect image rendering**


[369be](https://github.com/manticore-projects/suramadu/commit/369be014f8a8fa2) Andrey Breskalenko *2015-09-29 12:07:38*

**tried to fix composition, yet still haven't matched java compositions, see added tests for image compositions**


[be971](https://github.com/manticore-projects/suramadu/commit/be9717667b7e524) Andrey Breskalenko *2015-09-29 10:53:39*

**resolves #56 : fix of library name**


[ae025](https://github.com/manticore-projects/suramadu/commit/ae025d7a056cf0b) Viktor Meszaros *2015-09-28 00:52:51*

**docs version**


[99001](https://github.com/manticore-projects/suramadu/commit/990010318dffdca) Viktor Meszaros *2015-09-27 22:31:38*

**release plugin configuration**


[5dde4](https://github.com/manticore-projects/suramadu/commit/5dde438795321e1) Viktor Meszaros *2015-09-27 19:36:35*

**added @Override annotations instead of simple comments, removed throw declarations as no exception is currently thrown**


[b3970](https://github.com/manticore-projects/suramadu/commit/b397059a9342dc7) Andrey Breskalenko *2015-09-25 15:52:00*

**changed spaces to tabs in webswing-dd**


[58ade](https://github.com/manticore-projects/suramadu/commit/58ade72549a9a14) Andrey Breskalenko *2015-09-25 15:25:43*

**fixed tests**


[9df72](https://github.com/manticore-projects/suramadu/commit/9df72e051774283) Andrey Breskalenko *2015-09-25 15:21:36*

**removed image context where it wasn't really needed, moved font transform to image context**


[e7d34](https://github.com/manticore-projects/suramadu/commit/e7d349de7c6ef7f) Andrey Breskalenko *2015-09-25 15:07:53*

**deleted copyrights**


[7619e](https://github.com/manticore-projects/suramadu/commit/7619e18a4fbf573) Andrey Breskalenko *2015-09-25 12:25:19*

**fixed style & formatting**


[708ce](https://github.com/manticore-projects/suramadu/commit/708cee70b3a5bb0) Andrey Breskalenko *2015-09-25 12:07:38*

**merged remote-tracking branch 'upstream/master'**

* Conflicts: 
* webswing-app-launcher/src/main/java/org/webswing/SwingMain.java 

[96fa7](https://github.com/manticore-projects/suramadu/commit/96fa78dda6c5010) Andrey Breskalenko *2015-09-24 14:20:40*

**resolves #88 : fallback classloader for ignored_package classes outside bootstrap classloader (merged from Andrey Breskalenko)**


[e5d85](https://github.com/manticore-projects/suramadu/commit/e5d85cc60dcaae5) Viktor Meszaros *2015-09-22 22:08:25*

**resolves #87 hammer.js - r.js compatibility fix**


[2166e](https://github.com/manticore-projects/suramadu/commit/2166e1135f4a586) Viktor Meszaros *2015-09-22 21:32:29*

**made draw instructions readonly, instructions are now copied to ROWI if reset is false to avoid concurrent modification**


[c5595](https://github.com/manticore-projects/suramadu/commit/c5595ab1001bccb) Andrey Breskalenko *2015-09-22 13:02:27*

**resolves issue #9 : mobile browser optimizations**


[e8044](https://github.com/manticore-projects/suramadu/commit/e8044d126bad31c) Viktor Meszaros *2015-09-22 01:44:03*

**dispose command is now added through addInstruction method, changed synchronized methods to synchronized blocks for uniformity**


[f475d](https://github.com/manticore-projects/suramadu/commit/f475d6f2167b1e9) Andrey Breskalenko *2015-09-21 16:12:25*

**returned approach with value serialization for mutable values, defined two draw constant holders: for mutable and immutable values**


[f19f9](https://github.com/manticore-projects/suramadu/commit/f19f99e71bbeb90) Andrey Breskalenko *2015-09-21 15:20:36*

**returned deleted variable to avoid merge conflict**


[0746f](https://github.com/manticore-projects/suramadu/commit/0746f7ade796c06) Andrey Breskalenko *2015-09-18 13:49:08*

**added workaround for zero-vector gradient paints**


[cf68b](https://github.com/manticore-projects/suramadu/commit/cf68be73994bd9b) Andrey Breskalenko *2015-09-17 14:40:06*

**fixed ConcurrentModificationException that leaded to paint freeze, created method to calculate bias for uniformity & easier fix, if needed**


[69340](https://github.com/manticore-projects/suramadu/commit/69340fa42250e94) Andrey Breskalenko *2015-09-14 14:10:16*

**see issue #43 : maximize fix**


[08d2b](https://github.com/manticore-projects/suramadu/commit/08d2beaad465a62) Viktor Meszaros *2015-09-14 01:31:33*

**resolves issue #43 : auto-resizing maximized state**


[feb39](https://github.com/manticore-projects/suramadu/commit/feb3983dd390f25) Viktor Meszaros *2015-09-14 00:17:53*

**resolves issue #81 : tomcat 7 login fix**


[e865d](https://github.com/manticore-projects/suramadu/commit/e865dae36d6b3c6) Viktor Meszaros *2015-09-13 22:06:23*

**resolves issue #83 : focus lost fix**


[7b767](https://github.com/manticore-projects/suramadu/commit/7b767b79607ca85) Viktor Meszaros *2015-09-13 21:54:28*

**resolves  issue #85 , added browser locale, user ip and customArgs to config variables**


[3ac3c](https://github.com/manticore-projects/suramadu/commit/3ac3c16708b07f7) Viktor Meszaros *2015-09-13 15:01:13*

**release plugin**


[5e66d](https://github.com/manticore-projects/suramadu/commit/5e66d1cd9f98d82) Viktor Meszaros *2015-09-13 11:57:01*

**fixed problems with class loading; code cleaning**


[96391](https://github.com/manticore-projects/suramadu/commit/9639175d8401994) Andrey Breskalenko *2015-09-11 15:45:51*

**returned dispose operation, removed unused variable**


[bd408](https://github.com/manticore-projects/suramadu/commit/bd4083c6c3e438b) Andrey Breskalenko *2015-09-09 17:33:51*

**docs**


[844d7](https://github.com/manticore-projects/suramadu/commit/844d79de93ff8df) Viktor Meszaros *2015-09-01 23:24:14*

**docs**


[909b3](https://github.com/manticore-projects/suramadu/commit/909b3f4cdca415e) Viktor Meszaros *2015-09-01 01:32:27*

**implemented SET_FONT**


[e400f](https://github.com/manticore-projects/suramadu/commit/e400fb3a25c7848) Andrey Breskalenko *2015-08-31 18:00:54*

**made message calculation on demand, implemented equals and hash code methods based on content to avoid collisions when checking equality by hash code**


[f11c1](https://github.com/manticore-projects/suramadu/commit/f11c17aed3d675b) Andrey Breskalenko *2015-08-31 14:52:36*

**removed unused weight attribute from font proto**


[5b669](https://github.com/manticore-projects/suramadu/commit/5b6690799e45304) Andrey Breskalenko *2015-08-28 18:03:19*

**fixed & remade gradients import**


[e7863](https://github.com/manticore-projects/suramadu/commit/e7863fc502f122f) Andrey Breskalenko *2015-08-27 16:48:15*

**docs**


[71842](https://github.com/manticore-projects/suramadu/commit/7184265d7b83e01) Viktor Meszaros *2015-08-26 00:33:31*

**rendering fix 2**


[bf22e](https://github.com/manticore-projects/suramadu/commit/bf22e6ae61c24cd) Viktor Meszaros *2015-08-24 23:10:56*

**session recording and playback feature, rendering fix, small bug fixes and improvements**


[239ea](https://github.com/manticore-projects/suramadu/commit/239eaa3d63b3c52) Viktor Meszaros *2015-08-24 00:39:15*

**see issue #72 : additional clipboard tweaks**


[43381](https://github.com/manticore-projects/suramadu/commit/433810467869ad2) Viktor Meszaros *2015-08-18 19:26:53*

**resolves issue #72 : enhanced clipboard**


[64b6f](https://github.com/manticore-projects/suramadu/commit/64b6f136900cd38) Viktor Meszaros *2015-08-18 02:45:31*

**resolves issue #47 : landscape printing;  fix of DnD for java7 and java8; included extended Swingset3 demo**


[ad495](https://github.com/manticore-projects/suramadu/commit/ad495b363c1810f) Viktor Meszaros *2015-08-14 01:44:05*

**added null check for transform, removed check from graphics create, identity matrix is not ignored now**


[d359e](https://github.com/manticore-projects/suramadu/commit/d359e6e70dd09c4) Andrey Breskalenko *2015-08-11 17:09:41*

**changed bias correction and ellipse drawing to match java operations, removed bias from the server side, path is started only when appropriate drawing primitive is found**


[df68b](https://github.com/manticore-projects/suramadu/commit/df68b5c3e21f0d0) Andrey Breskalenko *2015-08-11 17:09:40*

**changed address to id, changed/fixed id generation and pooling, removed hash in DrawConstant subclasses**


[29153](https://github.com/manticore-projects/suramadu/commit/2915389279dc5f6) Andrey Breskalenko *2015-08-11 17:09:40*

**added texture proto for direct texture paint support**


[0bae0](https://github.com/manticore-projects/suramadu/commit/0bae04f982289d8) Andrey Breskalenko *2015-08-11 11:57:52*

**changed types in transform and stroke protos, added default values**


[dd448](https://github.com/manticore-projects/suramadu/commit/dd4483a658746b0) Andrey Breskalenko *2015-08-11 11:44:22*

**fixed glitch with globalalpha, fixed bug with transform, fixed possible bug in optimize instructions, changed antialias behaviour**


[93e64](https://github.com/manticore-projects/suramadu/commit/93e645bd67e3276) Andrey Breskalenko *2015-08-11 11:32:03*

**added idea files to .gitignore, code cleaning & refactoring**


[e059f](https://github.com/manticore-projects/suramadu/commit/e059ffc61585c2b) Andrey Breskalenko *2015-08-11 10:47:26*

**resolves issue #66 : latency improvement**


[c4e1e](https://github.com/manticore-projects/suramadu/commit/c4e1e7045685f3b) Viktor Meszaros *2015-08-06 23:39:26*

**Direct draw fix -npe if drawing null image**


[2d767](https://github.com/manticore-projects/suramadu/commit/2d767f10eb693e2) Viktor Meszaros *2015-08-06 22:44:33*

**resolves issue #79 : Better error message if building on unsupported platform (ie 32bit linux)**


[66d15](https://github.com/manticore-projects/suramadu/commit/66d159d3ba3fcf3) Viktor Meszaros *2015-08-06 22:30:54*

**applet focus and auto-resize fix**


[4c9d1](https://github.com/manticore-projects/suramadu/commit/4c9d130a4d9824e) Viktor Meszaros *2015-08-05 20:12:14*

**see issue #72 improved copy-paste**


[565ac](https://github.com/manticore-projects/suramadu/commit/565ac92f95d0f2f) Viktor Meszaros *2015-08-05 00:59:24*

**refactored webswing js; resolve #77 -file dialog disapearing, removed nodejs dep from pom.xml for compatibility issues**


[c4f3b](https://github.com/manticore-projects/suramadu/commit/c4f3bcc3cf88324) Viktor Meszaros *2015-08-04 22:10:47*

**see #71 : run script with Xvfb instead of X server**


[9e110](https://github.com/manticore-projects/suramadu/commit/9e11026493bb604) Viktor Meszaros *2015-07-30 01:11:03*

**resolves issue #76 : system tray exception fix**


[662ab](https://github.com/manticore-projects/suramadu/commit/662abf1875b73b6) Viktor Meszaros *2015-07-27 22:17:58*

**removed jmx connection , bug fixes and cleanup**


[2a418](https://github.com/manticore-projects/suramadu/commit/2a4182f214ef923) Viktor Meszaros *2015-07-26 23:49:18*

**automatic config file reloading if changed**


[b7cb4](https://github.com/manticore-projects/suramadu/commit/b7cb4facd6fc806) Viktor Meszaros *2015-07-23 22:51:35*

**resolves #74 : enforce jdk 1.8 for mvn build; version added to manifest, page and js file**


[53f20](https://github.com/manticore-projects/suramadu/commit/53f20f7ddf31346) Viktor Meszaros *2015-07-23 00:41:32*

**resolves #71 : better linux script ( start|stop|restart|status)**


[ff58a](https://github.com/manticore-projects/suramadu/commit/ff58aaa9ef3d575) Viktor Meszaros *2015-07-22 00:59:51*

**admin console refactoring 2**


[9af6c](https://github.com/manticore-projects/suramadu/commit/9af6c68468cba1d) Viktor Meszaros *2015-07-20 00:04:16*

**admin console refactoring**


[22e25](https://github.com/manticore-projects/suramadu/commit/22e25a823074532) Viktor Meszaros *2015-07-16 00:06:33*

**cleanup**


[47edf](https://github.com/manticore-projects/suramadu/commit/47edf0595172b1b) Viktor Meszaros *2015-07-02 21:42:23*

**resolving issue  #72 : enhanced clipboard, copy of html content**


[aaf2f](https://github.com/manticore-projects/suramadu/commit/aaf2f3eae6e14d8) Viktor Meszaros *2015-07-01 00:35:19*

**resolves issue #70 : window themes - added option to websiwng.config, added icon to header, fixed action resolution, possible to setup external theme with failover to default theme**


[932a3](https://github.com/manticore-projects/suramadu/commit/932a3f8252e40ae) Viktor Meszaros *2015-06-29 00:19:26*

**resolves issue #64 : rendering in IE9 and long-polling fallback for other browsers**


[4263b](https://github.com/manticore-projects/suramadu/commit/4263b10a0900b28) Viktor Meszaros *2015-06-28 20:02:23*

**small syntax fixes, especially keep the Java pid (but not the parent shell's)**

* Remove BasicTransferable due to copyright concerns, it is not mandatory in any way 

[4920e](https://github.com/manticore-projects/suramadu/commit/4920e6cb1c6e2ee) Andreas Reichel *2015-06-18 04:29:19*

**see issue #21 : fix for netbeans, configuration example for netbeans IDE**


[f89a5](https://github.com/manticore-projects/suramadu/commit/f89a5ec9d580080) Viktor Meszaros *2015-06-16 00:13:31*

**resolves issue #73 : firefox key events for =+:;**


[143dd](https://github.com/manticore-projects/suramadu/commit/143dda732c2fcb4) Viktor Meszaros *2015-06-15 22:42:21*

**resolving issue #22 : demo applet, bug fixes**


[ffd0f](https://github.com/manticore-projects/suramadu/commit/ffd0fc8a02df359) Viktor Meszaros *2015-06-15 22:37:13*

**issue #22 : applet params in html, applet object exposed for js, allowJsLink config option, bug fixes**


[c0c0f](https://github.com/manticore-projects/suramadu/commit/c0c0f269b5a7623) Viktor Meszaros *2015-06-14 23:02:25*

**issue #22 : js2java, java2js interaction for applets**


[192bf](https://github.com/manticore-projects/suramadu/commit/192bf7ca800b02c) Viktor Meszaros *2015-06-14 02:34:48*

**small syntax fixes, especially keep the Java pid (but not the parent shell's)**


[be55a](https://github.com/manticore-projects/suramadu/commit/be55acbbeab718f) Andreas Reichel *2015-06-09 04:44:04*

**Support basic HTML clipboard, which will download/open the HTML content from the server**


[4adb4](https://github.com/manticore-projects/suramadu/commit/4adb496b88c2427) Andreas Reichel *2015-06-07 06:35:22*

**add an improved server control script**


[17599](https://github.com/manticore-projects/suramadu/commit/17599706a460735) Andreas Reichel *2015-06-02 05:56:28*

**Implement XFWM4 theme decoration**


[c5103](https://github.com/manticore-projects/suramadu/commit/c5103cba3d67249) Andreas Reichel *2015-06-02 05:46:15*

**Add a few XFWM4 themes**


[6d156](https://github.com/manticore-projects/suramadu/commit/6d1568321a3c148) Andreas Reichel *2015-06-02 02:47:50*

**issue #22 : basic applets support**


[c2a1f](https://github.com/manticore-projects/suramadu/commit/c2a1fd5b617a0a9) Viktor Meszaros *2015-05-26 23:24:39*

**Reduced memory overhead, mostly for FontConsts.**


[86205](https://github.com/manticore-projects/suramadu/commit/862059dd5b5185d) Andrey Breskalenko *2015-05-26 16:33:17*

**fix of unsupported browser check**


[cfaf5](https://github.com/manticore-projects/suramadu/commit/cfaf5b327858882) Viktor Meszaros *2015-05-20 23:35:27*

**resolves #68 : ${variable} replacement support in webswing.config file 2**


[d4942](https://github.com/manticore-projects/suramadu/commit/d49423d674c6749) Viktor Meszaros *2015-05-20 22:09:41*

**resolves #68 : ${variable} replacement support in webswing.config file**


[27270](https://github.com/manticore-projects/suramadu/commit/272702717df61f4) Viktor Meszaros *2015-05-20 21:15:02*

**resolves Issue #65 : paste (ctrl+v) operation adds v chars 3 -ie hack**


[31fd2](https://github.com/manticore-projects/suramadu/commit/31fd281f25c7ea4) Viktor Meszaros *2015-05-17 18:10:10*

**new build profile 'dev' for faster development, support for exploded deployment, logs cleanup, debug port selection url param**


[29062](https://github.com/manticore-projects/suramadu/commit/290620a665603d4) Viktor Meszaros *2015-05-17 16:59:14*

**resolves Issue #65 : paste (ctrl+v) operation adds v chars 2**


[e4da1](https://github.com/manticore-projects/suramadu/commit/e4da10c0ae66783) Viktor Meszaros *2015-05-14 20:47:29*

**switched to binary socket**


[491b5](https://github.com/manticore-projects/suramadu/commit/491b50601ade28d) Viktor Meszaros *2015-05-14 20:17:40*

**resolves Issue #65 : paste (ctrl+v) operation adds v chars**


[624b1](https://github.com/manticore-projects/suramadu/commit/624b14db9ddae4b) Viktor Meszaros *2015-05-06 22:18:16*

**removing dependency on atmosphere's protocol**


[eb5e1](https://github.com/manticore-projects/suramadu/commit/eb5e1cc54dc8265) Viktor Meszaros *2015-05-06 22:08:20*

**release 2.2.1**


[91d43](https://github.com/manticore-projects/suramadu/commit/91d430416344d2f) Viktor Meszaros *2015-04-30 23:08:59*

**resolving issue #51 : java 8 support (merged from johannesreinhard)**


[aeec2](https://github.com/manticore-projects/suramadu/commit/aeec2e83ee22e07) Viktor Meszaros *2015-04-29 23:45:44*

**resolves issue #60 : JMX connection error logging**


[d1c46](https://github.com/manticore-projects/suramadu/commit/d1c46a80c1606d2) Viktor Meszaros *2015-04-29 00:17:24*

**resolves issue #49 : Mac OS X exception**


[d1165](https://github.com/manticore-projects/suramadu/commit/d11651b5d1a3259) Viktor Meszaros *2015-04-28 23:57:42*

**resolving issue #62 : outline mode for JDesktopPane**


[c8ddc](https://github.com/manticore-projects/suramadu/commit/c8ddce89e3468d6) Viktor Meszaros *2015-04-27 23:27:47*

**resolves issue #63 : printing large documents**


[3b21a](https://github.com/manticore-projects/suramadu/commit/3b21a38c32f6ead) Viktor Meszaros *2015-04-26 08:33:31*

**resolving #58 :  NPE on setTitle; resolving #59 : ?args= not working**


[8fe3a](https://github.com/manticore-projects/suramadu/commit/8fe3a05f14ab8ba) Viktor Meszaros *2015-04-03 15:14:09*

**fixing Issue #56 : Could not initialize class sun.awt.shell.ShellFolder**


[27d46](https://github.com/manticore-projects/suramadu/commit/27d46e166c9c134) Viktor Meszaros *2015-03-30 20:26:43*

**fixing issue #55 : stuck netbeans app**


[03fee](https://github.com/manticore-projects/suramadu/commit/03fee1fe43ad343) Viktor Meszaros *2015-03-27 00:14:02*

**resolving issue #54 : classloading problem**


[b28e5](https://github.com/manticore-projects/suramadu/commit/b28e52e2226a2f6) Viktor Meszaros *2015-03-25 20:52:47*

**2.3-SNAPSHOT development start**


[ce0e5](https://github.com/manticore-projects/suramadu/commit/ce0e56aff7055f5) Viktor Meszaros *2015-03-24 22:17:03*

**Release 2.2**


[e8127](https://github.com/manticore-projects/suramadu/commit/e8127c35054b21e) Viktor Meszaros *2015-03-24 22:11:41*

**mouse event fix**


[0a1b4](https://github.com/manticore-projects/suramadu/commit/0a1b4ed5643be77) Viktor Meszaros *2015-03-24 20:46:29*

**configurable cors, distribution update**


[d88f0](https://github.com/manticore-projects/suramadu/commit/d88f046aa81a85b) Viktor Meszaros *2015-03-24 01:24:26*

**cors upload fix, window resize fix**


[7e63c](https://github.com/manticore-projects/suramadu/commit/7e63c16f7692a7b) Viktor Meszaros *2015-03-23 00:34:38*

**tomcat deployment improvements, js bug fix, application selection in url**


[7ddb6](https://github.com/manticore-projects/suramadu/commit/7ddb686a2309b71) Viktor Meszaros *2015-03-21 00:52:20*

**refactoring js (8)**


[0841d](https://github.com/manticore-projects/suramadu/commit/0841dfab84ed1bc) Viktor Meszaros *2015-03-20 01:39:50*

**refactoring js (7)**


[b5be8](https://github.com/manticore-projects/suramadu/commit/b5be8ebe39a660f) Viktor Meszaros *2015-03-19 02:08:20*

**refactoring js (6)**


[996c0](https://github.com/manticore-projects/suramadu/commit/996c045e0041d71) Viktor Meszaros *2015-03-18 02:36:44*

**refactoring js (5)**


[b36a3](https://github.com/manticore-projects/suramadu/commit/b36a3d5da92e04a) Viktor Meszaros *2015-03-16 01:52:33*

**refactoring js (4)**


[599ca](https://github.com/manticore-projects/suramadu/commit/599ca779257ff07) Viktor Meszaros *2015-03-09 01:04:58*

**refactoring js (3)**


[b3616](https://github.com/manticore-projects/suramadu/commit/b361629ca7c0a85) Viktor Meszaros *2015-03-05 00:39:18*

**refactoring js (2)**


[4f40a](https://github.com/manticore-projects/suramadu/commit/4f40af93c9279ce) Viktor Meszaros *2015-03-04 00:10:17*

**refactoring js (1)**


[c091c](https://github.com/manticore-projects/suramadu/commit/c091c1ac43705b3) Viktor Meszaros *2015-03-03 00:18:52*

**changing format from json to protocol buffers (3)**


[9bb6b](https://github.com/manticore-projects/suramadu/commit/9bb6b7387ac9f9a) Viktor Meszaros *2015-03-01 22:40:39*

**more cleanup and changing format from json to protocol buffers (2)**


[b9473](https://github.com/manticore-projects/suramadu/commit/b9473224365f072) Viktor Meszaros *2015-02-27 00:37:20*

**changing format from json to protocol buffers (1)**


[8b23d](https://github.com/manticore-projects/suramadu/commit/8b23d70217416dc) Viktor Meszaros *2015-02-26 00:04:45*

**refactoring and cleanup of dto model classes**


[946a6](https://github.com/manticore-projects/suramadu/commit/946a65787648ee4) Viktor Meszaros *2015-02-25 22:42:24*

**bug fixes for netbeans platform based apps**


[41d28](https://github.com/manticore-projects/suramadu/commit/41d28ddc2e82cc8) Viktor Meszaros *2015-02-22 03:46:17*

**limiting number of input event messages, sending input events in groups, fix of copy event**


[a5739](https://github.com/manticore-projects/suramadu/commit/a5739c235b9b429) Viktor Meszaros *2015-02-20 22:40:57*

**javascript promise polyfill for admin console (IE support)**


[61b2a](https://github.com/manticore-projects/suramadu/commit/61b2a9cc685e044) Viktor Meszaros *2015-02-20 00:19:46*

**file upload improvements and bug fixes**


[ba2ac](https://github.com/manticore-projects/suramadu/commit/ba2aca127156815) Viktor Meszaros *2015-02-19 23:49:11*

**JFileChooser integration improvement (merged from johannesreinhard)**


[50c0b](https://github.com/manticore-projects/suramadu/commit/50c0bb5fe67cc5f) Viktor Meszaros *2015-02-17 00:48:59*

**jmx bean for webswing status monitoring**


[cd575](https://github.com/manticore-projects/suramadu/commit/cd5758193af33a6) Viktor Meszaros *2015-02-16 23:50:25*

**directdraw image rendering optimization**


[b7c7f](https://github.com/manticore-projects/suramadu/commit/b7c7f06f59a762b) Viktor Meszaros *2015-02-13 00:06:06*

**memory consumption optimization**


[46dd8](https://github.com/manticore-projects/suramadu/commit/46dd80c01e3b046) Viktor Meszaros *2015-02-12 22:34:31*

**directdraw transparent image overlay rendering fix**


[65c27](https://github.com/manticore-projects/suramadu/commit/65c275e49f4811e) Viktor Meszaros *2015-02-12 19:30:01*

**added support for wildcards in swing classpath entries ( * ? )**


[c3079](https://github.com/manticore-projects/suramadu/commit/c30795562bbf5ca) Viktor Meszaros *2015-02-12 00:04:45*

**auto fallback to classic rendering, long-polling fix and resume session fix for ie9**


[725f8](https://github.com/manticore-projects/suramadu/commit/725f82568dbc26c) Viktor Meszaros *2015-02-11 01:05:39*

**resolving issue #52 : IE9 long-polling falback fix  (merged from johannesreinhard)**


[c2315](https://github.com/manticore-projects/suramadu/commit/c231587e44ece34) Viktor Meszaros *2015-02-09 00:32:27*

**thumbnail, redirect after close, hide minimize and maximize button if not resizable  (merged from johannesreinhard)**


[5e0d6](https://github.com/manticore-projects/suramadu/commit/5e0d61df4a9bc6a) Viktor Meszaros *2015-02-08 00:32:18*

**start app from url ... with warning, if an app not exists or null  (merged from johannesreinhard)**


[56ec8](https://github.com/manticore-projects/suramadu/commit/56ec814eb9c22e7) Viktor Meszaros *2015-02-07 23:46:05*

**start app with custom arguments ie. localhost:8080/app/<appname>?args=-a test  (merged from johannesreinhard)**


[e22b0](https://github.com/manticore-projects/suramadu/commit/e22b0402f6e8e8b) Viktor Meszaros *2015-02-07 23:08:16*

**https handling (merged from johannesreinhard)**


[0457a](https://github.com/manticore-projects/suramadu/commit/0457ac82f2ef297) Viktor Meszaros *2015-02-07 22:28:47*

**Tempdir; don't create new directories if CREATE_NEW_TEMP is false (merged from johannesreinhard)**


[dd251](https://github.com/manticore-projects/suramadu/commit/dd2511a0528b05e) Viktor Meszaros *2015-02-07 14:44:55*

**gainedFocusEvent with temporary focus  (merged from johannesreinhard)**


[4647d](https://github.com/manticore-projects/suramadu/commit/4647dc18f2a2564) Viktor Meszaros *2015-02-06 23:42:48*

**directdraw - admin config, mirror view fix**


[cb741](https://github.com/manticore-projects/suramadu/commit/cb741bbd42319cc) Viktor Meszaros *2015-02-06 00:12:58*

**directdraw - async script loading, js compacting, amd support and polyfill**


[58f6d](https://github.com/manticore-projects/suramadu/commit/58f6db8715a4866) Viktor Meszaros *2015-02-05 21:26:56*

**directdraw - bug fixes and optimizations**


[44443](https://github.com/manticore-projects/suramadu/commit/44443747febda90) Viktor Meszaros *2015-01-30 13:55:51*

**directdraw - fix of regular rendering**


[d5468](https://github.com/manticore-projects/suramadu/commit/d54681194159ecc) Viktor Meszaros *2015-01-27 22:50:38*

**directdraw - debug viewer, bug fixes**


[4feb5](https://github.com/manticore-projects/suramadu/commit/4feb51acb85f875) Viktor Meszaros *2015-01-23 23:28:09*

**directdraw - final integration**


[795d9](https://github.com/manticore-projects/suramadu/commit/795d96a97d30195) Viktor Meszaros *2015-01-19 02:04:47*

**directdraw - rendering fix**


[b5072](https://github.com/manticore-projects/suramadu/commit/b5072946d5dbc0d) Viktor Meszaros *2015-01-14 00:18:24*

**directdraw - integration**


[c2cb2](https://github.com/manticore-projects/suramadu/commit/c2cb2c74b030d9d) Viktor Meszaros *2015-01-06 08:50:45*

**directdraw - scrolling fix**


[9eb33](https://github.com/manticore-projects/suramadu/commit/9eb337d9cfef333) Viktor Meszaros *2014-12-30 23:57:49*

**directdraw - optimizations**


[2e635](https://github.com/manticore-projects/suramadu/commit/2e635809bad013b) Viktor Meszaros *2014-12-29 23:54:46*

**directdraw - optimizations**


[3e4e9](https://github.com/manticore-projects/suramadu/commit/3e4e987d2aafcdd) Viktor Meszaros *2014-12-23 01:28:33*

**directdraw - integration**


[e9ac1](https://github.com/manticore-projects/suramadu/commit/e9ac127dbaa5f71) Viktor Meszaros *2014-12-22 23:41:52*

**directdraw - integration**


[27148](https://github.com/manticore-projects/suramadu/commit/27148f77f4198ca) Viktor Meszaros *2014-12-22 01:35:50*

**directdraw - integration**


[0c93e](https://github.com/manticore-projects/suramadu/commit/0c93e4af571276d) Viktor Meszaros *2014-12-20 00:15:24*

**directdraw - integration**


[46272](https://github.com/manticore-projects/suramadu/commit/46272e66fbb7ac6) Viktor Meszaros *2014-12-19 00:48:23*

**directdraw - integration/optimization**


[3f5b9](https://github.com/manticore-projects/suramadu/commit/3f5b95c7c656566) Viktor Meszaros *2014-12-18 00:51:32*

**directdraw - optimization**


[4e73f](https://github.com/manticore-projects/suramadu/commit/4e73f12b4e60946) Viktor Meszaros *2014-12-17 00:25:13*

**directdraw - optimizations**


[5898f](https://github.com/manticore-projects/suramadu/commit/5898f266c73e7da) Viktor Meszaros *2014-12-16 01:01:29*

**directdraw - cleanup and optimizations**


[cb3ab](https://github.com/manticore-projects/suramadu/commit/cb3abbd71861c47) Viktor Meszaros *2014-12-11 01:59:47*

**directdraw - javascript integration**


[9efa9](https://github.com/manticore-projects/suramadu/commit/9efa9fdcf56469e) Viktor Meszaros *2014-12-10 01:19:06*

**directdraw - java integration**


[04a38](https://github.com/manticore-projects/suramadu/commit/04a38fa283e4f80) Viktor Meszaros *2014-12-08 01:24:17*

**directdraw - build integration**


[d02f2](https://github.com/manticore-projects/suramadu/commit/d02f2a5312376b6) Viktor Meszaros *2014-12-06 00:16:09*

**directdraw - import**


[d0d71](https://github.com/manticore-projects/suramadu/commit/d0d711df16299c5) Viktor Meszaros *2014-12-05 01:01:13*

**Resolving #35 : Don't display app selector for one app; Resolving #45 : Possibility to turn off web authentication for swing application; Resolving #46 : select which swing application to start from url (/app/applicationName)**


[77489](https://github.com/manticore-projects/suramadu/commit/77489dcb71fc065) Viktor Meszaros *2014-11-18 22:02:07*

**resolving issue #44 : cannot start applications if a session is forcibly closed**


[ddf4c](https://github.com/manticore-projects/suramadu/commit/ddf4c5d6f66cdfa) unknown *2014-10-08 16:25:31*

**resolves issue #42 : altGr+keys handling**


[980b5](https://github.com/manticore-projects/suramadu/commit/980b5e161d40032) Viktor Meszaros *2014-08-26 15:34:02*

**added support for monitoring of swing instances via JMX, added monitoring of data traffic ( see #23 )**


[d5cf2](https://github.com/manticore-projects/suramadu/commit/d5cf2aecf8275cf) Viktor Meszaros *2014-08-26 12:35:14*

**resolves issue #40 : key event modifier; resolves issue #41 : prevent browser default actions for keyboard shortcuts; version increase to 2.2-SNAPSHOT**


[a9b7c](https://github.com/manticore-projects/suramadu/commit/a9b7ce156baa429) Viktor Meszaros *2014-08-24 18:20:33*

**Release 2.1**


[8a76b](https://github.com/manticore-projects/suramadu/commit/8a76b6d9ee282a1) Viktor Meszaros *2014-08-19 21:07:55*

**event handling and window resizing fixes**


[9b0a5](https://github.com/manticore-projects/suramadu/commit/9b0a574a16f3172) Viktor Meszaros *2014-08-18 16:41:32*

**new window manager for correct handling of window's z-order**


[f1706](https://github.com/manticore-projects/suramadu/commit/f17060e35c5fc6e) Viktor Meszaros *2014-08-18 00:43:43*

**removed duplicated code, fix for undecorated dialog, fix for programmatic window repositioning**


[25c17](https://github.com/manticore-projects/suramadu/commit/25c175080a5c998) Viktor Meszaros *2014-08-12 22:39:31*

**resolving issue #38 : admin console classpath editing bugfix**


[9ce32](https://github.com/manticore-projects/suramadu/commit/9ce3268aa8b9224) Viktor Meszaros *2014-08-08 12:24:29*

**resolving issue #34 : numpad key events support**


[d6573](https://github.com/manticore-projects/suramadu/commit/d6573f68f609f17) Viktor Meszaros *2014-08-07 16:03:41*

**resolving issue #37 : resizing dockable frames**


[c3563](https://github.com/manticore-projects/suramadu/commit/c3563c9115aa0ab) Viktor Meszaros *2014-08-07 15:58:51*

**resolving issue #32 : öüä+# chars typing in FF**


[fa9e4](https://github.com/manticore-projects/suramadu/commit/fa9e4f48cb131bd) Viktor Meszaros *2014-07-28 23:25:27*

**resolving issue #31 : don't load org.w3c.dom  with SwingClassloader**


[10837](https://github.com/manticore-projects/suramadu/commit/108373b4fde13af) Viktor Meszaros *2014-07-27 20:33:27*

**see issue #30 : multiple modal windows fix**


[87213](https://github.com/manticore-projects/suramadu/commit/8721306ff3a451b) Viktor Meszaros *2014-07-24 17:34:39*

**resolving bug #30 : combobox in modal dialog, windows LaF colors changed to win7 default theme colors**


[0e6be](https://github.com/manticore-projects/suramadu/commit/0e6be01a2b0f0ea) Viktor Meszaros *2014-07-22 22:09:04*

**eventQueue thread context classloader fix 2 (see #25)**


[6fa0a](https://github.com/manticore-projects/suramadu/commit/6fa0a60a2056291) Viktor Meszaros *2014-07-16 22:10:29*

**npe fix**


[fbd74](https://github.com/manticore-projects/suramadu/commit/fbd744252e4ac73) Viktor Meszaros *2014-07-15 19:20:20*

**small fixes, linux jvm crash fix**


[b73e6](https://github.com/manticore-projects/suramadu/commit/b73e69207f3f9a7) Viktor Meszaros *2014-07-15 00:46:40*

**classloading fix ( see bug #28 ) isolated fs fixes ( see issue #19 )**


[69d6f](https://github.com/manticore-projects/suramadu/commit/69d6f4a60b964b0) Viktor Meszaros *2014-07-13 23:03:45*

**resolving issue #25 : eventQueue thread context classloader  and resolving issue #26 : not throwing exception when resolving caps lock status**


[79e34](https://github.com/manticore-projects/suramadu/commit/79e34e042e4b049) Viktor Meszaros *2014-07-11 22:45:46*

**resolving #27 : null pointer bug**


[2766e](https://github.com/manticore-projects/suramadu/commit/2766ed013e2e3e6) Viktor Meszaros *2014-07-10 23:03:27*

**resolving #19 : file system isolation for JFileChooser, integration of download/upload/delete functions**


[1c3e9](https://github.com/manticore-projects/suramadu/commit/1c3e9dff30f7bf9) Viktor Meszaros *2014-07-10 22:57:46*

**resolving #24 : bug in build script under linux**


[794cd](https://github.com/manticore-projects/suramadu/commit/794cd8126a559b0) Viktor Meszaros *2014-06-25 19:30:41*

**resolving #15 : Support of java7**


[d00ea](https://github.com/manticore-projects/suramadu/commit/d00ea0d22e5fcc3) Viktor Meszaros *2014-06-20 23:17:29*

**release 2.0**


[2ce9a](https://github.com/manticore-projects/suramadu/commit/2ce9a4e8ba54076) Viktor Meszaros *2014-05-21 22:27:48*

**smaller icons in demo apps**


[e570a](https://github.com/manticore-projects/suramadu/commit/e570a011e5d5d6c) Viktor Meszaros *2014-05-21 17:42:49*

**firefox fixes**


[04de6](https://github.com/manticore-projects/suramadu/commit/04de64f3845380f) Viktor Meszaros *2014-05-20 22:37:08*

**pdf generation fix**


[db9b4](https://github.com/manticore-projects/suramadu/commit/db9b4e705a1726f) Viktor Meszaros *2014-05-12 21:35:20*

**document print fix**


[424fa](https://github.com/manticore-projects/suramadu/commit/424fa67b48dc25e) Viktor Meszaros *2014-05-12 00:43:41*

**configuration fixes**


[8cb55](https://github.com/manticore-projects/suramadu/commit/8cb5573c66f1fb6) Viktor Meszaros *2014-05-08 23:34:15*

**simplified DnD handling (for better performance) and fix of drop event handling**


[8aaaa](https://github.com/manticore-projects/suramadu/commit/8aaaaa140bebe44) Viktor Meszaros *2014-05-08 21:01:55*

**dnd fix (partial)**


[5e45c](https://github.com/manticore-projects/suramadu/commit/5e45cabb926404d) Viktor Meszaros *2014-05-06 21:33:19*

**link to admin console, large configuration file (up to 1mb) saving from console fix, user configuration runtime change fix**


[bb0ca](https://github.com/manticore-projects/suramadu/commit/bb0ca285043cb95) Viktor Meszaros *2014-05-05 21:16:27*

**linux execution fix**


[fa366](https://github.com/manticore-projects/suramadu/commit/fa3662fe4ac4847) Viktor Meszaros *2014-05-05 16:45:45*

**window repositioning  after browser resize**


[b53a0](https://github.com/manticore-projects/suramadu/commit/b53a00a3b20f06c) Viktor Meszaros *2014-04-30 22:52:30*

**DnD fix finished**


[4bd26](https://github.com/manticore-projects/suramadu/commit/4bd2624a1c7f15a) Viktor Meszaros *2014-04-30 20:55:38*

**DnD fix (partial)**


[11cde](https://github.com/manticore-projects/suramadu/commit/11cde575f54af92) Viktor Meszaros *2014-04-30 00:26:27*

**java 6 u45 fix**


[8554a](https://github.com/manticore-projects/suramadu/commit/8554ac6dbdf00c5) Viktor Meszaros *2014-04-28 20:12:38*

**system classloader calls handling, double click fix, Drag and drop fixes**


[ef399](https://github.com/manticore-projects/suramadu/commit/ef3992d20c0d1f0) Viktor Meszaros *2014-04-28 00:09:18*

**fix of windows native LaF**


[ccd4b](https://github.com/manticore-projects/suramadu/commit/ccd4b1674e354ee) Viktor Meszaros *2014-04-25 23:02:01*

**Drag and drop support**


[5163f](https://github.com/manticore-projects/suramadu/commit/5163f2772938d38) Viktor Meszaros *2014-04-24 22:57:32*

**handling JFrame.isUndecorated flag, debug flag per application in admin console, DnD partial(not working yet)**


[ea42b](https://github.com/manticore-projects/suramadu/commit/ea42b26d46f5e44) Viktor Meszaros *2014-04-22 22:50:06*

**+**


[6dc02](https://github.com/manticore-projects/suramadu/commit/6dc02aa46e6a0b0) Viktor Meszaros *2014-04-21 23:26:54*

**admin console finished**


[24d53](https://github.com/manticore-projects/suramadu/commit/24d53bce5026789) Viktor Meszaros *2014-04-21 23:26:25*

**app authorization, admin console (partial 10)**


[27ef3](https://github.com/manticore-projects/suramadu/commit/27ef38076fbaa93) Viktor Meszaros *2014-04-21 02:00:56*

**mirror view finished, admin console (partial 9)**


[3c886](https://github.com/manticore-projects/suramadu/commit/3c886dc57c86270) Viktor Meszaros *2014-04-20 01:02:31*

**admin console (partial 8)**


[3a8b8](https://github.com/manticore-projects/suramadu/commit/3a8b82d90b9318b) Viktor Meszaros *2014-04-18 01:13:21*

**admin console (partial 7)**


[23e51](https://github.com/manticore-projects/suramadu/commit/23e515497263689) Viktor Meszaros *2014-04-16 23:55:50*

**login /admin console (partial 6)**


[f2d75](https://github.com/manticore-projects/suramadu/commit/f2d75f46123d570) Viktor Meszaros *2014-04-16 00:16:30*

**user login / admin console (partial 5)**


[94a66](https://github.com/manticore-projects/suramadu/commit/94a66319c11d80b) Viktor Meszaros *2014-04-14 23:22:25*

**admin console 4 (partial)**


[0903e](https://github.com/manticore-projects/suramadu/commit/0903e146b27cdc9) Viktor Meszaros *2014-04-13 23:14:22*

**admin console configuration 3 (partial)**


[0a898](https://github.com/manticore-projects/suramadu/commit/0a89856620804df) Viktor Meszaros *2014-04-08 22:38:38*

**admin console configuration 2 (partial)**


[42c2f](https://github.com/manticore-projects/suramadu/commit/42c2fecbda9bdaf) Viktor Meszaros *2014-04-07 23:15:20*

**added aaa shiro framework, admin console - configuration (partial)**


[7f993](https://github.com/manticore-projects/suramadu/commit/7f9936343e39518) Viktor Meszaros *2014-04-06 23:21:30*

**logging and exception handling**


[cb13f](https://github.com/manticore-projects/suramadu/commit/cb13f6906f2c025) Viktor Meszaros *2014-03-31 22:14:59*

**fix of anti-aliasing setting**


[2bb85](https://github.com/manticore-projects/suramadu/commit/2bb851c54cbe4c6) Viktor Meszaros *2014-03-30 00:08:12*

**browser window resizing bug fix, window event handling bug fix**


[5998d](https://github.com/manticore-projects/suramadu/commit/5998de1072769c3) Viktor Meszaros *2014-03-28 01:04:39*

**fix of space key handling, scroll glitch fix, fix of corba rmi**


[041c7](https://github.com/manticore-projects/suramadu/commit/041c76b8ed9f5f7) Viktor Meszaros *2014-03-27 00:33:59*

**fix of scrolling glitch, resize cursor fix**


[12087](https://github.com/manticore-projects/suramadu/commit/1208787a64d85c2) Viktor Meszaros *2014-03-26 01:13:20*

**copy/paste support**


[c5ea2](https://github.com/manticore-projects/suramadu/commit/c5ea206145c4027) Viktor Meszaros *2014-03-25 00:24:46*

**cursor update handling**


[7d398](https://github.com/manticore-projects/suramadu/commit/7d398b555871aa9) Viktor Meszaros *2014-03-23 23:20:22*

**printing support, java.awt.Desktop operations open/edit/print support (will download file), added mozzila's pdf.js for printing on client side**


[97fe3](https://github.com/manticore-projects/suramadu/commit/97fe3022c389efb) Viktor Meszaros *2014-03-23 00:36:13*

**finished painting optimizations, window moving optimized**


[e8b2e](https://github.com/manticore-projects/suramadu/commit/e8b2ed75cdfb210) Viktor Meszaros *2014-03-21 00:05:12*

**paint optimization, html with background - (partial)**


[b4671](https://github.com/manticore-projects/suramadu/commit/b467188c9c92ee7) Viktor Meszaros *2014-03-20 00:53:27*

**multiple swing application handling, max connection setting per configured swing application, window handling fixes, app lifecycle fixes**


[178eb](https://github.com/manticore-projects/suramadu/commit/178ebdbabb6e5b6) Viktor Meszaros *2014-03-18 23:56:06*

**enforce application position to visible area, title update handling, enter/tab/delete events fix, minimize/maximize toggle functionality, multiple swing application handling (partly done)**


[d38a7](https://github.com/manticore-projects/suramadu/commit/d38a78a9b5ed169) Viktor Meszaros *2014-03-18 00:40:11*

**multiple clients fix, paint ack processing fix, swing lifecycle handling**


[49d66](https://github.com/manticore-projects/suramadu/commit/49d668ccaf7caf8) Viktor Meszaros *2014-03-16 15:47:55*

**single paint at a time, url links open in new window, assembly with Swingset3**


[44d31](https://github.com/manticore-projects/suramadu/commit/44d31f6bcbfd310) Viktor Meszaros *2014-03-15 01:07:05*

**packaging finished, full window size canvas with resizing,  force window to stay in view**


[861f1](https://github.com/manticore-projects/suramadu/commit/861f174619ba6ac) Viktor Meszaros *2014-03-14 00:01:54*

**packaging and swing execution**


[5b0fb](https://github.com/manticore-projects/suramadu/commit/5b0fb3fe94c7413) Viktor Meszaros *2014-03-13 08:17:45*

**packaging**


[0fe6d](https://github.com/manticore-projects/suramadu/commit/0fe6db0e5f420ff) Viktor Meszaros *2014-03-12 00:51:41*

**switch from netty to jetty, from socketio to atmosphere, java and javascript code adapted to new frameworks**


[5d36d](https://github.com/manticore-projects/suramadu/commit/5d36da52e9feb09) Viktor Meszaros *2014-03-10 00:14:35*

**support of minimize, maximize and resize of windows**


[237ad](https://github.com/manticore-projects/suramadu/commit/237ad84051b6d19) Viktor Meszaros *2014-03-05 22:34:26*

**implemented window events for window move and close window, fix of repainting**


[29f96](https://github.com/manticore-projects/suramadu/commit/29f9646ab023e84) Viktor Meszaros *2014-03-05 00:20:36*

**background repainting, code skeleton for window decoration events (move,resize,close,minimize,maximize)**


[c8849](https://github.com/manticore-projects/suramadu/commit/c88499237f74c73) Viktor Meszaros *2014-03-04 00:23:29*

**window decoration painting, simple default decoration implementated**


[09c8b](https://github.com/manticore-projects/suramadu/commit/09c8b97d42e042a) Viktor Meszaros *2014-03-01 01:39:06*

**fixed focus handling when multiple modal dialog opened**


[3066d](https://github.com/manticore-projects/suramadu/commit/3066d49f890f9a7) Viktor Meszaros *2014-02-26 00:33:12*

**focus window fixes for modal dialog**


[36f41](https://github.com/manticore-projects/suramadu/commit/36f414a6ed04267) Viktor Meszaros *2014-02-25 23:36:45*

**fix of synchronization deadlock, added window and component focus event sending, key events support for debug-server**


[b9d56](https://github.com/manticore-projects/suramadu/commit/b9d56b0141418c2) Viktor Meszaros *2014-02-25 22:18:25*

**repaint after window closed fix**


[9a219](https://github.com/manticore-projects/suramadu/commit/9a2191ae9cdc382) unknown *2013-10-16 22:52:11*

**fix of paint glitches, custom implementation of mouse peer**


[5c8ee](https://github.com/manticore-projects/suramadu/commit/5c8ee50c473b422) unknown *2013-10-16 22:30:14*

**window management, painting fixes**


[864f9](https://github.com/manticore-projects/suramadu/commit/864f9972293db0a) unknown *2013-10-15 09:20:39*

**refactored project structure**


[b9f8c](https://github.com/manticore-projects/suramadu/commit/b9f8c6ab3b9d23d) unknown *2013-10-07 09:19:23*

**new rendering approach via awt.Toolkit implemention**


[72bd6](https://github.com/manticore-projects/suramadu/commit/72bd64f0d4df841) unknown *2013-06-23 09:53:29*

**1.1 release**


[b1af9](https://github.com/manticore-projects/suramadu/commit/b1af9b0cb345d44) unknown *2013-04-08 17:12:46*

**painting improvement, window resize support**


[688de](https://github.com/manticore-projects/suramadu/commit/688de3840e09dda) unknown *2013-04-08 16:50:17*

**scrolling and general painting improved**


[5dd12](https://github.com/manticore-projects/suramadu/commit/5dd12a2d8b84f58) unknown *2013-04-08 10:14:08*

**url link and mailto support**


[d2810](https://github.com/manticore-projects/suramadu/commit/d281012ea3bdf19) unknown *2013-04-06 22:43:08*

**gitignore**


[82621](https://github.com/manticore-projects/suramadu/commit/82621090934e1cf) unknown *2013-04-04 19:00:10*

**contextClassloader fix - ejb calls possible**


[79ce8](https://github.com/manticore-projects/suramadu/commit/79ce85fd3c3b6dc) unknown *2013-04-04 18:52:31*

**new packaging for swing classpath isolation**


[198fc](https://github.com/manticore-projects/suramadu/commit/198fc9a326fd0de) unknown *2013-04-02 15:39:53*

**flashsocket fix, window closing fix,**


[f04f8](https://github.com/manticore-projects/suramadu/commit/f04f8533b7f0b80) unknown *2013-03-11 17:47:40*

**initializing, flashsocket, info log**


[e9201](https://github.com/manticore-projects/suramadu/commit/e92019e365f0b6b) unknown *2013-02-20 17:39:30*

**cleanup**


[9882d](https://github.com/manticore-projects/suramadu/commit/9882d7deb7eb00b) unknown *2013-02-18 19:01:24*

**cleanup**


[1f4e6](https://github.com/manticore-projects/suramadu/commit/1f4e6b797266ceb) unknown *2013-02-18 19:00:46*

**tst**


[7eba8](https://github.com/manticore-projects/suramadu/commit/7eba821ebf0f9b4) unknown *2013-02-18 17:20:19*

**build, logging, analytics**


[79c99](https://github.com/manticore-projects/suramadu/commit/79c991e5b07ecec) unknown *2013-02-18 01:27:36*

**page**


[abc24](https://github.com/manticore-projects/suramadu/commit/abc242198df0337) unknown *2013-02-15 21:04:45*

**page**


[121dd](https://github.com/manticore-projects/suramadu/commit/121dd1c40fb50c6) unknown *2013-02-15 16:07:54*

**release 1.0**


[f62e8](https://github.com/manticore-projects/suramadu/commit/f62e87dd03ff8e1) unknown *2013-02-14 09:59:30*

**added license info, created dist assembly configuration, minor structure changes**


[b3077](https://github.com/manticore-projects/suramadu/commit/b3077681bbd4a97) unknown *2013-02-14 09:40:19*

**building jar + gif for loading application**


[c3429](https://github.com/manticore-projects/suramadu/commit/c3429f6734be317) unknown *2013-02-12 10:31:06*

**package name refactoring**


[b45ad](https://github.com/manticore-projects/suramadu/commit/b45ad7ab24028fa) unknown *2013-02-02 11:51:25*

**max client count argument added**


[03bdf](https://github.com/manticore-projects/suramadu/commit/03bdfa310e59eec) unknown *2013-02-02 11:32:19*

**corrected exiting of unused swing processes, switched to the newest activemq version**


[9f302](https://github.com/manticore-projects/suramadu/commit/9f302d48d03f56a) unknown *2013-02-02 10:51:13*

**added capability to use fast png encoding library**


[4c25b](https://github.com/manticore-projects/suramadu/commit/4c25b659a013d85) unknown *2013-02-01 07:19:45*

**pom**


[0a811](https://github.com/manticore-projects/suramadu/commit/0a811a2c14259e5) unknown *2013-01-31 17:18:34*

**command line configuration**


[2bc90](https://github.com/manticore-projects/suramadu/commit/2bc90007b001bb6) unknown *2013-01-31 17:17:52*

**application shutdown synchronization, flash socket support for ie9**


[c46dd](https://github.com/manticore-projects/suramadu/commit/c46dd3a3174885d) unknown *2013-01-31 12:58:11*

**added JMS and separated swing jvm and server jvm, rendering now only through websocket with base64 encoded images, minor event handling fixes**


[873f7](https://github.com/manticore-projects/suramadu/commit/873f7e677ad85bd) unknown *2013-01-30 15:19:42*

**ie standart mode forced + websocket online indicator**


[e8cbf](https://github.com/manticore-projects/suramadu/commit/e8cbff801e41552) unknown *2013-01-25 10:36:44*

**ie compatibility + application closing + double click fix**


[4a2b2](https://github.com/manticore-projects/suramadu/commit/4a2b285cb9a5c76) unknown *2013-01-22 09:58:42*

**firefox compatibility fix + mouse events improvements**


[46d06](https://github.com/manticore-projects/suramadu/commit/46d067db7f2a56e) unknown *2013-01-21 23:12:44*

**keyboard events + resource serving fix + dialog hiding**


[dba6a](https://github.com/manticore-projects/suramadu/commit/dba6a726bedf740) unknown *2013-01-18 21:32:34*

**scroll glitch fix**


[e3988](https://github.com/manticore-projects/suramadu/commit/e3988f1e6726ffe) unknown *2013-01-02 17:06:15*

**Combobox popup fix, web window auto closing fix**


[48fc6](https://github.com/manticore-projects/suramadu/commit/48fc642958a3fd6) unknown *2013-01-02 15:23:48*

**support for JColorChooser and JFileChooser**


[66d0e](https://github.com/manticore-projects/suramadu/commit/66d0e3a29de197c) unknown *2012-12-21 18:01:11*

**Displaying of JOptionPane helper dialogs**


[6ad4c](https://github.com/manticore-projects/suramadu/commit/6ad4cf02bf25ad5) unknown *2012-12-18 17:04:07*

**closing window + small cleanup and reorganization**


[07d1c](https://github.com/manticore-projects/suramadu/commit/07d1c99a2358406) unknown *2012-12-12 23:48:14*

**performance and stability improvement**


[240ff](https://github.com/manticore-projects/suramadu/commit/240ffff68e148c7) unknown *2012-11-27 17:12:42*

**++**


[285a2](https://github.com/manticore-projects/suramadu/commit/285a2ac9f6db1b6) unknown *2012-11-15 17:01:38*

**refactoring and cleaning, enabled multiple clients**


[88594](https://github.com/manticore-projects/suramadu/commit/8859457c3af8be7) unknown *2012-11-15 17:01:14*

**fix of classloader, improvements in mouse events handling**


[d5549](https://github.com/manticore-projects/suramadu/commit/d55494479fc9c3a) unknown *2012-11-08 16:48:14*

**mouse interaction added + minor fixes**


[9a0e8](https://github.com/manticore-projects/suramadu/commit/9a0e8e6e2e9b7b9) unknown *2012-11-06 11:46:05*

**fix**


[c93de](https://github.com/manticore-projects/suramadu/commit/c93de88b1b42293) unknown *2012-10-27 17:25:00*

**every window has its own web canvas and virtaul window header**


[226ae](https://github.com/manticore-projects/suramadu/commit/226aea913caa169) unknown *2012-10-27 17:16:10*

**cleanup + copy area + double buffer support**


[263ae](https://github.com/manticore-projects/suramadu/commit/263ae6b2aba995f) unknown *2012-10-23 12:41:24*

**First running prototype! juchu:-)**


[50940](https://github.com/manticore-projects/suramadu/commit/50940b1f8a9e461) unknown *2012-10-19 16:02:44*

**Standard Rendering improvement**


[45701](https://github.com/manticore-projects/suramadu/commit/45701d76386a392) unknown *2012-10-18 09:21:23*

**immediate paint**


[9a8c4](https://github.com/manticore-projects/suramadu/commit/9a8c4812d0513c0) unknown *2012-07-10 10:01:38*

**initial**


[7e372](https://github.com/manticore-projects/suramadu/commit/7e372ac466dd309) unknown *2012-07-09 08:13:05*


