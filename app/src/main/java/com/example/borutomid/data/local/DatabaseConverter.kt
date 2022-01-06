package com.example.borutomid.data.local

import androidx.room.TypeConverter
import java.lang.StringBuilder

class DatabaseConverter {

    private val separator=","


    @TypeConverter
    fun converListToString(list:List<String>):String
    {
        val stringBuild=StringBuilder()
        for(item in list)
        {
            stringBuild.append(item).append(separator)
        }

        stringBuild.setLength(stringBuild.length - separator.length)
        return stringBuild.toString()
    }


    @TypeConverter
    fun converStringToList(string:String):List<String>
    {

        return string.split(separator)
    }
}