package net.frozenblock.configurableeverything.datafixer.util

import com.mojang.datafixers.DataFixerBuilder
import com.mojang.datafixers.schemas.Schema
import net.fabricmc.loader.api.ModContainer
import net.frozenblock.configurableeverything.config.DataFixerConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants
import net.frozenblock.configurableeverything.util.error
import net.frozenblock.configurableeverything.util.log
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.datafix.schemas.NamespacedSchema
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.QuiltDataFixerBuilder
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.QuiltDataFixes
import org.quiltmc.qsl.frozenblock.misc.datafixerupper.api.SimpleFixes
import java.util.Map
import java.util.function.BiFunction

object DataFixerUtils {

    private val SCHEMAS: MutableList<SchemaEntry> = ArrayList()
    private val REGISTRY_FIXERS: MutableList<RegistryFixer> = ArrayList()

    @JvmStatic
    fun addSchema(schema: SchemaEntry?) {
        if (schema != null) {
            SCHEMAS.add(schema)
        }
    }

    @JvmStatic
    fun addRegistryFixer(fixer: RegistryFixer?) {
        if (fixer != null) {
            REGISTRY_FIXERS.add(fixer)
        }
    }

    @JvmStatic
    val schemas: List<SchemaEntry> get() {
            val list = ArrayList(SCHEMAS)
            list.addAll(DataFixerConfig.get().schemas.value())
            return list
    }

    @JvmStatic
	val registryFixers: List<RegistryFixer>
        get() {
            val list = ArrayList(REGISTRY_FIXERS)
            list.addAll(DataFixerConfig.get().registryFixers.value())
            return list
        }

    // doesnt need jvmstatic because its never called in java
	fun applyDataFixes(mod: ModContainer?) {
        if (mod == null) return
        val config = DataFixerConfig.get()
        if (MainConfig.get().datafixer) {
            log("Applying configurable data fixes", ConfigurableEverythingSharedConstants.UNSTABLE_LOGGING)
            val schemas = schemas
            val dataVersion = config.dataVersion
            val builder = QuiltDataFixerBuilder(dataVersion)
            var maxSchema = 0
            val addedSchemas: MutableList<Schema> = ArrayList()
            if (schemas.isNotEmpty()) {
                val base = builder.addSchema(0, QuiltDataFixes.BASE_SCHEMA)
                addedSchemas.add(base)
            }
            for (fix in schemas) {
                val version = fix.version
                if (version > dataVersion) {
                    error("Data fix version $version is higher than the current data version $dataVersion", true)
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
                    error("Invalid data fix version: $version", true)
                }
            }
            QuiltDataFixes.buildAndRegisterFixer(mod, builder)
            log(
                """
                    Finished applying configurable data fixes
                    Data Version: $dataVersion
                    Max schema: $maxSchema
                    """.trimIndent(),
                ConfigurableEverythingSharedConstants.UNSTABLE_LOGGING
            )
        }
    }

    private fun handleFixer(builder: DataFixerBuilder, schema: Schema, entry: DataFixEntry, fixer: Fixer) {
        val oldId = fixer.oldId
        val newId = fixer.newId
        val fixName = "fix_" + oldId + "_to_" + newId
        when (entry.type) {
            "biome" -> SimpleFixes.addBiomeRenameFix(builder, fixName, Map.of(oldId, newId), schema)
            "block" -> SimpleFixes.addBlockRenameFix(builder, fixName, oldId, newId, schema)
            "entity" -> SimpleFixes.addEntityRenameFix(builder, fixName, oldId, newId, schema)
            "item" -> {
                SimpleFixes.addItemRenameFix(builder, fixName, oldId, newId, schema)
                addRegistryFixer(RegistryFixer(ResourceLocation("item"), java.util.List.of(Fixer(oldId, newId))))
            }

            else -> error("Invalid data fix type: " + entry.type, true)
        }
    }
}
