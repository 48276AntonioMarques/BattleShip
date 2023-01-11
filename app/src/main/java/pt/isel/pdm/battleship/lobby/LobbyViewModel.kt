package pt.isel.pdm.battleship.lobby

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
import pt.isel.pdm.battleship.common.Lobby
import pt.isel.pdm.battleship.common.User
import pt.isel.pdm.battleship.service.LobbyService
import java.lang.Exception

private const val UPDATE_TIME = 30000 // 30 Seconds

class LobbyViewModel(
    private val lobbyService: LobbyService
): ViewModel() {

    private val _lobby = mutableStateOf<Lobby?>(null)
    val lobby: State<Lobby?>
        get() = _lobby

    private val _subscribed = mutableStateOf(false)

    private val _shouldLeave = mutableStateOf(false)
    val shouldLeave: State<Boolean>
        get() = _shouldLeave

    fun subscribeState(context: Context, user: User, lobbyID: Int) {
        viewModelScope.launch {
            _subscribed.value = true
            while(_subscribed.value) {
                try {
                    Log.v("LobbyViewModel", "Fetching lobby")
                    _lobby.value = lobbyService.getLobby(user, lobbyID)
                }
                catch (e: Exception) {
                    Log.e("LobbyViewModel", "fetchState: ${e.message}")
                    val message = context.getString(R.string.app_lobby_error)
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
                delay(UPDATE_TIME.toLong())
            }
        }
    }

    fun cancel(context: Context, user: User, lobbyID: Int) {
        viewModelScope.launch {
            try {
                lobbyService.cancel(user, lobbyID)
                _shouldLeave.value = true
            }
            catch (e: Exception) {
                val message = context.getString(R.string.app_lobby_error_cancel)
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun clearState() {
        _subscribed.value = false
        _lobby.value = null
        _shouldLeave.value = false
    }
}