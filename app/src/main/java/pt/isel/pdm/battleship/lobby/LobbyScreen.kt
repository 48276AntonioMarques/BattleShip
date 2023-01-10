package pt.isel.pdm.battleship.lobby

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.battleship.R
import pt.isel.pdm.battleship.common.Lobby
import pt.isel.pdm.battleship.common.LobbyState
import pt.isel.pdm.battleship.common.LobbyState.*
import pt.isel.pdm.battleship.ui.TopBar
import pt.isel.pdm.battleship.ui.theme.BattleShipTheme

@Composable
fun LobbyScreen(
    onBackRequested: () -> Unit,
    lobby: Lobby?,
    isPlayer1: Boolean,
    onCancelRequested: () -> Unit
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
                if (lobby == null) {
                    Text(text = "${stringResource(id =  R.string.app_lobby_loading)}...")
                }
                else {
                    // Title
                    Text(text = "${lobby.user1} VS ${lobby.user2}")
                    // State
                    val playerTurn = "${stringResource(id = R.string.app_lobby_state_user_turn)}..."
                    val enemyTurn = "${stringResource(id = R.string.app_lobby_state_enemy_turn)}..."
                    val playerWon = stringResource(id = R.string.app_lobby_user_won)
                    val enemyWon = stringResource(id = R.string.app_lobby_user_lost)
                    val playerCancelled = stringResource(id = R.string.app_lobby_user_cancelled)
                    val enemyCancelled = stringResource(id = R.string.app_lobby_state_enemy_cancelled)
                    val state =
                        when(lobby.state) {
                            AWAITING_OPPONENT -> "${stringResource(id = R.string.app_lobby_state_awaiting_opponent)}..."
                            FLOATING -> "${stringResource(id = R.string.app_lobby_state_floating)}..."
                            USER1_TURN -> if (isPlayer1) playerTurn else enemyTurn
                            USER2_TURN -> if (isPlayer1) enemyTurn else playerTurn
                            WON -> if (isPlayer1) playerWon else enemyWon
                            LOST -> if (isPlayer1) enemyWon else playerWon
                            CANCELLED -> if (isPlayer1) playerCancelled else enemyCancelled
                            DODGED -> if (isPlayer1) enemyCancelled else playerCancelled
                        }
                    Text(text = state)
                    Button(onClick = onCancelRequested) {
                        Text(text = stringResource(id = R.string.app_lobby_cancel))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingLobbyScreenPreview() {
    BattleShipTheme {
        LobbyScreen(
            onBackRequested = { Log.v("LobbyScreen", "Back requested") },
            null,
            true,
            { Log.v("LobbyScreen", "Cancel!") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RandomCombinationLobbyScreenPreview() {
    val randomState = LobbyState.values()[(0 until LobbyState.values().size).random()]
    val randomPlayer = (0..1).random() == 0
    BattleShipTheme {
        LobbyScreen(
            onBackRequested = { Log.v("LobbyScreen", "Back requested") },
            Lobby(1, "Henrique", "Mario", randomState),
            randomPlayer,
            { Log.v("LobbyScreen", "Cancel!") }
        )
    }
}