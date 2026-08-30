package com.konaet.cover.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.konaet.cover.core.designsystem.components.KonaetBackdrop
import com.konaet.cover.core.designsystem.components.KonaetCube
import com.konaet.cover.core.designsystem.components.KonaetGlassCard
import com.konaet.cover.core.designsystem.components.KonaetMetric
import com.konaet.cover.core.designsystem.components.KonaetStatusPill
import com.konaet.cover.core.designsystem.components.KonaetWordmark
import com.konaet.cover.core.designsystem.tokens.KonaetColorTokens
import java.util.Locale
import kotlin.math.sqrt

private data class KonaetTab(
    val label: String,
    val icon: ImageVector,
)

private val tabs = listOf(
    KonaetTab("Início", Icons.Default.Home),
    KonaetTab("Pools", Icons.Default.Dashboard),
    KonaetTab("Eventos", Icons.Default.Notifications),
    KonaetTab("Risco", Icons.Default.Science),
    KonaetTab("Perfil", Icons.Default.Person),
)

@Composable
fun HomeScreen(navController: NavController) {
    var selectedTab by rememberSaveable { mutableStateOf(0) }

    KonaetBackdrop {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                NavigationBar(
                    containerColor = KonaetColorTokens.Surface.copy(alpha = 0.98f),
                    tonalElevation = 0.dp,
                ) {
                    tabs.forEachIndexed { index, tab ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.label,
                                )
                            },
                            label = { Text(tab.label, fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = KonaetColorTokens.Text,
                                selectedTextColor = KonaetColorTokens.Lavender,
                                indicatorColor = KonaetColorTokens.Violet.copy(alpha = 0.28f),
                                unselectedIconColor = KonaetColorTokens.Muted,
                                unselectedTextColor = KonaetColorTokens.Muted,
                            ),
                        )
                    }
                }
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .windowInsetsPadding(WindowInsets.statusBars),
            ) {
                AppHeader()
                when (selectedTab) {
                    0 -> HomeTabScreen()
                    1 -> PoolTabScreen()
                    2 -> ClaimsTabScreen()
                    3 -> LabTabScreen()
                    4 -> ProfileTabScreen(navController)
                }
            }
        }
    }
}

@Composable
private fun AppHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        KonaetCube(size = 42.dp)
        KonaetWordmark(
            modifier = Modifier
                .padding(start = 8.dp)
                .fillMaxWidth(0.38f),
        )
        Spacer(modifier = Modifier.weight(1f))
        KonaetStatusPill(text = "DEMO")
    }
}

@Composable
private fun ScreenContainer(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 8.dp),
        content = content,
    )
}

@Composable
fun HomeTabScreen() {
    ScreenContainer {
        Text(
            text = "PROTEÇÃO VERIFICÁVEL",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
        )
        Text(
            text = "Estado reconstruído a partir da trilha de eventos.",
            style = MaterialTheme.typography.bodyMedium,
            color = KonaetColorTokens.Muted,
        )

        Spacer(modifier = Modifier.height(18.dp))

        KonaetGlassCard(
            modifier = Modifier.fillMaxWidth(),
            emphasized = true,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(KonaetColorTokens.Verified.copy(alpha = 0.10f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = KonaetColorTokens.Verified,
                        modifier = Modifier.size(42.dp),
                    )
                }
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    KonaetStatusPill(text = "SISTEMA ÍNTEGRO")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Proteção ativa",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                    )
                    Text(
                        text = "Nenhum incidente no cenário atual",
                        style = MaterialTheme.typography.bodySmall,
                        color = KonaetColorTokens.Muted,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        KonaetGlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                KonaetMetric("pools simulados", "02")
                KonaetMetric("eventos causais", "128", accent = KonaetColorTokens.Violet)
                KonaetMetric("confiança", "95%", accent = KonaetColorTokens.Verified)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "ÚLTIMO EVENTO",
            style = MaterialTheme.typography.labelLarge,
            color = KonaetColorTokens.Lavender,
            fontWeight = FontWeight.Black,
        )
        Spacer(modifier = Modifier.height(8.dp))
        EventCard(
            title = "Estado do pool recalculado",
            detail = "Relógio lógico #128 · hash local validado",
            time = "agora",
        )
        Spacer(modifier = Modifier.height(18.dp))
    }
}

@Composable
fun PoolTabScreen() {
    ScreenContainer {
        SectionHeading(
            title = "POOLS DE PROTEÇÃO",
            subtitle = "Cenários sintéticos para testar capacidade e risco.",
        )
        Spacer(modifier = Modifier.height(18.dp))
        PoolCard(
            name = "NÚCLEO VIOLETA",
            code = "KV-001",
            occupancy = 0.68f,
            members = "680 / 1.000 participantes",
        )
        Spacer(modifier = Modifier.height(14.dp))
        PoolCard(
            name = "REDE OBSIDIANA",
            code = "KO-002",
            occupancy = 0.42f,
            members = "420 / 1.000 participantes",
        )
        Spacer(modifier = Modifier.height(18.dp))
        KonaetGlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = KonaetColorTokens.Verified,
                )
                Text(
                    text = "Dados demonstrativos. Nenhuma adesão ou transação real é criada.",
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = KonaetColorTokens.Muted,
                )
            }
        }
    }
}

@Composable
fun ClaimsTabScreen() {
    ScreenContainer {
        SectionHeading(
            title = "EVENTOS E EVIDÊNCIAS",
            subtitle = "Linha do tempo reconstruível do cenário demonstrativo.",
        )
        Spacer(modifier = Modifier.height(18.dp))
        KonaetGlassCard(
            modifier = Modifier.fillMaxWidth(),
            emphasized = true,
        ) {
            Icon(
                imageVector = Icons.Default.EventAvailable,
                contentDescription = null,
                tint = KonaetColorTokens.Verified,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterHorizontally),
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "NENHUMA SOLICITAÇÃO ATIVA",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "O ambiente está íntegro. Novos eventos aparecerão aqui com hash, ordem lógica e evidências associadas.",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = KonaetColorTokens.Muted,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        EventCard(
            title = "Verificação automática concluída",
            detail = "128 eventos conferidos · nenhuma divergência",
            time = "20:02",
        )
    }
}

@Composable
fun LabTabScreen() {
    var probability by rememberSaveable { mutableFloatStateOf(0.035f) }
    var sampleSize by rememberSaveable { mutableFloatStateOf(1_000f) }

    val n = sampleSize.toInt().coerceAtLeast(100)
    val expectedLosses = probability * n
    val z = 1.96
    val denominator = 1 + (z * z / n)
    val center = (probability + (z * z / (2 * n))) / denominator
    val margin = z * sqrt(
        (probability * (1 - probability) / n) +
            (z * z / (4.0 * n * n)),
    ) / denominator
    val lower = (center - margin).coerceAtLeast(0.0)
    val upper = (center + margin).coerceAtMost(1.0)

    ScreenContainer {
        SectionHeading(
            title = "LABORATÓRIO DE RISCO",
            subtitle = "Simulação de Bernoulli com intervalo de Wilson de 95%.",
        )
        Spacer(modifier = Modifier.height(18.dp))

        KonaetGlassCard(
            modifier = Modifier.fillMaxWidth(),
            emphasized = true,
        ) {
            Text(
                text = "PROBABILIDADE AJUSTADA",
                style = MaterialTheme.typography.labelMedium,
                color = KonaetColorTokens.Lavender,
                fontWeight = FontWeight.Black,
            )
            Text(
                text = formatPercent(probability.toDouble()),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Black,
                color = KonaetColorTokens.NeonPurple,
            )
            Slider(
                value = probability,
                onValueChange = { probability = it },
                valueRange = 0.005f..0.15f,
                colors = konaetSliderColors(),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("0,5%", style = MaterialTheme.typography.labelSmall)
                Text("limite 15%", style = MaterialTheme.typography.labelSmall)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        KonaetGlassCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "TAMANHO DA AMOSTRA",
                style = MaterialTheme.typography.labelMedium,
                color = KonaetColorTokens.Lavender,
                fontWeight = FontWeight.Black,
            )
            Text(
                text = String.format(Locale("pt", "BR"), "%,d", n),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
            )
            Slider(
                value = sampleSize,
                onValueChange = { sampleSize = it },
                valueRange = 100f..10_000f,
                steps = 98,
                colors = konaetSliderColors(),
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ResultCard(
                label = "PERDAS ESPERADAS",
                value = String.format(Locale("pt", "BR"), "%.1f", expectedLosses),
                modifier = Modifier.weight(1f),
            )
            ResultCard(
                label = "INTERVALO 95%",
                value = "${formatPercent(lower)}–${formatPercent(upper)}",
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        KonaetGlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = KonaetColorTokens.Ember,
                )
                Text(
                    text = "Coeficientes sintéticos para pesquisa. Resultado sem validade atuarial ou financeira.",
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = KonaetColorTokens.Muted,
                )
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
    }
}

@Composable
fun ProfileTabScreen(navController: NavController) {
    var dialog by rememberSaveable { mutableStateOf<String?>(null) }

    ScreenContainer {
        SectionHeading(
            title = "PERFIL DE DEMONSTRAÇÃO",
            subtitle = "Sessão local sem conta, cobrança ou identificadores reais.",
        )
        Spacer(modifier = Modifier.height(18.dp))
        KonaetGlassCard(
            modifier = Modifier.fillMaxWidth(),
            emphasized = true,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = KonaetColorTokens.Violet,
                    modifier = Modifier.size(54.dp),
                )
                Column(modifier = Modifier.padding(start = 14.dp)) {
                    Text(
                        text = "EXPLORADOR KONAET",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                    )
                    Text(
                        text = "demo@konaet.local",
                        style = MaterialTheme.typography.bodySmall,
                        color = KonaetColorTokens.Muted,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        ProfileAction(
            icon = Icons.Default.Settings,
            title = "Configurações da sessão",
            subtitle = "Preferências locais do modo demo",
            onClick = { dialog = "settings" },
        )
        ProfileAction(
            icon = Icons.Default.Security,
            title = "Privacidade",
            subtitle = "Como esta demonstração trata dados",
            onClick = { dialog = "privacy" },
        )
        ProfileAction(
            icon = Icons.Default.DeleteOutline,
            title = "Excluir dados locais",
            subtitle = "Limpar o estado temporário da demo",
            onClick = { dialog = "delete" },
        )
        ProfileAction(
            icon = Icons.Default.Logout,
            title = "Sair da demonstração",
            subtitle = "Voltar à tela de acesso",
            onClick = {
                navController.navigate("auth") {
                    popUpTo("home") { inclusive = true }
                }
            },
        )
        Spacer(modifier = Modifier.height(18.dp))
    }

    dialog?.let { dialogType ->
        val (title, message) = when (dialogType) {
            "settings" -> "Configurações da sessão" to
                "A versão alpha usa as preferências seguras padrão. Novas opções serão adicionadas sem alterar dados reais."
            "privacy" -> "Privacidade por desenho" to
                "O modo demonstração não exige cadastro, não grava identificadores reais de aparelhos e não envia dados pessoais para blockchain."
            else -> "Dados locais removidos" to
                "Esta sessão não possui conta real. O estado demonstrativo temporário foi considerado limpo."
        }
        AlertDialog(
            onDismissRequest = { dialog = null },
            title = { Text(title, fontWeight = FontWeight.Black) },
            text = { Text(message, color = KonaetColorTokens.Muted) },
            confirmButton = {
                TextButton(onClick = { dialog = null }) {
                    Text("ENTENDI")
                }
            },
            containerColor = KonaetColorTokens.Surface2,
            titleContentColor = KonaetColorTokens.Text,
            textContentColor = KonaetColorTokens.Muted,
        )
    }
}

@Composable
private fun SectionHeading(title: String, subtitle: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Black,
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = subtitle,
        style = MaterialTheme.typography.bodyMedium,
        color = KonaetColorTokens.Muted,
    )
}

@Composable
private fun PoolCard(
    name: String,
    code: String,
    occupancy: Float,
    members: String,
) {
    KonaetGlassCard(
        modifier = Modifier.fillMaxWidth(),
        emphasized = true,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                )
                Text(
                    text = code,
                    style = MaterialTheme.typography.labelSmall,
                    color = KonaetColorTokens.Muted,
                )
            }
            KonaetStatusPill(text = "BAIXO RISCO")
        }
        Spacer(modifier = Modifier.height(18.dp))
        LinearProgressIndicator(
            progress = { occupancy },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = KonaetColorTokens.NeonPurple,
            trackColor = KonaetColorTokens.Surface3,
        )
        Spacer(modifier = Modifier.height(9.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(members, style = MaterialTheme.typography.bodySmall)
            Text(
                text = "${(occupancy * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = KonaetColorTokens.Lavender,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun EventCard(title: String, detail: String, time: String) {
    KonaetGlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(KonaetColorTokens.Violet.copy(alpha = 0.13f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = KonaetColorTokens.Violet,
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = detail,
                    style = MaterialTheme.typography.bodySmall,
                    color = KonaetColorTokens.Muted,
                )
            }
            Text(
                text = time,
                style = MaterialTheme.typography.labelSmall,
                color = KonaetColorTokens.Muted,
            )
        }
    }
}

@Composable
private fun ResultCard(label: String, value: String, modifier: Modifier = Modifier) {
    KonaetGlassCard(modifier = modifier.height(112.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = KonaetColorTokens.Muted,
            fontWeight = FontWeight.Black,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = KonaetColorTokens.Lavender,
            fontWeight = FontWeight.Black,
        )
    }
}

@Composable
private fun ProfileAction(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = KonaetColorTokens.Surface),
        shape = RoundedCornerShape(18.dp),
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = KonaetColorTokens.Violet,
                modifier = Modifier.size(26.dp),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 13.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = KonaetColorTokens.Muted,
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = KonaetColorTokens.Muted,
            )
        }
    }
}

@Composable
private fun konaetSliderColors() = SliderDefaults.colors(
    thumbColor = KonaetColorTokens.NeonPurple,
    activeTrackColor = KonaetColorTokens.Violet,
    inactiveTrackColor = KonaetColorTokens.Surface3,
    activeTickColor = KonaetColorTokens.Lavender,
    inactiveTickColor = KonaetColorTokens.Outline,
)

private fun formatPercent(value: Double): String =
    String.format(Locale("pt", "BR"), "%.2f%%", value * 100)
