package com.system.design.data.local

// Entity
data class UserEntity(
    val id: String,
    val name: String,
    val email: String
)

// Minimal DAO-like interface
interface UserDao {
    suspend fun getUserEntity(): UserEntity?
    suspend fun insertUser(user: UserEntity)
}

// In-memory implementation
class InMemoryUserDao : UserDao {
    private var stored: UserEntity? = null
    override suspend fun getUserEntity(): UserEntity? = stored
    override suspend fun insertUser(user: UserEntity) { stored = user }
}
