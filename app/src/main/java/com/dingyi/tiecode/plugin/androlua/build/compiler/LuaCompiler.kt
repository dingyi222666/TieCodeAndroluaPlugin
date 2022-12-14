package com.dingyi.tiecode.plugin.androlua.build.compiler

import com.dingyi.tiecode.plugin.androlua.PluginApplication
import java.io.File


object LuaCompiler {

    private val globalLuaVM = PluginApplication.luaVm

    var isInit = false

    /**
     * Compile a lua script to bytecode.
     */
    fun compile(scriptPath: String, outputPath: String): Boolean {
        try {

            val tmpOutputPath = "$outputPath.tmp"

            if (!isInit) {
                globalLuaVM.init()
                //compile lua script to bytecode using lua src string
                globalLuaVM.doString("function compile(from,to) local f = io.open(from,\"r\") local t = io.open(to,\"w\") local s = f:read(\"*a\") f:close() local func,e = load(s,\"\") if func==nil and e!=null then return e end t:write(string.dump(func)) t:close() return nil end")
                isInit = true
            }

            //run compile func im lua vm to compile lua
            val errorString = globalLuaVM.runFunc("compile", scriptPath, tmpOutputPath)


            //rename tmp file to output file
            val tmpFile = File(tmpOutputPath)
            val outputFile = File(outputPath)
            tmpFile.copyTo(outputFile, true)
            tmpFile.delete()

            if (errorString is String) {
                tmpFile.delete()
                outputFile.delete()
                error(errorString)
            }

            return true
        } catch (e: Exception) {
            throw e
        }
    }
}