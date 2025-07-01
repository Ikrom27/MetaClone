package ru.metaclone.authorization.navgraph

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ru.metaclone.authorization.ui.LoginScreen
import ru.metaclone.authorization.ui.RegistrationScreen

@Composable
fun AuthNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = "LOGIN_ROUTE"
    ) {
        composable(
            "LOGIN_ROUTE",
            enterTransition = {
                scaleIn(initialScale = 0.92f) + fadeIn()
            },
            exitTransition = {
                fadeOut()
            }
        ) {
            LoginScreen(
                onRegisterNavigate = { defaultLogin ->
                    navController.navigate(route = RegisterRoute(defaultLogin = defaultLogin))
                }
            )
        }
        composable<RegisterRoute> { backStackEntry ->
            val registerRoute = backStackEntry.toRoute<RegisterRoute>()
            RegistrationScreen(
                defaultLoginValue = registerRoute.defaultLogin,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}