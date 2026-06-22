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
