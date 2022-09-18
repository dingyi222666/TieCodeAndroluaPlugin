package com.dingyi.tiecode.plugin.androlua.ktx

inline fun <reified T> getJavaClass(): Class<T> {
    return T::class.java
}