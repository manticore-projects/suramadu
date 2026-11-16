# Security Policy

## Supported Versions

| Version | Supported |
| ------- | --------- |
| Suramadu 27.x (latest release) | ✅ |
| Suramadu < 27 / WebSwing-named releases | ❌ — please upgrade |
| WebSwing (Webswing Limited / Webswing s.r.o. products) | ❌ — not our product, report to the vendor |

Suramadu is a fork of WebSwing v20.2.5, the last release published under the GNU AGPL v3.
It is **not** affiliated with, endorsed by, or supported by Webswing Limited or Webswing s.r.o.;
vulnerabilities in their commercial releases must be reported to them, not here. Issues
inherited from the common v20.2.5 codebase are in scope for Suramadu.

## Reporting a Vulnerability

Please report vulnerabilities **privately** — do not open a public issue.

- **Email:** [support@manticore-projects.com](mailto:support@manticore-projects.com)

Include what you can of the following:

- Affected version (release tag or commit) and deployment mode (servlet container, JDK)
- A description of the issue and its impact
- Steps to reproduce or a proof of concept
- Any suggested fix

What to expect:

- **Acknowledgement within 7 days.** Suramadu is maintained by a small team; a holiday
  week may stretch this, but you will hear from us.
- We will assess the report, keep you informed of progress, and credit you in the release
  notes unless you prefer otherwise.
- Confirmed vulnerabilities are fixed in the next release; critical issues trigger an
  out-of-band release. We publish a GitHub Security Advisory once a fix is available.
- Please allow us a reasonable embargo (90 days, or as coordinated) before public disclosure.

## Scope

In scope:

- The Suramadu server (WAR), session pool, and application toolkits
- The JavaScript/TypeScript browser client, including the DirectDraw renderer
- Build and release pipeline issues that affect the shipped artifacts (supply chain)

Out of scope:

- Vulnerabilities only exploitable with administrative access to the host running Suramadu
- Issues in the Swing applications *served by* Suramadu rather than in Suramadu itself
- Denial of service through resource exhaustion that container-level limits are expected
  to handle (report it anyway if the amplification is disproportionate)

## What We Run Continuously

Every push to `master` and a weekly schedule run, with failing gates (a red badge means an
open finding):

- **CodeQL** (Java and JavaScript/TypeScript) — the workflow fails if any alert is open on `master`
- **Semgrep** (`p/java`, `p/owasp-top-ten`, `p/security-audit`, `p/secrets`)
- **OWASP Dependency-Check** over the runtime classpath and npm lockfiles —
  `failBuildOnCVSS = 0`, so any unsuppressed CVE of any severity fails the build.
  False-positive suppressions live in
  [`config/dependency-check/suppressions.xml`](config/dependency-check/suppressions.xml)
  with review notes, auditable in this repository.

Reports and SARIF files are uploaded as workflow artifacts, so every green badge can be
verified against the underlying scan output.
