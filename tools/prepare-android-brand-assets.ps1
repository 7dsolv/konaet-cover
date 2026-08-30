[CmdletBinding()]
param(
    [Parameter(Mandatory = $true)]
    [string]$CubeSource
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"
Add-Type -AssemblyName System.Drawing

$repositoryRoot = Split-Path -Parent $PSScriptRoot
$sourcePath = [System.IO.Path]::GetFullPath($CubeSource)

if (-not (Test-Path -LiteralPath $sourcePath -PathType Leaf)) {
    throw "Cube source not found: $sourcePath"
}

function New-HighQualityGraphics {
    param([System.Drawing.Image]$Image)

    $graphics = [System.Drawing.Graphics]::FromImage($Image)
    $graphics.CompositingMode = [System.Drawing.Drawing2D.CompositingMode]::SourceOver
    $graphics.CompositingQuality = [System.Drawing.Drawing2D.CompositingQuality]::HighQuality
    $graphics.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $graphics.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::HighQuality
    $graphics.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::AntiAlias
    return $graphics
}

function Export-AdaptiveForeground {
    param(
        [System.Drawing.Image]$Source,
        [int]$CanvasSize,
        [string]$Destination
    )

    $output = [System.Drawing.Bitmap]::new(
        $CanvasSize,
        $CanvasSize,
        [System.Drawing.Imaging.PixelFormat]::Format32bppArgb
    )
    try {
        $graphics = New-HighQualityGraphics -Image $output
        try {
            $graphics.Clear([System.Drawing.Color]::Transparent)
            $markSize = [int][Math]::Round($CanvasSize * (68.0 / 108.0))
            $offset = [int](($CanvasSize - $markSize) / 2)
            $graphics.DrawImage($Source, $offset, $offset, $markSize, $markSize)
        }
        finally {
            $graphics.Dispose()
        }

        [System.IO.Directory]::CreateDirectory((Split-Path -Parent $Destination)) | Out-Null
        $output.Save($Destination, [System.Drawing.Imaging.ImageFormat]::Png)
    }
    finally {
        $output.Dispose()
    }
}

function Export-StoreIcon {
    param(
        [System.Drawing.Image]$Source,
        [string]$Destination
    )

    $size = 512
    $output = [System.Drawing.Bitmap]::new(
        $size,
        $size,
        [System.Drawing.Imaging.PixelFormat]::Format32bppArgb
    )
    try {
        $graphics = New-HighQualityGraphics -Image $output
        try {
            $background = [System.Drawing.Drawing2D.LinearGradientBrush]::new(
                [System.Drawing.Rectangle]::new(0, 0, $size, $size),
                [System.Drawing.Color]::FromArgb(255, 22, 5, 34),
                [System.Drawing.Color]::FromArgb(255, 4, 5, 10),
                45.0
            )
            try {
                $graphics.FillRectangle($background, 0, 0, $size, $size)
            }
            finally {
                $background.Dispose()
            }

            $markSize = 420
            $offset = [int](($size - $markSize) / 2)
            $graphics.DrawImage($Source, $offset, $offset, $markSize, $markSize)
        }
        finally {
            $graphics.Dispose()
        }

        [System.IO.Directory]::CreateDirectory((Split-Path -Parent $Destination)) | Out-Null
        $output.Save($Destination, [System.Drawing.Imaging.ImageFormat]::Png)
    }
    finally {
        $output.Dispose()
    }
}

$cube = [System.Drawing.Image]::FromFile($sourcePath)
try {
    $densitySizes = [ordered]@{
        "mdpi" = 108
        "hdpi" = 162
        "xhdpi" = 216
        "xxhdpi" = 324
        "xxxhdpi" = 432
    }

    foreach ($entry in $densitySizes.GetEnumerator()) {
        $destination = Join-Path $repositoryRoot (
            "apps\android\app\src\main\res\mipmap-{0}\ic_launcher_foreground.png" -f $entry.Key
        )
        Export-AdaptiveForeground -Source $cube -CanvasSize $entry.Value -Destination $destination
    }

    $storeIcon = Join-Path $repositoryRoot "docs\store-listing\graphics\icon-512.png"
    Export-StoreIcon -Source $cube -Destination $storeIcon
}
finally {
    $cube.Dispose()
}

Get-ChildItem -LiteralPath (Join-Path $repositoryRoot "apps\android\app\src\main\res") -Recurse -File -Filter "ic_launcher_foreground.png" |
    Select-Object FullName, Length
Get-Item -LiteralPath (Join-Path $repositoryRoot "docs\store-listing\graphics\icon-512.png") |
    Select-Object FullName, Length
