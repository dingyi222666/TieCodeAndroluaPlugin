package com.dingyi.tiecode.plugin.androlua

import android.content.Context
import com.dingyi.tiecode.plugin.androlua.ktx.getJavaClass
import com.dingyi.tiecode.plugin.androlua.project.LuaProject
import com.dingyi.tiecode.plugin.androlua.project.LuaProjectTemplate
import com.tiecode.plugin.action.ActionController
import com.tiecode.plugin.app.PluginApp
import com.tiecode.plugin.app.ProjectPluginApp

class PluginApplication : ProjectPluginApp() {


    override fun onInitPlugin(pluginContext: Context) {

        actionController = PluginActionController()

        setProjectClass("AndroLua项目", 0x53.toString(), getJavaClass<LuaProject>())
        addProjectTemplate(LuaProjectTemplate())


    }

    override fun onCreate() {
        super.onCreate()

        application = this

    }




    companion object {
        lateinit var application: PluginApplication
    }
}