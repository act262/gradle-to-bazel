package io.zcx.plugin.util

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
}
