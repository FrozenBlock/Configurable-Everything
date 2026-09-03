package net.frozenblock.configurableeverything.scripting.util.api

import net.frozenblock.configurableeverything.config.*
import net.mehvahdjukaar.candlelight.api.ClientOnly

object ConfigData {
    val MAIN = MainConfig
    val BIOME = BiomeConfig
    val BIOME_PLACEMENT = BiomePlacementConfig
    val BLOCK = BlockConfig
    val DATAFIXER = DataFixerConfig
    val ENTITY = EntityConfig
    val FLUID = FluidConfig
    val GRAVITY = GravityConfig
    val ITEM = ItemConfig
    val LOOT = LootConfig
    val REGISTRY = RegistryConfig
    val SCREEN_SHAKE = ScreenShakeConfig
    val SCULK_SPREADING = SculkSpreadingConfig
    @ClientOnly
    val SPLASH_TEXT = SplashTextConfig
    val STRUCTURE = StructureConfig
    val SURFACE_RULE = SurfaceRuleConfig
    val TAG = TagConfig
    val WORLD = WorldConfig
}
