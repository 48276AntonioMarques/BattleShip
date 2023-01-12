package pt.isel.pdm.battleship.game.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pt.isel.pdm.battleship.R
import pt.isel.pdm.battleship.common.LobbyState

@Composable
fun gameStateToText(
    state: LobbyState
): String = when (state) {
        LobbyState.USER1_TURN ->
            stringResource(id = R.string.app_game_turn_player)
        LobbyState.USER2_TURN ->
            stringResource(id = R.string.app_game_turn_enemy)
        LobbyState.FLOATING ->
            stringResource(id = R.string.app_lobby_state_floating)
        else ->
            state.name
    }