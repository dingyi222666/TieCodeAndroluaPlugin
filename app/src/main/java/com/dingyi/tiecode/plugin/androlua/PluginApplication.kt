package com.dingyi.tiecode.plugin.androlua

import android.content.Context
import com.dingyi.tiecode.plugin.androlua.ktx.getJavaClass
import com.dingyi.tiecode.plugin.androlua.project.LuaProject
import com.dingyi.tiecode.plugin.androlua.project.LuaProjectTemplate
import com.tiecode.plugin.app.ProjectPluginApp

class PluginApplication : ProjectPluginApp() {


    override fun onInitPlugin(superContext: Context) {

        actionController = PluginActionController()

        setProjectClass("AndroLua项目", 0x53.toString(), getJavaClass<LuaProject>())

        addProjectTemplate(LuaProjectTemplate())

        application = this

    }

    override fun onCreate() {
        super.onCreate()


    }




    companion object {
        lateinit var application: PluginApplication
    }
}