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
    suspend fun cancel(user: User, inviteID: Int)
    suspend fun getLobby(user: User, lobbyID: Int): Lobby
    suspend fun createLobby(user: User, enemy: String): Lobby
}

class RealLobbyService(
    private val client: OkHttpClient,
    private val jsonFormatter: Gson,
    private val lobbiesURL: URL,
    private val createURL: URL,
    private val lobbyURL: URL
) : LobbyService {

    @Throws(IOException::class, UnexpectedResponseTypeException::class)
    override suspend fun getInvites(user: User): List<Invite> =
        api(
            client = client,
            requestBuilder = {
                createGet(url = lobbiesURL.toExternalForm()).authenticate("Bearer ${user.token}")
            },
            onResponse = { response ->
                validateResponse(response) ?: throw UnexpectedResponseTypeException("Invalid Response")
                try {
                    val body = response.body?.string()
                    jsonFormatter.fromJson<InvitesListDto>(
                        body,
                        InvitesListDtoType.type
                    ).toInvitesList(user)
                } catch (e: JsonSyntaxException) {
                    throw UnexpectedResponseTypeException("Generating JSON: ${e.message}")
                }
            }
        )

    override suspend fun accept(user: User, inviteID: Int): Boolean =
        api(
            client = client,
            requestBuilder = {
                createPost(
                    url = getLobbyURL(inviteID),
                    body = null,
                    mediaType = JsonMediaType
                ).authenticate("Bearer ${user.token}")
            },
            onResponse = { response ->
                validateResponse(response) != null
            }
        )

    override suspend fun cancel(user: User, inviteID: Int) {
        api(
            client = client,
            requestBuilder = {
                createDelete(
                    url = getLobbyURL(inviteID),
                    body = null,
                    mediaType = JsonMediaType
                ).authenticate("Bearer ${user.token}")
            },
            onResponse = { response ->
                validateResponse(response) ?: throw UnexpectedResponseTypeException("Unable to cancel")
            }
        )
    }

    override suspend fun getLobby(user: User, lobbyID: Int): Lobby =
        api(
        client = client,
        requestBuilder = {
            createGet(url = getLobbyURL(lobbyID)).authenticate("Bearer ${user.token}")
        },
        onResponse = { response ->
            validateResponse(response) ?: throw UnexpectedResponseTypeException("Invalid Response")
            try {
                val body = response.body?.string()
                jsonFormatter.fromJson<LobbyDtoEntity>(
                    body,
                    LobbyDtoType.type
                ).toLobby()
            } catch (e: JsonSyntaxException) {
                throw UnexpectedResponseTypeException("Generating JSON: ${e.message}")
            }
        }
        )

    override suspend fun createLobby(user: User, enemy: String): Lobby =
        api(
            client = client,
            requestBuilder = {
                createPost(
                    url = createURL.toExternalForm(),
                    body = "{\"name\":\"${enemy}\"}",
                    mediaType = JsonMediaType
                ).authenticate("Bearer ${user.token}")
            },
            onResponse = { response ->
                validateResponse(response) ?: throw UnexpectedResponseTypeException("Invalid Response")
                try {
                    val body = response.body?.string()
                    jsonFormatter.fromJson<LobbyDtoEntity>(
                        body,
                        LobbyDtoType.type
                    ).toLobby()
                } catch (e: JsonSyntaxException) {
                    throw UnexpectedResponseTypeException("Generating JSON: ${e.message}")
                }
            }
        )

    private fun getLobbyURL(id: Int): String = lobbyURL.toExternalForm() + "$id"
}

class FakeLobbyService : LobbyService {
    override suspend fun getInvites(user: User) =
        listOf(
            Lobby(3, "Ramiro", "Henrique", LobbyState.AWAITING_OPPONENT),
            Lobby(1, "Mario", "Henrique", LobbyState.CANCELLED)
        ).map { lobby ->
            lobby.toInvite(User("Henrique", "token ab1234"))
        }

    override suspend fun accept(user: User, inviteID: Int) = inviteID == 3

    override suspend fun cancel(user: User, inviteID: Int) { delay (1000) }

    override suspend fun getLobby(user: User, lobbyID: Int): Lobby =
        Lobby(lobbyID, user.name, "Mario", LobbyState.FLOATING)

    override suspend fun createLobby(user: User, enemy: String): Lobby =
        Lobby(4, user.name, "Mario", LobbyState.AWAITING_OPPONENT)
}