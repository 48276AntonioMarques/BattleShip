package pt.isel.pdm.battleship.game

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
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
fun GameScreen() {
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
                // Change this Column's with a Row's based on the orientation of the device
                Column {
                    Text(text = "Henrique VS Ramiro")
                    Text(text = "00:01")
                    Text(text = "Henrique's Turn.")
                }
                Column{
                    // enemy's Board
                    Board()
                    // your Board
                    Board()
                }
                Column {
                    Button(
                        onClick = { Log.v("GameScreen", "Shooting...") }
                    ) {
                        Text("Shoot!")
                    }
                    Button(
                        onClick = { Log.v("GameScreen", "Giving it up... Letting you down...") }
                    ) {
                        Text("Forfeit!")
                    }
                }
            }
        }
    }
}

@Composable
fun Board() {

}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    BattleShipTheme {
        GameScreen()
    }
}