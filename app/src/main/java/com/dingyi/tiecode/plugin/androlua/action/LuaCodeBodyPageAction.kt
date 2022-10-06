package com.dingyi.tiecode.plugin.androlua.action

import android.content.res.AssetManager
import com.dingyi.tiecode.plugin.androlua.PluginApplication
import com.tiecode.develop.component.api.multiple.Window
import com.tiecode.develop.component.widget.editor.TieCodeEditor
import com.tiecode.develop.util.firstparty.net.URIUtils
import com.tiecode.plugin.action.page.code.CodeBodyPageAction
import com.tiecode.plugin.api.log.Logger
import com.tiecode.plugin.api.log.foundation.TiecodeLogger
import org.eclipse.tm4e.core.registry.IGrammarSource
import org.eclipse.tm4e.core.registry.IThemeSource
import java.io.IOException
import java.io.InputStreamReader
import java.net.URI


class LuaCodeBodyPageAction : CodeBodyPageAction() {
    override fun onWindowCreate(window: Window, uri: URI) {
        val suffix = URIUtils.getURISuffix(uri)
        if ((suffix == "lua") or (suffix == "aly") and (window is TieCodeEditor)) {
            setCustomHighlighter(window as TieCodeEditor)
        }
        if (window is TieCodeEditor) {
            setCustomHighlighter(window)
        }
    }




    private fun setCustomHighlighter(editor: TieCodeEditor) {
        try {
            val assets = PluginApplication.application.assets

            loadTextMateGrammar(
                editor, "lua.tmLanguage.json",
                assets.open("textmate/lua/lua.tmLanguage.json"),
                InputStreamReader(assets.open("textmate/lua/language-configuration.json"))
            )


        } catch (e: IOException) {
            throw e;
        }
    }



}