package com.dingyi.tiecode.plugin.androlua

import com.dingyi.tiecode.plugin.androlua.action.LuaProjectFilePageAction
import com.dingyi.tiecode.plugin.androlua.ktx.getJavaClass
import com.tiecode.plugin.action.ActionController
import com.tiecode.plugin.action.page.code.ProjectFilePageAction

class PluginActionController: ActionController() {

    init {
        addAction(getJavaClass<ProjectFilePageAction>(),LuaProjectFilePageAction())
    }


}