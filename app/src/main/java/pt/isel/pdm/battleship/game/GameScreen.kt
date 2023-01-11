package pt.isel.pdm.battleship.game

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.battleship.R
import pt.isel.pdm.battleship.common.LobbyState
import pt.isel.pdm.battleship.service.Game
import pt.isel.pdm.battleship.ui.TopBar
import pt.isel.pdm.battleship.ui.theme.BattleShipTheme

@Composable
fun GameScreen(
    game: Game?
) {
    BattleShipTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                if (game == null) {
                    Text(text = "${stringResource(id = R.string.app_game_loading)}...")
                }
                else {
                    val config =LocalConfiguration.current
                    val isPortrait = config.orientation == Configuration.ORIENTATION_PORTRAIT

                    val head: @Composable Any.() -> Unit = {
                        Text(text = "${game.player} VS ${game.enemy}")
                        when (game.state) {
                            LobbyState.USER1_TURN ->
                                Text(stringResource(id = R.string.app_game_turn_player))
                            LobbyState.USER2_TURN ->
                                Text(stringResource(id = R.string.app_game_turn_enemy))
                            LobbyState.FLOATING ->
                                Text(text = stringResource(id = R.string.app_lobby_state_floating))
                            else ->
                                Text(text = game.state.name)
                        }
                    }
                    if (isPortrait) Column(content = head) else Row(content = head)

                    val boards: @Composable Any.() -> Unit = {
                        // enemy's Board
                        Board()
                        // your Board
                        Board()
                    }
                    if (isPortrait) Column(content = boards) else Row(content = boards)

                    val actions: @Composable Any.() -> Unit = {
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
                    if (isPortrait) Column(content = actions) else Row(content = actions)
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
fun LoadingGameScreenPreview() {
    BattleShipTheme {
        GameScreen(null)
    }
}