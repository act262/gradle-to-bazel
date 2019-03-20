# make a bazel android workspace
android_sdk_repository(
    name = "androidsdk",
#    path = '',
    build_tools_version = "28.0.3",
    api_level = 28
)