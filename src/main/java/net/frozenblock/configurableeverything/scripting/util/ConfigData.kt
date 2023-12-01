package net.frozenblock.configurableeverything.scripting.util

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.loader.api.FabricLoader
import net.frozenblock.configurableeverything.config.*
import net.frozenblock.configurableeverything.util.experimental
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.ConfigModification
import net.frozenblock.lib.config.api.registry.ConfigRegistry

/**
 * Simplified modification of CE configs
 *
 * Planned for 1.1
 */
sealed class ConfigData<T : Any?>(val config: Config<T>?) {
    data object MAIN : ConfigData<MainConfig>(MainConfig)
    data object BIOME : ConfigData<BiomeConfig>(BiomeConfig)
    data object BIOME_PLACEMENT : ConfigData<BiomePlacementConfig>(BiomePlacementConfig)
    data object BLOCK : ConfigData<BlockConfig>(BlockConfig)
    data object DATAFIXER : ConfigData<DataFixerConfig>(DataFixerConfig)
    data object ENTITY : ConfigData<EntityConfig>(EntityConfig)
    data object FLUID : ConfigData<FluidConfig>(FluidConfig)
    data object GAME : ConfigData<GameConfig>(GameConfig)
    data object ITEM : ConfigData<ItemConfig>(ItemConfig)
    data object REGISTRY : ConfigData<RegistryConfig>(RegistryConfig)
    data object SCREEN_SHAKE : ConfigData<ScreenShakeConfig>(ScreenShakeConfig)
    @Environment(EnvType.CLIENT)
    data object SPLASH_TEXT : ConfigData<SplashTextConfig>(SplashTextConfig)
    data object STRUCTURE : ConfigData<StructureConfig>(StructureConfig)
    data object SURFACE_RULE : ConfigData<SurfaceRuleConfig>(SurfaceRuleConfig)
    data object WORLD : ConfigData<WorldConfig>(WorldConfig)

    fun get(): T? = experimental { config?.config() }

    fun modify(modification: ConfigModification<T>)
        = experimental { config?.apply { ConfigRegistry.register(this, modification) }}
}
