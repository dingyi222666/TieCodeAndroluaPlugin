package com.dingyi.tiecode.plugin.androlua.build.task

import com.dingyi.tiecode.plugin.androlua.build.base.LuaTask
import com.tiecode.plugin.api.log.model.TieLogMessage

class CleanUpTask: LuaTask() {
    override fun getName() = "清除缓存"

    override fun perform(): Boolean {
        try {
            getBuildDir().deleteRecursively()
        } catch (e:Exception) {
            runOnUiThread {

                logger.postLog(
                    TieLogMessage(
                        "出现错误，无法清理缓存！",
                        TieLogMessage.ERROR
                    )
                )

                logger.postTrace(e)
            }
            return false;
        }
        return true
    }
}