# Create android library
android_library(
    visibility = ["//visibility:public"],

    name = '$name',

    # package name
    custom_package ='$package',

    # AndroidManifest
    manifest = "$manifest",

    exports = [
        'wrapper',
    ]
)

#if($kotlin)
#parse('include_kt_android_library_wrapper.ftl')
#else
#parse('include_android_library_wrapper.ftl')
#end

# All aar dependencies