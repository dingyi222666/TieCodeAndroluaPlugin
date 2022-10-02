package com.dingyi.tiecode.plugin.androlua.build.context

interface BuildContext {

    fun getKey(key: String): Any?

    fun putValue(key: String, value: Any)

    fun getTaskResult(): Any

    fun putTaskResult(value: Any)

}