package com.dingyi.tiecode.plugin.androlua.build.task

import com.dingyi.tiecode.plugin.androlua.build.base.LuaTask
import com.tiecode.plugin.api.log.Logger
import com.tiecode.plugin.api.log.model.TieLogMessage
import com.tiecode.plugin.api.project.model.Project
import com.tiecode.plugin.api.project.task.define.BuildVariant
import com.tiecode.plugin.api.project.task.define.TaskPerformState
import org.luaj.vm2.LuaJVM
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

class ReadConfigTask : LuaTask() {
    override fun getName() = "读取项目配置文件"


    override fun initialize(
        project: Project,
        buildVariant: BuildVariant,
        logger: Logger
    ): TaskPerformState {
        return super.initialize(project, buildVariant, logger)
    }

    override fun perform(): Boolean {

        val initLuaFile = getSrcDir("init.lua")

        if (!initLuaFile.isFile) {
            runOnUiThread {
                logger.log(TieLogMessage("找不到项目配置文件，无法构建项目！", TieLogMessage.ERROR))
            }
            return false
        }


        val vm = LuaJVM()


        val config = vm.loadFile(initLuaFile.path)

        config.keys().forEach {
            val key = it.tojstring()

            val value = config[it]

            taskContext.putValue(key, toJavaObject(value))

        }


        val checkPropertyArray = arrayOf("packagename", "appcode", "appver")


        checkPropertyArray.forEach {
            val value = taskContext.getKey(it)

            if (value == null) {
                runOnUiThread {
                    logger.log(
                        TieLogMessage(
                            "配置文件缺失${it}属性，请检查init.lua!",
                            TieLogMessage.ERROR
                        )
                    )
                }
                return false
            }
        }

        return true
    }

    private fun toJavaObject(value: LuaValue): Any {
        return when (value) {
            is LuaTable -> {
                val result = mutableMapOf<Any, Any>()
                value.keys().forEach {
                    result[it] = toJavaObject(value[it])
                }
                result
            }

            else -> value.tojstring()
        }
    }

}