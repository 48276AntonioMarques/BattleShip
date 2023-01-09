package pt.isel.pdm.battleship.common

import android.os.Parcelable
import android.util.Log
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize
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

//FIXME: Gson crashes trying to parse this :'(
data class Rank(val rank: Int, val username: String, val games: Int, val winRate: Float)
data class RankDto(val rank: Int, val username: String, val games: Int, val winRate: Float)
val RankDtoType = object : TypeToken<RankDto>() { }
fun RankDto.toRank() = Rank(rank, username, games, winRate)

data class Leaderboard(val fields: List<String>, val ranks: List<Rank>)
data class LeaderboardDtoProperties(val fields: List<String>)
typealias LeaderboardDto = SirenEntity<LeaderboardDtoProperties>
val LeaderboardDtoType = SirenEntity.getType<LeaderboardDto>()
fun LeaderboardDto.toLeaderboard(): Leaderboard {
    Log.e("DTOs", "${this.properties == null} ${this.properties?.fields}")
    return Leaderboard(this.properties!!.fields, listOf())
}

class RankDtoDeserializer(private val propertiesType: Type) : JsonDeserializer<RankDto> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): RankDto {
        return RankDto(
            json.asJsonObject.getAsJsonObject("rank").asInt,
            json.asJsonObject.getAsJsonObject("username").asString,
            json.asJsonObject.getAsJsonObject("games").asInt,
            json.asJsonObject.getAsJsonObject("winRate").asFloat
        )
/*
        val entity = json.asJsonObject
        val entityPropertiesMember = "properties"
        return if (entity.has(entityPropertiesMember)) {
            val item = context.deserialize<T>(
                entity.getAsJsonObject(entityPropertiesMember),
                propertiesType
            )
            EmbeddedEntity(
                rel = entity.getAsListOfString("rel") ?: emptyList(),
                clazz = entity.getAsListOfString("class"),
                properties = item,
                links = entity.getAsListOf("links", SirenLink::class.java, context),
                actions = entity.getAsListOf("actions", SirenAction::class.java, context),
                title = entity.get("title")?.asString
            )
        }
        else {
            context.deserialize(entity, EmbeddedLink::class.java)
        }*/
    }
}

enum class LobbyState {AWAITING_OPPONENT, FLOATING, USER1_TURN, USER2_TURN, WON, LOST, CANCELLED, DODGED}
data class Lobby(val id: Int, val user1: String, val user2: String, val state: LobbyState)

data class Invite(val lobbyID: Int, val senderName: String)

fun Lobby.toInvite(receiver: User) = Invite(id, if (user1 == receiver.name) user2 else user1)