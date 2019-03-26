package io.zcx.plugin.bazel

import com.android.builder.model.Version
import org.gradle.util.VersionNumber

class Versions {

    static AGP_3_1_0 = VersionNumber.parse('3.1.0')
    static AGP_3_2_0 = VersionNumber.parse('3.2.0')
    static AGP_3_3_0 = VersionNumber.parse('3.3.0')


    static AGP_CURRENT = VersionNumber.parse(Version.ANDROID_GRADLE_PLUGIN_VERSION)
}