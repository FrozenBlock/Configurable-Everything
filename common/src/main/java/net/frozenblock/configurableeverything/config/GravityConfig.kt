package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.gravity.util.DimensionGravityBelt
import net.frozenblock.configurableeverything.util.CEConfig
import net.frozenblock.lib.config.v2.entry.ConfigEntry
import net.frozenblock.lib.config.v2.entry.EntryType
import net.frozenblock.lib.gravity.api.GravityBelt
import net.frozenblock.lib.gravity.api.functions.AbsoluteGravityFunction
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

private val DIMENSION_GRAVITY_BELT_LIST: EntryType<DimensionGravityBelt> = EntryType.create(
    DimensionGravityBelt.CODEC,
    DimensionGravityBelt.STREAM_CODEC,
)

object GravityConfig : CEConfig("gravity") {
    @JvmField
    var gravityBelts: ConfigEntry<MutableList<DimensionGravityBelt>> = this.entry("gravityBelts",
        DIMENSION_GRAVITY_BELT_LIST.asList(),
        mutableListOf(
            DimensionGravityBelt(
                Level.OVERWORLD,
                mutableListOf(
                    GravityBelt(128.0, 319.0, AbsoluteGravityFunction(Vec3(0.0, 0.1, 0.0))),
                    GravityBelt(500.0, Double.MAX_VALUE, AbsoluteGravityFunction(Vec3(0.0, 0.01, 0.0)))
                )
            )
        )
    )
}
