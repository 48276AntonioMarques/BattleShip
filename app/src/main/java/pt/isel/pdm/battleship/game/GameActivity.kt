package pt.isel.pdm.battleship.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import pt.isel.pdm.battleship.DependenciesContainer
import pt.isel.pdm.battleship.common.*
import pt.isel.pdm.battleship.game.compose.CellState
import pt.isel.pdm.battleship.lobby.LobbyActivity
import pt.isel.pdm.battleship.service.*

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

        val shipsList = ShipType.values().toList()
        setContent {
            val game = gvm.game.value
            val shipsPlacedCounter = rememberSaveable { mutableStateOf(0) }
            val shipToPlace = if (shipsPlacedCounter.value < shipsList.size)
                Ship(
                    Coordinate(0,0),
                    Direction.VERTICAL,
                    shipsList[shipsPlacedCounter.value]
                )
            else null
            val shipDirection = remember { mutableStateOf(Direction.VERTICAL) }
            val fleet = remember { mutableStateOf(Fleet(emptyList(), emptyList())) }
            GameScreen(
                game = game,
                shipToPlace = shipToPlace,
                rotate = { dir -> shipDirection.value = dir },
                fleet = fleet.value,
                onCellClick = { location, state ->
                    Log.v("GameActivity", "Cell clicked")
                    if (shipToPlace != null) {
                        if (state == CellState.WATER) {
                            val fv = fleet.value
                            val ship = Ship(location, shipDirection.value, shipToPlace.type)
                            fleet.value = Fleet(fv.ships + ship, fv.shots)
                            shipsPlacedCounter.value++
                        }
                    }
                }
            )
        }
    }
}