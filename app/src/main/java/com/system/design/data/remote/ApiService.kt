package com.system.design.data.remote

import kotlinx.coroutines.delay

// Remote DTO
data class UserDto(
    val id: String,
    val name: String,
    val email: String
)

interface UserRemoteSource {
    suspend fun fetchUser(): UserDto
}

// Simple stub implementation (replace with Retrofit if needed)
class UserRemoteSourceStub : UserRemoteSource {
    override suspend fun fetchUser(): UserDto {
        delay(50)
        return UserDto(id = "1", name = "Jane Doe", email = "jane@system.design")
    }
}
