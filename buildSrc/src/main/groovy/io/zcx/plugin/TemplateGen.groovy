package io.zcx.plugin

import com.android.build.gradle.AppExtension
import it.unimi.dsi.fastutil.Hash
import org.apache.commons.io.FilenameUtils
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.apache.velocity.app.VelocityEngine
import org.gradle.api.Project
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.artifacts.ResolvedDependency

import java.nio.file.Files
import java.nio.file.Path
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

    private static void globDependencies(Project project, Writer writer) {

        def depsDir = new File("${project.projectDir}/build/bazel/deps")
        if (!depsDir.exists()) depsDir.mkdirs()

        def aar_import = new File("${project.projectDir}/build/bazel/aar_import")
        if (!aar_import.exists()) aar_import.mkdirs()


//        project.configurations.implementation.setCanBeResolved(true)
//        project.configurations.api.setCanBeResolved(true)

        Set<ResolvedDependency> resolvedDependencySet = project.configurations.implementation.resolvedConfiguration.firstLevelModuleDependencies + project.configurations.api.resolvedConfiguration.firstLevelModuleDependencies
        def resolvedArtifactSet = new HashSet<ResolvedArtifact>(20)

        resolvedDependencySet.each {
            resolvedArtifactSet += it.allModuleArtifacts
        }

        // Put all dependency file here
        resolvedArtifactSet.each {
            def file = it.file
            def link = linkPath(project, it)

            if (Files.notExists(link)) {
                println " $it <<---- link to ---->>  ${link}"
                Files.createSymbolicLink(link, file.toPath())
            }
        }

        // gen aar_import rules
//        resolvedDependencySet.each {
//            def children = it.children
//            it.moduleArtifacts
//
//            if (isAarFile(it.moduleArtifacts[0].file)) {
//                genRules_aar_import(project, it)
//            }
//        }

        resolvedDependencySet.each {
            flatDep(it)
        }

        Set<ResolvedAarDependency> resolvedAarDependencySet = new HashSet<>(20)

        flatResolvedDependency.each { resolvedDependency ->
            if (isAarFile(resolvedDependency.moduleArtifacts[0].file)) {
                def aarDep = ResolvedAarDependency.wrap(resolvedDependency)
                println "${resolvedDependency} is a AndroidArchive file ======> ${aarDep}"
                resolvedAarDependencySet.add(aarDep)
            } else if (isJarFile(resolvedDependency.moduleArtifacts[0].file)) {
                println "${resolvedDependency} is Jar "
            } else {
                println "${resolvedDependency} Unknow ...."
            }
        }
        resolvedAarDependencySet.sort().each {
            def resolvedDependency = it.resolvedDependency

            def name = getTargetName(resolvedDependency)
            println name

            def context = new VelocityContext()
            context.put('aarName', name)
            context.put('aarTarget', getTargetPath(project.projectDir, linkFile(project, resolvedDependency.moduleArtifacts[0])))

            def childrenArtifacts = resolvedDependency.allModuleArtifacts - resolvedDependency.moduleArtifacts
            def aarExports = ''

            resolvedDependency.children.each {
                if (isAarFile(it.moduleArtifacts[0].file)) {
                    aarExports += "'${getTargetName(it)}',\n"
                }
            }
            context.put('aarExports', aarExports)
//                engine.mergeTemplate("aar_import.ftl", "UTF-8", context, writer)
            def s = '''
aar_import(
    name = '$aarName',
    aar = '$aarTarget',
    exports = [
        $aarExports
    ]
)
'''
            engine.evaluate(context, writer, "eeeeee", s)

        }
    }

    static flatResolvedDependency = new HashSet<ResolvedDependency>()

    static void flatDep(ResolvedDependency resolvedDependency) {
        flatResolvedDependency.add(resolvedDependency)

        resolvedDependency.children.each {
            flatDep(it)
        }
    }

    public static boolean isJarFile(File file) {
        FilenameUtils.getExtension(file.path) == 'jar'
    }

    public static boolean isAarFile(File file) {
        FilenameUtils.getExtension(file.path) == 'aar'
    }

    public static Path linkPath(Project project, ResolvedArtifact resolvedArtifact) {
        def depsDir = new File("${project.buildDir}/bazel/deps")
        if (!depsDir.exists()) {
            depsDir.mkdirs()
        }

        Paths.get("${depsDir}/${resolvedArtifact.id.componentIdentifier}_${resolvedArtifact.file.name}"
                .replace(':', '_'))
    }

    public static File linkFile(Project project, ResolvedArtifact resolvedArtifact) {
        linkPath(project, resolvedArtifact).toFile()
    }

    public static String getTargetPath(File dir, File file) {
        file.path.replaceFirst(dir.path, "").replaceFirst("/", "")
    }

    public static String getTargetName(ResolvedDependency resolvedDependency) {
        resolvedDependency.name.replace(':', '_')
    }

    public static void genRules_aar_import(Project project, ResolvedDependency resolvedDependency) {
        def name = getTargetName(resolvedDependency)

        def context = new VelocityContext()
        context.put('aarName', name)
        context.put('aarTarget', getTargetPath(project.projectDir, linkFile(project, resolvedDependency.moduleArtifacts[0])))

        def childrenArtifacts = resolvedDependency.allModuleArtifacts - resolvedDependency.moduleArtifacts
        def aarExports = ''

        resolvedDependency.children.each {
            aarExports += "'${getTargetName(it)}',\n"
        }
        context.put('aarExports', aarExports)
        def writer = new PrintWriter("${project.projectDir}/build/bazel/aar_import/${name}.bazel")
        engine.mergeTemplate("aar_import.ftl", "UTF-8", context, writer)
        writer.close()
    }

    public static void genAppBuild(Project project) {

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


        context.put('depsDir', "build/bazel/deps")

        project.configurations.implementation.setCanBeResolved(true)
        project.configurations.api.setCanBeResolved(true)

        Set<ResolvedDependency> resolvedDependencySet = project.configurations.implementation.resolvedConfiguration.firstLevelModuleDependencies + project.configurations.api.resolvedConfiguration.firstLevelModuleDependencies
        def aarDeps = '\n'
        resolvedDependencySet.each {
            if (isAarFile(it.moduleArtifacts[0].file)) {
                aarDeps += "'${getTargetName(it)}',\n"
            }
        }
        context.put('aarDeps', aarDeps)
        def writer = new PrintWriter("${project.projectDir}/BUILD.bazel")
        engine.mergeTemplate("BUILD_application.ftl", "UTF-8", context, writer)

        globDependencies(project, writer)

        writer.close()
    }

    public static void genLibraryBuild(Project project) {
//        globDependencies(project)
////        def android = libraryPlugin.extension as LibraryExtension
//
//        def context = new VelocityContext()
//
//        def writer = new PrintWriter("${project.projectDir}/BUILD.bazel")
//        engine.mergeTemplate("BUILD_library.ftl", "UTF-8", context, writer)
//        writer.close()
    }
}