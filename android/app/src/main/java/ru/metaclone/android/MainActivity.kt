package ru.metaclone.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import ru.metaclone.android.ui.theme.MetaCloneTheme
import ru.metaclone.authorization.navgraph.AuthNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MetaCloneTheme {
                val navController = rememberNavController()
                AuthNavGraph(
                    navController = navController
                )
            }
        }
    }
}