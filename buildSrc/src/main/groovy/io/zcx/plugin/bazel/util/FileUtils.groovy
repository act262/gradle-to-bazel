package io.zcx.plugin.bazel.util

import org.apache.commons.io.FilenameUtils

import java.nio.file.Files

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
}