package pt.isel.pdm.battleship.auth

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import pt.isel.pdm.battleship.DependenciesContainer
import pt.isel.pdm.battleship.common.KotlinActivity
import pt.isel.pdm.battleship.screen.AuthScreen
import pt.isel.pdm.battleship.service.AuthType
import pt.isel.pdm.battleship.menu.MenuActivity

class AuthActivity : KotlinActivity() {

    private val aus by lazy { (application as DependenciesContainer).authService }
    private val avm by viewModels { AuthViewModel(aus) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (avm.user.value != null) {
                // There is a user logged in so this activity isn't meant to be on display no matter what
                MenuActivity.user.value = avm.user.value
                finish()
            }

            val username = rememberSaveable { mutableStateOf("") }
            val authType = rememberSaveable { mutableStateOf(AuthType.LOGIN) }

            AuthScreen(
                authType = authType.value,
                username = username.value,
                onLoginTextUpdate = { field -> username.value = field.text },
                onRegisterTextUpdate = { field -> username.value = field.text },
                onLoginRequested = { avm.fetchLogin(this, username.value) },
                onRegisterRequested = { avm.fetchRegister(this, username.value) },
                changeAuthType = { newAuthType -> authType.value = newAuthType },
                avm.isAuthenticating.value
            )
        }
    }
}