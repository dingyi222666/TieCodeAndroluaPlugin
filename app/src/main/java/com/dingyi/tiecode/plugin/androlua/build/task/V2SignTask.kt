package com.dingyi.tiecode.plugin.androlua.build.task

import com.dingyi.tiecode.plugin.androlua.build.base.LuaTask
import com.tiecode.platform.util.sign.APKSigner
import com.tiecode.platform.util.sign.model.KeyParams
import com.tiecode.platform.util.sign.model.KeyStoreParams
import com.tiecode.plugin.api.log.Logger
import com.tiecode.plugin.api.project.model.Project
import com.tiecode.plugin.api.project.task.define.BuildVariant
import com.tiecode.plugin.api.project.task.define.TaskPerformState

class V2SignTask : LuaTask() {
    override fun getName(): String = "执行V2签名"

    override fun initialize(
        project: Project,
        buildVariant: BuildVariant,
        logger: Logger
    ): TaskPerformState {
        super.initialize(project, buildVariant, logger)

        val needV2Sign = taskContext.getKey("v2Sign")



        if (needV2Sign !is Boolean || needV2Sign != true) {
            return TaskPerformState.NO_SOURCE;
        }

        return TaskPerformState.NORMAL;
    }

    override fun perform(): Boolean {
        //TODO
       return true
    }

}