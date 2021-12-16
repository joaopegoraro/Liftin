package xyz.joaophp.liftin.utils

import java.lang.reflect.Constructor

object Helpers {
    fun makeVoid(): Void {
        val c: Constructor<Void> = Void::class.java.getDeclaredConstructor()
        c.isAccessible = true
        return c.newInstance()
    }
}