package io.zcx.plugin.util

import org.gradle.api.Project
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.artifacts.ResolvedDependency

import java.nio.file.Path
import java.nio.file.Paths

class DependenciesUtils {

    static Path getArtifactPath(Project project, ResolvedArtifact resolvedArtifact) {
        Paths.get("${BazelUtils.getBazelDepsDir(project)}/${getTargetPath(resolvedArtifact)}")
    }

    static File getArtifactFile(Project project, ResolvedArtifact resolvedArtifact) {
        new File(BazelUtils.getBazelDepsDir(project), "${getTargetPath(resolvedArtifact)}")
    }

    static String getTargetPath(ResolvedArtifact resolvedArtifact) {
        BazelUtils.makeValidTargetName("${resolvedArtifact.id.componentIdentifier}_${resolvedArtifact.file.name}")
    }

    /**
     * It is a Android archive dependency.
     */
    static boolean isAarDependency(ResolvedDependency resolvedDependency) {
        FileUtils.isAarFile(resolvedDependency.moduleArtifacts[0].file)
    }
}