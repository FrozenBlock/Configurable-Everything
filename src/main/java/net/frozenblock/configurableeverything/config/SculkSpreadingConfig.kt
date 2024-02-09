@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.configurableeverything.util.experimental
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.frozenblock.lib.shadow.blue.endless.jankson.Comment
import net.minecraft.world.item.DyeColor

@UnsyncableConfig
data class SculkSpreadingConfig(

    @JvmField
    @EntrySyncData("activators")
    @Comment("List of activator blocks.")
    var activators: List<String?>? = arrayListOf(
        "minecraft:sculk_sensor",
        "minecraft:sculk_shrieker",
    ),
) {
    companion object : JsonConfig<SculkSpreadingConfig>(
        MOD_ID,
        SculkSpreadingConfig::class.java,
        makeConfigPath("sculk_spreading"),
        CONFIG_JSONTYPE,
        null,
        null
    ) {

        init {
            experimental {
                ConfigRegistry.register(this)
            }
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): SculkSpreadingConfig = if (real) this.instance() else this.config()
    }
}
