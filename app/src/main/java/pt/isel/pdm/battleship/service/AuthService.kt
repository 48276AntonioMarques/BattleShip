package pt.isel.pdm.battleship.service

import com.google.gson.Gson
import android.util.Log
import kotlinx.coroutines.delay
import java.net.URL
import kotlin.coroutines.suspendCoroutine
import okhttp3.*
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

data class Param (val name: String, val type: String)

data class User(val name: String, val token: String)

interface AuthService {
    suspend fun getLoginParams(): List<Param>
    suspend fun login(params: List<Param>): User
}

class RealAuthService(
    private val client: OkHttpClient,
    private val jsonFormatter: Gson,
    private val authURL: URL
) : AuthService {
    override suspend fun getLoginParams(): List<Param> {

        val result = suspendCoroutine<List<Param>> { continuation ->
            val request = Request.Builder()
                .url(authURL)
                .build()
            Log.v("AuthService", "Thread ${Thread.currentThread().name}: waiting for logins params")
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.v("AuthService", "Thread ${Thread.currentThread().name}: responding to logins params")
                    val contentType = response.body?.contentType()
                    if (response.isSuccessful && contentType != null && contentType == SirenMediaType) {
                        val quoteDto = jsonFormatter.fromJson<UserDto>(
                            response.body?.string(),
                            UserDtoType.type
                        )
                        continuation.resume(quoteDto.toQuote())
                    }
                    else {
                        TODO()
                    }
                }
            })
        }
        return result
    }

    override suspend fun login(params: List<Param>): User {
        TODO("Not yet implemented")
    }
}

class FakeAuthService : AuthService {
    override suspend fun getLoginParams(): List<Param> {
        val params = listOf(
            Param("username", "string"),
                Param("password", "string")
        )
        delay(timeMillis = 1000)
        return params
    }

    override suspend fun login(params: List<Param>): User {
        val user = User(name = "", token = "token abc1234")
        delay(timeMillis = 1000)
        return user
    }
}