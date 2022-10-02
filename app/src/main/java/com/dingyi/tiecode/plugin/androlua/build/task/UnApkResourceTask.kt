package com.dingyi.tiecode.plugin.androlua.build.task


import com.androlua.LuaUtil
import com.androlua.util.ZipUtil
import com.dingyi.tiecode.plugin.androlua.PluginApplication
import com.dingyi.tiecode.plugin.androlua.build.base.LuaTask
import com.dingyi.tiecode.plugin.androlua.ktx.getJavaClass
import com.tiecode.develop.util.firstparty.zip.ZipUtils
import com.tiecode.plugin.api.log.Logger
import com.tiecode.plugin.api.log.model.LogMessage
import com.tiecode.plugin.api.log.model.TieLogMessage
import com.tiecode.plugin.api.project.model.Project
import com.tiecode.plugin.api.project.task.define.BuildVariant
import com.tiecode.plugin.api.project.task.define.TaskPerformState
import com.tiecode.plugin.api.project.task.model.Task
import java.lang.Exception

class UnApkResourceTask : LuaTask() {
    override fun getName(): String {
        return "解压打包资源"
    }


    override fun initialize(
        project: Project,
        buildVariant: BuildVariant,
        logger: Logger
    ): TaskPerformState {

        super.initialize(project, buildVariant, logger)

        val resourceDir = getBuildDir("resource")


        val resourceFiles = resourceDir.listFiles()

        if (!resourceDir.isDirectory) {
            return TaskPerformState.NORMAL
        }

        //classes.dex 1
        //AndroidManifest.xml 1
        //lua 1
        //lib 1
        //arsc 1
        //res 1

        val matchFileCount = resourceFiles
            ?.filterNotNull()
            ?.count {
                val name = it.name
                name.endsWith("dex") || name == "AndroidManifest.xml" ||
                        name.endsWith("arsc") || name == "lua" ||
                        name == "lib" || name == "res"
            } ?: 0

        if (matchFileCount < 6) {
            return TaskPerformState.NORMAL
        }

        return TaskPerformState.UP_TO_DATE

    }

    override fun perform(): Boolean {


        try {

            val targetDir = getBuildDir("resource")

            runOnUiThread {
                //  logger.log(TieLogMessage(targetDir.toString()))
            }

            targetDir.mkdirs()

            ZipUtils
                .unZipAssetsFolder(
                    PluginApplication.application,
                    "androlua+5.0.19.zip",
                    targetDir.toString()
                )


        } catch (e: Exception) {

            runOnUiThread {

                logger.postLog(
                    TieLogMessage(
                        "无法解压所需的构建资源，项目构建失败",
                        TieLogMessage.ERROR
                    )
                )

                logger.postTrace(e)
            }

            return false
        }


        return true
    }
}