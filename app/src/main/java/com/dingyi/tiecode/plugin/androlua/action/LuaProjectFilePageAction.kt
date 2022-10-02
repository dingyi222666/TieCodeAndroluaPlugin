package com.dingyi.tiecode.plugin.androlua.action

import com.tiecode.plugin.action.page.code.ProjectFilePageAction

class LuaProjectFilePageAction: ProjectFilePageAction() {

    init {

    }



    override fun onCreate() {
        addSupportOpenFileTypes(".lua",".aly")
    }

}