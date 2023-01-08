package pt.isel.pdm.battleship.service

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel(
    private val service: AuthService
) : ViewModel() {

    private val _authType = mutableStateOf(AuthType.LOGIN)
    val authType: State<AuthType>
        get() = _authType

    fun changeAuth(newType: AuthType) {
        _authType.value = newType
    }

    fun fetchLogin(username: String) {
        viewModelScope.launch {
            val user = service.login(username)
            Log.v("AuthViewModel", user.toString())
        }
    }

    fun fetchRegister(username: String) {
        viewModelScope.launch {
            val user = service.register(username)
            Log.v("AuthViewModel", user.toString())
        }
    }
}