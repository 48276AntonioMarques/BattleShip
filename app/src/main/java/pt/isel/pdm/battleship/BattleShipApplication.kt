package pt.isel.pdm.battleship

import android.app.Application
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import pt.isel.pdm.battleship.common.*
import pt.isel.pdm.battleship.service.*
import java.net.URL

interface DependenciesContainer {
    val generalService: GeneralService
    val authService: AuthService
    val lobbyService: LobbyService
    val gameService: GameService
}
private const val api = "https://c0b4-194-210-198-67.eu.ngrok.io/"

class BattleShipApplication : DependenciesContainer, Application() {
    private val client by lazy { OkHttpClient()}
    private val gson by lazy {
        // Important: Add typeAdapters here 😢
        GsonBuilder()
            .registerTypeHierarchyAdapter(
                SubEntity::class.java,
                SubEntityDeserializer<RankDto>(RankDto::class.java)
            )
            .registerTypeHierarchyAdapter(
                SubEntity::class.java,
                SubEntityDeserializer<LobbyDto>(LobbyDto::class.java)
            )
            .registerTypeHierarchyAdapter(RankDto::class.java, RankDtoDeserializer())
            .create()
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
            lobbiesURL = URL(api + "lobbies/"),
            createURL = URL(api + "lobbies/create/"),
            lobbyURL = URL(api + "lobbies/"),

        )*/FakeLobbyService()

    override val gameService: GameService
        get() = /*RealGameService(
            client = client,
            jsonFormatter = gson,
            gameURL = URL(api + "game/"),
            fleetURL = URL(api + "game/my/fleet/"),
            enemyFleetURL = URL(api + "game/enemy/fleet/")
        )*/FakeGameService()
}