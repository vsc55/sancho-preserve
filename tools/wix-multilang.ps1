<#
.SYNOPSIS
    Turns jpackage's single-language .msi into ONE multilingual installer.

.DESCRIPTION
    jpackage (with WiX 3.14) builds a single MSI in the base language (en) and,
    when given an explicit --temp dir, leaves its intermediate WiX files behind:
    the compiled *.wixobj, the localization *.wxl, and the staged app image. This
    script reuses those to assemble a single MSI that carries every UI language:

      1. Re-light the same *.wixobj once per culture -> one MSI per language.
      2. torch each language MSI against the base -> a language transform (.mst).
      3. Embed every .mst into a copy of the base MSI as a sub-storage named by
         its LCID, and set the Package "Template" summary language list.

    At install time Windows Installer applies the sub-storage transform whose LCID
    matches the machine's UI language (falling back to the base language), so a
    single sancho.msi shows German on a German Windows, Spanish on a Spanish one,
    and so on -- with no separate per-language downloads.

    Steps 1-2 use torch/light from WiX 3.14 (must be on PATH, as jpackage needs);
    step 3 uses the WindowsInstaller COM API directly, so it needs no Windows SDK
    scripts (wisubstg.vbs / WiLangId.vbs) on the build machine or CI runner.

    English (en / 1033) is the base and is always included. The extra languages
    are those with a MsiInstallerStrings_<culture>.wxl in -ResourceDir that maps to
    an entry in the table below.

.PARAMETER WorkDir
    The jpackage --temp directory (contains wixobj/, config/, images/).

.PARAMETER LangDir
    packaging/windows/wix-lang -- the extra-language MsiInstallerStrings_*.wxl
    (de/ja/zh/es). These are kept OUT of jpackage's --resource-dir on purpose:
    jpackage auto-discovers every MsiInstallerStrings_<culture>.wxl there and, on
    finding a language it does not bundle (Spanish), makes it the primary culture
    and mis-builds. The base English wxl (with the associations-checkbox string)
    does live in --resource-dir, so jpackage copies it into WorkDir/config itself.

.PARAMETER OutFile
    Destination path for the finished multilingual .msi (jpackage's output is
    overwritten / replaced by this).

.NOTES
    Requires WiX 3.14 (light.exe, torch.exe) on PATH -- the same toolchain
    jpackage uses on JDK 21. Windows only.
#>
param(
    [Parameter(Mandatory = $true)][string]$WorkDir,
    [Parameter(Mandatory = $true)][string]$LangDir,
    [Parameter(Mandatory = $true)][string]$OutFile
)

$ErrorActionPreference = "Stop"

# Each language: the WiX culture used to re-light it (drives WixUIExtension's
# standard-dialog translation) and one or more decimal LCIDs to register the
# transform under. A translation can cover several LCIDs (e.g. Portuguese serves
# both Portugal 2070 and Brazil 1046); Windows applies whichever matches the OS.
# English (1033) is the base.
$BASE = [pscustomobject]@{ Key = "en"; Culture = "en-us"; Lcids = @(1033); Wxl = "MsiInstallerStrings_en.wxl" }
$EXTRA = @(
    [pscustomobject]@{ Key = "de"; Culture = "de-de"; Lcids = @(1031);       Wxl = "MsiInstallerStrings_de.wxl" }
    [pscustomobject]@{ Key = "fr"; Culture = "fr-fr"; Lcids = @(1036);       Wxl = "MsiInstallerStrings_fr.wxl" }
    [pscustomobject]@{ Key = "it"; Culture = "it-it"; Lcids = @(1040);       Wxl = "MsiInstallerStrings_it.wxl" }
    [pscustomobject]@{ Key = "ja"; Culture = "ja-jp"; Lcids = @(1041);       Wxl = "MsiInstallerStrings_ja.wxl" }
    [pscustomobject]@{ Key = "pt"; Culture = "pt-pt"; Lcids = @(2070, 1046); Wxl = "MsiInstallerStrings_pt.wxl" }
    [pscustomobject]@{ Key = "zh"; Culture = "zh-cn"; Lcids = @(2052);       Wxl = "MsiInstallerStrings_zh_CN.wxl" }
    [pscustomobject]@{ Key = "es"; Culture = "es-es"; Lcids = @(3082);       Wxl = "MsiInstallerStrings_es.wxl" }
)

# --- locate jpackage's leftover intermediates -----------------------------------
$config = Join-Path $WorkDir "config"
$wixobj = @(Get-ChildItem -Path $WorkDir -Recurse -Filter *.wixobj | ForEach-Object { $_.FullName })
if (-not (Test-Path $config)) { throw "config dir not found under $WorkDir (did jpackage run with --temp?)" }
if ($wixobj.Count -eq 0)      { throw "no *.wixobj found under $WorkDir" }

# The app image root is the directory that holds sancho.exe; light resolves the
# packaged files relative to it, so it must be the working directory.
$exe = Get-ChildItem -Path (Join-Path $WorkDir "images") -Recurse -Filter "sancho.exe" -ErrorAction SilentlyContinue | Select-Object -First 1
if (-not $exe) { throw "sancho.exe not found under $WorkDir/images" }
$imageRoot = Split-Path -Parent $exe.FullName

# Stage the extra-language wxl (de/ja/zh/es, each carrying the associations-checkbox
# string) into config/, overriding jpackage's token-less de/ja/zh and adding es. The
# base en wxl is already in config/ (jpackage copied it from its --resource-dir).
foreach ($f in (Get-ChildItem -Path $LangDir -Filter "MsiInstallerStrings_*.wxl")) {
    Copy-Item -Force $f.FullName (Join-Path $config $f.Name)
}

$langOut = Join-Path $WorkDir "lang"
New-Item -ItemType Directory -Force -Path $langOut | Out-Null

function Invoke-Light([pscustomobject]$lang, [string]$outMsi) {
    $loc = Join-Path $config $lang.Wxl
    if (-not (Test-Path $loc)) { throw "missing localization: $loc" }
    Push-Location $imageRoot
    try {
        # -sval: skip ICE validation (jpackage does the same; a language re-light
        #        trips ICE27 on the sequenced RemoveExistingProducts otherwise).
        # -spdb: no .wixpdb. -b: extra binder search path for shared resources.
        & light.exe -nologo -sval -spdb `
            -ext WixUtilExtension -ext WixUIExtension `
            -cultures:$($lang.Culture) -loc $loc -b $config `
            @wixobj -out $outMsi
        if ($LASTEXITCODE -ne 0) { throw "light failed for $($lang.Key) (exit $LASTEXITCODE)" }
    } finally { Pop-Location }
}

# --- 1) base MSI + one MSI per extra language -----------------------------------
Write-Host "==> multilang: re-lighting base ($($BASE.Culture)) + $($EXTRA.Count) languages"
$baseMsi = Join-Path $langOut "sancho-$($BASE.Key).msi"
Invoke-Light $BASE $baseMsi

$transforms = @()   # @{ Lcid; Mst } -- one entry per LCID (a language may have several)
foreach ($lang in $EXTRA) {
    $langMsi = Join-Path $langOut "sancho-$($lang.Key).msi"
    Invoke-Light $lang $langMsi

    # 2) language transform: applying it to the base MSI yields the language MSI.
    #    Registered under each of the language's LCIDs (same transform bytes).
    $mst = Join-Path $langOut "$($lang.Lcids[0]).mst"
    & torch.exe -nologo -p -t language $baseMsi $langMsi -out $mst
    if ($LASTEXITCODE -ne 0) { throw "torch failed for $($lang.Key) (exit $LASTEXITCODE)" }
    foreach ($lcid in $lang.Lcids) { $transforms += [pscustomobject]@{ Lcid = $lcid; Mst = $mst } }
    Write-Host "    + $($lang.Culture) -> $($lang.Lcids -join '/')"
}

# --- 3) embed transforms + set the language list --------------------------------
# One sub-storage per transform (named by decimal LCID) plus the Package Template
# language list. Done in wix-embed-transforms.vbs via the WindowsInstaller COM API,
# which handles MSI's indexed record properties natively (PowerShell's COM reflection
# does not) and needs only cscript -- no Windows SDK scripts on the build/CI machine.
Copy-Item -Force $baseMsi $OutFile

$langList = ($BASE.Lcids + ($EXTRA | ForEach-Object { $_.Lcids })) -join ','
$vbs = Join-Path $PSScriptRoot "wix-embed-transforms.vbs"
$vbsArgs = @($OutFile, $langList)
foreach ($t in $transforms) { $vbsArgs += @("$($t.Lcid)", $t.Mst) }

& cscript.exe //nologo $vbs @vbsArgs
if ($LASTEXITCODE -ne 0) { throw "wix-embed-transforms.vbs failed (exit $LASTEXITCODE)" }

Write-Host "==> multilang: $OutFile  (languages: $langList)"
