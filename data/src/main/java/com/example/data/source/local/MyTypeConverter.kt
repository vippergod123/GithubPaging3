package com.example.data.source.local

import androidx.room.TypeConverter
import com.example.domain.entity.GithubRepo
import com.example.domain.entity.Owner
import com.google.gson.Gson

class  MyTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun jsonToOwner(data: String): Owner {
        return gson.fromJson(data, Owner::class.java)
    }

    @TypeConverter
    fun ownerToJson(cl: Owner): String {
        return gson.toJson(cl)
    }

}