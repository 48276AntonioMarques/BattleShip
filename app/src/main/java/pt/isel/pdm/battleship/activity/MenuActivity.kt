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
import pt.isel.pdm.battleship.screen.MenuScreen
import pt.isel.pdm.battleship.service.AuthViewModel
import java.lang.Exception

class MenuActivity : ComponentActivity() {

    private val _as by lazy { (application as DependenciesContainer).authService }

    @Suppress("UNCHECKED_CAST")
    private val avm by viewModels<AuthViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(_as) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val user = avm.user.value
            Log.v("Menu Activity", "$user")
            val feedbackText = remember {
                mutableStateOf("")
            }
            MenuScreen(
                onRankingRequested = { navigateToRankingScreen() },
                onAuthorRequested = { navigateToAuthorScreen() },
                onAuthRequested = { navigateToAuthScreen() },
                onInvitesRequested = { navigateToInvitesScreen()},
                onChallengeRequested = { createChallenge(feedbackText) },
                user,
                (1..99).random(),
                feedbackText.value
            )
        }
    }

    class LobbyCreationException(override val message: String?): Exception()

    /**
     * This function is meant create a lobby validate its creation
     * and redirect user into LobbyActivity with context
     * @throws LobbyCreationException to be caught on MenuScreen
     */
    private fun createChallenge(feedbackText: MutableState<String>) {
        try {
            //TODO: try to create lobby using the ¿service?
            navigateToLobbyScreen()
        }
        catch (e: LobbyCreationException) {
            feedbackText.value = e.message?: "Try again."
        }
    }

    private fun navigateToRankingScreen() {
        val intent = Intent(this, RankingActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToAuthorScreen() {
        val intent = Intent(this, AuthorActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToAuthScreen() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToInvitesScreen() {
        val intent = Intent(this, InvitesActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToLobbyScreen() {
        val intent = Intent(this, InvitesActivity::class.java)
        startActivity(intent)
    }
}
