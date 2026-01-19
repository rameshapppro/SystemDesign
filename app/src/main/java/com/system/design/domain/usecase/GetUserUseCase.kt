package com.system.design.domain.usecase

import com.system.design.domain.model.User
import com.system.design.domain.repository.UserRepository
import com.system.design.utils.Result

class GetUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(): Result<User> = repository.getUser()
}
