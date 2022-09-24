package com.dingyi.tiecode.plugin.androlua.project

import android.graphics.drawable.Drawable
import com.dingyi.tiecode.plugin.androlua.PluginApplication
import com.dingyi.tiecode.plugin.androlua.R
import com.tiecode.develop.util.firstparty.image.DrawableUtils
import com.tiecode.develop.util.firstparty.zip.ZipUtils
import com.tiecode.plugin.api.project.model.Message
import com.tiecode.plugin.api.project.model.Project
import com.tiecode.plugin.api.project.model.ProjectTemplate
import com.tiecode.plugin.api.project.model.RequireMessages
import java.io.File
import kotlin.concurrent.thread

class LuaProjectTemplate(

) : ProjectTemplate() {
    override fun getName(): String {
        return "AndroLua项目"
    }

    override fun getPicture(): Drawable? {
        return DrawableUtils.getDrawable(PluginApplication.application, R.drawable.androlua)
    }

    override fun getDefaultProjectName(): String {
        return "我的AndroLua项目"
    }

    override fun getRequireMessages(): RequireMessages {
        val messages = RequireMessages()

        messages.addRequireMessage(
            Message(
                Message.Kind.STRING, "appName", "软件名称", "如MyLuaApplication", "MyLuaApplication"
            )
        )

        messages.addRequireMessage(
            Message(
                Message.Kind.STRING, "appPackageName", "软件包名", "如com.lua.app", "com.lua.app"
            )
        )

        return messages
    }

    override fun create(
        project: Project,
        startupKeys: RequireMessages,
        callback: Project.OnProjectCreateListener
    ) {

        //不管了 直到没有官方的thread pool之前都这样
        thread {

            try {
                ZipUtils
                    .unZipAssetsFolder(
                        PluginApplication.application,
                        "androlua_template.zip",
                        project.projectDir.toString()
                    )

                val targetInitFilePath = File(project.projectDir, "src/init.lua")

                var targetInitFileString = targetInitFilePath.readText()

                startupKeys.messages.forEach {
                    targetInitFileString =
                        targetInitFileString.replace("\${" + it.key + "}", it.value.toString())
                }


                targetInitFilePath.writeText(targetInitFileString)

                callback.onSuccess()
            } catch (e: Exception) {
                callback.onFail(e.message)
            }
        }.start()

    }

}