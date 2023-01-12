package pt.isel.pdm.battleship.game.compose

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pt.isel.pdm.battleship.R
import pt.isel.pdm.battleship.service.*
import pt.isel.pdm.battleship.ui.FlipDirection
import pt.isel.pdm.battleship.ui.FlipLine
import pt.isel.pdm.battleship.ui.theme.BattleShipTheme

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

@Composable
fun GameDisplay(
    isFloating: Boolean,
    board: @Composable Any.() -> Unit,
    enemyBoard: @Composable Any.() -> Unit,
    shipPlacer: @Composable Any.() -> Unit,
    sailButton: @Composable Any.() -> Unit,
    shotButton: @Composable Any.() -> Unit
) {
    if (isFloating) {
        shipPlacer.shipPlacer()
        board.board()
        sailButton.sailButton()
    }
    else {
        board.board()
        enemyBoard.enemyBoard()
        shotButton.shotButton()
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
