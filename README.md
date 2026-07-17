# Sancho Preserve

This repository is an archival preservation of **Sancho**, a Java/SWT graphical user interface for the MLDonkey peer-to-peer client, originally developed in the mid-2000s.

Sancho provides a desktop interface to monitor and control MLDonkey core, supporting file transfers, search, and network management.

## ⚠️ Project Status

This is **not an active development project**.

It is a preservation / archival repository intended to:
- Preserve the original source code snapshot (circa 2004–2008)
- Provide reproducible builds (AppImage, legacy Linux/Windows/macOS binaries when available)
- Document historical context of early P2P GUI tools

## Original Project

Sancho was originally developed in the early 2000s as a Java/SWT graphical interface for the MLDonkey client.

The original project website is no longer active, but it has been preserved via the Internet Archive:

https://web.archive.org/web/20190304231314/http://sancho.awardspace.com/

This repository is an archival preservation of the original software and is not actively maintained as a modern project.

## 📦 Contents

- `sancho-src/` — Original Java source tree
- `appimage/` — AppDir used to build AppImage package
- `releases/` — Packaged binaries (AppImage, installers, legacy archives)
- `LICENSE` — Combined licensing information

## 🧩 Original Software

Sancho was originally developed by Rutger M. Ovidius (2004–2005) and distributed under the Common Public License (CPL), with dependencies including:

- Eclipse SWT / JFace (CPL)
- GNU Trove (LGPL)
- GNU RegExp (LGPL)
- JSch (BSD-style license)
- PircBot

## 🏷 Versioning

This repository preserves the original versioning scheme used by Sancho.

Example:
- `0.9.4-23` — early development snapshot
- `0.9.4-59` — later release snapshot used for packaged binaries

Git tags in this repository correspond to preserved snapshots, not necessarily chronological commits.

## 🔧 Building

> **Source note:** `sancho-src/src` here is the **0.9.4-59** tree, which does
> **not** come from authentic source — it was recovered by decompiling the last
> published `sancho-0.9.4-59` binary (Vineflower) and then ported to modern
> SWT/JFace so it compiles and runs on a current JDK. It adds the features that
> never had preserved source (IRC, web browser, network stats, file comments) and
> already uses `Tree` instead of the deprecated `TableTree`. Reconstruction
> caveats: the GCJ-only `Prov` bootstrap is stubbed out, a couple of removed-JFace
> hooks (cell-edit mouse handling, per-node preference icons) are no-ops, and
> `MyMenuManager` / dynamic field-editor clearing use reflection.
>
> The genuine early **0.9.4-23** source (hand-written, with its original comments)
> is preserved at the [`0.9.4-23`](../../releases/tag/0.9.4-23) tag; check it out
> with `git checkout 0.9.4-23`.

### Modern build (Maven + VS Code)

A `pom.xml` was added so the original 2004-era source tree compiles and runs on a
current JDK. It has been verified to build and launch on **Windows with JDK 17+**
(tested with Temurin 25).

```bash
mvn clean package        # compiles sancho-src/src, produces target/sancho-0.9.4-23.jar
mvn compile              # just compile
```

Run from Maven:

```bash
mvn exec:java "-Dexec.args="   # or: java -cp "target/classes;<deps>" sancho.core.Sancho
```

**Recommended VM args** (silence JDK 25 native-access warnings and the legacy
comparator issue in the old table sorters):

```
--enable-native-access=ALL-UNNAMED
-Djava.util.Arrays.useLegacyMergeSort=true
```

#### In VS Code

1. Install the **Extension Pack for Java** (Red Hat).
2. Open the repository folder — the Java extension imports the Maven project and
   resolves dependencies automatically.
3. Press **F5** / use the **"Sancho (GUI)"** run configuration in
   [.vscode/launch.json](.vscode/launch.json).

#### Notes on dependencies

Original external libraries are resolved from Maven Central: **Eclipse SWT + JFace**,
**GNU Trove** (`gnu.trove`, Trove 2.x flat package), **GNU RegExp**, and **JSch**.
PircBot is listed historically but is not referenced by the source.

Three deprecated Eclipse classes removed from modern SWT/JFace
(`org.eclipse.swt.custom.TableTree`, `TableTreeEditor`, and
`org.eclipse.jface.viewers.TableTreeViewer`) are vendored back into
`sancho-src/src` so the original `TableTree`-based download views still compile.

Because the stock Eclipse jars are signed, and the JVM refuses to load an unsigned
class into a signed package, signature-stripped copies of SWT and JFace are kept in
[`local-repo/`](local-repo/) under the `org.sancho.thirdparty` group id and consumed
via a project-local Maven repository. Regenerate them with
[`tools/unsign-libs.ps1`](tools/unsign-libs.ps1). For an OS other than Windows,
strip signatures from the matching `org.eclipse.swt.*` fragment and adjust `pom.xml`.

### Windows executable (jpackage)

A portable Windows app image — `sancho.exe` with a bundled Java runtime, so it runs
without a separate JDK — is produced by [tools/build-exe.ps1](tools/build-exe.ps1):

```powershell
pwsh tools/build-exe.ps1
# output: target/dist/sancho/sancho.exe   (folder is portable, copy it anywhere)
```

Requires JDK 17+ (for `jpackage`) and Maven on `PATH`. A WiX Toolset install is only
needed if you extend the script to emit an `.msi`/`.exe` installer (`--type msi`).

#### Torrent / protocol association (send to core on double-click)

Sancho has a built-in registry integration — no external `.reg` editing needed:

1. Run `sancho.exe` and open **Preferences → sancho: Windows registry**.
2. **File extensions** tab → **Register association** → **Update windows registry**
   associates `.torrent` files. The **Protocols** tab does the same for `ed2k:`,
   `magnet:` and `sig2dat:`. (This runs `regedit`, so Windows may prompt for elevation.)

Double-clicking a `.torrent` (or an `ed2k:`/`magnet:` link) then runs
`sancho.exe -l "<file-or-link>"`, which connects to the MLDonkey core and hands it the
download directly (`S_DLLINK`), without opening the full GUI. As the preference page
warns, for `.torrent` files the core should be able to read the file (same machine, or
mldonkey core > 2.5.17).

The launcher is named `sancho.exe` and started with `-Duser.dir=$ROOTDIR` on purpose:
the registry page derives the executable path from `user.dir`, and `$ROOTDIR` pins it to
the folder that actually contains `sancho.exe`, so the written association is always correct.

### AppImage

An AppImage can be generated using:

```bash
./appimagetool-x86_64.AppImage Sancho.AppDir/

Output:

Sancho-x86_64.AppImage
```
## 🖥 Running
### AppImage (recommended)
```chmod +x Sancho-0.9.4-59-x86_64.AppImage 
./Sancho-0.9.4-59-x86_64.AppImage 
```
### Java (legacy)
Requires a Java Runtime Environment (JRE) and SWT GTK libraries.

## 📜 License

This repository preserves original licensing terms.

Sancho original code:

Copyright (C) 2004–2005 Rutger M. Ovidius
Licensed under the Common Public License (CPL)

Third-party components remain under their respective licenses.

See LICENSE and sancho-src/CPL.txt for details.

## 📌 Notes

This repository is maintained for historical preservation purposes.
No guarantees of compatibility, security, or correctness are provided.
