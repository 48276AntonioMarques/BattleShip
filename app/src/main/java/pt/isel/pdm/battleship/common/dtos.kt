package pt.isel.pdm.battleship.common

data class UserDtoProperties(val name: String, val token: String)

typealias UserDto = SirenEntity<UserDtoProperties>
val UserDtoType = SirenEntity.getType<UserDtoProperties>()