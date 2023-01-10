package pt.isel.pdm.battleship

import android.app.Application
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import pt.isel.pdm.battleship.service.*

interface DependenciesContainer {
    val generalService: GeneralService
    val authService: AuthService
    val lobbyService: LobbyService
}
private const val api = "https://5d4a-194-210-198-67.eu.ngrok.io/"

class BattleShipApplication : DependenciesContainer, Application() {
    private val client by lazy { OkHttpClient()}
    private val gson by lazy {
        // Important: Add typeAdapters here 😢
        GsonBuilder().create()
    }

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