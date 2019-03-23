package(default_visibility = ["//visibility:public"])

# create android library
android_library(
    name = '$name',

    # applicationId
    custom_package ="$applicationId",

    # AndroidManifest
    manifest = "$manifestFile",

    # java + kotlin sources
    srcs = $srcs,

    # res
    resource_files = $res,

    deps = [
        'jarDeps',
        'aarDeps',
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
        #foreach($item in $aarDeps)
        '$item',
        #end
    ]
)

