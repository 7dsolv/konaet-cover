package com.konaet.cover.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.konaet.cover.core.designsystem.tokens.KonaetColorTokens
import com.konaet.cover.core.designsystem.tokens.KonaetSpacing

@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("demo@konaet.local") }
    var password by remember { mutableStateOf("password") }
    var passwordVisible by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KonaetColorTokens.Obsidian)
            .padding(KonaetSpacing.Large.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(KonaetSpacing.Large.dp * 2))

        // Header
        Text(
            text = "Sign In",
            fontSize = 32.sp,
            color = KonaetColorTokens.Text,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(KonaetSpacing.Large.dp))

        Text(
            text = "Welcome back to KONAET COVER",
            fontSize = 14.sp,
            color = KonaetColorTokens.Muted
        )

        Spacer(modifier = Modifier.height(KonaetSpacing.Large.dp * 2))

        // Email field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            placeholder = { Text("you@example.com") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = KonaetColorTokens.Verified,
                unfocusedBorderColor = KonaetColorTokens.Surface,
                focusedLabelColor = KonaetColorTokens.Verified,
                cursorColor = KonaetColorTokens.Verified,
                focusedTextColor = KonaetColorTokens.Text,
                unfocusedTextColor = KonaetColorTokens.Text
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(KonaetSpacing.Large.dp))

        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            placeholder = { Text("••••••••") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle password"
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = KonaetColorTokens.Verified,
                unfocusedBorderColor = KonaetColorTokens.Surface,
                focusedLabelColor = KonaetColorTokens.Verified,
                cursorColor = KonaetColorTokens.Verified,
                focusedTextColor = KonaetColorTokens.Text,
                unfocusedTextColor = KonaetColorTokens.Text
            ),
            singleLine = true
        )

        // Error message
        if (uiState is AuthUiState.Error) {
            Spacer(modifier = Modifier.height(KonaetSpacing.Large.dp))
            Text(
                text = (uiState as AuthUiState.Error).message,
                color = KonaetColorTokens.Danger,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        KonaetColorTokens.Danger.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(KonaetSpacing.Large.dp)
            )
        }

        Spacer(modifier = Modifier.height(KonaetSpacing.Large.dp * 2))

        // Sign In button
        Button(
            onClick = {
                viewModel.login(email, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = uiState !is AuthUiState.Loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = KonaetColorTokens.Verified,
                disabledContainerColor = KonaetColorTokens.Verified.copy(alpha = 0.5f)
            )
        ) {
            if (uiState is AuthUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = KonaetColorTokens.Obsidian
                )
            } else {
                Text("Sign In", color = KonaetColorTokens.Obsidian, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(KonaetSpacing.Large.dp))

        OutlinedButton(
            onClick = viewModel::continueInDemoMode,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = uiState !is AuthUiState.Loading,
        ) {
            Text("Entrar no modo demonstração")
        }

        Spacer(modifier = Modifier.height(KonaetSpacing.Large.dp))

        // Sign up link
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Don't have an account? ",
                color = KonaetColorTokens.Muted,
                fontSize = 12.sp
            )
            TextButton(onClick = { /* TODO: Navigate to signup */ }) {
                Text("Sign Up", color = KonaetColorTokens.Verified, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Footer
        Text(
            text = "🔒 Your data is encrypted end-to-end",
            fontSize = 11.sp,
            color = KonaetColorTokens.Muted,
            modifier = Modifier.padding(vertical = KonaetSpacing.Large.dp)
        )
    }
}
