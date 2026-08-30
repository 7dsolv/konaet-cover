[CmdletBinding()]
param(
    [Parameter(Mandatory = $true)]
    [string]$FeatureSource,

    [string]$OutputDirectory = ""
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

Add-Type -AssemblyName System.Drawing

$repositoryRoot = Split-Path -Parent $PSScriptRoot
if (-not $OutputDirectory) {
    $OutputDirectory = Join-Path $repositoryRoot "docs\store-listing\graphics"
}

$sourcePath = [System.IO.Path]::GetFullPath($FeatureSource)
$outputPath = [System.IO.Path]::GetFullPath($OutputDirectory)

if (-not (Test-Path -LiteralPath $sourcePath -PathType Leaf)) {
    throw "Feature graphic source not found: $sourcePath"
}

[System.IO.Directory]::CreateDirectory($outputPath) | Out-Null

function New-HighQualityGraphics {
    param([System.Drawing.Image]$Image)

    $graphics = [System.Drawing.Graphics]::FromImage($Image)
    $graphics.CompositingQuality = [System.Drawing.Drawing2D.CompositingQuality]::HighQuality
    $graphics.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $graphics.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::HighQuality
    $graphics.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::AntiAlias
    return $graphics
}

function Export-FeatureGraphic {
    param(
        [string]$Source,
        [string]$Destination
    )

    $inputImage = [System.Drawing.Image]::FromFile($Source)
    try {
        $expectedRatio = 1024.0 / 500.0
        $actualRatio = $inputImage.Width / [double]$inputImage.Height
        if ([Math]::Abs($actualRatio - $expectedRatio) -gt 0.002) {
            throw "Feature source must use the 1024:500 aspect ratio; received $($inputImage.Width)x$($inputImage.Height)."
        }

        $outputImage = [System.Drawing.Bitmap]::new(
            1024,
            500,
            [System.Drawing.Imaging.PixelFormat]::Format24bppRgb
        )
        try {
            $graphics = New-HighQualityGraphics -Image $outputImage
            try {
                $graphics.Clear([System.Drawing.Color]::FromArgb(11, 14, 18))
                $graphics.DrawImage($inputImage, 0, 0, 1024, 500)
            }
            finally {
                $graphics.Dispose()
            }

            $outputImage.Save($Destination, [System.Drawing.Imaging.ImageFormat]::Png)
        }
        finally {
            $outputImage.Dispose()
        }
    }
    finally {
        $inputImage.Dispose()
    }
}

function Export-StoreIcon {
    param([string]$Destination)

    $size = 512
    $scale = $size / 108.0
    $outputImage = [System.Drawing.Bitmap]::new(
        $size,
        $size,
        [System.Drawing.Imaging.PixelFormat]::Format32bppArgb
    )

    try {
        $graphics = New-HighQualityGraphics -Image $outputImage
        try {
            $graphics.Clear([System.Drawing.Color]::FromArgb(255, 11, 14, 18))
            $graphics.ScaleTransform($scale, $scale)

            $shield = [System.Drawing.Drawing2D.GraphicsPath]::new()
            try {
                $shield.StartFigure()
                $shield.AddLine(54, 16, 84, 28)
                $shield.AddLine(84, 28, 80, 62)
                $shield.AddBezier(80, 62, 78, 77, 68, 88, 54, 94)
                $shield.AddBezier(54, 94, 40, 88, 30, 77, 28, 62)
                $shield.AddLine(28, 62, 24, 28)
                $shield.CloseFigure()

                $shieldBrush = [System.Drawing.SolidBrush]::new(
                    [System.Drawing.Color]::FromArgb(255, 0, 200, 150)
                )
                try {
                    $graphics.FillPath($shieldBrush, $shield)
                }
                finally {
                    $shieldBrush.Dispose()
                }
            }
            finally {
                $shield.Dispose()
            }

            $darkBrush = [System.Drawing.SolidBrush]::new(
                [System.Drawing.Color]::FromArgb(255, 11, 14, 18)
            )
            try {
                $graphics.FillRectangle($darkBrush, 43, 40, 7, 28)
                $graphics.FillRectangle($darkBrush, 58, 40, 7, 28)
                $graphics.FillRectangle($darkBrush, 50, 51, 8, 6)
            }
            finally {
                $darkBrush.Dispose()
            }

            $violetBrush = [System.Drawing.SolidBrush]::new(
                [System.Drawing.Color]::FromArgb(255, 123, 92, 255)
            )
            try {
                $graphics.FillRectangle($violetBrush, 50, 30, 8, 8)
                $graphics.FillRectangle($violetBrush, 50, 70, 8, 8)
            }
            finally {
                $violetBrush.Dispose()
            }
        }
        finally {
            $graphics.Dispose()
        }

        $outputImage.Save($Destination, [System.Drawing.Imaging.ImageFormat]::Png)
    }
    finally {
        $outputImage.Dispose()
    }
}

$featureDestination = Join-Path $outputPath "feature-graphic-1024x500.png"
$iconDestination = Join-Path $outputPath "icon-512.png"

Export-FeatureGraphic -Source $sourcePath -Destination $featureDestination
Export-StoreIcon -Destination $iconDestination

Get-Item -LiteralPath $featureDestination, $iconDestination |
    Select-Object FullName, Length
