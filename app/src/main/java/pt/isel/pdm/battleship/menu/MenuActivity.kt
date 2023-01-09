package pt.isel.pdm.battleship.menu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import pt.isel.pdm.battleship.auth.AuthActivity
import pt.isel.pdm.battleship.DependenciesContainer
import pt.isel.pdm.battleship.common.*
import pt.isel.pdm.battleship.lobby.InvitesActivity
import pt.isel.pdm.battleship.lobby.LobbyActivity
import java.lang.Exception

class MenuActivity : KotlinActivity() {

    private val dc by lazy { (application as DependenciesContainer) }
    private val mvm by viewModels { MenuViewModel(dc.generalService) }

    companion object {
        val user = mutableStateOf<User?>(null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val receiver = rememberSaveable { mutableStateOf("") }
            MenuScreen(
                onRankingRequested = { navigateToRankingScreen() },
                onAuthorRequested = { navigateToAuthorScreen() },
                onAuthRequested = { navigateToAuthScreen() },
                onInvitesRequested = { navigateToInvitesScreen(user.value)},
                onChallengeRequested = { lobbyID -> createChallenge(user.value, lobbyID) },
                onChallengeUpdate = { field -> receiver.value = field.text },
                receiver = receiver.value,
                user.value,
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
    private fun createChallenge(user: User?, receiverName: String) {
        try {
            val lobbyID = (1..10).random()
            //TODO: try to create lobby using the ¿service?
            navigateToLobbyScreen(user, lobbyID)
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

    private fun navigateToInvitesScreen(user: User?) {
        if (user != null) {
            InvitesActivity.navigate(this, user)
        }
    }

    private fun navigateToLobbyScreen(user: User?, lobbyID: Int) {
        if (user != null) {
            LobbyActivity.navigate(this, user, lobbyID)
        }
    }
}
