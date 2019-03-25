#include('include_load_kotlin.ftl')

# Wrap all dependencies for final binary
kt_android_library(
    name = 'wrapper',

    # package name
    custom_package ='$package',

    # AndroidManifest
    manifest = "$manifest",

    # java + kotlin sources
    srcs = $srcs,

    # res
    resource_files = $res,

    # apt
    plugins = ['aptPlugin'],

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

# Annotation processor
java_import(
    name = 'aptDeps',
    jars = glob(['build/bazel/apt/**/*.jar'])
)

java_plugin(
    name = 'aptPlugin',

    deps = ['aptDeps'],

    processor_class = 'com.alibaba.android.arouter.compiler.processor.RouteProcessor',
)


