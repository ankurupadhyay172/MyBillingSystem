package com.billing.mybilling.session

import android.content.Context
import android.content.SharedPreferences
import com.billing.mybilling.data.model.response.Users
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SessionManagerImpl @Inject constructor(@ApplicationContext context: Context):SessionManager {

    private val pref: SharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)

    companion object{
        const val PREF_NAME = "user_session"
        const val USER_DATA = "user"
    }

    override fun saveUser(users: Users) {
        val editor = pref.edit()
        editor.putString(USER_DATA,Gson().toJson(users))
        editor.commit()
    }

    override fun getUser(): Users? {
        val userJson = pref.getString(USER_DATA, null)
        return if (userJson != null) Gson().fromJson(userJson, Users::class.java) else null

    }

    override fun deleteUser() {
        val editor = pref.edit()
        editor.clear()
        editor.commit()
    }
}