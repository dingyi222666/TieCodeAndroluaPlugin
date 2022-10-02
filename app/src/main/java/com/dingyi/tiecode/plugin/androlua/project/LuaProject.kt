package com.dingyi.tiecode.plugin.androlua.project

import com.dingyi.tiecode.plugin.androlua.build.context.BuildContext
import com.tiecode.plugin.api.project.model.Project

class LuaProject : Project(), BuildContext {


    private val cacheContentInMemory = mutableMapOf<String, Any>()

    override fun initProject() {
        super.initProject()
    }

    override fun getKind(): Int {
        return Kind.APPLICATION
    }

    override fun getKey(key: String): Any? {
        return cacheContentInMemory.get(key)
    }

    override fun putValue(key: String, value: Any) {
        cacheContentInMemory.put(key, value)
    }

    override fun getTaskResult(): Any {
        return cacheContentInMemory["Task-Result-For-LuaProject"] ?: error("No task performed or no value returned")
    }

    override fun putTaskResult(value: Any) {
        cacheContentInMemory.put("Task-Result-For-LuaProject", value)
    }
}