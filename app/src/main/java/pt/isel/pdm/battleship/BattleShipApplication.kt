package pt.isel.pdm.battleship

import android.app.Application
import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.battleship.service.*
import java.net.URL

interface DependenciesContainer {
    val generalService: GeneralService
    val authService: AuthService
    val lobbyService: LobbyService
}

private const val api = "https://5d4a-194-210-198-67.eu.ngrok.io/"

class BattleShipApplication : DependenciesContainer, Application() {
    private val client by lazy { OkHttpClient() }
    private val gson by lazy { Gson() }

    override val generalService: GeneralService
        get() = /*RealGeneralService(
            client = client,
            jsonFormatter = gson,
            leaderboardURL = URL(api + "leaderboard/")
        )*/FakeGeneralService()

    override val authService: AuthService
        get() = /*RealAuthService(
            client = client,
            jsonFormatter = gson,
            registerURL = URL(api + "users/"),
            loginURL = URL(api + "users/login/")
        )*/FakeAuthService()

    override val lobbyService: LobbyService
        get() = /*RealLobbyService(
            client = client,
            jsonFormatter = gson,
            lobbiesURL = URL(api + "lobbies/")
        )*/FakeLobbyService()
}