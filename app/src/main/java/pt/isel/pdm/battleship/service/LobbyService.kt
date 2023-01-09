package pt.isel.pdm.battleship.service

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import pt.isel.pdm.battleship.common.*
import java.io.IOException
import java.net.URL

interface LobbyService {
    suspend fun getInvites(user: User): List<Invite>
    suspend fun accept(user: User, inviteID: Int): Boolean
    suspend fun dodge(user: User, inviteID: Int)
}

class RealLobbyService(
    private val client: OkHttpClient,
    private val jsonFormatter: Gson,
    private val lobbiesURL: URL,
) : LobbyService {

    @Throws(IOException::class, UnexpectedResponseTypeException::class)
    override suspend fun getInvites(user: User): List<Invite> =
        api(
            client = client,
            requestBuilder = {
                createGet(url = lobbiesURL.toExternalForm())
            },
            onResponse = { response ->
                Log.v("GeneralService", "success: ${response.isSuccessful}, type: ${response.body?.contentType()}")
                validateResponse(response) ?: throw UnexpectedResponseTypeException("Invalid Response")
                try {
                    val body = response.body?.string()
                    Log.v("GeneralService", body?:"")
                    emptyList()
                } catch (e: JsonSyntaxException) {
                    throw UnexpectedResponseTypeException("Generating JSON: ${e.message}")
                }
            }
        )

    override suspend fun accept(user: User,inviteID: Int): Boolean =
        api(
            client = client,
            requestBuilder = {
                Log.v("LobbyService", user.token)
                createPost(
                    url = lobbiesURL.toExternalForm(),
                    body = "",
                    mediaType = JsonMediaType
                ).addHeader("Authorization", "Bearer ${user.token}")
            },
            onResponse = { response ->
                Log.v("GeneralService", "success: ${response.isSuccessful}, type: ${response.body?.contentType()}")
                validateResponse(response) ?: throw UnexpectedResponseTypeException("Invalid Response")
                try {
                    val body = response.body?.string()
                    Log.v("GeneralService", body?:"")
                    true
                } catch (e: JsonSyntaxException) {
                    throw UnexpectedResponseTypeException("Generating JSON: ${e.message}")
                }
            }
        )

    override suspend fun dodge(user: User, inviteID: Int) {
        TODO("Not yet implemented")
    }
}

class FakeLobbyService : LobbyService {
    override suspend fun getInvites(user: User) =
        listOf(
            Lobby(3, "Ramiro", "Henrique", LobbyState.AWAITING_OPPONENT),
            Lobby(1, "Mario", "Henrique", LobbyState.CANCELLED)
        ).map { lobby ->
            lobby.toInvite(User("Henrique", "token abcde1234"))
        }

    override suspend fun accept(user: User, inviteID: Int) = inviteID == 3

    override suspend fun dodge(user: User, inviteID: Int) { delay (1000) }
}