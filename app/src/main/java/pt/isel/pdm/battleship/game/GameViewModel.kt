package pt.isel.pdm.battleship.game

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.isel.pdm.battleship.R
import pt.isel.pdm.battleship.common.User
import pt.isel.pdm.battleship.service.Game
import pt.isel.pdm.battleship.service.GameService

private const val UPDATE_TIME = 30_000 // 30 Seconds

class GameViewModel(
    private val gameService: GameService
): ViewModel() {

    private val _subscribed = mutableStateOf(false)

    private val _game = mutableStateOf<Game?>(null)
    val game: State<Game?>
        get() = _game

    fun subscribeState(context: Context, user: User, gameID: Int) {
        viewModelScope.launch {
        _subscribed.value = true
            while (_subscribed.value) {
                try {
                    _game.value = gameService.getState()
                }
                catch (e: Exception) {
                    Log.v("GameViewModel", "${e.message}")
                    val message = context.getString(R.string.app_game_error)
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
                delay(UPDATE_TIME.toLong())
            }
        }
    }
}