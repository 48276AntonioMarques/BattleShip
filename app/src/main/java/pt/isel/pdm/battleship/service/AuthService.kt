package pt.isel.pdm.battleship.service

import com.google.gson.Gson
import android.util.Log
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.delay
import java.net.URL
import okhttp3.*
import pt.isel.pdm.battleship.common.*
import java.io.IOException

data class User(val name: String, val token: String)
fun UserDto.toUser(): User {
    return User(this.properties!!.name, this.properties.token)
}

enum class AuthType{ LOGIN, REGISTER }

interface AuthService {
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

    @Throws(IOException::class, UnexpectedResponseTypeException::class)
    override suspend fun register(username: String): User =
        auth(
            requestBuilder = {
                createPost(
                    url = registerURL.toExternalForm(),
                    body = "{\"name\":\"${username}\"}",
                    mediaType = JsonMediaType
                )
            }
        )

    @Throws(IOException::class, UnexpectedResponseTypeException::class)
    override suspend fun login(username: String): User =
        auth(
            requestBuilder = {
                createPost(
                    url = loginURL.toExternalForm(),
                    body = "{\"name\":\"${username}\"}",
                    mediaType = JsonMediaType
                )
            }
        )

    @Throws(IOException::class, UnexpectedResponseTypeException::class)
    private suspend fun auth(requestBuilder: Request.Builder.() -> Unit) : User =
        api(
            client,
            requestBuilder = requestBuilder,
            onResponse = { response ->
                validateResponse(response)?: throw UnexpectedResponseTypeException("Invalid Response")
                try {
                    val body = response.body?.string()
                    return@api jsonFormatter.fromJson<UserDto>(
                        body,
                        UserDtoType.type
                    ).toUser()
                } catch (e: JsonSyntaxException) {
                    throw UnexpectedResponseTypeException("Generating JSON: ${e.message}")
                }
            }
        )

    override suspend fun logout() {
        Log.v("AuthService", "Thread ${Thread.currentThread().name}: logging out")
    }
}

class FakeAuthService : AuthService {

    override suspend fun register(username: String): User {
        val newUser = User(name = username, token = "token abc1234")
        delay(timeMillis = 1000)
        return newUser
    }

    override suspend fun login(username: String): User {
        Log.v("AuthService", "Making fake request to login")
        val newUser = User(name = username, token = "token abc1234")
        delay(timeMillis = 1000)
        Log.v("AuthService", "returning fake request to login")
        return newUser
    }

    override suspend fun logout() {
        Log.v("AuthService", "Thread ${Thread.currentThread().name}: logging out")
    }
}