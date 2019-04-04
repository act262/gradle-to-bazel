package io.zcx.plugin.bazel.util

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.api.AndroidSourceSet
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.Project

/**
 * Android project utility
 */
class AndroidUtils {

    static boolean isAndroidProject(Project project) {
        isAndroidAppProject(project) || isAndroidLibProject(project)
    }

    static boolean isAndroidAppProject(Project project) {
        project.plugins.hasPlugin("com.android.application")
    }

    static boolean isAndroidLibProject(Project project) {
        project.plugins.hasPlugin("com.android.library")
    }

    /**
     * Is that project support kotlin.
     */
    static boolean isKotlinProject(Project project) {
        project.plugins.hasPlugin('kotlin-android')
    }

    /**
     * Is that project using kotlin.
     * @param project rootProject
     */
    static boolean usingKotlin(Project project) {
        project.rootProject.buildscript.configurations.classpath.dependencies.any {
            it.group == 'org.jetbrains.kotlin' && it.name == 'kotlin-gradle-plugin'
        }
    }

    /**
     * Collect all this variant's AndroidSourceSet
     */
    static Set<AndroidSourceSet> collectSourceSet(BaseExtension android, BaseVariant variant) {
        def buildType = variant.buildType
        android.sourceSets.findAll {
            return it.name == 'main' ||                // main sources
                    it.name == buildType.name ||       // for buildType sources, never empty
                    it.name == variant.flavorName ||   // for flavor sources， maybe empty
                    it.name == variant.name            // flavor + buildType
        }
    }
}
