package com.example.webappconverted.data.prefs

import android.content.Context
class PrefsManager(context: Context) {
    private val prefs = context.getSharedPreferences("webapp_prefs", Context.MODE_PRIVATE)
    fun setCurrentUser(u: String) = prefs.edit().putString("currentUser", u).apply()
    fun getCurrentUser(): String? = prefs.getString("currentUser", null)
    fun clear() = prefs.edit().clear().apply()
    fun setTurn(t: String) = prefs.edit().putString("turn", t).apply()
    fun getTurn(): String? = prefs.getString("turn", null)
}
