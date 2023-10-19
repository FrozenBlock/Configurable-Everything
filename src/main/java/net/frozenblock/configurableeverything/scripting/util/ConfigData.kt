package net.frozenblock.configurableeverything.scripting.util

import net.frozenblock.configurableeverything.config.*
import net.frozenblock.configurableeverything.util.ENABLE_EXPERIMENTAL_FEATURES
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.ConfigModification
import net.frozenblock.lib.config.api.registry.ConfigRegistry

/**
 * Simplified modification of CE configs
 *
 * Planned for 1.1
 */
sealed class ConfigData<T : Any>(val config: Config<T>) {
    data object MAIN : ConfigData<MainConfig>(MainConfig.INSTANCE)
    data object BIOME : ConfigData<BiomeConfig>(BiomeConfig.INSTANCE)
    data object BIOME_PLACEMENT : ConfigData<BiomePlacementConfig>(BiomePlacementConfig.INSTANCE)
    data object BLOCK : ConfigData<BlockConfig>(BlockConfig.INSTANCE)
    data object DATAFIXER : ConfigData<DataFixerConfig>(DataFixerConfig.INSTANCE)
    data object ENTITY : ConfigData<EntityConfig>(EntityConfig.INSTANCE)
    data object FLUID : ConfigData<FluidConfig>(FluidConfig.INSTANCE)
    data object GAME : ConfigData<GameConfig>(GameConfig.INSTANCE)
    data object ITEM : ConfigData<ItemConfig>(ItemConfig.INSTANCE)
    data object REGISTRY : ConfigData<RegistryConfig>(RegistryConfig.INSTANCE)
    data object SCREENSHAKE : ConfigData<ScreenShakeConfig>(ScreenShakeConfig.INSTANCE)
    data object SPLASH_TEXT : ConfigData<SplashTextConfig>(SplashTextConfig.INSTANCE)
    data object SURFACE_RULE : ConfigData<SurfaceRuleConfig>(SurfaceRuleConfig.INSTANCE)
    data object WORLD : ConfigData<WorldConfig>(WorldConfig.INSTANCE)

    fun modify(modification: ConfigModification<T>) {
        if (ENABLE_EXPERIMENTAL_FEATURES)
            ConfigRegistry.register(config, modification)
        else throw UnsupportedOperationException("Experimental features are disabled")
    }
}
