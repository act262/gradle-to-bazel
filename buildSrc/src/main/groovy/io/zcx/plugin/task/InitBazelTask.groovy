package io.zcx.plugin.task

import com.android.build.gradle.api.ApplicationVariant
import io.zcx.plugin.TemplateGen
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

import javax.inject.Inject

class InitBazelTask extends DefaultTask {

    Project rootProject
    ApplicationVariant variant

    @Inject
    InitBazelTask(Project rootProject, ApplicationVariant variant) {
        this.rootProject = rootProject
        this.variant = variant

        setGroup('BazelWrapper')
        setDescription("Initial Bazel task for ${variant.name}")
    }

    @TaskAction
    void doTask() {
        TemplateGen.genWorkspace(rootProject)

        rootProject.subprojects { Project project ->
            println "\n ======> wrapToBazel task exec =====> $project\n"

            // for android application
            project.plugins.withId('com.android.application') {
                TemplateGen.genAppBuild(project, variant)
            }

            // for android library
            project.plugins.withId('com.android.library') {
                TemplateGen.genLibraryBuild(project)
            }

            // for java library
            project.plugins.withId('java-library') {
                TemplateGen.genJavaLibraryBuild(project)
            }
        }
    }
}