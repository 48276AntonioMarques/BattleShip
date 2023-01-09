package pt.isel.pdm.battleship.service

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.battleship.R
import pt.isel.pdm.battleship.common.UnexpectedResponseTypeException
import java.io.IOException
import java.lang.Exception
import java.lang.NullPointerException

class AuthViewModel(
    private val service: AuthService
) : ViewModel() {

    private val _user = mutableStateOf<User?>(null)
    val user : State<User?>
        get() = _user

    private val _isAuthing = mutableStateOf(false)
    val isAuthenticating : State<Boolean>
        get() = _isAuthing

    fun fetchLogin(context: Context, username: String) {
        viewModelScope.launch {
            try {
                _isAuthing.value = true
                _user.value = service.login(username)
            }
            catch (e: Exception) {
                context.handleFailure(
                    e,
                    "${context.getString(R.string.app_auth_login_failed)}."
                )
            }
            finally {
                _isAuthing.value = false
            }
        }
    }

    fun fetchRegister(context: Context, username: String) {
        viewModelScope.launch {
            try {
                _isAuthing.value = true
                _user.value = service.register(username)
            }
            catch (e: Exception) {
                context.handleFailure(
                    e,
                    "${context.getString(R.string.app_auth_register_failed)}."
                )
            }
            finally {
                _isAuthing.value = false
            }
        }
    }

    fun fetchLogout(context: Context) {
        viewModelScope.launch {
            try {
                service.logout()
            }
            catch (e: Exception) {
                context.handleFailure(
                    e,
                    "${context.getString(R.string.app_auth_logout_failed)}."
                )
            }
        }
    }

    private fun Context.handleFailure(e: Exception, defaultMessage: String) {
        try { throw e }
        catch (e: IOException) {
            Log.v("AuthViewModel", "IOException: ${e.message}")
            val message = "${getString(R.string.app_auth_io_fail)}!"
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
        catch (e: UnexpectedResponseTypeException) {
            Log.v("AuthViewModel", "UnexpectedResponseTypeException: ${e.message}")
            val message = "${getString(R.string.app_auth_unexpected_response_type)}!"
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
        catch (e: NullPointerException) {
            Log.v("AuthViewModel", "NullPointerException: ${e.message}")
            val message = "${getString(R.string.app_auth_null_pointer)}!"
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
        catch (e: Exception) {
            Log.v("AuthViewModel", "Exception: ${e.message}")
            Toast.makeText(this, defaultMessage, Toast.LENGTH_LONG).show()
        }
    }
}