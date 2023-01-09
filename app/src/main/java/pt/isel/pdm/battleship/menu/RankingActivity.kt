package pt.isel.pdm.battleship.menu

import android.os.Bundle
import androidx.activity.compose.setContent
import pt.isel.pdm.battleship.DependenciesContainer
import pt.isel.pdm.battleship.common.KotlinActivity

class RankingActivity : KotlinActivity() {

    private val gs by lazy { (application as DependenciesContainer).generalService }
    private val rvm by viewModels{ RankingViewModel(gs) }

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