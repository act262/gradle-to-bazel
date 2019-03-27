package io.zcx.plugin.bazel.task

import com.android.build.gradle.api.ApplicationVariant
import io.zcx.plugin.bazel.TemplateGen
import io.zcx.plugin.bazel.task.AbstractBazelGradleTask
import org.gradle.api.Project

import javax.inject.Inject

class GenerateBazelTask extends AbstractBazelGradleTask {

    Project rootProject
    ApplicationVariant variant

    @Inject
    GenerateBazelTask(Project rootProject, ApplicationVariant variant) {
        this.rootProject = rootProject
        this.variant = variant

        setDescription("Initial Bazel task for ${variant.name}")
    }

    @Override
    void doTask() {
        println "====================>  ${name} task exec start  <========================"

        TemplateGen.genWorkspace(rootProject)

        rootProject.subprojects { Project project ->
            println "============> $project"

            // for android application
            project.plugins.withId('com.android.application') {
                TemplateGen.genAppBuild(project, variant)
            }

            // for android library
            project.plugins.withId('com.android.library') {
                TemplateGen.genLibraryBuild(project, variant)
            }

            // for java library
            project.plugins.withId('java-library') {
                TemplateGen.genJavaLibraryBuild(project)
            }
        }

        println "====================>  ${name} task exec end  <========================"
    }
}