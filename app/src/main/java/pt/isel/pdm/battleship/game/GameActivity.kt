package pt.isel.pdm.battleship.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.isel.pdm.battleship.common.EXTRAS
import pt.isel.pdm.battleship.common.User
import pt.isel.pdm.battleship.common.toLocalUserDto
import pt.isel.pdm.battleship.lobby.LobbyActivity

class GameActivity : ComponentActivity() {

    companion object {
        fun navigate(origin: Context, user: User, gameID: Int) {
            val intent = Intent(origin, LobbyActivity::class.java)
            intent.putExtra(EXTRAS.USER, user.toLocalUserDto())
            intent.putExtra(EXTRAS.GAME_ID, gameID)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameScreen()
        }
    }
}