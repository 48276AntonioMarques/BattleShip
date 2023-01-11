package pt.isel.pdm.battleship.lobby

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import pt.isel.pdm.battleship.DependenciesContainer
import pt.isel.pdm.battleship.common.*
import pt.isel.pdm.battleship.game.GameActivity

class LobbyActivity : KotlinActivity() {

    private val ls by lazy { (application as DependenciesContainer).lobbyService }
    private val lvm by viewModels { LobbyViewModel(ls) }

    companion object {
        fun navigate(origin: Context, user: User, lobbyID: Int) {
            val intent = Intent(origin, LobbyActivity::class.java)
            intent.putExtra(EXTRAS.USER, user.toLocalUserDto())
            intent.putExtra(EXTRAS.LOBBY_ID, lobbyID)
            origin.startActivity(intent)
        }
    }

    private val user: User?
        get() = intent.getParcelableExtra<LocalUserDto>(EXTRAS.USER)?.toUser()

    private val lobbyID: Int
        get() = intent.getIntExtra(EXTRAS.LOBBY_ID, -1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (user == null || lobbyID < 0) {
            finish()
            return
        }

        if (lvm.lobby.value == null) {
            lvm.subscribeState(this, user!!, lobbyID)
        }

        setContent {
            if (lvm.lobby.value != null && lvm.lobby.value!!.isGoing()) {
                Log.v("LobbyActivity", "Loading Game")
                lvm.clearState()
                GameActivity.navigate(this, user!!, lobbyID)
            }
            val canGoBack = lvm.lobby.value != null && lvm.lobby.value!!.isFinished()
            if (lvm.shouldLeave.value) { finish() }
            val lobby = lvm.lobby
            val isPlayer1 = user!!.name == lobby.value?.user1
            LobbyScreen(
                onBackRequested = { finish() },
                lobby.value,
                isPlayer1,
                canGoBack = canGoBack,
                onCancelRequested = { cancel(user!!, isPlayer1) }
            )
        }
    }

    private fun cancel(user: User, isPlayer1: Boolean) {
        if (isPlayer1) {
            lvm.cancel(this, user, lobbyID)
        }
    }

    private fun Lobby.isGoing() =
        when (state) {
            LobbyState.FLOATING -> true
            LobbyState.USER1_TURN -> true
            LobbyState.USER2_TURN -> true
            else -> false
        }

    private fun Lobby.isFinished() =
        when (state) {
            LobbyState.WON -> true
            LobbyState.LOST -> true
            LobbyState.CANCELLED -> true
            LobbyState.DODGED -> true
            else -> false
        }
}