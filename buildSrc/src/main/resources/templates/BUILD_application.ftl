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
        '//app:appcompat-v7-28.0.0.aar',
    ]
)

java_import(
    name = 'jarDeps',
    jars = glob(['$depsDir/*.jar'])
)

aar_import(
    name = 'appcompat-v7-28.0.0.aar',
    aar = 'build/bazel/deps/appcompat-v7-28.0.0.aar'
)