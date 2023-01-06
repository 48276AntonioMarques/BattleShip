package pt.isel.pdm.battleship.service

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

    private val _user: MutableState<User?> = mutableStateOf(null)
    val user: State<User?>
        get() = _user

    private val _authType = mutableStateOf(AuthType.LOGIN)
    val authType: State<AuthType>
        get() = _authType

    private val _loginUsername = mutableStateOf("")
    val loginUsername: String
        get() = _loginUsername.value

    private val _registerUsername = mutableStateOf("")
    val registerUsername: String
        get() = _registerUsername.value

    private val _loginErrorText = mutableStateOf("")
    val loginError: State<String>
        get() = _loginErrorText

    private val _registerErrorText = mutableStateOf("")
    val registerErrorText: State<String>
        get() = _registerErrorText

    fun changeAuth(newType: AuthType) {
        _authType.value = newType
    }

    fun setLoginText(text: String) {
        _loginUsername.value = text
    }

    fun setRegisterText(text: String) {
        _registerUsername.value = text
    }

    fun fetchLogin() {
        viewModelScope.launch {
            _user.value = service.login(loginUsername)
        }
    }

    fun fetchRegister() {
        viewModelScope.launch {
            _user.value = service.register(registerUsername)
        }
    }
}