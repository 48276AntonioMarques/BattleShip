package pt.isel.pdm.battleship.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import pt.isel.pdm.battleship.DependenciesContainer
import pt.isel.pdm.battleship.common.*
import pt.isel.pdm.battleship.lobby.LobbyActivity

class GameActivity : KotlinActivity() {

    private val gs by lazy { (application as DependenciesContainer).gameService }
    private val gvm by viewModels { GameViewModel(gs) }

    companion object {
        fun navigate(origin: Context, user: User, gameID: Int) {
            val intent = Intent(origin, GameActivity::class.java)
            intent.putExtra(EXTRAS.USER, user.toLocalUserDto())
            intent.putExtra(EXTRAS.GAME_ID, gameID)
            origin.startActivity(intent)
        }
    }

    private val user: User?
        get() = intent.getParcelableExtra<LocalUserDto>(EXTRAS.USER)?.toUser()

    private val gameID: Int
        get() = intent.getIntExtra(EXTRAS.GAME_ID, -1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.v("GameActivity", "onCreate")
        if (user == null || gameID < 0) {
            finish()
            return
        }
        Log.v("GameActivity", "${user?.name} on game $gameID")

        if (gvm.game.value == null) {
            gvm.subscribeState(this, user!!, gameID)
        }

        setContent {
            val game = gvm.game.value
            GameScreen(
                game
            )
        }
    }
}