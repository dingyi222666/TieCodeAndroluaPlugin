package com.dingyi.tiecode.plugin.androlua.data

import androidx.annotation.WorkerThread

interface Data {
    @WorkerThread
    fun updateAll()

    @WorkerThread
    fun readAll()

    @WorkerThread
    fun deleteAll()


}