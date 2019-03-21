# create android application
android_binary(
    name = "$name",

    # applicationId
    custom_package ="$applicationId",

    manifest_values = {
        'minSdkVersion':'$minSdkVersion',
        'maxSdkVersion':'$maxSdkVersion',
        'versionCode':'$versionCode',
        'versionName':'$versionName',
    },

    # AndroidManifest
    manifest = "$manifestFile",

    # java + kotlin sources
    srcs = $srcs,

    # res
    resource_files = $res,

    multidex = 'native',
    dex_shards = 25,

    deps = [
        '//app:jarDeps',
        '//app:aarDeps',
    ]
)

# All jar dependencies
java_import(
    name = 'jarDeps',
    jars = glob(['build/bazel/deps/*.jar'])
)

# All aar dependencies
android_library(
    name = 'aarDeps',

    exports = [
        $aarDeps
    ]
)