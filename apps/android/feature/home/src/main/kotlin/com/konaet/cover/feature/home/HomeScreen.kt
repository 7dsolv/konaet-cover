package com.konaet.cover.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.konaet.cover.core.designsystem.tokens.KonaetColorTokens
import com.konaet.cover.core.designsystem.tokens.KonaetSpacing

@Composable
fun HomeScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = KonaetColorTokens.Surface,
                modifier = Modifier.height(80.dp)
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Pool") },
                    label = { Text("Pool") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Claims") },
                    label = { Text("Claims") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Science, contentDescription = "Lab") },
                    label = { Text("Lab") },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 }
                )
            }
        },
        containerColor = KonaetColorTokens.Obsidian
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(KonaetColorTokens.Obsidian)
        ) {
            when (selectedTab) {
                0 -> HomeTabScreen()
                1 -> PoolTabScreen()
                2 -> ClaimsTabScreen()
                3 -> LabTabScreen()
                4 -> ProfileTabScreen()
            }
        }
    }
}

@Composable
fun HomeTabScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(KonaetSpacing.Large.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Protection Status",
            fontSize = 24.sp,
            color = KonaetColorTokens.Text,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = KonaetSpacing.Large.dp)
        )

        // Protection Ring (circle visual)
        Box(
            modifier = Modifier
                .size(150.dp)
                .background(
                    color = KonaetColorTokens.Surface,
                    shape = androidx.compose.foundation.shape.CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "✓",
                fontSize = 64.sp,
                color = KonaetColorTokens.Verified
            )
        }

        Spacer(modifier = Modifier.height(KonaetSpacing.Large.dp * 2))

        Text(
            text = "Fully Protected",
            fontSize = 20.sp,
            color = KonaetColorTokens.Verified
        )

        Spacer(modifier = Modifier.height(KonaetSpacing.Large.dp))

        Text(
            text = "No active incidents",
            fontSize = 14.sp,
            color = KonaetColorTokens.Muted
        )
    }
}

@Composable
fun PoolTabScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(KonaetSpacing.Large.dp)
    ) {
        Text(
            text = "Protection Pools",
            fontSize = 24.sp,
            color = KonaetColorTokens.Text,
            modifier = Modifier.padding(bottom = KonaetSpacing.Large.dp)
        )

        repeat(2) {
            PoolCard("Demo Pool ${it + 1}", "Low Risk")
            Spacer(modifier = Modifier.height(KonaetSpacing.Large.dp))
        }
    }
}

@Composable
fun ClaimsTabScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(KonaetSpacing.Large.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No active claims",
            fontSize = 16.sp,
            color = KonaetColorTokens.Muted
        )
    }
}

@Composable
fun LabTabScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(KonaetSpacing.Large.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Risk Lab - Coming soon",
            fontSize = 16.sp,
            color = KonaetColorTokens.Muted
        )
    }
}

@Composable
fun ProfileTabScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(KonaetSpacing.Large.dp)
    ) {
        Text(
            text = "User Profile",
            fontSize = 24.sp,
            color = KonaetColorTokens.Text
        )

        Spacer(modifier = Modifier.height(KonaetSpacing.Large.dp * 2))

        ProfileMenuItem("Account Settings")
        ProfileMenuItem("Privacy")
        ProfileMenuItem("Delete Account")
        ProfileMenuItem("Logout")
    }
}

@Composable
fun PoolCard(name: String, riskLevel: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = KonaetSpacing.Small.dp),
        colors = CardDefaults.cardColors(
            containerColor = KonaetColorTokens.Surface
        )
    ) {
        Column(modifier = Modifier.padding(KonaetSpacing.Large.dp)) {
            Text(name, color = KonaetColorTokens.Text, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(KonaetSpacing.Small.dp))
            Text(riskLevel, color = KonaetColorTokens.Muted, fontSize = 12.sp)
        }
    }
}

@Composable
fun ProfileMenuItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = KonaetSpacing.Medium.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, color = KonaetColorTokens.Text, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = KonaetColorTokens.Muted)
    }
}
