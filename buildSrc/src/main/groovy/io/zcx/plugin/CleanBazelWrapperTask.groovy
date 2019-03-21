package io.zcx.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

class CleanBazelWrapperTask extends DefaultTask {
    CleanBazelWrapperTask() {

        setGroup('BazelWrapper')
    }

    @TaskAction
    public void doTask() {
        // clean all bazel directory and bazel files
        project.subprojects { Project subProject ->
            def bazelDir = subProject.file("build/bazel")
            bazelDir.deleteDir()

            println "Clean : ${bazelDir}"
        }

        println "do clean wrapper task"
    }
}