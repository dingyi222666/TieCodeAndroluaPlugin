package com.dingyi.tiecode.plugin.androlua.data

abstract class KeyValueData<K, V>(
    //不使用，占位。
    private val databaseName: String
) : Data {

    abstract fun put(key: K, value: V)

    abstract fun get(key: K):V

    abstract fun getOrNull(key: K):V?

    abstract fun getOrPut(key: K,defaultValue:V):V

    abstract fun delete(key: K)

    abstract fun findKey(func: (K) -> Boolean): List<Pair<K, V>>

    abstract fun findValue(func: (V) -> Boolean): List<Pair<K, V>>

    abstract fun toMap(): Map<K, V>

    abstract fun toList(): List<Pair<K, V>>

}