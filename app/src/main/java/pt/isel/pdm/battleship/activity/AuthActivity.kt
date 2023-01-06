package pt.isel.pdm.battleship.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isel.pdm.battleship.DependenciesContainer
import pt.isel.pdm.battleship.screen.AuthScreen
import pt.isel.pdm.battleship.service.AuthType
import pt.isel.pdm.battleship.service.AuthViewModel
import java.lang.Exception

class AuthActivity : ComponentActivity() {

    private val service by lazy { (application as DependenciesContainer).authService }

    @Suppress("UNCHECKED_CAST")
    private val avm by viewModels<AuthViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(service) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val authType = avm.authType
            val loginUsername = avm.loginUsername
            val registerUsername = avm.registerUsername
            val loginErrorText = remember { mutableStateOf("") }
            val registerErrorText = remember { mutableStateOf("") }
            AuthScreen(
                authType = authType.value,
                loginUsername = loginUsername,
                registerUsername = registerUsername,
                onLoginTextUpdate = { field -> avm.setLoginText(field.text) },
                onRegisterTextUpdate = { field -> avm.setRegisterText(field.text) },
                onLoginRequested = { issueLogin() },
                onRegisterRequested = { issueRegistration() },
                loginErrorText = loginErrorText.value,
                registerErrorText = registerErrorText.value,
                changeAuthType = { newAuthType -> avm.changeAuth(newAuthType) }
            )
        }
    }

    private fun issueLogin() {
        try {
            avm.fetchLogin()
            navigateToMenuScreen()
        }
        catch (e: Exception) {
            Log.v("AuthActivity", "Error logging in: $e")
            // Launch Toast
        }
    }

    private fun issueRegistration() {
        try {
            avm.fetchRegister()
            navigateToMenuScreen()
        }
        catch (e: Exception) {
            Log.v("AuthActivity", "Error logging in: $e")
            // Launch Toast
        }
    }

    private fun navigateToMenuScreen() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }
}