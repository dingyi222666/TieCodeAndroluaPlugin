package com.dingyi.tiecode.plugin.androlua.build.base


import com.dingyi.tiecode.plugin.androlua.PluginApplication
import com.dingyi.tiecode.plugin.androlua.build.context.BuildContext
import com.dingyi.tiecode.plugin.androlua.project.LuaProject
import com.tiecode.plugin.api.log.Logger
import com.tiecode.plugin.api.log.model.LogMessage
import com.tiecode.plugin.api.log.model.TieLogMessage
import com.tiecode.plugin.api.project.model.Project
import com.tiecode.plugin.api.project.task.define.BuildVariant
import com.tiecode.plugin.api.project.task.define.TaskPerformState
import com.tiecode.plugin.api.project.task.model.Task
import java.io.File
import java.lang.Exception

abstract class LuaTask : Task() {

    lateinit var taskContext: BuildContext
        private set

    lateinit var logger: Logger
        private set

    lateinit var project: LuaProject
        private set


    fun getBuildDir(): File {
        return project.projectDir.resolve("build")
    }

    fun getBuildDir(path: String): File {
        return getBuildDir().resolve(path)
    }


    override fun initialize(
        project: Project,
        buildVariant: BuildVariant,
        logger: Logger
    ): TaskPerformState {
        if (project is LuaProject) {
            taskContext = project
            this.project = project
        }
        this.logger = logger
        return TaskPerformState.NORMAL
    }

    /**
     * If you don't need to supply a return value at the end of the task, override this method to implement your own execution logic
     */
    override fun perform(): Boolean {
        try {
            performWithResult()?.let {
                taskContext.putTaskResult(it)
            }
            return true
        } catch (e: Exception) {
            logger.log(TieLogMessage("执行${name}任务失败，抛出错误", TieLogMessage.ERROR))
            logger.postTrace(e)
            return false;
        }
    }


    fun runOnUiThread(runnable: Runnable) {
        PluginApplication.application.runOnUiThread(runnable)
    }

    @SuppressWarnings
    fun performWithResult(): Any? {
        return null
    }
}