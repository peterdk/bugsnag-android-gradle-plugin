package com.bugsnag.android.gradle

/**
 * Defines configuration options (Gradle plugin extensions) for the BugsnagPlugin
 */
class BugsnagPluginExtension {

    String endpoint = 'https://upload.bugsnag.com'
    String apiKey = null
    boolean autoUpload = true
    boolean autoProguardConfig = true
    boolean uploadDebugBuildMappings = false
    boolean overwrite = false
    int retryCount = 0
    boolean ndk = false
    String sharedObjectPath = null


    // release API values
    String versionName = null
    Integer versionCode = null
    String builderName = null
    String releaseStage = null
    String vcsProvider = null
    String vcsUrl = null
    String vcsHash = null
    boolean autoAssignRelease = false
    Map<String, String> metadata = null

}
