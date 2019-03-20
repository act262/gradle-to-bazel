package io.zcx.plugin

import com.android.build.gradle.AppExtension
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.apache.velocity.app.VelocityEngine
import org.gradle.api.Project

import java.nio.file.Files
import java.nio.file.Paths

public class TemplateGen {

    static VelocityEngine engine;

    static {
        engine = new VelocityEngine()
        engine.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, 'buildSrc/src/main/resources/templates');
        engine.init()
    }

    /**
     * 生成 WORKSPACE 文件
     */
    public static void generateFileWORKSPACE() {
        def context = new VelocityContext()
        // FIXME: should get those env from project's settings
        context.put('build_tools_version', '28.0.3')
        context.put('api_level', '28')

        def writer = new PrintWriter("WORKSPACE")

        engine.mergeTemplate('WORKSPACE.ftl', 'UTF-8', context, writer)

        writer.close()
    }

    private static void globDependencies(Project project) {
        project.configurations.implementation.setCanBeResolved(true)
        project.configurations.api.setCanBeResolved(true)

        // glob all artifact files
        def resolvedFiles = project.configurations.implementation.resolve() + project.configurations.api.resolve()

        def libsDir = new File("${project.buildDir}/bazel/deps")
        if (!libsDir.exists()) {
            libsDir.mkdirs()
        }

        resolvedFiles.each {
            def link = Paths.get("${libsDir}/${it.name}")
            if (Files.notExists(link)) {
                println " $it <<---- link to ---->>  ${link}"
                Files.createSymbolicLink(link, it.toPath())
            }
        }
    }

    public static void genAppBuild(Project project) {
        globDependencies(project)

        def android = project.extensions.android as AppExtension

        def context = new VelocityContext()
        context.put('name', project.name)
        context.put('applicationName', project.name)
        context.put('applicationId', android.defaultConfig.applicationId)
        // for AndroidManifest value
        context.put('minSdkVersion', 16)
        context.put('maxSdkVersion', 28)
        context.put('versionCode', android.defaultConfig.versionCode)
        context.put('versionName', android.defaultConfig.versionName)

        context.put('buildToolsVersion', android.buildToolsVersion)
        context.put('compileSdkVersion', android.compileSdkVersion)

        context.put('manifestFile', 'src/main/AndroidManifest.xml')

        def srcDirs = ''
        android.sourceSets.main.java.srcDirs.each { File dir ->
            // src/main/java/**
            def srcDir = dir.path.replaceFirst(project.projectDir.path, "")
                    .replaceFirst("/", "")
            srcDirs += "'${srcDir}/**',"
        }
        srcDirs = "glob([$srcDirs])"
        context.put('srcs', srcDirs)

        def resDirs = ''
        android.sourceSets.main.res.srcDirs.each { File dir ->
            // src/main/res/**
            def resDir = dir.path.replaceFirst(project.projectDir.path, "")
                    .replaceFirst("/", "")
            resDirs += "'$resDir/**',"
        }
        resDirs = "glob([$resDirs])"
        context.put('res', resDirs)

        context.put('depsDir',"build/bazel/deps")

        def writer = new PrintWriter("${project.projectDir}/BUILD.bazel")
        engine.mergeTemplate("BUILD_application.ftl", "UTF-8", context, writer)
        writer.close()
    }

    public static void genLibraryBuild(Project project) {
        globDependencies(project)
//        def android = libraryPlugin.extension as LibraryExtension

        def context = new VelocityContext()

        def writer = new PrintWriter("${project.projectDir}/BUILD.bazel")
        engine.mergeTemplate("BUILD_library.ftl", "UTF-8", context, writer)
        writer.close()
    }
}