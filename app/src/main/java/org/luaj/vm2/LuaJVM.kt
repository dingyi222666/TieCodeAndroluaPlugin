package org.luaj.vm2

import com.luajava.LuajLuaState
import org.luaj.vm2.lib.jse.JsePlatform
import java.io.Closeable
import java.io.FileInputStream

class LuaJVM:Closeable {
    lateinit var state: LuajLuaState

    init {
        init()
    }

    fun init() {
        state = LuajLuaState(JsePlatform.standardGlobals())
        state.openLibs()
        state.openBase()
    }

    fun doString(str: String) {
        state.globals.load(str).call()
    }

    fun loadFile(path: String): LuaTable {
        val table = LuaValue.tableOf()
        try {
            state.globals.load(FileInputStream(path), "@d", "bt", table).invoke()
        } catch (ignored: Exception) {
        }
        return table
    }

    fun loadString(str: String): LuaTable {
        val table = LuaValue.tableOf()
        state.globals["load"].call(LuaValue.valueOf(str), table)
        return table
    }

    fun doFile(absolutePath: String): LuaValue {
        return state.globals.loadfile(absolutePath).call()
    }

    operator fun get(key: String): Any {
        return state.getLuaValue(state.getGlobal(key))
    }

    override fun close() {
        state.close()
    }

    fun runFunc(name: String, vararg array: Any): Varargs {
        val values = arrayOfNulls<LuaValue>(array.size)
        for (i in array.indices) {
            values[i] = LuaValue.valueOf(array[i].toString())
        }
        return state.globals[name].invoke(values)
    }
}