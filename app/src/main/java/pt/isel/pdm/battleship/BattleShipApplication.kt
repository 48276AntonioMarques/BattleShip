package pt.isel.pdm.battleship

import android.app.Application
import com.google.gson.Gson
import pt.isel.pdm.battleship.service.AuthService
import pt.isel.pdm.battleship.service.RealAuthService
import okhttp3.OkHttpClient
import pt.isel.pdm.battleship.service.FakeAuthService
import java.net.URL

interface DependenciesContainer {
    val authService: AuthService
}

private val authAPI = URL("https://localhost:8080/")

class BattleShipApplication : DependenciesContainer, Application() {
    private val client by lazy { OkHttpClient() }
    private val gson by lazy { Gson() }

    override val authService: AuthService
        get() = /*RealAuthService(
            client = client,
            jsonFormatter = gson,
            authURL = authAPI
        )*/FakeAuthService()
}