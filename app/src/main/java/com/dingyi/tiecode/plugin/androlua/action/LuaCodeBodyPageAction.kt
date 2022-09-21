package com.dingyi.tiecode.plugin.androlua.action

import com.tiecode.develop.component.api.multiple.Window
import com.tiecode.develop.component.widget.editor.TieCodeEditor
import com.tiecode.develop.util.firstparty.net.URIUtils
import com.tiecode.plugin.action.page.code.CodeBodyPageAction
import java.net.URI


class LuaCodeBodyPageAction : CodeBodyPageAction() {
    override fun onWindowCreate(window: Window, uri: URI) {
        val suffix = URIUtils.getURISuffix(uri)
        if ("html" != suffix) {
            return
        }
        if (window is TieCodeEditor) {
            //setLanguage(window as TieCodeEditor)
        }
    }


    private fun setLanguage(editor: TieCodeEditor) {

    }
}