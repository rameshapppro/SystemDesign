package com.system.design.domain.repository

import com.system.design.domain.model.User
import com.system.design.utils.Result

interface UserRepository {
    suspend fun getUser(): Result<User>
}
