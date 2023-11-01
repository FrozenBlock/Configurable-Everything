package net.frozenblock.configurableeverything.datapack.util

import com.mojang.serialization.Decoder
import com.mojang.serialization.Lifecycle
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.log
import net.frozenblock.lib.config.api.instance.ConfigSerialization
import net.frozenblock.lib.config.api.instance.json.JanksonOps
import net.frozenblock.lib.shadow.blue.endless.jankson.JsonElement
import net.minecraft.core.Registry
import net.minecraft.core.WritableRegistry
import net.minecraft.resources.FileToIdConverter
import net.minecraft.resources.RegistryOps
import net.minecraft.resources.RegistryOps.RegistryInfoLookup
import net.minecraft.resources.ResourceKey
import net.minecraft.server.packs.resources.ResourceManager
import java.util.*
import kotlin.io.path.Path

object DatapackUtil {

    @JvmStatic
    fun addedRepositories(): List<CERepositorySource> {
        val config = MainConfig.get().datapack
        if (config?.applyDatapackFolders == true) {
            val list: MutableList<CERepositorySource> = arrayListOf()
            config.datapackFolders?.forEach {
                it?.apply {
                    log("Adding datapack repository at $this")
                    list.add(CERepositorySource(Path(this)))
                }
            }
            return list
        }
        return emptyList()
    }

    @JvmStatic
    fun <E : Any> loadJson5Contents(
        lookup: RegistryInfoLookup,
        manager: ResourceManager,
        registryKey: ResourceKey<out Registry<E>>,
        registry: WritableRegistry<E>,
        decoder: Decoder<E>,
        exceptions: MutableMap<ResourceKey<*>, Exception>,
        directory: String
    ) {
        val fileToIdConverter = FileToIdConverter(directory, ".json5")
        val registryOps = RegistryOps.create(JanksonOps.INSTANCE, lookup)

        for ((resourceLocation, resource) in fileToIdConverter.listMatchingResources(manager)) {
            val resourceKey = ResourceKey.create(registryKey, fileToIdConverter.fileToId(resourceLocation))
            try {
                val reader = resource.openAsReader()
                try {
                    val jsonElement: JsonElement = ConfigSerialization.createJankson("").load(reader.readText())
                    val dataResult = decoder.parse(registryOps, jsonElement)
                    val `object`: E = dataResult.getOrThrow(
                        false
                    ) {}
                    registry.register(
                        resourceKey,
                        `object`,
                        if (resource.isBuiltin) Lifecycle.stable() else dataResult.lifecycle()
                    )
                } catch (e: Exception) {
                    try {
                        reader.close()
                    } catch (e1: Exception) {
                        e.addSuppressed(e1)
                    }
                    throw e
                }
                reader.close()
            } catch (e: Exception) {
                exceptions[resourceKey] = IllegalStateException(
                    String.format(
                        Locale.ROOT,
                        "Failed to parse %s from pack %s",
                        resourceLocation,
                        resource.sourcePackId()
                    ), e
                )
            }
        }
    }
}
