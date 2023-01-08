package pt.isel.pdm.battleship.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import pt.isel.pdm.battleship.DependenciesContainer
import pt.isel.pdm.battleship.common.KotlinActivity
import pt.isel.pdm.battleship.screen.AuthScreen
import pt.isel.pdm.battleship.service.AuthViewModel

class AuthActivity : KotlinActivity() {

    private val aus by lazy { (application as DependenciesContainer).authService }
    private val avm by viewModels { AuthViewModel(aus) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.v("AuthActivity", "user is null: ${aus.user == null}")
        if (aus.user != null) {
            // There is a user logged in soo this page isn't meant to be on display no matter what
            Log.v("AuthActivity", "finishing activity requested")
            finish()
        }

        setContent {
            val username = rememberSaveable { mutableStateOf("") }
            AuthScreen(
                authType = avm.authType.value,
                loginUsername = username.value,
                registerUsername = username.value,
                onLoginTextUpdate = { field -> username.value = field.text },
                onRegisterTextUpdate = { field -> username.value = field.text },
                onLoginRequested = {
                    Log.v("AuthActivity", "Fetching Login")
                    avm.fetchLogin(username.value)
               },
                onRegisterRequested = { avm.fetchRegister(username.value) },
                changeAuthType = { newAuthType -> avm.changeAuth(newAuthType) }
            )
        }
    }
}