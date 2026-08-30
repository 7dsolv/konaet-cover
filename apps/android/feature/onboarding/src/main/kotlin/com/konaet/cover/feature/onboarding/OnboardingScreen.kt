package com.konaet.cover.feature.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.konaet.cover.core.designsystem.components.KonaetBackdrop
import com.konaet.cover.core.designsystem.components.KonaetBrandHero
import com.konaet.cover.core.designsystem.components.KonaetGlassCard
import com.konaet.cover.core.designsystem.components.KonaetPrimaryButton
import com.konaet.cover.core.designsystem.components.KonaetStatusPill
import com.konaet.cover.core.designsystem.tokens.KonaetColorTokens

@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    KonaetBackdrop {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues())
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            KonaetStatusPill(
                text = "ALPHA · DEMO VERIFICÁVEL",
                color = KonaetColorTokens.Lavender,
            )

            Spacer(modifier = Modifier.height(6.dp))

            KonaetBrandHero(
                cubeSize = 176.dp,
                wordmarkWidthFraction = 0.72f,
            )

            Text(
                text = "Proteção coletiva reconstruída por evidências.",
                style = MaterialTheme.typography.bodyLarge,
                color = KonaetColorTokens.Muted,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                FeatureCard(
                    index = "01",
                    title = "PROTEÇÃO",
                    description = "Modelo coletivo",
                    modifier = Modifier.weight(1f),
                )
                FeatureCard(
                    index = "02",
                    title = "PROVA",
                    description = "Trilha causal",
                    modifier = Modifier.weight(1f),
                )
                FeatureCard(
                    index = "03",
                    title = "PRIVACIDADE",
                    description = "Dados mínimos",
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            KonaetGlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "AMBIENTE CONTROLADO",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Black,
                    color = KonaetColorTokens.Ember,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Simulação técnica · sem dinheiro real · sem contratação de seguro",
                    style = MaterialTheme.typography.bodySmall,
                    color = KonaetColorTokens.Muted,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            KonaetPrimaryButton(
                text = "EXPLORAR O KONAET",
                onClick = onComplete,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Código aberto · Build verificável · Android 9+",
                style = MaterialTheme.typography.labelSmall,
                color = KonaetColorTokens.Muted,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun FeatureCard(
    index: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    KonaetGlassCard(modifier = modifier.height(102.dp)) {
        Text(
            text = index,
            style = MaterialTheme.typography.labelSmall,
            color = KonaetColorTokens.NeonPurple,
            fontWeight = FontWeight.Black,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = KonaetColorTokens.Text,
            fontWeight = FontWeight.Black,
            maxLines = 1,
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.labelSmall,
            color = KonaetColorTokens.Muted,
            maxLines = 1,
        )
    }
}
