package com.bugsnag.android.gradle

import com.bugsnag.android.gradle.internal.JNI_LIBS_DIR
import com.bugsnag.android.gradle.internal.register
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.ArchiveOperations
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.CopySpec
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.file.FileTree
import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.WorkResult
import java.io.File
import javax.inject.Inject

open class BugsnagInstallJniLibsTask @Inject constructor(
    objects: ObjectFactory,
    projectLayout: ProjectLayout,
    private val fsOperations: FileSystemOperations,
    private val archiveOperations: ArchiveOperations
) : DefaultTask() {
    init {
        description =
            "Copies shared object files from the bugsnag-android AAR to the required build directory"
        group = BugsnagPlugin.GROUP_NAME
    }

    @get:OutputDirectory
    val buildDirDestination: DirectoryProperty = objects.directoryProperty()
        .convention(projectLayout.buildDirectory.dir(JNI_LIBS_DIR))

    @get:InputFiles
    val bugsnagArtifacts: ConfigurableFileCollection = objects.fileCollection()

    fun copy(action: (CopySpec) -> Unit): WorkResult = fsOperations.copy(action)
    fun zipTree(file: File): FileTree = archiveOperations.zipTree(file)

    /**
     * Looks at all the dependencies and their dependencies and finds the `com.bugsnag` artifacts with SO files.
     */
    @TaskAction
    fun setupNdkProject() {
        val destination = buildDirDestination.asFile.get()
        bugsnagArtifacts.forEach { file: File ->
            copy {
                it.from(zipTree(file))
                it.into(destination)
            }
        }
    }

    companion object {
        private val sharedObjectAarIds = listOf(
            "bugsnag-android",
            "bugsnag-android-ndk",
            "bugsnag-plugin-android-anr",
            "bugsnag-plugin-android-ndk"
        )

        internal fun resolveBugsnagArtifacts(project: Project): FileCollection {
            val files = project.configurations
                .filter { it.toString().contains("CompileClasspath") }
                .map { it.resolvedConfiguration }
                .flatMap { it.firstLevelModuleDependencies }
                .filter { it.moduleGroup == "com.bugsnag" }
                .flatMap { it.allModuleArtifacts }
                .filter {
                    val identifier = it.id.componentIdentifier.toString()
                    sharedObjectAarIds.any { bugsnagId -> identifier.contains(bugsnagId) }
                }
                .map { it.file }
                .toSet()
            return project.files(files)
        }

        /**
         * Registers the appropriate subtype to this [project] with the given [name] and
         * [configurationAction]
         */
        internal fun register(
            project: Project,
            name: String,
            configurationAction: BugsnagInstallJniLibsTask.() -> Unit
        ): TaskProvider<out BugsnagInstallJniLibsTask> {
            return project.tasks.register(name, configurationAction)
        }
    }
}
