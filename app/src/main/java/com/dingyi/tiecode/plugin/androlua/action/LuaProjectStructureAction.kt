package com.dingyi.tiecode.plugin.androlua.action

import android.R
import com.dingyi.tiecode.plugin.androlua.PluginApplication
import com.dingyi.tiecode.plugin.androlua.project.LuaProject
import com.tiecode.develop.component.api.tree.TreeItemViewHolder
import com.tiecode.develop.component.api.tree.TreeNode
import com.tiecode.develop.util.firstparty.image.DrawableUtils
import com.tiecode.plugin.action.page.code.ProjectFilePageAction
import com.tiecode.plugin.api.project.ProjectContext
import com.tiecode.plugin.api.project.structure.PSObject
import java.io.File


class LuaProjectStructureAction : ProjectFilePageAction() {
    private lateinit var project: LuaProject

    override fun onCreate() {

        loadProjectStructureDefault()

        addSupportOpenFileTypes(".lua", ".aly")

        registerPSObjectFilter {
            it.isDirectory && it.name == ".tiecode"
        }

      /*  registerPSObjectFilter {
           it.name == "project.json"
        }*/

        project = ProjectContext.getCurrentProject() as LuaProject
        expandPath(project.name)
    }




    override fun onFileNodeViewBind(holder: TreeItemViewHolder, node: TreeNode<PSObject>) {

    }
}