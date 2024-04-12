package net.frozenblock.configurableeverything.biome_placement.util

import net.frozenblock.configurableeverything.config.BiomePlacementConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.DataReloadManager
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.value

object BiomePlacementChanges : DataReloadManager<BiomePlacementChange>(
    "biome_placement_change_reloader",
    "Biome Placement Change Manager",
    "biome_placement",
    "Load Biome Placement Changes",
    "biome placement change",
    BiomePlacementChange.CODEC
) {
    override val shouldApply: Boolean
        get() = MainConfig.get().datapack.biome_placement

    override fun apply(values: Collection<BiomePlacementChange>) {
        val config = BiomePlacementConfig.get()
        if (MainConfig.get().biome_placement) {
            val addedBiomes = config.addedBiomes.value
            val removedBiomes = config.removedBiomes.value
            val placementChange = BiomePlacementChange(addedBiomes, removedBiomes)

            this.data?.put(id("config"), placementChange)
            this.add(id("config"), placementChange)
        }
    }
}
