package xyz.joaophp.liftin.utils

import java.lang.reflect.Constructor
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Helpers {
    fun makeVoid(): Void {
        val c: Constructor<Void> = Void::class.java.getDeclaredConstructor()
        c.isAccessible = true
        return c.newInstance()
    }

    @Throws(Exception::class)
    fun stringToEpoch(dateString: String): Long {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val date = LocalDate.parse(dateString, formatter)
        return date.toEpochDay()
    }

    @Throws(Exception::class)
    fun epochToString(epochLong: Long): String {
        return LocalDate.ofEpochDay(epochLong).toString()
    }
}