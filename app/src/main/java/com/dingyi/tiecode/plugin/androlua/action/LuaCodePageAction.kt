package com.dingyi.tiecode.plugin.androlua.action

import com.dingyi.tiecode.plugin.androlua.PluginApplication
import com.dingyi.tiecode.plugin.androlua.ktx.getJavaClass
import com.tiecode.plugin.action.component.task.TaskPerformerAction
import com.tiecode.plugin.action.page.code.CodePageAction
import com.tiecode.plugin.api.project.ProjectContext
import com.tiecode.plugin.api.project.task.performer.TaskPerformer

class LuaCodePageAction : CodePageAction() {

    override fun onLoadProject() {

        val currentProject = ProjectContext.getCurrentProject()

        val actionController = PluginApplication
            .application
            .appliedActionController

        if (currentProject.classificationId == (0x53).toString()) {


            val taskPerformer =
                actionController.getAction(getJavaClass<TaskPerformerAction>()).actionable


            initBuildTask(taskPerformer)

        }

    }

    private fun initBuildTask(taskPerformer: TaskPerformer) {
       /* taskPerformer.setTasks(

        )*/
    }
}