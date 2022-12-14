package com.dingyi.tiecode.plugin.androlua

import com.dingyi.tiecode.plugin.androlua.action.LuaCodeBodyPageAction
import com.dingyi.tiecode.plugin.androlua.action.LuaCodePageAction
import com.dingyi.tiecode.plugin.androlua.action.LuaLogPageAction
import com.dingyi.tiecode.plugin.androlua.action.LuaProjectFilePageAction
import com.dingyi.tiecode.plugin.androlua.action.LuaProjectListPageAction
import com.dingyi.tiecode.plugin.androlua.action.LuaProjectStructureAction
import com.dingyi.tiecode.plugin.androlua.ktx.getJavaClass
import com.tiecode.plugin.action.ActionController
import com.tiecode.plugin.action.component.task.TaskPerformerAction
import com.tiecode.plugin.action.page.code.CodeBodyPageAction
import com.tiecode.plugin.action.page.code.CodePageAction
import com.tiecode.plugin.action.page.code.LogPageAction
import com.tiecode.plugin.action.page.code.ProjectFilePageAction
import com.tiecode.plugin.action.page.project.ProjectListPageAction

class PluginActionController : ActionController() {

    init {
        addAction(getJavaClass<ProjectFilePageAction>(), LuaProjectFilePageAction())
        addAction(getJavaClass<CodeBodyPageAction>(), LuaCodeBodyPageAction())
        addAction(getJavaClass<ProjectFilePageAction>(), LuaProjectStructureAction())
        addAction(getJavaClass<ProjectListPageAction>(), LuaProjectListPageAction())
        addAction(
            getJavaClass<CodePageAction>(), LuaCodePageAction()
        )
        addAction(
            getJavaClass<LogPageAction>(),LuaLogPageAction()
        )

    }


}