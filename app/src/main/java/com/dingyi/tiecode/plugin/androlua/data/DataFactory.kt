package com.dingyi.tiecode.plugin.androlua.data

import com.dingyi.tiecode.plugin.androlua.data.internal.QuickKVData
import java.io.File

object DataFactory {


    fun <T> createKeyValueData(
        workspaceDir: File,
        databaseName: String,
        dataClass: Class<T>
    ): KeyValueData<String, T> {

        val targetSerializable = allSerializable[dataClass] as Serializable<T>

        return QuickKVData(workspaceDir, databaseName, targetSerializable)
    }
}

