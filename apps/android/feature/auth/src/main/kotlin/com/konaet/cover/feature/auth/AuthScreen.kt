package com.konaet.cover.feature.auth

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.konaet.cover.core.designsystem.components.KonaetBackdrop
import com.konaet.cover.core.designsystem.components.KonaetBrandHero
import com.konaet.cover.core.designsystem.components.KonaetGlassCard
import com.konaet.cover.core.designsystem.components.KonaetPrimaryButton
import com.konaet.cover.core.designsystem.components.KonaetStatusPill
import com.konaet.cover.core.designsystem.tokens.KonaetColorTokens
import com.konaet.cover.core.designsystem.tokens.KonaetRadius

@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showApiLogin by remember { mutableStateOf(false) }

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
                    enabled = uiState !is AuthUiState.Loading,
                    leadingIcon = Icons.Default.ArrowForward,
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            TextButton(onClick = { showApiLogin = !showApiLogin }) {
                Icon(
                    imageVector = Icons.Default.Api,
                    contentDescription = null,
                    tint = KonaetColorTokens.Violet,
                )
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text(
                    text = if (showApiLogin) "Ocultar acesso à API" else "Conectar à API de desenvolvimento",
                    color = KonaetColorTokens.Lavender,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            AnimatedVisibility(visible = showApiLogin) {
                KonaetGlassCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "API DE DESENVOLVIMENTO",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("E-mail") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = konaetTextFieldColors(),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(KonaetRadius.Medium.dp),
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Senha") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null)
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) {
                                        Icons.Default.VisibilityOff
                                    } else {
                                        Icons.Default.Visibility
                                    },
                                    contentDescription = if (passwordVisible) {
                                        "Ocultar senha"
                                    } else {
                                        "Mostrar senha"
                                    },
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = konaetTextFieldColors(),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(KonaetRadius.Medium.dp),
                    )

                    if (uiState is AuthUiState.Error) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = (uiState as AuthUiState.Error).message,
                            style = MaterialTheme.typography.bodySmall,
                            color = KonaetColorTokens.Danger,
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = { viewModel.login(email.trim(), password) },
                        enabled = email.isNotBlank() && password.isNotBlank() &&
                            uiState !is AuthUiState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                    ) {
                        if (uiState is AuthUiState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.height(22.dp),
                                color = KonaetColorTokens.Violet,
                                strokeWidth = 2.dp,
                            )
                        } else {
                            Text("CONECTAR", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "A demonstração não envia dados pessoais e não movimenta dinheiro real.",
                style = MaterialTheme.typography.bodySmall,
                color = KonaetColorTokens.Muted,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun konaetTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = KonaetColorTokens.Violet,
    unfocusedBorderColor = KonaetColorTokens.Outline,
    focusedLabelColor = KonaetColorTokens.Lavender,
    unfocusedLabelColor = KonaetColorTokens.Muted,
    cursorColor = KonaetColorTokens.NeonPurple,
    focusedTextColor = KonaetColorTokens.Text,
    unfocusedTextColor = KonaetColorTokens.Text,
    focusedLeadingIconColor = KonaetColorTokens.Violet,
    unfocusedLeadingIconColor = KonaetColorTokens.Muted,
    focusedTrailingIconColor = KonaetColorTokens.Violet,
    unfocusedTrailingIconColor = KonaetColorTokens.Muted,
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
)
