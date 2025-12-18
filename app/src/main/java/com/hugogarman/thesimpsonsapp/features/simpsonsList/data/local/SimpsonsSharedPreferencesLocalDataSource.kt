package com.hugogarman.thesimpsonsapp.features.simpsonsList.data.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hugogarman.thesimpsonsapp.features.simpsonsList.domain.Simpson
import java.util.concurrent.TimeUnit

class SimpsonsSharedPreferencesLocalDataSource(
    private val context: Context,
    private val gson: Gson
) {

    private val sharedPref = context.getSharedPreferences(
        "simpsonsList",
        Context.MODE_PRIVATE
    )

    private val CacheExpirationTime = TimeUnit.MINUTES.toMillis(5)

    fun save(simpsonsList: List<Simpson>) {
        with(sharedPref.edit()) {
            val jsonSimpsonsList = gson.toJson(simpsonsList)
            val currentTime = System.currentTimeMillis()
            putString("list", jsonSimpsonsList)
            putLong("lastUpdate", currentTime)
            apply()
        }
    }

    fun findAll() : List<Simpson> {
        val jsonSimpsonsList = sharedPref.getString("list", "[]")
        val type = object: TypeToken<List<Simpson>>() {}.type
        return gson.fromJson(jsonSimpsonsList, type)
    }

    fun isCacheValid() : Boolean {
        val lastUpdate = sharedPref.getLong("lastUpdate", 0)
        return (System.currentTimeMillis() - lastUpdate) <= CacheExpirationTime
    }
}