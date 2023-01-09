package pt.isel.pdm.battleship.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import pt.isel.pdm.battleship.DependenciesContainer
import pt.isel.pdm.battleship.common.KotlinActivity
import pt.isel.pdm.battleship.screen.MenuScreen
import pt.isel.pdm.battleship.service.AuthViewModel
import pt.isel.pdm.battleship.service.User
import java.lang.Exception

class MenuActivity : KotlinActivity() {

    private val aus by lazy { (application as DependenciesContainer).authService }
    private val avm by viewModels { AuthViewModel(aus) }

    companion object {
        val user = mutableStateOf<User?>(null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val user = user.value
            Log.v("Menu Activity", "mUser: $user")
            MenuScreen(
                onRankingRequested = { navigateToRankingScreen() },
                onAuthorRequested = { navigateToAuthorScreen() },
                onAuthRequested = { navigateToAuthScreen() },
                onInvitesRequested = { navigateToInvitesScreen()},
                onChallengeRequested = { createChallenge() },
                user,
                (1..99).random()
            )
        }
    }

    class LobbyCreationException(override val message: String?): Exception()

    /**
     * This function is meant create a lobby validate its creation
     * and redirect user into LobbyActivity with context
     * @throws LobbyCreationException to be caught on MenuScreen
     */
    private fun createChallenge() {
        try {
            //TODO: try to create lobby using the ¿service?
            navigateToLobbyScreen()
        }
        catch (e: LobbyCreationException) {
            Toast.makeText(this, "Try again.", Toast.LENGTH_LONG).show()
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
