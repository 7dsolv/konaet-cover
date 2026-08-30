package com.konaet.cover.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konaet.cover.core.designsystem.tokens.KonaetColorTokens
import com.konaet.cover.core.designsystem.tokens.KonaetSpacing

@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KonaetColorTokens.Obsidian)
            .padding(horizontal = KonaetSpacing.Large.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = "KONAET",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = KonaetColorTokens.Verified,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "COVER",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = KonaetColorTokens.Text,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Proteção verificável de dispositivos",
                fontSize = 16.sp,
                color = KonaetColorTokens.Muted,
            )
            Spacer(modifier = Modifier.height(60.dp))

            OnboardingCard(
                title = "PROTEÇÃO",
                description = "Cobertura coletiva experimental em modo de simulação.",
            )
            Spacer(modifier = Modifier.height(16.dp))
            OnboardingCard(
                title = "PROVA",
                description = "Decisões reconstruídas a partir de eventos e evidências.",
            )
            Spacer(modifier = Modifier.height(16.dp))
            OnboardingCard(
                title = "PRIVACIDADE",
                description = "Sem dados pessoais ou identificadores reais em blockchain.",
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "DEMO · SIMULAÇÃO · SEM DINHEIRO REAL",
                fontSize = 12.sp,
                color = KonaetColorTokens.Ember,
                modifier = Modifier.padding(bottom = KonaetSpacing.Large.dp),
            )
            Button(
                onClick = onComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
            ) {
                Text("Começar", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun OnboardingCard(title: String, description: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = KonaetColorTokens.Surface,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
            )
            .padding(KonaetSpacing.Large.dp),
    ) {
        Column {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = KonaetColorTokens.Text,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = KonaetColorTokens.Muted,
            )
        }
    }
}
