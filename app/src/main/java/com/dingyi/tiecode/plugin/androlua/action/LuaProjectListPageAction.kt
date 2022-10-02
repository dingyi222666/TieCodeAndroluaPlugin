package com.dingyi.tiecode.plugin.androlua.action

import android.widget.Toast
import com.tiecode.develop.component.api.option.TieItem
import com.tiecode.develop.component.api.option.TieMenu
import com.tiecode.develop.component.widget.option.TieMenuItem
import com.tiecode.plugin.action.page.project.ProjectListPageAction


class LuaProjectListPageAction:ProjectListPageAction() {

    companion object {

        private val MENU_TITLE = "构建项目"
        private val MENU_ID = 100
    }

    override fun onCreateProjectMenu(menu: TieMenu) {
       /* menu.addItem(TieMenuItem(MENU_TITLE, MENU_ID))*/
    }

    override fun onProjectMenuClick(item: TieItem) {
      /*  if (item.id == MENU_ID) {
            Toast.makeText(activity, MENU_TITLE, Toast.LENGTH_SHORT).show()
        }*/
    }
}