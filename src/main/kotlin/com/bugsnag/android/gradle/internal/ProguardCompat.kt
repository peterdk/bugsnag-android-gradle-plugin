package com.bugsnag.android.gradle.internal

import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.api.BaseVariantOutput
import com.bugsnag.android.gradle.GroovyCompat
import org.gradle.api.Project
import org.gradle.util.VersionNumber
import java.io.File
import java.nio.file.Paths

/**
 * Finds the mapping file locations for Proguard > 7. T
 */
internal fun findMappingFileProguard7(
    project: Project,
    variant: BaseVariant,
): List<File> {
    val file = findProguardMappingFile(project, arrayOf("outputs", "proguard", variant.dirName, "mapping"))
    return listOf(
        file
    )
}

private fun findProguardMappingFile(
    project: Project,
    path: Array<String>
): File {
    val buildDir = project.buildDir.toString()
    return Paths.get(buildDir, *path,  "mapping.txt").toFile()
}

/**
 * Returns true if the Proguard plugin has been applied to the project
 */
internal fun Project.hasProguardPlugin(): Boolean {
    return pluginManager.hasPlugin("com.guardsquare.proguard")
}

internal fun getProguardAabTaskName(variant: BaseVariant): String {
    val buildType = variant.buildType.name.capitalize()
    val flavor = variant.flavorName.capitalize()
    return "transformClassesAndResourcesWithProguardTransformFor$flavor$buildType"
}

