package com.dingyi.tiecode.plugin.androlua.build.base

import com.dingyi.tiecode.plugin.androlua.build.task.CleanUpTask
import com.dingyi.tiecode.plugin.androlua.build.task.CompileLuaTask
import com.dingyi.tiecode.plugin.androlua.build.task.ModifyAndroidManifestTask
import com.dingyi.tiecode.plugin.androlua.build.task.PackageApkTask
import com.dingyi.tiecode.plugin.androlua.build.task.ReadConfigTask
import com.dingyi.tiecode.plugin.androlua.build.task.UnApkResourceTask
import com.dingyi.tiecode.plugin.androlua.project.LuaProject
import com.tiecode.plugin.api.log.Logger
import com.tiecode.plugin.api.project.task.define.BuildVariant
import com.tiecode.plugin.api.project.task.model.Task
import com.tiecode.plugin.api.project.task.performer.ITaskPerformer
import com.tiecode.plugin.api.project.task.performer.ITaskPerformer.ResultListener
import com.tiecode.plugin.api.project.task.performer.TaskBuilder


class LuaTaskBuilder(
    project: LuaProject,
    logger: Logger
//永远都是debug
) : TaskBuilder<LuaProject, Logger>(project, logger, BuildVariant.DEBUG) {

    fun runBuild(listener: ResultListener) {
        super.build(
            listOf(
                ReadConfigTask(),
                UnApkResourceTask(),
                ModifyAndroidManifestTask(),
                CompileLuaTask(),
                PackageApkTask(),
            ), listener
        )
    }


    fun runClean(listener: ResultListener) {
        super.build(
            listOf(
                CleanUpTask()
            ), listener
        )
    }

}