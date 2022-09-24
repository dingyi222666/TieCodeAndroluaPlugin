package com.dingyi.tiecode.plugin.androlua

import android.content.Context
import com.dingyi.tiecode.plugin.androlua.ktx.getJavaClass
import com.dingyi.tiecode.plugin.androlua.project.LuaProject
import com.dingyi.tiecode.plugin.androlua.project.LuaProjectTemplate
import com.tiecode.plugin.app.ProjectPluginApp
import io.github.dingyi.androlua.vm.LuaGlobal
import io.github.dingyi.androlua.vm.SingleLuaVM

class PluginApplication : ProjectPluginApp() {


    override fun onInitPlugin(superContext: Context) {

        actionController = PluginActionController()

        setProjectClass("AndroLua项目", 0x53.toString(), getJavaClass<LuaProject>())

        addProjectTemplate(LuaProjectTemplate())

        application = this

        LuaGlobal.init(this)

        luaVm = SingleLuaVM()


    }

    override fun onCreate() {
        super.onCreate()


    }

    companion object {
        lateinit var application: PluginApplication
        lateinit var luaVm: SingleLuaVM
    }
}