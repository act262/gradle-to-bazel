package io.zcx.plugin.bazel.util

import org.apache.commons.io.FilenameUtils
import org.gradle.api.Project

import java.nio.file.Files
import java.nio.file.Paths

class FileUtils {

    public static boolean isJarFile(File file) {
        FilenameUtils.getExtension(file.path) == 'jar'
    }

    public static boolean isAarFile(File file) {
        FilenameUtils.getExtension(file.path) == 'aar'
    }


    static makeSureSymbolicLink(File linkTo, File targetFile) {
        def linkToPath = linkTo.toPath()
        if (Files.notExists(linkToPath)) {

            Files.createSymbolicLink(linkTo.toPath(), targetFile.toPath())
            println " $targetFile <<---- link to ---->>  ${linkTo}"
        }
    }

    static String relativePath(File base, File path) {
        base.toURI().relativize(path.toURI()).getPath();
    }

    /**
     * Get a relative path to rootProject's dir
     *
     * i.e.
     * rootDir ~/xxx/yyy
     * path ~/xxx/yyy/xxx
     *
     * return xxx
     */
    static String relativeProjectPath(Project project) {
        Paths.get(project.rootDir.path).relativize(Paths.get(project.projectDir.path)).toString()
    }
}