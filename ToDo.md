# ToDo — potential improvements

Backlog of improvements for the modernized `sancho-p2p` build. Done items live in
[CHANGELOG.md](CHANGELOG.md).

## High value

- [x] ~~Cross-platform **build**~~ — done: OS/arch pom profiles auto-select the SWT
  fragment, JFace decoupled/unsigned, CI builds on Linux/Windows/macOS. See
  CHANGELOG.
- [ ] **Cross-platform release artifacts.** The build is multi-OS, but releases
  still only ship the Windows `sancho.exe`. Produce Linux/macOS artifacts too
  (jpackage app image / `.deb` / `.dmg`, or a portable launcher) from a release
  matrix, so non-Windows users get a download.

- [ ] **Functional end-to-end verification.** So far only *build + launch* are
  verified. Stand up a test MLDonkey core and validate the real flows (connect,
  search, download, sort live tables, IRC, web browser, cell editing, preference
  icons). This is what would confirm the decompiled/ported code behaves correctly.

## Nice to have

- [ ] **Uber-jar (shaded).** `sancho-0.9.4-60.jar` needs its `lib/` next to it.
  Produce a single runnable `sancho-all.jar` (deps are already unsigned, so the
  shade plugin works) and attach it to releases for a one-file `java -jar`.

- [ ] **Drop `gnu.regexp`.** It is used in only a few files (e.g. `SwissArmy`,
  the preference filter). Migrate to `java.util.regex` and remove the ancient,
  unmaintained (~2001) dependency.

- [ ] **MSI installer.** Instead of the portable app image, emit a proper
  `.msi`/`.exe` installer (`jpackage --type msi`, needs WiX in CI) with Start Menu
  shortcuts and file associations registered at install time.

## Housekeeping

- [ ] **Dependabot** for SWT/JFace/PircBot/Trove version bumps.
- [ ] **Audit remaining platform code** (`_wpf`, other legacy branches) and remove
  dead paths, as was done for the `_win32` browser.
- [ ] **Comparator hardening (optional).** The legacy merge sort is requested to
  tolerate table data that mutates mid-sort. A "proper" fix would snapshot the
  sort keys before sorting — larger and riskier; only worth it if the VM-level
  mitigation ever proves insufficient.
- [ ] **The local `sancho-0.9.4-59/` folder** (313 MB, the published binary + a
  bundled JRE used for decompilation) is git-ignored. Remove it locally once no
  longer needed.
