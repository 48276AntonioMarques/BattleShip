package pt.isel.pdm.battleship.lobby

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.battleship.common.EXTRAS
import pt.isel.pdm.battleship.common.User
import pt.isel.pdm.battleship.common.toLocalUserDto

class LobbyActivity : ComponentActivity() {

    companion object {
        fun navigate(origin: Context, user: User, lobbyID: Int) {
            val intent = Intent(origin, LobbyActivity::class.java)
            intent.putExtra(EXTRAS.USER, user.toLocalUserDto())
            intent.putExtra(EXTRAS.LOBBY_ID, lobbyID)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LobbyScreen(
                onBackRequested = { finish() }
            )
        }
    }
}