package com.dingyi.tiecode.plugin.androlua.action

import com.dingyi.tiecode.plugin.androlua.build.base.LuaTaskBuilder
import com.dingyi.tiecode.plugin.androlua.ktx.getJavaClass
import com.dingyi.tiecode.plugin.androlua.project.LuaProject
import com.tiecode.plugin.action.page.code.CodePageAction
import com.tiecode.plugin.action.page.code.LogPageAction
import com.tiecode.plugin.api.log.Logger
import com.tiecode.plugin.api.log.model.LogMessage
import com.tiecode.plugin.api.log.model.TieLogMessage
import com.tiecode.plugin.api.project.ProjectContext

class LuaCodePageAction : CodePageAction() {

    private lateinit var project: LuaProject

    private lateinit var logger: Logger


    override fun onCreate() {


        logger = actionController.getAction(getJavaClass<LogPageAction>()).logger



        project = ProjectContext.getCurrentProject() as LuaProject


    }

    override fun onLoadProject() {


    }


    private fun runBuild() {

        openLogPage()

        val builder = LuaTaskBuilder(project, logger)

        builder.runBuild { _, _ ->

        }
    }

    //TODO: 读取包名，如果已经安装则尝试调用LuaActivity启动以实现热启动
    override fun runProject() {

        actionController.getAction(getJavaClass<LogPageAction>()).clearLog()

        runBuild()
    }
}