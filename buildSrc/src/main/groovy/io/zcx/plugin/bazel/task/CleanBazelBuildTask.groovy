package io.zcx.plugin.bazel.task


import io.zcx.plugin.bazel.util.BazelUtils
import org.gradle.api.Project

/**
 * Clean WORKSPACE, BUILD.bazel, deps ..
 */
class CleanBazelBuildTask extends AbstractBazelGradleTask {

    @Override
    void doTask() {
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