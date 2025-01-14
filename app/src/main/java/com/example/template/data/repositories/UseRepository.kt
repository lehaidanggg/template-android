package com.example.template.data.repositories

import com.example.template.data.local.daos.UserDao
import com.example.template.data.local.entities.User
import com.example.template.di.IoDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class UseRepository @Inject constructor(
    private val userDao: UserDao,
    @IoDispatcher private val dispatcher: CoroutineContext
) {

    fun insert(user: User) = flow {
        emit(userDao.insertUser(user))
    }.flowOn(dispatcher)

    fun getAll() = flow {
        emit(userDao.getAllUsers())
    }.flowOn(dispatcher)

}