package com.bugsnag.android.gradle

import org.gradle.api.provider.Property

interface BugsnagFileUploadTask {
    val failOnUploadError: Property<Boolean>
    val overwrite: Property<Boolean>
    val endpoint: Property<String>
    val retryCount: Property<Int>
    val timeoutMillis: Property<Long>

    fun configureWith(bugsnag: BugsnagPluginExtension) {
        failOnUploadError.set(bugsnag.failOnUploadError)
        overwrite.set(bugsnag.overwrite)
        endpoint.set(bugsnag.endpoint)
        retryCount.set(bugsnag.retryCount)
        timeoutMillis.set(bugsnag.requestTimeoutMs)
    }
}
