package pt.isel.pdm.battleship.menu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import pt.isel.pdm.battleship.DependenciesContainer
import pt.isel.pdm.battleship.auth.AuthActivity
import pt.isel.pdm.battleship.common.KotlinActivity

class RankingActivity : KotlinActivity() {

    private val gs by lazy { (application as DependenciesContainer).generalService }
    private val rvm by viewModels{ RankingViewModel(gs) }

    companion object {
        fun navigate(origin: Context) {
            val intent = Intent(origin, RankingActivity::class.java)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (rvm.leaderboard.value == null) {
            rvm.fetchLeaderboard(this)
        }
        setContent {
            RankingScreen(
                onBackRequested = { finish() },
                rvm.leaderboard.value
            )
        }
    }
}