# Make a bazel android workspace

#if($workspace)
workspace(name = '$!workspace')
#end

android_sdk_repository(
    name = 'androidsdk',
    # Your ANDROID_HOME
#    path = '$path',
    build_tools_version = '$build_tools_version',
    api_level = $api_level
)