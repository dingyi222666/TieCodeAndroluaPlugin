package com.dingyi.tiecode.plugin.androlua.data.internal

import com.dingyi.tiecode.plugin.androlua.PluginApplication
import com.dingyi.tiecode.plugin.androlua.data.KeyValueData
import com.dingyi.tiecode.plugin.androlua.data.Serializable
import com.github.sumimakito.quickkv.QuickKV
import java.io.File


class QuickKVData<T, V : Serializable<T>>(
    private val workspaceDir: File,
    private val databaseName: String,
    private val serializable: Serializable<T>
) : KeyValueData<String, T>(databaseName) {


    private val quickKV = QuickKV(PluginApplication.tiecodeContext)
        .apply {
            workspaceDir.mkdirs()
            setWorkspace(workspaceDir.path)
        }.getDatabase(databaseName, true)

    override fun put(key: String, value: T) {
        quickKV.put(key, serializable.dump(value))
    }

    override fun get(key: String): T {
        return getOrNull(key) ?: error("The value of the key($key) cannot be null")
    }

    override fun getOrNull(key: String): T? {
        return quickKV.get(key) as T?
    }

    override fun delete(key: String) {
        quickKV.remove(key)
    }

    override fun findKey(func: (String) -> Boolean): List<Pair<String, T>> {
        return quickKV.keys.filter { func(it.toString()) }
            .map { it.toString() to quickKV.get(it) as T }
    }

    override fun findValue(func: (T) -> Boolean): List<Pair<String, T>> {
        return quickKV.values.filter { func(it as T) }
            .map { it.toString() to quickKV.get(it) as T }
    }

    override fun getOrPut(key: String, defaultValue: T): T {
        val value = quickKV.get(key) ?: quickKV.put(key, defaultValue).let { defaultValue }
        return value as T
    }

    override fun toMap(): Map<String, T> {
        return toList().toMap()
    }

    override fun toList(): List<Pair<String, T>> {
        return quickKV.values
            .map { it.toString() to quickKV.get(it) as T }
    }

    override fun updateAll() {
        quickKV.persist()
    }

    override fun readAll() {
        quickKV.sync()
    }

    override fun deleteAll() {
        quickKV.clear()
        updateAll()
    }


}