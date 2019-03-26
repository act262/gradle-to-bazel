package io.zcx.plugin.bazel

import com.android.build.gradle.AppExtension
import io.zcx.plugin.bazel.task.CleanBazelBuildTask
import io.zcx.plugin.bazel.task.FullCleanBazelTask
import io.zcx.plugin.bazel.task.GenerateBazelTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class GradleToBazelPlugin implements Plugin<Project> {

    @Override
    void apply(Project rootProject) {

        rootProject.tasks.create('cleanBazelBuild', CleanBazelBuildTask)
        rootProject.tasks.create('cleanBazelAll', FullCleanBazelTask)
                .dependsOn('cleanBazelBuild')

        rootProject.subprojects { Project project ->
            project.plugins.withId('com.android.application') {
                println "Found $project has application."

                def android = project.extensions.android as AppExtension
                android.applicationVariants.all { variant ->
                    String name = variant.name // stgDebug stgRelease
                    def taskName = "genBazel-${name}"
                    rootProject.tasks.create(taskName, GenerateBazelTask, rootProject, variant)

                    println " ===============> Create $taskName <=================="
                }
            }
        }
    }


}