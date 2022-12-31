package pt.isel.pdm.battleship.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.battleship.DependenciesContainer
import pt.isel.pdm.battleship.service.AuthScreen

class AuthActivity : ComponentActivity() {

    private val service by lazy { (application as DependenciesContainer).authService }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthScreen()
        }
    }
}