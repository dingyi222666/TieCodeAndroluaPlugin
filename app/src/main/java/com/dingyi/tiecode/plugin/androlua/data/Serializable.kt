package com.dingyi.tiecode.plugin.androlua.data

import com.dingyi.tiecode.plugin.androlua.ktx.getJavaClass

interface Serializable<V> {

    fun dump(data: V): String

    fun load(data: String): V

}


class FunctionSerializable<T>(
    private val dumpFunction: (T) -> String,
    private val loadFunction: (String) -> T
) : Serializable<T> {
    override fun dump(data: T): String = dumpFunction.invoke(data)


    override fun load(data: String): T = loadFunction(data)

}

val StringSerializable = FunctionSerializable(
    { it }, { it }
)

val allSerializable = mutableMapOf<Class<*>, Serializable<*>>().apply {
    put(getJavaClass<String>(), StringSerializable)
}