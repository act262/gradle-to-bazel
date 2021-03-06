package io.zcx.plugin.bazel.util

import io.zcx.plugin.bazel.Constants
import org.gradle.api.Project
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.artifacts.ResolvedDependency

class BazelUtils {

    /**
     * Get a legal workspace name.
     */
    static String getWorkspaceName(Project rootProject) {
        rootProject.name
                .replace('.', '_')
                .replace(':', '_')
                .replace('-', '_')
    }

    /**
     * WORKSPACE
     */
    static File getWorkspaceFile(Project rootProject) {
        new File(rootProject.projectDir, Constants.FN_WORKSPACE)
    }

    /**
     * BUILD.bazel
     */
    static File getBuildFile(Project project) {
        new File(project.projectDir, Constants.FN_BUILD)
    }

    static String getBazelProjectName(Project project) {
        "//${FileUtils.relativeProjectPath(project)}"
    }

    static String getBazelTargetName(Project project) {
        project.name
    }

    /**
     * Current project's bazel directory
     *
     * i.e : ~/xxx/app/build/bazel
     */
    static File getBazelDir(Project project) {
        new File(project.buildDir, Constants.FD_BAZEL)
    }

    /**
     * Current project's bazel dependencies directory
     *
     * i.e.
     * ~/xxx/app/build/bazel/deps
     *
     */
    static String getBazelDepsPath(Project project) {
        "${getBazelDir(project)}/${Constants.FD_BAZEL_DEPS}"
    }

    static File getBazelDepsDir(Project project) {
        def depsDir = new File(getBazelDepsPath(project))
        if (!depsDir.exists()) {
            depsDir.mkdirs()
        }
        return depsDir
    }

    static File getBazelAptDir(Project project, ResolvedArtifact resolvedArtifact) {
        def depsDir = new File("${getBazelDir(project)}/apt/${resolvedArtifact.name}")
        if (!depsDir.exists()) {
            depsDir.mkdirs()
        }
        return depsDir
    }

    /**
     * Bazel target relative path
     * @param dir current bazel target
     * @param file
     */
    static String getTargetPath(File dir, File file) {
        file.path.replaceFirst(dir.path, "").replaceFirst("/", "")
    }

    static String getProjectTargetPath(Project project, File file) {
        file.path.replaceFirst(project.projectDir.path, "").replaceFirst("/", "")
    }

    /**
     * Use ResolvedDependency as target name, convert to valid bazel target name.
     */
    static String getTargetName(ResolvedDependency resolvedDependency) {
        makeValidTargetName(resolvedDependency.name)
    }

    /**
     * Convert to a valid bazel target name
     */
    static String makeValidTargetName(String input) {
        input.replace(':', '_')
    }


}