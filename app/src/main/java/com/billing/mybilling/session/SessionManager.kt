package com.billing.mybilling.session

import com.billing.mybilling.data.model.response.Users

interface SessionManager {
    fun saveUser(users: Users)
    fun getUser():Users?
    fun deleteUser()
}