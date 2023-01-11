package pt.isel.pdm.battleship.game

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleship.R
import pt.isel.pdm.battleship.common.LobbyState
import pt.isel.pdm.battleship.service.*
import pt.isel.pdm.battleship.service.Direction
import pt.isel.pdm.battleship.ui.theme.BattleShipTheme

@Composable
fun GameScreen(
    game: Game?,
    shipToPlace: ShipType?,
    shipDirection: pt.isel.pdm.battleship.service.Direction?,
    rotate: (pt.isel.pdm.battleship.service.Direction) -> Unit,
    placeShip: (Ship) -> Unit,
    fleet: Fleet
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
                    val config = LocalConfiguration.current
                    val isPortrait = config.orientation == Configuration.ORIENTATION_PORTRAIT

                    val head: @Composable Any.() -> Unit = {
                        // Text(text = "${game.player} VS ${game.enemy}")
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

                    val small = 13.dp
                    val big = 40.dp
                    val isFloating = game.state == LobbyState.FLOATING
                    val enemyBoard: @Composable Any.() -> Unit = {
                        Board(
                            name = game.enemy,
                            cellSize = if (isFloating) small else big,
                            onCellClick = { _, _ -> Log.v("GameScreen", "Shooting...") },
                            Fleet(emptyList(), emptyList())
                        )
                    }
                    val ships: @Composable Any.() -> Unit = {
                        if (shipToPlace != null && shipDirection != null) {
                            Text(text = shipToPlace.name)
                            val shipSize = shipToPlace.relativeLocations.size
                            val newRotation: Direction =
                                if (shipDirection == Direction.VERTICAL)
                                    Direction.HORIZONTAL
                                else
                                    Direction.VERTICAL
                            val shipPlaceholder: @Composable Any.() -> Unit = {
                                (0 until shipSize).map {
                                    Cell(
                                        location = Coordinate(0,0),
                                        cellState = CellState.SHIP,
                                        onCellClick = { _, _ -> /*rotate(newRotation)*/ },
                                        cellSize =  small
                                    )
                                }
                            }
                            if (shipDirection == Direction.VERTICAL) Column(content = shipPlaceholder)
                            else Row(content = shipPlaceholder)
                            Button(
                                onClick = { rotate(newRotation) }
                            ) {
                                Text(text = "Rotate")
                            }
                        }
                    }
                    val boards: @Composable Any.() -> Unit = {
                        // your Board
                        Board(
                            name = game.player,
                            cellSize = if (isFloating) big else small,
                            onCellClick = { location, state ->
                                Log.v("GameScreen", "Cell clicked")
                                if (state == CellState.WATER && shipDirection != null && shipToPlace != null) {
                                    placeShip(Ship(location, shipDirection, shipToPlace))
                                }
                            },
                            fleet = fleet
                        )
                        if (isFloating)
                            if (isPortrait) Column(content = ships) else Row(content = ships)
                        else
                            Column(content = enemyBoard)
                    }
                    if (isPortrait)
                        Column(horizontalAlignment = Alignment.CenterHorizontally, content = boards)
                    else
                        Row(verticalAlignment = Alignment.CenterVertically, content = boards)

                    Row {
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
}

enum class CellState { SHIP, SUNK, WATER, SHOT, HIT, SELECTED }

@Composable
fun Cell(
    location: Coordinate,
    cellState: CellState,
    onCellClick: (Coordinate, CellState) -> Unit,
    cellSize: Dp,
) {
    val text =
        when(cellState) {
            CellState.SUNK -> "X"
            CellState.SHIP -> "0"
            CellState.WATER -> "~"
            CellState.SHOT -> "!"
            CellState.HIT -> "x"
            CellState.SELECTED -> "?"
        }
    val backColor =
        colorResource(id =
            when(cellState) {
                CellState.SUNK -> R.color.purple_700
                CellState.SHIP -> R.color.gray
                CellState.WATER -> R.color.teal_200
                CellState.SHOT -> R.color.teal_700
                CellState.HIT -> R.color.gray
                CellState.SELECTED -> R.color.teal_700
            }
        )
    val color =
        colorResource(id =
        when(cellState) {
            CellState.SUNK -> R.color.black
            CellState.SHIP -> R.color.gray
            CellState.WATER -> R.color.teal_700
            CellState.SHOT -> R.color.black
            CellState.HIT -> R.color.black
            CellState.SELECTED -> R.color.teal_200
        }
        )
    Text(
        text = text,
        textAlign = TextAlign.Center,
        color = color,
        modifier =
        Modifier
            .size(cellSize)
            .background(backColor)
            .clickable { onCellClick(location, cellState) }
    )
}

@Composable
fun Board(
    name: String,
    cellSize: Dp,
    onCellClick: (Coordinate, CellState) -> Unit,
    fleet: Fleet
) {
    val height = 10
    val width = 10
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = name)
        (0 until height).map { y ->
            Row {
                (0 until width).map { x ->
                    val location = Coordinate(x, y)
                    val ships = fleet.ships.flatMap { ship ->
                        ship.type.rotate(ship.direction).map { rl -> rl + ship.location }
                    }
                    Log.v("GameScreen", "${fleet.ships} |||| $ships")
                    val cellStateShip = if (ships.contains(location)) CellState.SHIP else CellState.WATER
                    Cell(location, cellStateShip, onCellClick, cellSize)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultBoardPreview() {
    BattleShipTheme {
        Board(
            name = "Board",
            cellSize = 20.dp,
            onCellClick = { _, _ -> },
            Fleet(emptyList(), emptyList())
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingGameScreenPreview() {
    BattleShipTheme {
        GameScreen(
            game = null,
            shipToPlace = null,
            shipDirection = null,
            rotate = { },
            placeShip = { },
            Fleet(emptyList(), emptyList())
        )
    }
}