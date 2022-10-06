package com.dingyi.tiecode.plugin.androlua.build.task

import android.util.Xml
import android2.content.Context
import com.dingyi.tiecode.plugin.androlua.PluginApplication
import com.dingyi.tiecode.plugin.androlua.build.base.LuaTask
import com.dingyi.tiecode.plugin.androlua.build.base.createKeyValueData
import com.dingyi.tiecode.plugin.androlua.data.KeyValueData
import com.dingyi.tiecode.plugin.androlua.ktx.md5
import com.dingyi.xml2axml.AXMLPrinter
import com.dingyi.xml2axml.Encoder
import com.dingyi.xml2axml.format.XmlFormatter
import com.tiecode.plugin.api.log.Logger
import com.tiecode.plugin.api.log.model.TieLogMessage
import com.tiecode.plugin.api.project.model.Project
import com.tiecode.plugin.api.project.task.define.BuildVariant
import com.tiecode.plugin.api.project.task.define.TaskPerformState
import org.dom4j.Element
import org.dom4j.io.OutputFormat
import org.dom4j.io.SAXReader
import org.dom4j.io.XMLWriter
import org.xmlpull.v1.XmlPullParser
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File

class ModifyAndroidManifestTask : LuaTask() {
    override fun getName() = "生成清单文件"

    private lateinit var compileHistoryData: KeyValueData<String, String>

    private lateinit var initFile: File

    private lateinit var modifyAndroidManifestFile: File

    override fun initialize(
        project: Project,
        buildVariant: BuildVariant,
        logger: Logger
    ): TaskPerformState {
        super.initialize(project, buildVariant, logger)


        compileHistoryData = createKeyValueData("compileHistory")

        initFile = getSrcDir("init.lua")

        modifyAndroidManifestFile = getBuildDir("resource/AndroidManifest_modified.xml")


        val needGenerated = initFile.md5 == compileHistoryData.get(initFile.absolutePath) &&
                modifyAndroidManifestFile.isFile && modifyAndroidManifestFile.md5 ==
                compileHistoryData.getOrNull(modifyAndroidManifestFile.absolutePath)


        return if (!needGenerated) TaskPerformState.NORMAL else TaskPerformState.UP_TO_DATE
    }


    //TODO: 修改aml
    override fun perform(): Boolean {

        try {

            val sourceManifestXml = getBuildDir("resource/AndroidManifest.xml")


            val byteArrayOutputStream = ByteArrayOutputStream()
            AXMLPrinter.decode(sourceManifestXml.inputStream(), byteArrayOutputStream)

            val xmlPullParser = Xml.newPullParser()

            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true)

            XmlFormatter
                .usePullParser(xmlPullParser)

            val formatted =
                XmlFormatter
                    .format(byteArrayOutputStream.toByteArray().decodeToString())

            val saxReader = SAXReader()
            //saxReader.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,true)
            val document = saxReader.read(ByteArrayInputStream(formatted.encodeToByteArray()))

            val manifestElement = document.rootElement

            //必备属性，没有直接报错(在前面的列表里面)

            manifestElement
                .attribute("package")
                .value = taskContext.getKey("packagename").toString()

            manifestElement
                .attribute("versionCode")
                .value = taskContext.getKey("appcode").toString()

            manifestElement
                .attribute("versionName")
                .value = taskContext.getKey("appver").toString()


            (document.selectNodes("//provider").get(0)
                    as Element).attribute("authorities")
                .value = taskContext.getKey("packagename").toString()

            document.selectNodes("//@android:label").forEach {
                it.text = taskContext.getKey("appname").toString()
            }


            val permissionList = taskContext.getKey("user_permission") as Map<*, String>?


            if (permissionList != null) {

                val permissionValues = permissionList.values.toMutableList()

                document.selectNodes("//uses-permission").forEach {
                    if (it is Element) {
                        if (permissionValues.isNotEmpty()) {
                            it.attribute("name").value =
                                "android.permission.${permissionValues.removeFirst()}"
                        } else {
                            manifestElement.remove(it)
                        }
                    }
                }

                permissionValues.forEach { permissionString ->
                    manifestElement.addElement("uses-permission")
                        .addAttribute("android:name", "android.permission.$permissionString")
                }
            }

            val minSdk = taskContext.getKey("minsdk")

            val targetsdk = taskContext.getKey("targetsdk")

            val usesSdkElement = document.selectNodes("//uses-sdk").get(0) as Element

            if (minSdk != null) {
                usesSdkElement.attribute("minSdkVersion").value = minSdk.toString()
            }

            if (targetsdk != null) {
                usesSdkElement.attribute("targetSdkVersion").value = targetsdk.toString()
            }


            val format = OutputFormat.createPrettyPrint()

            format.isNewlines = true
            format.isTrimText = false

            format.encoding = "utf-8"

            val outputStream = ByteArrayOutputStream()

            val writer = XMLWriter(outputStream, format)

            writer.write(document)

            writer.flush()

            val encodeXml =
                Encoder().encodeString(Context(), outputStream.toByteArray().decodeToString())

            modifyAndroidManifestFile.writeBytes(encodeXml)


            compileHistoryData.put(
                modifyAndroidManifestFile.absolutePath,
                modifyAndroidManifestFile.md5
            )
            compileHistoryData.updateAll()
            return true
        } catch (e: Exception) {
            runOnUiThread {

                logger.postLog(
                    TieLogMessage(
                        "生成清单文件时出错：",
                        TieLogMessage.ERROR
                    )
                )

                logger.postTrace(e)
            }
            return false
        }
    }
}