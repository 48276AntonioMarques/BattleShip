package pt.isel.pdm.battleship.menu

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleship.R
import pt.isel.pdm.battleship.common.Leaderboard
import pt.isel.pdm.battleship.common.Lobby
import pt.isel.pdm.battleship.common.User
import pt.isel.pdm.battleship.service.LobbyService
import java.lang.Exception

class MenuViewModel(
    private val lobbyService: LobbyService
): ViewModel() {

    private val _lobby = mutableStateOf<Lobby?>(null)
    val lobby: State<Lobby?>
        get() = _lobby

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean>
        get() = _isLoading

    fun createLobby(context: Context, user: User, enemyName: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _lobby.value = lobbyService.createLobby(user, enemyName)
            }
            catch (e: Exception) {
                val message = context.getString(R.string.app_menu_error)
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    fun clearState() {
        _lobby.value = null
        _isLoading.value = false
    }
}