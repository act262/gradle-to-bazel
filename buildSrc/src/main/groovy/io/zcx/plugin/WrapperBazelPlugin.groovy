package io.zcx.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

public class WrapperBazelPlugin implements Plugin<Project> {

    @Override
    void apply(Project rootProject) {
        rootProject.tasks.create('cleanBazelWrapper', CleanBazelWrapperTask)

        rootProject.tasks.create(name: 'initBazelWrapper', group: 'BazelWrapper').doFirst {

            TemplateGen.generateFileWORKSPACE()

            rootProject.subprojects { Project project ->
                println "\n ======> wrapToBazel task exec =====> $project\n"

                // for android application
                project.plugins.withId('com.android.application') {
                    TemplateGen.genAppBuild(project)
                }

                // for android library
                project.plugins.withId('com.android.library') {
                    TemplateGen.genLibraryBuild(project)
                }

                // for java library
                // TODO: ???
            }
        }

    }


}