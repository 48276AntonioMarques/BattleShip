package pt.isel.pdm.battleship.menu

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleship.common.Leaderboard
import pt.isel.pdm.battleship.common.Rank
import pt.isel.pdm.battleship.ui.TopBar
import pt.isel.pdm.battleship.ui.theme.BattleShipTheme

@Composable
fun RankingScreen(
    onBackRequested: () -> Unit,
    leaderboard: Leaderboard?
) {
    BattleShipTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                TopBar(onBackRequested = onBackRequested)
            }
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                if (leaderboard == null) {
                    // loading animation
                    Text(text = "Loading...")
                }
                else {
                    // Table Head
                    Row(
                        Modifier.padding(30.dp)
                    ) {
                        leaderboard.fields?.map { field ->
                            Text(
                                text = field,
                                style = TextStyle(textDecoration = TextDecoration.Underline),
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                    // Table Body
                    leaderboard.ranks?.map { rank ->
                        Row (
                            modifier = Modifier.padding(1.dp)
                        ) {
                            listOf<String>(
                                rank.rank.toString(),
                                rank.username,
                                rank.games.toString(),
                                rank.winRate.toString()
                            ).map {
                                Text(text = it, modifier = Modifier.padding(5.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingRankingScreenPreview() {
    BattleShipTheme {
        RankingScreen(
            onBackRequested = { Log.v("RankingScreen", "This will get back.") },
            leaderboard = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RankingScreenPreview() {
    BattleShipTheme {
        val leaderboard =
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
        RankingScreen(
            onBackRequested = { Log.v("RankingScreen", "This will get back.") },
            leaderboard = leaderboard
        )
    }
}