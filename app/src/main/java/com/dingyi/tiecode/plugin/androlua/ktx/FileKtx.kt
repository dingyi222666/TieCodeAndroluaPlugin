package com.dingyi.tiecode.plugin.androlua.ktx

import com.androlua.LuaUtil
import java.io.File

val File.md5: String
    get() = LuaUtil.getFileMD5(this)

fun File.substringPath(other: File): String {
    val otherPath = other.absolutePath
    return absolutePath.substring(otherPath.length + 1)
}