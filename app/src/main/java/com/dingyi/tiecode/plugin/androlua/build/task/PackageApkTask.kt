package com.dingyi.tiecode.plugin.androlua.build.task

import com.dingyi.tiecode.plugin.androlua.PluginApplication
import com.dingyi.tiecode.plugin.androlua.build.apkbuilder.ApkBuilder
import com.dingyi.tiecode.plugin.androlua.build.apkbuilder.SignatureReader
import com.dingyi.tiecode.plugin.androlua.build.base.LuaTask
import com.dingyi.tiecode.plugin.androlua.ktx.substringPath
import com.tiecode.develop.util.firstparty.android.ProviderUtils
import com.tiecode.plugin.api.log.model.TieLogMessage

class PackageApkTask : LuaTask() {
    override fun getName() = "生成Apk文件"


    //TODO: welcome.png 和 icon.png的自定义
    override fun perform(): Boolean {

        val outputApk = getBuildDir("output/app.apk")

        outputApk.apply {
            parentFile?.mkdirs()
            createNewFile()
        }

        val apkBuilder = ApkBuilder(
            getBuildDir("output/app.apk")
        )

        apkBuilder.setSignature(SignatureReader.readSignatureForAssets("testkey"))

        apkBuilder.start()

        val resourceDir = getBuildDir("resource")

        //底包
        resourceDir.walk()
            .filterNot {
                it.name == "AndroidManifest.xml" || it.name == "AndroidManifest_modified.xml" || it.isDirectory || it.substringPath(
                    resourceDir
                ).startsWith("assets")
            }
            .forEach {
                apkBuilder.addFile(
                    it,
                    it.substringPath(resourceDir)
                )
            }

        //AndroidManifest.xml
        apkBuilder.addFile(
            resourceDir.resolve("AndroidManifest_modified.xml"),
            "AndroidManifest.xml"
        )

        val codeDir = getBuildDir("compiled")


        codeDir
            .walk()
            .filterNot { it.isDirectory }
            .forEach {
                apkBuilder.addFile(it, "assets/" + it.substringPath(codeDir))
            }


        val srcDir = getSrcDir()

        srcDir
            .walk()
            .filterNot { it.extension == "lua" || it.extension == "aly" || it.isDirectory }
            .forEach {
                apkBuilder.addFile(it, "assets/" + it.substringPath(srcDir))
            }


        val iconFile = getSrcDir("icon.png")

        if (iconFile.isFile) {
            apkBuilder.addFile(iconFile, "res/drawable/icon.png")
        }


        val welcomeFile = getSrcDir("welcome.png")

        if (welcomeFile.isFile) {
            apkBuilder.addFile(iconFile, "res/drawable/welcome.png")
        }


        apkBuilder.finish()


        runOnUiThread {
            logger.log(TieLogMessage("已生成apk到 ${outputApk.path}"))
        }

        ProviderUtils.installAPK(PluginApplication.tiecodeContext,outputApk.path)

        return true

    }
}