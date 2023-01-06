package pt.isel.pdm.battleship.service

import com.google.gson.Gson
import android.util.Log
import kotlinx.coroutines.delay
import java.net.URL
import kotlin.coroutines.suspendCoroutine
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

data class User(val name: String, val token: String)
fun UserDto.toUser(): User {
    this.properties?: throw Exception()
    return User(this.properties.name, this.properties.token)
}

enum class AuthType{ LOGIN, REGISTER }

interface AuthService {
    suspend fun login(username: String): User
    suspend fun register(username: String): User
}

class RealAuthService(
    private val client: OkHttpClient,
    private val jsonFormatter: Gson,
    private val authURL: URL
) : AuthService {
    override suspend fun login(username: String): User {
        return suspendCoroutine { continuation ->
            val request = Request.Builder()
                .url(authURL)
                .method("POST", "{\"name\":\"$username\"}".toRequestBody("application/json".toMediaType()))
                .build()
            Log.v("AuthService", "Thread ${Thread.currentThread().name}: logging in")
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.v("AuthService", "Thread ${Thread.currentThread().name}: parsing response")
                    val contentType = response.body?.contentType()
                    if (response.isSuccessful && contentType != null && contentType == SirenMediaType) {
                        val userDto = jsonFormatter.fromJson<UserDto>(
                            response.body?.string(),
                            UserDtoType.type
                        )
                        continuation.resume(userDto.toUser())
                    }
                    else {
                        continuation.resumeWithException(Exception("User failed to login"))
                        TODO("Display error message when login fails")
                    }
                }
            })
        }
    }

    override suspend fun register(username: String): User {
        return suspendCoroutine { continuation ->
            val request = Request.Builder()
                .url(authURL)
                .method("POST", "{\"name\":\"$username\"}".toRequestBody("application/json".toMediaType()))
                .build()
            Log.v("AuthService", "Thread ${Thread.currentThread().name}: registering")
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.v("AuthService", "Thread ${Thread.currentThread().name}: parsing registry")
                    val contentType = response.body?.contentType()
                    if (response.isSuccessful && contentType != null && contentType == SirenMediaType) {
                        val userDto = jsonFormatter.fromJson<UserDto>(
                            response.body?.string(),
                            UserDtoType.type
                        )
                        continuation.resume(userDto.toUser())
                    }
                    else {
                        continuation.resumeWithException(Exception("User failed to get registered"))
                        TODO("Display error message when login fails")
                    }
                }
            })
        }
    }
}

class FakeAuthService : AuthService {
    override suspend fun login(username: String): User {
        val newUser = User(name = username, token = "token abc1234")
        delay(timeMillis = 1000)
        return newUser
    }

    override suspend fun register(username: String): User {
        val newUser = User(name = username, token = "token abc1234")
        delay(timeMillis = 1000)
        return newUser
    }
}