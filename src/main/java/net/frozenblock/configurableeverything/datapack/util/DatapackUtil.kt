package net.frozenblock.configurableeverything.datapack.util

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Decoder
import com.mojang.serialization.DynamicOps
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.log
import net.frozenblock.lib.config.api.instance.ConfigSerialization
import net.frozenblock.lib.config.api.instance.json.JanksonOps
import net.frozenblock.lib.config.api.instance.xjs.XjsOps
import net.frozenblock.lib.shadow.xjs.data.serialization.JsonContext as XjsContext
import net.frozenblock.lib.shadow.xjs.data.Json as Xjs
import net.minecraft.core.Registry
import net.minecraft.resources.FileToIdConverter
import net.minecraft.resources.Identifier
import net.minecraft.resources.RegistryDataLoader
import net.minecraft.resources.RegistryOps
import net.minecraft.resources.RegistryOps.RegistryInfoLookup
import net.minecraft.resources.ResourceKey
import net.minecraft.server.packs.resources.Resource
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.thread.ParallelMapTransform
import net.minecraft.world.level.validation.DirectoryValidator
import java.io.BufferedReader
import java.util.*
import java.util.concurrent.Executor
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
        lookup: RegistryInfoLookup,
        manager: ResourceManager,
        registryKey: ResourceKey<out Registry<E>>,
        loadedEntries: Map<Identifier, RegistryDataLoader.PendingRegistration<E>>,
        codec: Decoder<E>,
        executor: Executor,
        directory: String,
    ) {
        loadContents(lookup, manager, registryKey, loadedEntries, codec,
            executor, directory, "json5", JanksonOps.INSTANCE) { reader ->
            ConfigSerialization.createJankson("").load(reader.readText())
        }
        loadContents(lookup, manager, registryKey, loadedEntries, codec,
            executor, directory, "djs", XjsOps.INSTANCE) { reader ->
            Xjs.parse(reader.readText())
        }
        loadContents(lookup, manager, registryKey, loadedEntries, codec,
            executor, directory, "jsonc", XjsOps.INSTANCE) { reader ->
            XjsContext.getParser("jsonc").parse(reader)
        }
        loadContents(lookup, manager, registryKey, loadedEntries, codec,
            executor, directory, "hjson", XjsOps.INSTANCE) { reader ->
            XjsContext.getParser("hjson").parse(reader)
        }
        loadContents(lookup, manager, registryKey, loadedEntries, codec,
            executor, directory, "txt", XjsOps.INSTANCE) { reader ->
            XjsContext.getParser("txt").parse(reader)
        }
        loadContents(lookup, manager, registryKey, loadedEntries, codec,
            executor, directory, "ubjson", XjsOps.INSTANCE) { reader ->
            XjsContext.getParser("ubjson").parse(reader)
        }
    }

    @JvmStatic
    fun <E : Any, R : Any> loadContents(
        lookup: RegistryInfoLookup,
        manager: ResourceManager,
        registryKey: ResourceKey<out Registry<E>>,
        loadedEntries: Map<Identifier, RegistryDataLoader.PendingRegistration<E>>,
        codec: Decoder<E>,
        executor: Executor,
        directory: String,
        extension: String,
        ops: DynamicOps<R>,
        createBase: (BufferedReader) -> R,
    ) {
        val fileToIdConverter = FileToIdConverter(directory, ".${extension}")
        val registryOps = RegistryOps.create(ops, lookup)

        val map = ParallelMapTransform.schedule(
            fileToIdConverter.listMatchingResources(manager),
            { resourceId, thunk ->
                val elementKey = ResourceKey.create(registryKey, fileToIdConverter.fileToId(resourceId))
                val registrationInfo = RegistryDataLoader.REGISTRATION_INFO_CACHE.apply(thunk.knownPackInfo())
                return@schedule RegistryDataLoader.PendingRegistration(elementKey, loadFromResource(codec, registryOps, createBase, elementKey, thunk), registrationInfo)
            }, executor
        )

        // Convert the map's Identifier keys so their file extension is overridden as ".json"
        val joined = map.join()
        val transformed: Map<Identifier, RegistryDataLoader.PendingRegistration<E>> = joined.mapKeys { (id, _) ->
            // Preserve namespace, replace path's file extension with .json (or append .json if none)
            val ns = id.namespace
            val path = id.path
            val lastDot = path.lastIndexOf('.')
            val newPath = if (lastDot == -1) "$path.json" else path.substring(0, lastDot) + ".json"
            Identifier.fromNamespaceAndPath(ns, newPath)
        }

        if (loadedEntries is HashMap)
            loadedEntries.putAll(transformed)
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
        } catch (e: Exception) {
            return Either.right(IllegalStateException(
                String.format(
                    Locale.ROOT,
                    "Failed to parse %s from pack %s",
                    elementKey.identifier(),
                    resource.sourcePackId()
                ), e
            ))
        }
    }
}
