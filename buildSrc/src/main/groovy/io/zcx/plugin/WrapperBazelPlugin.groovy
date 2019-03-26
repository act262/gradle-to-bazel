package io.zcx.plugin

import com.android.build.gradle.AppExtension
import io.zcx.plugin.task.InitBazelTask
import org.gradle.api.Plugin
import org.gradle.api.Project

public class WrapperBazelPlugin implements Plugin<Project> {

    @Override
    void apply(Project rootProject) {

        rootProject.tasks.create('cleanBazelWrapper', CleanBazelWrapperTask)

        rootProject.subprojects { Project project ->
            project.plugins.withId('com.android.application') {
                println "Found $project has application."

                def android = project.extensions.android as AppExtension
                android.applicationVariants.all { variant ->
                    String name = variant.name // stgDebug stgRelease
                    def taskName = "${name}InitBazel"
                    rootProject.tasks.create(taskName, InitBazelTask, rootProject, variant)

                    println " ===============> Create $taskName <=================="
                }
            }
        }
    }


}