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
    private val mvm by viewModels { MenuViewModel(dc.lobbyService) }

    companion object {
        val user = mutableStateOf<User?>(null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val lobbyID = mvm.lobby.value?.id
            val user = user.value
            if (lobbyID != null && user != null) {
                mvm.clearState()
                LobbyActivity.navigate(this, user, lobbyID)
            }
            val receiver = rememberSaveable { mutableStateOf("") }
            MenuScreen(
                onRankingRequested = { RankingActivity.navigate(this) },
                onAuthorRequested = { AuthorActivity.navigate(this) },
                onAuthRequested = { AuthActivity.navigate(this) },
                onInvitesRequested = { InvitesActivity.navigate(this, user)},
                onChallengeRequested = { id -> createChallenge(user, id) },
                onChallengeUpdate = { field -> receiver.value = field.text },
                receiver = receiver.value,
                user,
                onLogoutRequested = { MenuActivity.user.value = null }
            )
        }
    }

    private fun createChallenge(user: User?, receiverName: String) {
        if (user != null) {
            mvm.createLobby(this, user, receiverName)
        }
    }
}
