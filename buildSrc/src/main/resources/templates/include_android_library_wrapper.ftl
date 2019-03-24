# Wrap all dependencies for final binary
android_library(
    name = 'wrapper',

    # package name
    custom_package ='$package',

    # AndroidManifest
    manifest = "$manifest",

    # java + kotlin sources
    srcs = $srcs,

    # res
    resource_files = $res,

    deps = [
        'jarDeps',
        #foreach($item in $aarDeps)
        '$item',
        #end
    ]
)

# All jar dependencies
java_import(
    name = 'jarDeps',

    jars = glob(['build/bazel/deps/*.jar'])
)
