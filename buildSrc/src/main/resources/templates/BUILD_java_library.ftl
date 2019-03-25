java_library(
    visibility = ["//visibility:public"],

    name = '$name',

    srcs = glob(["src/**/*.java"]),

    exports = [
#        'wrapper',
    ],
)