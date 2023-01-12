package pt.isel.pdm.battleship.game.compose

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import pt.isel.pdm.battleship.service.Coordinate
import pt.isel.pdm.battleship.service.Direction
import pt.isel.pdm.battleship.service.Ship

@Composable
fun ShipPlacer(
    shipToPlace: Ship?,
    cellSize: Dp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = shipToPlace?.type?.name?: "")
        ShipPlaceholder(ship = shipToPlace, cellSize = cellSize)
    }
}

private const val PLACEHOLDER_SIZE = 5
@Composable
private fun ShipPlaceholder(
    ship: Ship?,
    cellSize: Dp
) {
    val middle = PLACEHOLDER_SIZE / 2
    val shipLength = ship?.type?.relativeLocations?.size ?: 0
    val shipStart = (PLACEHOLDER_SIZE - shipLength) / 2
    val locations = ship?.type?.rotate(ship.direction)?.map { rl ->
        rl + if (ship.direction == Direction.VERTICAL)
                Coordinate(middle, shipStart)
            else
                Coordinate(shipStart, middle)
    }?: emptyList()
    locations.forEach { l -> Log.v("GameShipPlacer", "$l") }
    Column {
        (0 until PLACEHOLDER_SIZE).map { y ->
            Row {
                (0 until PLACEHOLDER_SIZE).map { x ->
                    Cell(
                        location = Coordinate(0,0),
                        cellState = if (locations.contains(Coordinate(x,y)))
                            CellState.SHIP
                        else CellState.WATER,
                        onCellClick = { _, _ -> },
                        cellSize =  cellSize
                    )
                }
            }
        }
    }
}