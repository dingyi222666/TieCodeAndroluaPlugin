package com.dingyi.tiecode.plugin.androlua.action

import com.dingyi.tiecode.plugin.androlua.build.base.LuaTaskBuilder
import com.dingyi.tiecode.plugin.androlua.ktx.getJavaClass
import com.dingyi.tiecode.plugin.androlua.project.LuaProject
import com.tiecode.develop.component.api.option.TieItem
import com.tiecode.develop.component.api.option.TieMenu
import com.tiecode.develop.component.widget.option.TieMenuItem
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

    override fun onCreateProjectMenu(menu: TieMenu) {
        menu.addItem(TieMenuItem("清除缓存", 0x145))
    }


    override fun onProjectMenuClick(item: TieItem) {
        if (item.id == 0x145) {
            runClean()
        }
    }

    private fun runClean() {

        actionController.getAction(getJavaClass<LogPageAction>()).clearLog()


        openLogPage()

        val builder = LuaTaskBuilder(project, logger)

        builder.runClean { _, _ ->

        }
    }

    private fun runBuild() {

        actionController.getAction(getJavaClass<LogPageAction>()).clearLog()


        openLogPage()

        val builder = LuaTaskBuilder(project, logger)

        builder.runBuild { _, _ ->

        }
    }


    override fun runProject() {



        runBuild()
    }
}