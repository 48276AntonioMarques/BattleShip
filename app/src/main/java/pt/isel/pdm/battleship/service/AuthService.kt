package pt.isel.pdm.battleship.service

import com.google.gson.Gson
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.delay
import java.net.URL
import okhttp3.*
import pt.isel.pdm.battleship.common.JsonMediaType
import pt.isel.pdm.battleship.common.api
import pt.isel.pdm.battleship.common.createPost
import pt.isel.pdm.battleship.common.validateResponse
import java.lang.Exception
import kotlin.coroutines.resumeWithException

data class User(val name: String, val token: String)
fun UserDto.toUser(): User {
    Log.e("AuthService", "user response: $this")
    this.properties?: throw Exception()
    return User(this.properties.name, this.properties.token)
}

enum class AuthType{ LOGIN, REGISTER }

interface AuthService {
    val user: User?
    suspend fun register(username: String): User
    suspend fun login(username: String): User
    suspend fun logout()
}

class RealAuthService(
    private val client: OkHttpClient,
    private val jsonFormatter: Gson,
    private val loginURL: URL,
    private val registerURL: URL
) : AuthService {

    private val _user = mutableStateOf<User?>(null)
    override val user : User?
        get() = _user.value

    override suspend fun register(username: String): User =
        api(
            client,
            requestBuilder =  {
                createPost(
                    url = registerURL.toExternalForm(),
                    body = "{\"name\":\"${username}\"}",
                    mediaType = JsonMediaType
                )
            },
            onResponse = { _, response ->
                validateResponse(response)?: run {
                    val e = Exception("response body is invalid")
                    resumeWithException(e)
                    throw Exception(e)
                }
                Log.v("AuthService", response.body?.string()?: "<empty>")
                try {
                    jsonFormatter.fromJson<UserDto>(
                        response.body?.string(),
                        UserDtoType.type
                    ).toUser()
                }
                catch (e: Exception) {
                    Log.e("AuthService",e.toString())
                    throw e
                }
            }
        )

    override suspend fun login(username: String): User =
        api (
            client,
            requestBuilder = {
                createPost(
                    url = loginURL.toExternalForm(),
                    body = "{\"name\":\"${username}\"}",
                    mediaType = JsonMediaType
                )
            },
            onResponse = { _, response ->
                validateResponse(response)?: run {
                    val e = Exception("response body is invalid")
                    resumeWithException(e)
                    throw Exception(e)
                }
                val body = response.body?.string()
                Log.v("AuthService", body?: "<empty>")
                try {
                    jsonFormatter.fromJson<UserDto>(
                        body,
                        UserDtoType.type
                    ).toUser()
                }
                catch (e: Exception) {
                    Log.e("AuthService",e.toString())
                    throw e
                }
            }
        )

    override suspend fun logout() {
        Log.v("AuthService", "Thread ${Thread.currentThread().name}: logging out")
        _user.value = null
    }
}

class FakeAuthService : AuthService {

    private val _user = mutableStateOf<User?>(null)
    override val user: User?
        get() = _user.value

    override suspend fun register(username: String): User {
        val newUser = User(name = username, token = "token abc1234")
        delay(timeMillis = 1000)
        _user.value = newUser
        return newUser
    }

    override suspend fun login(username: String): User {
        Log.v("AuthService", "Making fake request to login")
        val newUser = User(name = username, token = "token abc1234")
        delay(timeMillis = 1000)
        Log.v("AuthService", "returning fake request to login")
        _user.value = newUser
        return newUser
    }

    override suspend fun logout() {
        Log.v("AuthService", "Thread ${Thread.currentThread().name}: logging out")
        _user.value = null
    }
}