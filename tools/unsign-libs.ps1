<#
.SYNOPSIS
    Rebuilds local-repo/ with signature-stripped copies of Eclipse SWT and JFace.

.DESCRIPTION
    The stock Eclipse SWT / JFace jars on Maven Central are signed. Sancho vendors
    a few deprecated classes back into their original packages
    (org.eclipse.swt.custom.TableTree / TableTreeEditor and
    org.eclipse.jface.viewers.TableTreeViewer), and the JVM refuses to load an
    unsigned class into a signed package ("signer information does not match").

    This script downloads the signed jars, removes META-INF/ECLIPSE_.SF and
    META-INF/ECLIPSE_.RSA, and installs the unsigned results into the project-local
    Maven repository (local-repo/) under the org.sancho.thirdparty group id that
    pom.xml depends on. The checked-in local-repo/ already contains these jars;
    run this only to regenerate or to bump versions.

.NOTES
    Requires: a JDK and Maven on PATH. Adjust the versions below to match pom.xml.
#>

$ErrorActionPreference = "Stop"
$root      = Split-Path -Parent $PSScriptRoot
$localRepo = Join-Path $root "local-repo"
$work      = Join-Path $env:TEMP "sancho-unsign"

# Keep these aligned with pom.xml.
$swtVersion   = "3.124.0"
$jfaceVersion = "3.31.0"
$swtArtifact  = "org.eclipse.swt.win32.win32.x86_64"

New-Item -ItemType Directory -Force -Path $work | Out-Null
Add-Type -AssemblyName System.IO.Compression.FileSystem

function Get-Artifact($group, $artifact, $version, $dest) {
    Write-Host "Resolving $group`:$artifact`:$version"
    & mvn -q org.apache.maven.plugins:maven-dependency-plugin:3.6.1:copy `
        "-Dartifact=$group`:$artifact`:$version" `
        "-DoutputDirectory=$dest" | Out-Null
}

function Remove-Signature($jarPath) {
    $zip = [System.IO.Compression.ZipFile]::Open($jarPath, "Update")
    foreach ($e in @("META-INF/ECLIPSE_.SF", "META-INF/ECLIPSE_.RSA")) {
        $entry = $zip.GetEntry($e)
        if ($entry) { $entry.Delete() }
    }
    $zip.Dispose()
}

# --- SWT -------------------------------------------------------------------
Get-Artifact "org.eclipse.platform" $swtArtifact $swtVersion $work
$swtJar = Join-Path $work "$swtArtifact-$swtVersion.jar"
Remove-Signature $swtJar

$swtPom = Join-Path $work "swt-unsigned-pom.xml"
@"
<project xmlns="http://maven.apache.org/POM/4.0.0">
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.sancho.thirdparty</groupId>
  <artifactId>$swtArtifact</artifactId>
  <version>$swtVersion-unsigned</version>
</project>
"@ | Set-Content -Encoding UTF8 $swtPom

& mvn -q install:install-file "-Dfile=$swtJar" "-DpomFile=$swtPom" "-DlocalRepositoryPath=$localRepo"

# --- JFace -----------------------------------------------------------------
Get-Artifact "org.eclipse.platform" "org.eclipse.jface" $jfaceVersion $work
$jfaceJar = Join-Path $work "org.eclipse.jface-$jfaceVersion.jar"
Remove-Signature $jfaceJar

$jfacePom = Join-Path $work "jface-unsigned-pom.xml"
@"
<project xmlns="http://maven.apache.org/POM/4.0.0">
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.sancho.thirdparty</groupId>
  <artifactId>org.eclipse.jface</artifactId>
  <version>$jfaceVersion-unsigned</version>
  <dependencies>
    <dependency>
      <groupId>org.sancho.thirdparty</groupId>
      <artifactId>$swtArtifact</artifactId>
      <version>$swtVersion-unsigned</version>
    </dependency>
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

Write-Host "Done. Unsigned SWT/JFace installed into $localRepo"
