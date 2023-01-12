package pt.isel.pdm.battleship.game

import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleship.R
import pt.isel.pdm.battleship.common.LobbyState
import pt.isel.pdm.battleship.game.compose.*
import pt.isel.pdm.battleship.service.*
import pt.isel.pdm.battleship.service.Direction
import pt.isel.pdm.battleship.ui.TopBar
import pt.isel.pdm.battleship.ui.theme.BattleShipTheme

private const val BOARD_SIZE = 10

@Composable
fun GameScreen(
    game: Game?,
    shipToPlace: Ship?,
    rotate: (Direction) -> Unit,
    fleet: Fleet,
    onCellClick: (Coordinate, CellState) -> Unit
) {
    BattleShipTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = colorResource(id = R.color.teal_200),
            topBar = {
                TopBar(
                    title = gameStateToText(state = game?.state?: LobbyState.FLOATING),
                    onBackRequested = {}
                )
            }
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Top,
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
                    val heightDp = config.screenHeightDp
                    val widthDp = config.screenWidthDp
                    val big: Dp = widthDp.dp / BOARD_SIZE
                    val small = (heightDp - widthDp - 100).dp / 10
                    val isFloating = game.state == LobbyState.FLOATING
                    GameDisplay(
                        isFloating = isFloating,
                        board = {
                            Board(
                                name = game.player,
                                cellSize = if (isFloating) big else small,
                                onCellClick = onCellClick,
                                fleet = fleet
                            )
                        },
                        shipPlacer = {
                            ShipPlacer(
                                shipToPlace = shipToPlace,
                                cellSize = small
                            )
                        },
                        sailButton = {
                            Row {
                                Button(
                                    onClick = {
                                        val dir =
                                            if (
                                                shipToPlace == null || shipToPlace.direction == Direction.HORIZONTAL)
                                                Direction.VERTICAL
                                            else
                                                Direction.HORIZONTAL
                                        rotate(dir)
                                    }
                                ) {
                                    Text(text = "Rotate")
                                }
                                Button(
                                    onClick = { Log.v("GameScreen", "Sailing...") }
                                ) {
                                    Text("Sail!")
                                }
                            }
                        },
                        enemyBoard = {
                            Board(
                                name = game.enemy,
                                cellSize = big,
                                onCellClick = { _, _ -> Log.v("GameScreen", "Shooting...") },
                                Fleet(emptyList(), emptyList())
                            )
                        },
                        shotButton = {
                            Row {
                                Button(
                                    onClick = { Log.v("GameScreen", "Shooting...") }
                                ) {
                                    Text("Shoot!")
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingGameScreenPreview() {
    BattleShipTheme {
        GameScreen(
            game = null,
            shipToPlace = null,
            rotate = { },
            Fleet(emptyList(), emptyList()),
            onCellClick = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FloatingGameScreenPreview() {
    BattleShipTheme {
        val shipsList = ShipType.values().toList()
        val index = remember { mutableStateOf(0) }
        val shipDirection = remember { mutableStateOf(Direction.VERTICAL) }
        val fleet = remember { mutableStateOf(Fleet(emptyList(), emptyList())) }
        val shipToPlace = if (index.value < shipsList.size)
            Ship(
                Coordinate(0,0),
                Direction.VERTICAL,
                shipsList[index.value]
            )
        else null
        GameScreen(
            game = Game(1, "Henrique", "Mario", LobbyState.FLOATING),
            shipToPlace = shipToPlace,
            rotate = { dir -> shipDirection.value = dir },
            Fleet(emptyList(), emptyList()),
            onCellClick = { location, state ->
                Log.v("GameScreen", "Cell clicked")
                if (shipToPlace != null) {
                    if (state == CellState.WATER) {
                        val fv = fleet.value
                        val ship = Ship(location, shipDirection.value, shipToPlace.type)
                        fleet.value = Fleet(fv.ships + ship, fv.shots)
                        index.value++
                    }
                }
            }
        )
    }
}