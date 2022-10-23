package com.dingyi.tiecode.plugin.androlua

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast

import com.dingyi.tiecode.plugin.androlua.ktx.getJavaClass
import com.dingyi.tiecode.plugin.androlua.project.LuaProject
import com.dingyi.tiecode.plugin.androlua.project.LuaProjectTemplate
import com.tiecode.develop.util.firstparty.image.DrawableUtils
import com.tiecode.plugin.app.ProjectPluginApp
import io.github.dingyi.androlua.vm.LuaGlobal
import io.github.dingyi.androlua.vm.SingleLuaVM

class PluginApplication : ProjectPluginApp() {


    val mainHandler = Handler(Looper.getMainLooper())

    override fun onInitPlugin(superContext: Context) {

        tiecodeContext = superContext



        if (Build.VERSION.SDK_INT < 26) {
            Toast.makeText(superContext, "本插件不支持安卓版本小于8的系统", 0).show()
            return
        }


        //super
        actionController = PluginActionController()


        setProjectClass(
            "AndroLua工程", 0x53.toString(), getJavaClass<LuaProject>(),
            DrawableUtils.getDrawable(this, R.drawable.androlua)
        )

        addProjectTemplate(LuaProjectTemplate())

        application = this

        LuaGlobal.init(this)

        luaVm = SingleLuaVM()


    }

    override fun onCreate() {
        super.onCreate()

    }

    override fun onInstall(superContext: Context) {
        super.onInstall(superContext)

        if (Build.VERSION.SDK_INT < 26) {
            Toast.makeText(superContext, "本插件不支持安卓版本小于8的系统", Toast.LENGTH_LONG)
                .show()
            return
        }

    }

    companion object {
        lateinit var application: PluginApplication
        lateinit var luaVm: SingleLuaVM
        lateinit var tiecodeContext: Context
    }


    fun runOnUiThread(runnable: Runnable) {
        mainHandler.post(runnable)
    }
}