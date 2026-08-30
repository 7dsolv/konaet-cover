package com.konaet.cover.core.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konaet.cover.core.designsystem.R
import com.konaet.cover.core.designsystem.tokens.KonaetColorTokens
import com.konaet.cover.core.designsystem.tokens.KonaetRadius
import com.konaet.cover.core.designsystem.tokens.KonaetSpacing

@Composable
fun KonaetBackdrop(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        KonaetColorTokens.Void,
                        KonaetColorTokens.Obsidian,
                        Color(0xFF0D0714),
                    ),
                ),
            )
            .drawBehind {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            KonaetColorTokens.Violet.copy(alpha = 0.18f),
                            Color.Transparent,
                        ),
                        center = Offset(size.width * 0.5f, size.height * 0.08f),
                        radius = size.width * 0.72f,
                    ),
                    radius = size.width * 0.72f,
                    center = Offset(size.width * 0.5f, size.height * 0.08f),
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            KonaetColorTokens.ElectricViolet.copy(alpha = 0.10f),
                            Color.Transparent,
                        ),
                        center = Offset(size.width, size.height * 0.72f),
                        radius = size.width * 0.8f,
                    ),
                    radius = size.width * 0.8f,
                    center = Offset(size.width, size.height * 0.72f),
                )
            },
        content = content,
    )
}

@Composable
fun KonaetCube(
    modifier: Modifier = Modifier,
    size: Dp = 160.dp,
) {
    Image(
        painter = painterResource(R.drawable.konaet_cube),
        contentDescription = "Cubo KONAET COVER",
        modifier = modifier.size(size),
        contentScale = ContentScale.Fit,
    )
}

@Composable
fun KonaetWordmark(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(R.drawable.konaet_wordmark),
        contentDescription = "KONAET COVER",
        modifier = modifier.aspectRatio(2f),
        contentScale = ContentScale.Fit,
    )
}

@Composable
fun KonaetBrandHero(
    modifier: Modifier = Modifier,
    cubeSize: Dp = 176.dp,
    wordmarkWidthFraction: Float = 0.72f,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        KonaetCube(size = cubeSize)
        KonaetWordmark(
            modifier = Modifier.fillMaxWidth(wordmarkWidthFraction),
        )
    }
}

@Composable
fun KonaetGlassCard(
    modifier: Modifier = Modifier,
    emphasized: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(KonaetRadius.Large.dp)
    val borderBrush = if (emphasized) {
        Brush.linearGradient(
            listOf(
                KonaetColorTokens.NeonPurple.copy(alpha = 0.9f),
                KonaetColorTokens.Violet.copy(alpha = 0.35f),
                KonaetColorTokens.Verified.copy(alpha = 0.45f),
            ),
        )
    } else {
        Brush.linearGradient(
            listOf(
                KonaetColorTokens.Outline.copy(alpha = 0.85f),
                KonaetColorTokens.Outline.copy(alpha = 0.25f),
            ),
        )
    }

    Column(
        modifier = modifier
            .shadow(
                elevation = if (emphasized) 18.dp else 8.dp,
                shape = shape,
                ambientColor = KonaetColorTokens.NeonPurple.copy(alpha = 0.35f),
                spotColor = KonaetColorTokens.NeonPurple.copy(alpha = 0.35f),
            )
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        KonaetColorTokens.Surface2.copy(alpha = 0.96f),
                        KonaetColorTokens.Surface.copy(alpha = 0.94f),
                    ),
                ),
                shape = shape,
            )
            .border(1.dp, borderBrush, shape)
            .padding(KonaetSpacing.Large.dp),
        content = content,
    )
}

@Composable
fun KonaetPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
) {
    val shape = RoundedCornerShape(KonaetRadius.Medium.dp)
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .height(56.dp)
            .shadow(
                elevation = 16.dp,
                shape = shape,
                ambientColor = KonaetColorTokens.NeonPurple.copy(alpha = 0.5f),
                spotColor = KonaetColorTokens.NeonPurple.copy(alpha = 0.5f),
            )
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        KonaetColorTokens.ElectricViolet,
                        KonaetColorTokens.NeonPurple,
                    ),
                ),
                shape = shape,
            ),
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = KonaetColorTokens.Text,
            disabledContainerColor = KonaetColorTokens.Surface2,
            disabledContentColor = KonaetColorTokens.Muted,
        ),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingIcon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.size(10.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun KonaetStatusPill(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = KonaetColorTokens.Verified,
) {
    Surface(
        modifier = modifier,
        color = color.copy(alpha = 0.10f),
        contentColor = color,
        shape = CircleShape,
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.45f)),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun KonaetMetric(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    accent: Color = KonaetColorTokens.Lavender,
) {
    Column(modifier = modifier) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
            color = accent,
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = KonaetColorTokens.Muted,
        )
    }
}
