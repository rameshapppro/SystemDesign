package com.system.design.data.repository

import com.system.design.data.local.UserDao
import com.system.design.data.mapper.UserMapper
import com.system.design.data.remote.UserDto
import com.system.design.data.remote.UserRemoteSource
import com.system.design.domain.model.User
import com.system.design.domain.repository.UserRepository
import com.system.design.utils.Result

class UserRepositoryImpl(
    private val remote: UserRemoteSource,
    private val userDao: UserDao,
    private val mapper: UserMapper
) : UserRepository {
    override suspend fun getUser(): Result<User> {
        return try {
            val local = userDao.getUserEntity()
            if (local != null) {
                Result.Success(mapper.toDomain(local))
            } else {
                val dto: UserDto = remote.fetchUser()
                val entity = mapper.dtoToEntity(dto)
                userDao.insertUser(entity)
                Result.Success(mapper.toDomain(entity))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
