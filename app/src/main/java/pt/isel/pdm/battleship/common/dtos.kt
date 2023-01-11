package pt.isel.pdm.battleship.common

import android.os.Parcelable
import android.util.Log
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize
import pt.isel.pdm.battleship.service.Game
import java.lang.reflect.Type

@Parcelize
data class LocalUserDto(
    val name: String,
    val token: String
) : Parcelable

fun User.toLocalUserDto() = LocalUserDto(name, token)
fun LocalUserDto.toUser() = User(name, token)

data class User(val name: String, val token: String)
data class UserDtoProperties(val name: String, val token: String)
typealias UserDto = SirenEntity<UserDtoProperties>
val UserDtoType = SirenEntity.getType<UserDtoProperties>()
fun UserDto.toUser(): User = User(this.properties!!.name, this.properties.token)

data class Rank(val rank: Int, val username: String, val games: Int, val winRate: Float)
data class RankDto(val rank: Int, val username: String, val games: Int, val winRate: Float)
fun RankDto.toRank() = Rank(rank, username, games, winRate)

data class Leaderboard(val fields: List<String>, val ranks: List<Rank>)
data class LeaderboardDtoProperties(val fields: List<String>, val ranks: List<RankDto>)
typealias LeaderboardDto = SirenEntity<LeaderboardDtoProperties>
val LeaderboardDtoType = SirenEntity.getType<LeaderboardDtoProperties>()
fun LeaderboardDto.toLeaderboard(): Leaderboard {
    Log.e("DTOs", "${this.properties == null} ${this.properties?.fields}")
    return Leaderboard(this.properties!!.fields, this.properties.ranks.map { rank -> rank.toRank() })
}

class RankDtoDeserializer : JsonDeserializer<RankDto> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): RankDto {
        return RankDto(
            json.asJsonObject.getAsJsonPrimitive("rank").asInt,
            json.asJsonObject.getAsJsonPrimitive("username").asString,
            json.asJsonObject.getAsJsonPrimitive("games").asInt,
            json.asJsonObject.getAsJsonPrimitive("winRate").asFloat
        )
    }
}

data class Invite(val lobbyID: Int, val senderName: String)
data class InvitesListDtoProperties(val invites: List<LobbyDto>)
typealias InvitesListDto = SirenEntity<InvitesListDtoProperties>
val InvitesListDtoType = SirenEntity.getType<InvitesListDtoProperties>()
fun InvitesListDto.toInvitesList(user: User) =
    entities?.mapNotNull { entity ->
        (entity as EmbeddedEntity<LobbyDto>).properties?.toLobby()?.toInvite(user)
    } ?: emptyList()

typealias LobbyDtoEntity = SirenEntity<LobbyDto>
val LobbyDtoType = SirenEntity.getType<LobbyDto>()
fun LobbyDtoEntity.toLobby() = properties!!.toLobby()

data class UserNameDto(val name: String)
data class LobbyDto(val id: Int, val user1: UserNameDto, val user2: UserNameDto, val state: String)
fun LobbyDto.toLobby() = Lobby(id, user1.name, user2.name, LobbyState.valueOf(state))
enum class LobbyState {AWAITING_OPPONENT, FLOATING, USER1_TURN, USER2_TURN, WON, LOST, CANCELLED, DODGED}
data class Lobby(val id: Int, val user1: String, val user2: String, val state: LobbyState)
fun Lobby.toInvite(receiver: User) = Invite(id, if (user1 == receiver.name) user2 else user1)

data class GameDtoProperties(val id: Int, val player: String, val enemy: String, val state: String)
typealias GameDto = SirenEntity<GameDtoProperties>
val GameDtoType = SirenEntity.getType<GameDtoProperties>()
fun GameDto.toGame(user: User): Game =
    Game(
        properties!!.id,
        if (user.name == properties.player) properties.player else properties.enemy,
        if (user.name == properties.player) properties.enemy else properties.player,
        LobbyState.valueOf(properties.state)
    )
