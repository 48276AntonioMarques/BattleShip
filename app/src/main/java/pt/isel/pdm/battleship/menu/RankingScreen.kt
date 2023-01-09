package pt.isel.pdm.battleship.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.battleship.ui.TopBar
import pt.isel.pdm.battleship.ui.theme.BattleShipTheme

@Composable
fun RankingScreen() {
    BattleShipTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                TopBar(onBackRequested = { Log.v("RankingScreen", "This is going to get u back. Eventually... ") }) },
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                //TODO: Build this table with information fetched from (some yet to come) service
                Row {
                    Text(text = "Rank")
                    Text(text = "Username")
                    Text(text = "WinRate")
                    Text(text = "GamesPlayed")
                }
                Row {
                    Text(text = "1.")
                    Text(text = "Henrique")
                    Text(text = "90%")
                    Text(text = "10")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RankingScreenPreview() {
    BattleShipTheme {
        RankingScreen()
    }
}