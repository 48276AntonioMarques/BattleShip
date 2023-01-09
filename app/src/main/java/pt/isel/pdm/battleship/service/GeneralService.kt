package pt.isel.pdm.battleship.service

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.Request
import pt.isel.pdm.battleship.common.*
import java.io.IOException
import java.lang.Exception
import java.net.URL

interface GeneralService {
    suspend fun getLeaderboard(): Leaderboard
}

class RealGeneralService(
    private val client: OkHttpClient,
    private val jsonFormatter: Gson,
    private val leaderboardURL: URL
) : GeneralService {

    @Throws(IOException::class, UnexpectedResponseTypeException::class)
    override suspend fun getLeaderboard(): Leaderboard =
        api(
            client = client,
            requestBuilder = {
                createGet(
                    url = leaderboardURL.toExternalForm()
                )
            },
            onResponse = { response ->
                Log.v("GeneralService", "success: ${response.isSuccessful}, type: ${response.body?.contentType()}")
                validateResponse(response) ?: throw UnexpectedResponseTypeException("Invalid Response")
                try {
                    val body = response.body?.string()
                    Log.v("GeneralService", body?:"")
                    Leaderboard(emptyList(), emptyList())
                } catch (e: JsonSyntaxException) {
                    throw UnexpectedResponseTypeException("Generating JSON: ${e.message}")
                }
            }
        )
}

class FakeGeneralService : GeneralService {
    override suspend fun getLeaderboard() =
        Leaderboard(
            listOf("Rank", "Name", "Games", "Win Rate"),
            listOf(
                Rank(1, "Mateus", 0, 0f),
                Rank(2, "Joao", 0, 0f),
                Rank(3, "Tiago", 0, 0f),
                Rank(4, "Mario", 0, 0f),
                Rank(5, "Henrique", 0, 0f),
            )
        )
}