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
        get() = MainConfig.datapackBiomePlacement.get()
    override fun apply(values: Collection<BiomePlacementChange>) {
        if (MainConfig.biome_placement.get()) {
            val addedBiomes = BiomePlacementConfig.addedBiomes.get()
            val removedBiomes = BiomePlacementConfig.removedBiomes.get()
            val replacedBiomes = BiomePlacementConfig.replacedBiomes.get()
            val placementChange = BiomePlacementChange(addedBiomes, removedBiomes, replacedBiomes)

            this.data?.put(id("config"), placementChange)
            this.add(id("config"), placementChange)
        }
    }
}
