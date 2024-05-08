package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.gravity.util.DimensionGravityBelt
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.frozenblock.lib.gravity.api.GravityBelt
import net.frozenblock.lib.gravity.api.functions.AbsoluteGravityFunction
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

private val DIMENSION_GRAVITY_BELT_LIST: TypedEntryType<MutableList<DimensionGravityBelt>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        DimensionGravityBelt.CODEC.mutListOf()
    )
)

@UnsyncableConfig
data class GravityConfig(
    @JvmField
    @EntrySyncData("gravityBelts")
    var gravityBelts: TypedEntry<MutableList<DimensionGravityBelt>> = TypedEntry.create(
        DIMENSION_GRAVITY_BELT_LIST,
        mutableListOf(
            DimensionGravityBelt(
                Level.OVERWORLD,
                mutableListOf(
                    GravityBelt(128.0, 319.0, AbsoluteGravityFunction(Vec3(0.0, 0.1, 0.0))),
                    GravityBelt(500.0, Double.POSITIVE_INFINITY, AbsoluteGravityFunction(Vec3(0.0, 0.01, 0.0)))
                )
            )
        )
    ),
) {
    companion object : CEConfig<GravityConfig>(
        GravityConfig::class,
        "gravity"
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): GravityConfig = if (real) this.instance() else this.config()
    }
}
