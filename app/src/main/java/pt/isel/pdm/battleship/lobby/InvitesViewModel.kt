package pt.isel.pdm.battleship.lobby

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleship.R
import pt.isel.pdm.battleship.common.Invite
import pt.isel.pdm.battleship.common.User
import pt.isel.pdm.battleship.service.LobbyService
import java.lang.Exception

class InvitesViewModel(
    private val lobbyService: LobbyService
): ViewModel() {

    private val user = mutableStateOf<User?>(null)

    private val _invites = mutableStateOf<List<Invite>?>(null)
    val invites : State<List<Invite>?>
        get() = _invites

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean>
        get() = _isLoading

    fun setCredentials(user: User) {
        this.user.value = user
    }

    fun fetchInvites(context: Context) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _invites.value = lobbyService.getInvites(user.value!!)
            }
            catch (e: Exception) {
                Log.e("InvitesViewModel", "${e.message}")
                Toast.makeText(context, context.getString(R.string.app_invites_error), Toast.LENGTH_LONG).show()
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    fun accept(context: Context, inviteID: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                lobbyService.accept(user.value!!, inviteID)
            }
            catch (e: Exception) {
                Log.e("InvitesViewModel", "${e.message}")
                Toast.makeText(context, context.getString(R.string.app_invites_error), Toast.LENGTH_LONG).show()
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    fun dodge(context: Context, inviteID: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                lobbyService.dodge(user.value!!, inviteID)
            }
            catch (e: Exception) {
                Log.e("InvitesViewModel", "${e.message}")
                Toast.makeText(context, context.getString(R.string.app_invites_error), Toast.LENGTH_LONG).show()
            }
            finally {
                _isLoading.value = false
            }
        }
    }
}