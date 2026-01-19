package com.system.design.data.mapper

import com.system.design.data.local.UserEntity
import com.system.design.data.remote.UserDto
import com.system.design.domain.model.User

class UserMapper {
    fun dtoToEntity(dto: UserDto): UserEntity = UserEntity(dto.id, dto.name, dto.email)
    fun toDomain(entity: UserEntity): User = User(entity.id, entity.name, entity.email)
}
