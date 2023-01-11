package pt.isel.pdm.battleship.service

import com.google.gson.Gson
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import pt.isel.pdm.battleship.common.Lobby
import pt.isel.pdm.battleship.common.LobbyState
import pt.isel.pdm.battleship.common.User

data class Game(val id: Int, val player: String, val enemy: String, val state: LobbyState)
data class Coordinate(val x: Int, val y: Int) {
    operator fun plus(coordinate: Coordinate) = Coordinate(x + coordinate.x, y + coordinate.y)
}
enum class Direction { VERTICAL, HORIZONTAL }
enum class ShipType(val relativeLocations: List<Coordinate>) {
    Carrier(List(5) { Coordinate(0, it) }),
    BattleShip(List(4) { Coordinate(0, it) }),
    Destroyer(List(3) { Coordinate(0, it) }),
    Submarine(List(3) { Coordinate(0, it) }),
    PatrolBoat(List(2) { Coordinate(0, it) });

    fun rotate(direction: Direction): List<Coordinate> = relativeLocations.map { coords ->
        if (direction == Direction.VERTICAL) coords else Coordinate(coords.y, coords.x)
    }
}
data class Ship(val location: Coordinate, val direction: Direction, val type: ShipType)
data class Shot(val location: Coordinate, val result: ShotResult)
enum class ShotResult { UNKNOWN, MISS, HIT, SINK }
data class Fleet(val ships: List<Ship>, val shots: List<Shot>)
data class EnemyFleet(val shots: List<Shot>)

class OutOfTurnException(message: String): Exception(message)

interface GameService {
    suspend fun getState(): Game
    suspend fun submitFleet(fleet: Fleet) : Fleet
    suspend fun getFleetState(): Fleet
    suspend fun submitShots(shots: List<Shot>): EnemyFleet

}

class RealGameService(
    val client: OkHttpClient,
    val jsonFormatter: Gson
): GameService {
    override suspend fun getState(): Game {
        TODO("Not yet implemented")
    }

    override suspend fun submitFleet(fleet: Fleet): Fleet {
        TODO("Not yet implemented")
    }

    override suspend fun getFleetState(): Fleet {
        TODO("Not yet implemented")
    }

    override suspend fun submitShots(shots: List<Shot>): EnemyFleet {
        TODO("Not yet implemented")
    }

}

class FakeGameService: GameService {
    val player = User("Henrique", "token ab1234")
    val lobby = Lobby(5, "Henrique", "Mario", LobbyState.FLOATING)
    var game = Game(lobby.id, lobby.user1, lobby.user2, lobby.state)
    var fleet = Fleet(emptyList(), emptyList())
    var enemyFleet = Fleet(emptyList(), emptyList())

    override suspend fun getState(): Game {
        delay(500)
        return game
    }

    override suspend fun submitFleet(fleet: Fleet): Fleet {
        this.fleet = fleet
        if (enemyFleet.ships.isNotEmpty() && fleet.ships.isNotEmpty()) {
            game = Game(lobby.id, lobby.user1, lobby.user2, LobbyState.USER1_TURN)
        }
        delay(500)
        return fleet
    }

    override suspend fun getFleetState(): Fleet {
        delay(500)
        return fleet
    }

    override suspend fun submitShots(shots: List<Shot>): EnemyFleet {
        if (game.state != LobbyState.USER1_TURN) throw OutOfTurnException("Is not your turn to play")
        val hits: List<Shot> = shots.flatMap { shot ->
            enemyFleet.ships.flatMap { ship ->
                ship.type.relativeLocations.map { rl -> rl + ship.location }.filter { location ->
                    shot.location  == location
                }
            }.map { location -> Shot(location, ShotResult.HIT) }
        }
        val misses : List<Shot> = shots.filter { shot -> !hits.contains(shot) }.map { shot ->
            Shot(shot.location, ShotResult.MISS)
        }
        val sunks: List<Shot> = enemyFleet.ships.flatMap { ship ->
            ship.type.relativeLocations.map { rl ->
                rl + ship.location
            }.filter { location ->
                hits.filter { shot ->
                    shot.location == location
                }.size >= ship.type.relativeLocations.size
            }.map { sinkingShot ->
                Shot(sinkingShot, ShotResult.SINK)
            }
        }
        val finalHits = hits.filter { hit -> !sunks.contains(hit) }
        enemyFleet = Fleet(enemyFleet.ships, enemyFleet.shots + sunks + finalHits + misses)
        delay(500)
        return EnemyFleet(enemyFleet.shots)
    }
}
