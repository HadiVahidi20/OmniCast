package com.hadify.omnicast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.hadify.omnicast.core.navigation.NavigationManager
import com.hadify.omnicast.core.navigation.OmniCastNavGraph
import com.hadify.omnicast.core.ui.OmniCastTheme
import com.hadify.omnicast.core.ui.theme.ThemeManager
import com.hadify.omnicast.home.HomeScreen
import com.hadify.omnicast.feature.zodiac.ui.ZodiacScreen
import com.hadify.omnicast.feature.profile.ui.screen.ProfileScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationManager: NavigationManager

    @Inject
    lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            OmniCastApp(
                navigationManager = navigationManager,
                themeManager = themeManager
            )
        }
    }
}

@Composable
fun OmniCastApp(
    navigationManager: NavigationManager,
    themeManager: ThemeManager
) {
    // Apply theme with proper system detection
    OmniCastTheme(themeManager = themeManager) {
        val navController = rememberNavController()

        // Now we provide the actual screen composables to the navigation graph
        // This resolves circular dependency by moving screen composition to app level
        OmniCastNavGraph(
            navController = navController,
            navigationManager = navigationManager,
            homeScreen = {
                HomeScreen()
            },
            zodiacScreen = {
                // ✅ REAL ZODIAC SCREEN - This is the exciting part!
                ZodiacScreen(
                    onNavigateBack = { navigationManager.navigateBack() },
                    onNavigateToProfile = { navigationManager.navigateToProfile() }
                )
            },
            profileScreen = {
                ProfileScreen(
                    onNavigateBack = { navigationManager.navigateBack() }
                )
            }
        )
    }
}