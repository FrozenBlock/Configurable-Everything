package net.frozenblock.configurableeverything.util

import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.metadata.ModOrigin
import java.io.File
import java.io.FileOutputStream
import java.util.ArrayDeque
import java.util.LinkedHashSet
import java.util.jar.JarFile
import kotlin.io.path.Path
import kotlin.jvm.optionals.getOrNull

private val RESOLVED_MODS = Path(".$MOD_ID/resolved_mods/").apply {
    toFile().recreateDir()
}

fun getModFile(modId: String): File? {
    val modContainer = FabricLoader.getInstance().getModContainer(modId).getOrNull() ?: return null
    val origin = modContainer.origin
    val kind = origin.kind
    return when (kind) {
        ModOrigin.Kind.PATH -> origin.paths[0].toFile()
        ModOrigin.Kind.NESTED -> getNestedModFile(origin)
        else -> null
    }
}

fun getNestedModFile(origin: ModOrigin): File? {
    if (origin.kind != ModOrigin.Kind.NESTED) return null

    val parentId = origin.parentModId
    val parentFile = getModFile(parentId) ?: return null
    val subLocation = origin.parentSubLocation

    val parentJar = JarFile(parentFile)
    val nestedJarEntry = parentJar.getJarEntry(subLocation)

    val fileName = subLocation.split('/').last()
    val newFile: File = RESOLVED_MODS.resolve(Path(fileName)).toFile()

    FileOutputStream(newFile).use { jarOutput ->
        parentJar.getInputStream(nestedJarEntry).use { reader ->
            reader.copyTo(jarOutput)
        }
    }

    return newFile
}

fun expandModsWithChildren(rootIds: Collection<String>): Set<String> {
    val result: MutableSet<String> = LinkedHashSet(rootIds)
    val queue: ArrayDeque<String> = ArrayDeque(rootIds)

    val allMods = FabricLoader.getInstance().allMods

    while (queue.isNotEmpty()) {
        val parent = queue.removeFirst()
        for (mod in allMods) {
            try {
                val origin = mod.origin
                if (origin.kind == ModOrigin.Kind.NESTED && origin.parentModId == parent) {
                    val id = mod.metadata.id
                    if (result.add(id)) queue.addLast(id)
                }
            } catch (e: Exception) {
                // be defensive: some mod origins may be unusual; skip on error
            }
        }
    }

    return result
}
