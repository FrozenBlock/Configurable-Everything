package net.frozenblock.configurableeverything.datapack.util

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Decoder
import com.mojang.serialization.DynamicOps
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.log
import net.frozenblock.configurableeverything.util.logError
import net.frozenblock.lib.config.api.instance.ConfigSerialization
import net.frozenblock.lib.config.api.instance.json.JanksonOps
import net.frozenblock.lib.config.api.instance.xjs.XjsOps
import net.frozenblock.lib.shadow.xjs.data.serialization.JsonContext as XjsContext
import net.frozenblock.lib.shadow.xjs.data.Json as Xjs
import net.minecraft.core.Registry
import net.minecraft.core.WritableRegistry
import net.minecraft.resources.FileToIdConverter
import net.minecraft.resources.Identifier
import net.minecraft.resources.RegistryDataLoader
import net.minecraft.resources.RegistryOps
import net.minecraft.resources.RegistryOps.RegistryInfoLookup
import net.minecraft.resources.ResourceKey
import net.minecraft.server.packs.resources.Resource
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.world.level.validation.DirectoryValidator
import java.io.BufferedReader
import java.util.*
import kotlin.io.path.Path

object DatapackUtil {

    @JvmStatic
    fun addedRepositories(validator: DirectoryValidator?): List<CERepositorySource> {
        val config = MainConfig.get().datapack
        if (validator == null) return emptyList()
        if (config.applyDatapackFolders) {
            val list: MutableList<CERepositorySource> = arrayListOf()
            config.datapackFolders.forEach {
                log("Adding datapack repository at $it")
                list.add(CERepositorySource(Path(it), validator = validator))
            }
            return list
        }
        return emptyList()
    }

    @JvmStatic
    fun <E : Any> loadJson5Contents(
        registry: WritableRegistry<E>,
        lookup: RegistryInfoLookup,
        manager: ResourceManager,
        registryKey: ResourceKey<out Registry<E>>,
        codec: Decoder<E>,
        directory: String,
    ) {
        loadContents(
            registry, lookup, manager, registryKey, codec,
            directory, "json5", JanksonOps.INSTANCE
        ) { reader ->
            ConfigSerialization.createJankson("").load(reader.readText())
        }
        loadContents(
            registry, lookup, manager, registryKey, codec,
            directory, "djs", XjsOps.INSTANCE
        ) { reader ->
            Xjs.parse(reader.readText())
        }
        loadContents(
            registry, lookup, manager, registryKey, codec,
            directory, "jsonc", XjsOps.INSTANCE
        ) { reader ->
            XjsContext.getParser("jsonc").parse(reader)
        }
        loadContents(
            registry, lookup, manager, registryKey, codec,
            directory, "hjson", XjsOps.INSTANCE
        ) { reader ->
            XjsContext.getParser("hjson").parse(reader)
        }
        loadContents(
            registry, lookup, manager, registryKey, codec,
            directory, "txt", XjsOps.INSTANCE
        ) { reader ->
            XjsContext.getParser("txt").parse(reader)
        }
        loadContents(
            registry, lookup, manager, registryKey, codec,
            directory, "ubjson", XjsOps.INSTANCE
        ) { reader ->
            XjsContext.getParser("ubjson").parse(reader)
        }
    }

    @JvmStatic
    fun <E : Any, R : Any> loadContents(
        registry: WritableRegistry<E>,
        lookup: RegistryInfoLookup,
        manager: ResourceManager,
        registryKey: ResourceKey<out Registry<E>>,
        codec: Decoder<E>,
        directory: String,
        extension: String,
        ops: DynamicOps<R>,
        createBase: (BufferedReader) -> R,
    ) {
        val fileToIdConverter = FileToIdConverter(directory, ".${extension}")
        val registryOps = RegistryOps.create(ops, lookup)

        val resources: Map<Identifier, Resource> = fileToIdConverter.listMatchingResources(manager)

        for ((resourceId, resource) in resources) {
            val elementKey = ResourceKey.create(registryKey, fileToIdConverter.fileToId(resourceId))
            val registrationInfo = RegistryDataLoader.REGISTRATION_INFO_CACHE.apply(resource.knownPackInfo())

            // Parse the resource
            val parsed = loadFromResource(codec, registryOps, createBase, elementKey, resource)

            if (parsed.left().isPresent) {
                try {
                    registry.register(elementKey, parsed.left().get(), registrationInfo)
                } catch (e: Exception) {
                    logError("Failed to register ${elementKey.identifier()} from $resourceId", e)
                }
            } else {
                logError("Failed to load ${elementKey.identifier()} from $resourceId", parsed.right().get())
            }
        }
    }

    fun <E : Any, R : Any> loadFromResource(
        decoder: Decoder<E>,
        ops: RegistryOps<R>,
        createBase: (BufferedReader) -> R,
        elementKey: ResourceKey<E>,
        resource: Resource
    ): Either<E, Exception> {
        try {
            val reader = resource.openAsReader()

            lateinit var either: Either<E, Exception>
            try {
                val parsedElement: R = createBase(reader)
                val dataResult = decoder.parse(ops, parsedElement)
                val `object`: E = dataResult.getOrThrow()
                either = Either.left(`object`)
            } catch (e: Exception) {
                try {
                    reader.close()
                } catch (e1: Exception) {
                    e.addSuppressed(e1)
                }
                throw e
            }

            reader?.close()

            return either
        } catch (cause: Exception) {
            return Either.right(
                IllegalStateException(
                    String.format(
                        Locale.ROOT,
                        "Failed to parse %s from pack %s",
                        elementKey.identifier(),
                        resource.sourcePackId()
                    ), cause
                )
            )
        }
    }
}
