# ==================================================================================================
# Kotlin support rules

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
rules_kotlin_version = "4c71740a1b63b785fc90afd8d4d4d5bfda527107"

http_archive(
    name = "io_bazel_rules_kotlin",
    urls = ["https://github.com/bazelbuild/rules_kotlin/archive/%s.zip" % rules_kotlin_version],
    type = "zip",
    strip_prefix = "rules_kotlin-%s" % rules_kotlin_version
)

load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kotlin_repositories", "kt_register_toolchains")
kotlin_repositories()
kt_register_toolchains()

# ==================================================================================================
