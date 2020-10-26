package com.example.drugapp.util

import android.content.Context
import android.text.TextUtils
import com.alibaba.fastjson.JSONObject
import com.example.drugapp.model.bean.UserInfo
import com.google.gson.Gson

object ShapeUtil {

    private val NAME: String = "drugapp"
    private val USER: String = "user"

    fun setUser(c: Context, u: UserInfo): ShapeUtil {
        c.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit()
            .putString(USER, JSONObject.toJSON(u).toString()).commit()
        return this
    }

    fun getUser(c: Context): UserInfo? {
        val s = c.getSharedPreferences(NAME, Context.MODE_PRIVATE).getString(USER, "")
        if (TextUtils.isEmpty(s)) {
            return null
        }
        val user = Gson().fromJson(s, UserInfo::class.java)
//        val user = UserInfo();
//        user.account = "12345678901"
        return user
    }

    fun clearUser(c: Context){
        c.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit().remove(USER).commit();
    }


}