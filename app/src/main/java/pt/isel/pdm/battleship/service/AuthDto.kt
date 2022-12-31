package pt.isel.pdm.battleship.service

data class UserDtoProperties(val name: String, val token: String, val author: String)

typealias UserDto = SirenEntity<UserDtoProperties>
val UserDtoType = SirenEntity.getType<UserDtoProperties>()