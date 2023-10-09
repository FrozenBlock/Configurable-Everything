package net.frozenblock.configurableeverything.datafixer.util

import com.mojang.datafixers.DataFixerBuilder
import com.mojang.datafixers.schemas.Schema
import net.fabricmc.loader.api.ModContainer
import net.frozenblock.configurableeverything.config.DataFixerConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.UNSTABLE_LOGGING
import net.frozenblock.configurableeverything.util.log
import net.frozenblock.configurableeverything.util.logError
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.datafix.schemas.NamespacedSchema
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.QuiltDataFixerBuilder
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.QuiltDataFixes
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.SimpleFixes

object DataFixerUtils {

    @JvmStatic
    val SCHEMAS: MutableList<SchemaEntry?> = ArrayList()
        get() {
            val list = ArrayList(field)
            DataFixerConfig.get().schemas?.value?.let { list.addAll(it) }
            return list
        }

    @JvmStatic
    val REGISTRY_FIXERS: MutableList<RegistryFixer?> = ArrayList()
        get() {
            val list = ArrayList(field)
            DataFixerConfig.get().registryFixers?.value?.let { list.addAll(it) }
            return list
        }

    // doesnt need jvmstatic because its never called in java
	fun applyDataFixes(mod: ModContainer?) {
        if (mod == null) return
        val config = DataFixerConfig.get()
        if (MainConfig.get().datafixer == true) {
            log("Applying Configurable Everything's data fixes", UNSTABLE_LOGGING)
            val schemas = SCHEMAS
            val dataVersion = config.dataVersion
            if (dataVersion == null) {
                logError("Data version is null", true)
                return
            }
            val builder = QuiltDataFixerBuilder(dataVersion)
            var maxSchema = 0
            val addedSchemas: MutableList<Schema> = ArrayList()
            if (schemas.isNotEmpty()) {
                val base = builder.addSchema(0, QuiltDataFixes.BASE_SCHEMA)
                addedSchemas.add(base)
            }
            for (fix in schemas) {
                if (fix == null) continue
                val version = fix.version
                if (version > dataVersion) {
                    logError("Data fix version $version is higher than the current data version $dataVersion", true)
                    continue
                }
                if (version > maxSchema) {
                    val schema = builder.addSchema(
                        version
                    ) { versionKey: Int, parent: Schema ->
                        NamespacedSchema(
                            versionKey,
                            parent
                        )
                    }
                    addedSchemas.add(schema)
                    maxSchema = version
                }
                try {
                    val schema = addedSchemas[version]
                    for (entry in fix.entries) {
                        for (fixer in entry.fixers) {
                            handleFixer(builder, schema, entry, fixer)
                        }
                    }
                } catch (e: IndexOutOfBoundsException) {
                    logError("Invalid data fix version: $version", true)
                }
            }
            QuiltDataFixes.buildAndRegisterFixer(mod, builder)
            log(
                """
                    Finished applying configurable data fixes
                    Data Version: $dataVersion
                    Max schema: $maxSchema
                    """.trimIndent(),
                UNSTABLE_LOGGING
            )
        }
    }

    private fun handleFixer(builder: DataFixerBuilder, schema: Schema, entry: DataFixEntry, fixer: Fixer) {
        val oldId = fixer.oldId
        val newId = fixer.newId
        val fixName = "fix_" + oldId + "_to_" + newId
        when (entry.type) {
            "biome" -> SimpleFixes.addBiomeRenameFix(builder, fixName, mapOf(Pair(oldId, newId)), schema)
            "block" -> SimpleFixes.addBlockRenameFix(builder, fixName, oldId, newId, schema)
            "entity" -> SimpleFixes.addEntityRenameFix(builder, fixName, oldId, newId, schema)
            "item" -> {
                SimpleFixes.addItemRenameFix(builder, fixName, oldId, newId, schema)
                REGISTRY_FIXERS.add(RegistryFixer(ResourceLocation("item"), listOf(Fixer(oldId, newId))))
            }

            else -> logError("Invalid data fix type: " + entry.type, true)
        }
    }
}
