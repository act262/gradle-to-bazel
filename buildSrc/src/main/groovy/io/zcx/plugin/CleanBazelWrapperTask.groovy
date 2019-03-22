package io.zcx.plugin

import io.zcx.plugin.util.BazelUtils
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

class CleanBazelWrapperTask extends DefaultTask {
    CleanBazelWrapperTask() {

        setGroup('BazelWrapper')
    }

    @TaskAction
    public void doTask() {
        println "Clean task start..."

        // clean all bazel directory and bazel files
        def workspaceFile = BazelUtils.getWorkspaceFile(project)
        workspaceFile.delete()

        project.subprojects { Project subProject ->
            def bazelDir = BazelUtils.getBazelDir(subProject)
            bazelDir.deleteDir()

            def buildFile = BazelUtils.getBuildFile(subProject)
            buildFile.delete()
        }

        println "Clean task Done"
    }

}