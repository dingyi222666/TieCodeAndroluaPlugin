import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.dingyi.tiecode.plugin.androlua"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/kotlin")/* resources.srcDirs("src/main/res")*/

        }
        getByName("test") {
            java.srcDirs("src/test/kotlin")
            resources.srcDirs("src/test/resources")
        }
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }

        debug {
            isMinifyEnabled = false
            ndk {
//                this.abiFilters.addAll(arrayOf("armeabi-v7a", "arm64-v8a"))
            }
        }
    }
    kotlinOptions {
        jvmTarget = "11"
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildOutputs.all {
        val output = this
        if (output is BaseVariantOutputImpl) {
            runCatching {
                output.outputFileName = "${rootProject.name}_${this.name}.tpk"
            }.onFailure {
                println(it)
                output.outputFileName = "${this.name}.apk"
            }
        }
    }

}

fun String.execute() {

    println("待执行命令:${this}\n\n")

    val processBuilder = ProcessBuilder("cmd", "/c", this)

    processBuilder.redirectErrorStream(true)

    val process = processBuilder.start()


    val reader = BufferedReader(InputStreamReader(process.inputStream, StandardCharsets.UTF_8))


    while (process.isAlive) {
        while (reader.ready()) {
            val s = reader.readLine();
            println("输出结果:${s}\n")
        }
    }


    val status = process.waitFor()

    if (status != 0) {
        error("\n\n执行失败，状态码为:${status}")
    }

    reader.close()

}


project.afterEvaluate {

    project.android.applicationVariants.all {

        val variant = this

        val variantsName = variant.name.capitalize()

        val installPluginTask = tasks.create("install${variantsName}Plugin") {

            doLast {

                println("请在安装了一次插件之后在使用该task\n\n")

                if (!org.gradle.internal.os.OperatingSystem.current().isWindows) {
                    println("只支持windows")
                    return@doLast
                }

                val outputName = (variant.outputs.filter {
                    it is BaseVariantOutputImpl && it.outputFileName.endsWith("tpk")
                }.get(0) as BaseVariantOutputImpl).outputFileName


                val buildOutputPath =
                    project.file("build/outputs/apk/debug/").resolve(outputName).absolutePath


                val pushOutputPath =
                    "/storage/emulated/0/Android/data/com.tiecode.develop/files/plugins/${variant.applicationId}"


                "adb push \"$buildOutputPath\" \"$pushOutputPath\"".execute()

                "adb shell am force-stop com.tiecode.develop".execute()

                "adb shell am start com.tiecode.develop/com.tiecode.develop.page.module.splash.Splash".execute()
            }


        }

        installPluginTask.group = "install"

        installPluginTask.dependsOn("assemble${variantsName}")


    }
}




dependencies {

    compileOnly(files("libs/tiecode-plugin-api-1.0.0.jar"))

    implementation("io.github.dingyi222666:androlua-standlone:1.0.4")


}



