package com.konaet.cover.feature.auth

import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.konaet.cover.core.designsystem.components.KonaetBackdrop
import com.konaet.cover.core.designsystem.components.KonaetBrandHero
import com.konaet.cover.core.designsystem.components.KonaetGlassCard
import com.konaet.cover.core.designsystem.components.KonaetPrimaryButton
import com.konaet.cover.core.designsystem.components.KonaetStatusPill
import com.konaet.cover.core.designsystem.tokens.KonaetColorTokens

@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            onLoginSuccess()
        }
    }

    KonaetBackdrop {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues())
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            KonaetBrandHero(
                cubeSize = 118.dp,
                wordmarkWidthFraction = 0.58f,
            )

            KonaetStatusPill(
                text = "ACESSO PÚBLICO · ALPHA",
                color = KonaetColorTokens.Lavender,
            )

            Spacer(modifier = Modifier.height(20.dp))

            KonaetGlassCard(
                modifier = Modifier.fillMaxWidth(),
                emphasized = true,
            ) {
                Text(
                    text = "ENTRE NO LABORATÓRIO",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = KonaetColorTokens.Text,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Navegue por pools, eventos causais e simulações de risco sem criar conta.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = KonaetColorTokens.Muted,
                )
                Spacer(modifier = Modifier.height(18.dp))
                KonaetPrimaryButton(
                    text = "ENTRAR NO MODO DEMONSTRAÇÃO",
                    onClick = viewModel::continueInDemoMode,
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = Icons.Default.ArrowForward,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            KonaetGlassCard(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = KonaetColorTokens.Verified,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "SEM CADASTRO · SEM ANÚNCIOS",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = KonaetColorTokens.Text,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "A versão da Google Play funciona localmente e não coleta nem compartilha dados pessoais.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = KonaetColorTokens.Muted,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Demonstração técnica · sem dinheiro real · sem contratação de seguro",
                style = MaterialTheme.typography.bodySmall,
                color = KonaetColorTokens.Muted,
                textAlign = TextAlign.Center,
            )
        }
    }
}
