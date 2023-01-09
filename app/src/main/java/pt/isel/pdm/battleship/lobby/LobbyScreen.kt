package pt.isel.pdm.battleship.lobby

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
fun LobbyScreen(
    onBackRequested: () -> Unit
) {
    BattleShipTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                TopBar(onBackRequested = onBackRequested)
            },
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                //TODO: Build this table with information fetched from (some yet to come) service
                Text(text = "AWAITING OPPONENT...")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LobbyScreenPreview() {
    BattleShipTheme {
        LobbyScreen(
            onBackRequested = { Log.v("LobbyScreen", "Back requested") }
        )
    }
}