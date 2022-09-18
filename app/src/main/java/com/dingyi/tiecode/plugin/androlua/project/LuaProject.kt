package com.dingyi.tiecode.plugin.androlua.project

import com.tiecode.plugin.api.project.model.Project

class LuaProject: Project() {

    override fun initProject() {
        super.initProject()
    }

    override fun getKind(): Int {
        return Kind.APPLICATION
    }
}