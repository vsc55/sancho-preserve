<#
.SYNOPSIS
    Rebuilds local-repo/ with a signature-stripped copy of Eclipse JFace.

.DESCRIPTION
    The stock Eclipse JFace jar on Maven Central is signed. Sancho vendors a few
    classes back into the org.eclipse.jface.viewers package (ICustomViewer,
    CustomTableViewer, CustomTreeViewer), and the JVM refuses to load an unsigned
    class into a signed package ("signer information does not match").

    This script downloads the signed JFace jar, removes META-INF/ECLIPSE_.SF and
    META-INF/ECLIPSE_.RSA, and installs the unsigned result into the project-local
    Maven repository (local-repo/) under the org.sancho.thirdparty group id that
    pom.xml depends on. It is platform-independent — SWT is NOT stripped, because
    nothing is injected into its packages; the build uses the stock (signed) SWT
    fragment for each OS (see the pom profiles).

    The checked-in local-repo/ already contains this jar; run this only to
    regenerate or to bump the JFace version.

.NOTES
    Requires: a JDK and Maven on PATH. Keep $jfaceVersion aligned with pom.xml.
#>

$ErrorActionPreference = "Stop"
$root      = Split-Path -Parent $PSScriptRoot
$localRepo = Join-Path $root "local-repo"
$work      = Join-Path $env:TEMP "sancho-unsign"

$jfaceVersion = "3.31.0"

New-Item -ItemType Directory -Force -Path $work | Out-Null
Add-Type -AssemblyName System.IO.Compression.FileSystem

Write-Host "Resolving org.eclipse.platform:org.eclipse.jface:$jfaceVersion"
& mvn -q org.apache.maven.plugins:maven-dependency-plugin:3.6.1:copy `
    "-Dartifact=org.eclipse.platform:org.eclipse.jface:$jfaceVersion" `
    "-DoutputDirectory=$work" | Out-Null

$jfaceJar = Join-Path $work "org.eclipse.jface-$jfaceVersion.jar"

# Strip the signature so the vendored viewers classes can share the package.
$zip = [System.IO.Compression.ZipFile]::Open($jfaceJar, "Update")
foreach ($e in @("META-INF/ECLIPSE_.SF", "META-INF/ECLIPSE_.RSA")) {
    $entry = $zip.GetEntry($e)
    if ($entry) { $entry.Delete() }
}
$zip.Dispose()

# Platform-independent pom: depends on JFace's runtime deps, NOT on SWT (the
# consuming project provides the per-OS stock SWT fragment).
$jfacePom = Join-Path $work "jface-unsigned-pom.xml"
@"
<project xmlns="http://maven.apache.org/POM/4.0.0">
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.sancho.thirdparty</groupId>
  <artifactId>org.eclipse.jface</artifactId>
  <version>$jfaceVersion-unsigned</version>
  <dependencies>
    <dependency>
      <groupId>org.eclipse.platform</groupId>
      <artifactId>org.eclipse.core.commands</artifactId>
      <version>3.11.100</version>
    </dependency>
    <dependency>
      <groupId>org.eclipse.platform</groupId>
      <artifactId>org.eclipse.equinox.common</artifactId>
      <version>3.18.100</version>
    </dependency>
  </dependencies>
</project>
"@ | Set-Content -Encoding UTF8 $jfacePom

& mvn -q install:install-file "-Dfile=$jfaceJar" "-DpomFile=$jfacePom" "-DlocalRepositoryPath=$localRepo"

Write-Host "Done. Unsigned JFace installed into $localRepo"
