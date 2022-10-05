package com.dingyi.tiecode.plugin.androlua.build.task

import com.dingyi.tiecode.plugin.androlua.PluginApplication
import com.dingyi.tiecode.plugin.androlua.build.base.LuaTask
import com.dingyi.tiecode.plugin.androlua.build.base.createKeyValueData
import com.dingyi.tiecode.plugin.androlua.data.KeyValueData
import com.dingyi.tiecode.plugin.androlua.ktx.md5
import com.tiecode.plugin.api.log.Logger
import com.tiecode.plugin.api.log.model.TieLogMessage
import com.tiecode.plugin.api.project.model.Project
import com.tiecode.plugin.api.project.task.define.BuildVariant
import com.tiecode.plugin.api.project.task.define.TaskPerformState
import java.io.File

class ModifyAndroidManifestTask : LuaTask() {
    override fun getName() = "生成清单文件"

    private lateinit var compileHistoryData: KeyValueData<String, String>

    private lateinit var initFile: File

    private lateinit var modifyAndroidManifestFile: File

    override fun initialize(
        project: Project,
        buildVariant: BuildVariant,
        logger: Logger
    ): TaskPerformState {
        super.initialize(project, buildVariant, logger)


        compileHistoryData = createKeyValueData("compileHistory")

        initFile = getSrcDir("init.lua")

        modifyAndroidManifestFile = getBuildDir("resource/AndroidManifest_modified.xml")


        val needGenerated = initFile.md5 == compileHistoryData.get(initFile.absolutePath) &&
                modifyAndroidManifestFile.isFile && modifyAndroidManifestFile.md5 ==
                compileHistoryData.getOrNull(modifyAndroidManifestFile.absolutePath)


        return if (!needGenerated) TaskPerformState.NORMAL else TaskPerformState.UP_TO_DATE
    }


    //TODO: 修改aml
    override fun perform(): Boolean {


        try {
            getBuildDir("resource/AndroidManifest.xml").copyTo(modifyAndroidManifestFile, true)
            compileHistoryData.put(
                modifyAndroidManifestFile.absolutePath,
                modifyAndroidManifestFile.md5
            )
            compileHistoryData.updateAll()
            return true
        } catch (e: Exception) {
            runOnUiThread {

                logger.postLog(
                    TieLogMessage(
                        "生成清单文件时出错：",
                        TieLogMessage.ERROR
                    )
                )

                logger.postTrace(e)
            }
            return false
        }
    }
}