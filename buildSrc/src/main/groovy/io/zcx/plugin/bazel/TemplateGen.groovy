package io.zcx.plugin.bazel

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.tasks.ManifestProcessorTask
import io.zcx.plugin.bazel.model.ResolvedAarDependency
import io.zcx.plugin.bazel.util.AndroidUtils
import io.zcx.plugin.bazel.util.BazelUtils
import io.zcx.plugin.bazel.util.DependenciesUtils
import io.zcx.plugin.bazel.util.FileUtils
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.apache.velocity.app.VelocityEngine
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.artifacts.ResolvedDependency

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
    static void genWorkspace(Project project) {
        def context = new VelocityContext()
        context.put('workspace', BazelUtils.getWorkspaceName(project))
        // FIXME: should get those env from project's settings
        context.put('path', '~/Library/Android/sdk')
        context.put('build_tools_version', '28.0.3')
        context.put('api_level', '28')

        context.put('kotlin', true)

        def writer = new PrintWriter(BazelUtils.getWorkspaceFile(project))

        engine.mergeTemplate('WORKSPACE.ftl', 'UTF-8', context, writer)

        writer.close()
    }

    static void genAppBuild(Project project, ApplicationVariant variant) {
        def android = project.extensions.android as AppExtension

        def context = new VelocityContext()
        context.put('kotlin', AndroidUtils.hasKotlinSupport(project))


        def androidSourceSets = AndroidUtils.collectSourceSet(android, variant)

        def originManifestFile = android.sourceSets.main.manifest.srcFile

        def variantName = variant.name.capitalize()
        ManifestProcessorTask processManifestTask = project.tasks["process${variantName}Manifest"]
        def manifestFile

        if (Versions.AGP_CURRENT >= Versions.AGP_3_3_0) {
            // Provider<RegularFile>
            manifestFile = processManifestTask.manifestOutputFile.get().getAsFile()
        } else if (Versions.AGP_CURRENT >= Versions.AGP_3_1_0) {
            // 3.1.0 ~ 3.2.x             // File
            manifestFile = new File(processManifestTask.manifestOutputDirectory, 'AndroidManifest.xml')
        } else {
            throw new GradleException("Not support lower AGP, current " + Versions.AGP_CURRENT)
        }

        def manifestParser = new XmlParser(false, false)
                .parse(manifestFile)
        def label = manifestParser.application."@android:label"
        def minSdkVersion = manifestParser.'uses-sdk'.'@android:minSdkVersion'.text()
        def maxSdkVersion = manifestParser.'uses-sdk'.'@android:targetSdkVersion'.text()

        // package from origin AndroidManifest's package field
        def custom_package = new XmlParser(false, false).parse(originManifestFile).@package

        context.put('name', BazelUtils.getBazelTargetName(project))
        context.put('applicationName', label)
        context.put('applicationId', variant.applicationId)
        context.put('package', custom_package)

        // for AndroidManifest value
        context.put('minSdkVersion', minSdkVersion)
        context.put('maxSdkVersion', maxSdkVersion)
        context.put('versionCode', variant.versionCode)
        context.put('versionName', variant.versionName)

        context.put('buildToolsVersion', android.buildToolsVersion)
        context.put('compileSdkVersion', android.compileSdkVersion)

        context.put('manifest', BazelUtils.getProjectTargetPath(project, manifestFile))


        def srcDirs = ''
        androidSourceSets.collect { it.java.srcDirs }.flatten().each { File dir ->
            // src/main/java/**
            def srcDir = BazelUtils.getTargetPath(project.projectDir, dir)
            srcDirs += "'${srcDir}/**',"
        }

        // Append annotationProcessor generate source
        srcDirs += "'build/generated/source/kapt/${variant.name}/**/*.java',"
        srcDirs += "'build/generated/source/apt/${variant.name}/**/*.java',"

        srcDirs = "glob([$srcDirs])"
        context.put('srcs', srcDirs)

        def resDirs = ''
        androidSourceSets.collect { it.res.srcDirs }.flatten().each { File dir ->
            // src/main/res/**
            def resDir = BazelUtils.getTargetPath(project.projectDir, dir)
            resDirs += "'$resDir/**',"
        }
        resDirs = "glob([$resDirs])"
        context.put('res', resDirs)


        project.configurations.implementation.setCanBeResolved(true)
        project.configurations.api.setCanBeResolved(true)

        // pick only project dependency
        def projectDependency = project.configurations.implementation.dependencies.findAll {
            it instanceof ProjectDependency
        } as Set<ProjectDependency>
        project.configurations.implementation.dependencies.removeAll(projectDependency)

        projectDependency += project.configurations.api.dependencies.findAll {
            it instanceof ProjectDependency
        }
        project.configurations.api.dependencies.removeAll(projectDependency)

        Set<ResolvedDependency> resolvedDependencySet = project.configurations.implementation.resolvedConfiguration.firstLevelModuleDependencies + project.configurations.api.resolvedConfiguration.firstLevelModuleDependencies

        // aar dependencies
        def aarDeps = resolvedDependencySet.findAll {
            DependenciesUtils.isAarDependency(it)
        }.collect {
            BazelUtils.getTargetName(it)
        }.sort()

        aarDeps += projectDependency.collect { ProjectDependency dependency ->
            BazelUtils.getBazelProjectName(dependency.dependencyProject)
        }
        context.put('aarDeps', aarDeps)

        // Annotation processor
        def apt
        if (AndroidUtils.hasKotlinSupport(project)) {
            project.configurations.kapt.setCanBeResolved(true)
            apt = project.configurations.kapt.resolvedConfiguration.firstLevelModuleDependencies
        } else {
            project.configurations.annotationProcessor.setCanBeResolved(true)
            apt = project.configurations.annotationProcessor.resolvedConfiguration.firstLevelModuleDependencies
        }

        def resolvedArtifactSet = new HashSet<ResolvedArtifact>(30)
        apt.each {
            resolvedArtifactSet += it.allModuleArtifacts
        }
        resolvedArtifactSet.each {
            def file = it.file
            def link = DependenciesUtils.getAptArtifactFile(project, it)

            FileUtils.makeSureSymbolicLink(link, file)
        }

        def writer = new PrintWriter(BazelUtils.getBuildFile(project))
        engine.mergeTemplate("BUILD_application.ftl", "UTF-8", context, writer)

        writeAarImportDeps(project, resolvedDependencySet, writer)

        writer.close()
    }


    static void genLibraryBuild(Project project) {
        def android = project.extensions.android as LibraryExtension

        def context = new VelocityContext()
        context.put('kotlin', AndroidUtils.hasKotlinSupport(project))

        context.put('name', BazelUtils.getBazelTargetName(project))

        // TODO: get a variant type
        def packageName = new XmlParser().parse(new File(project.projectDir, 'src/main/AndroidManifest.xml')).@package
        context.put('package', packageName)

        // for AndroidManifest value
        context.put('minSdkVersion', 16)
        context.put('maxSdkVersion', 28)
        context.put('versionCode', android.defaultConfig.versionCode)
        context.put('versionName', android.defaultConfig.versionName)

        context.put('manifest', 'src/main/AndroidManifest.xml')

        def srcDirs = ''
        android.sourceSets.main.java.srcDirs.each { File dir ->
            // src/main/java/**
            def srcDir = BazelUtils.getTargetPath(project.projectDir, dir)
            srcDirs += "'${srcDir}/**',"
        }
        srcDirs += "'build/generated/source/**/*.java',"

        srcDirs = "glob([$srcDirs])"
        context.put('srcs', srcDirs)

        def resDirs = ''
        android.sourceSets.main.res.srcDirs.each { File dir ->
            // src/main/res/**
            def resDir = BazelUtils.getTargetPath(project.projectDir, dir)
            resDirs += "'$resDir/**',"
        }
        resDirs = "glob([$resDirs])"
        context.put('res', resDirs)

        project.configurations.implementation.setCanBeResolved(true)
        project.configurations.api.setCanBeResolved(true)

        // pick only project dependency
        def projectDependency = project.configurations.implementation.dependencies.findAll {
            it instanceof ProjectDependency
        } as Set<ProjectDependency>
        project.configurations.implementation.dependencies.removeAll(projectDependency)

        projectDependency += project.configurations.api.dependencies.findAll {
            it instanceof ProjectDependency
        }
        project.configurations.api.dependencies.removeAll(projectDependency)

        Set<ResolvedDependency> resolvedDependencySet = project.configurations.implementation.resolvedConfiguration.firstLevelModuleDependencies + project.configurations.api.resolvedConfiguration.firstLevelModuleDependencies

        // aar dependencies
        def aarDeps = resolvedDependencySet.findAll {
            DependenciesUtils.isAarDependency(it)
        }.collect {
            BazelUtils.getTargetName(it)
        }.sort()
        aarDeps += projectDependency.collect { ProjectDependency dependency ->
            BazelUtils.getBazelProjectName(dependency.dependencyProject)
        }

        context.put('aarDeps', aarDeps)

        def writer = new PrintWriter(BazelUtils.getBuildFile(project))

        engine.mergeTemplate("BUILD_library.ftl", "UTF-8", context, writer)
        writeAarImportDeps(project, resolvedDependencySet, writer)

        writer.close()
    }

    private static void writeAarImportDeps(Project project,
                                           Set<ResolvedDependency> resolvedDependencySet,
                                           Writer writer) {
        def resolvedArtifactSet = new HashSet<ResolvedArtifact>(30)

        resolvedDependencySet.each {
            resolvedArtifactSet += it.allModuleArtifacts
        }

        // Put all dependency file here
        resolvedArtifactSet.each {
            def file = it.file
            def link = DependenciesUtils.getArtifactFile(project, it)

            FileUtils.makeSureSymbolicLink(link, file)
        }

        // flatten all dependency, include jar,aar
        def flattenResolvedDependency = new HashSet<ResolvedDependency>()
        resolvedDependencySet.each {
            flattenDependency(flattenResolvedDependency, it)
        }

        // pick only aar dependency
        Set<ResolvedAarDependency> resolvedAarDependencySet = new HashSet<>(20)
        flattenResolvedDependency.each { resolvedDependency ->
            if (DependenciesUtils.isAarDependency(resolvedDependency)) {
                def aarDep = ResolvedAarDependency.wrap(resolvedDependency)
//                println "${resolvedDependency} is a AndroidArchive file ======> ${aarDep}"
                resolvedAarDependencySet.add(aarDep)
            } else if (FileUtils.isJarFile(resolvedDependency.moduleArtifacts[0].file)) {
//                println "${resolvedDependency} is Jar "
            } else {
                println "${resolvedDependency} Unknow ...."
            }
        }

        resolvedAarDependencySet.sort().each {
//            println "resolvedAarDependency ==>  $it"
            def resolvedDependency = it.resolvedDependency

            def name = BazelUtils.getTargetName(resolvedDependency)

            def context = new VelocityContext()
            context.put('aarName', name)
            context.put('aarTarget', BazelUtils.getTargetPath(project.projectDir, DependenciesUtils.getArtifactFile(project, resolvedDependency.moduleArtifacts[0])))

            def aarExports = resolvedDependency.children.findAll {
                DependenciesUtils.isAarDependency(it)
            }.collect {
                BazelUtils.getTargetName(it)
            }
            context.put('aarExports', aarExports)

            // TODO: fix file path
            def reader = new FileReader("buildSrc/src/main/resources/templates/aar_import.ftl")
            engine.evaluate(context, writer, "aar_import", reader)
        }
    }

    static void flattenDependency(Set<ResolvedDependency> container, ResolvedDependency resolvedDependency) {
        container.add(resolvedDependency)

        resolvedDependency.children.each {
            flattenDependency(container, it)
        }
    }


    static void genJavaLibraryBuild(Project project) {

        def context = new VelocityContext()
        context.put('kotlin', AndroidUtils.hasKotlinSupport(project))

        context.put('name', BazelUtils.getBazelTargetName(project))

        // TODO: other dep

        def writer = new PrintWriter(BazelUtils.getBuildFile(project))
        engine.mergeTemplate("BUILD_java_library.ftl", "UTF-8", context, writer)

        writer.close()
    }
}