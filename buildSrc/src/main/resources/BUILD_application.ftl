# Create android application
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
    manifest = "$manifest",

    multidex = 'native',
    dex_shards = 10,

    deps = [
        'wrapper'
    ]
)

#if($kotlin)
#parse('include_kt_android_library_wrapper.ftl')
#else
#parse('include_android_library_wrapper.ftl')
#end

# All aar dependencies

