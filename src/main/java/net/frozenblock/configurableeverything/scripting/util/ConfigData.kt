package net.frozenblock.configurableeverything.scripting.util

import net.frozenblock.configurableeverything.config.*
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.ConfigModification
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import java.util.function.Consumer

sealed class ConfigData<T : Any>(private val config: Config<T>) {
    object MAIN : ConfigData(MainConfig.INSTANCE)
    object BIOME : ConfigData(BiomeConfig.INSTANCE)
    object BIOME_PLACEMENT : ConfigData(BiomePlacementConfig.INSTANCE)
    object BLOCK : ConfigData(BlockConfig.INSTANCE)
    object DATAFIXER : ConfigData(DataFixerConfig.INSTANCE)
    object ENTITY : ConfigData(EntityConfig.INSTANCE)
    object FLUID : ConfigData(FluidConfig.INSTANCE)
    object GAME : ConfigData(GameConfig.INSTANCE)
    object ITEM : ConfigData(ItemConfig.INSTANCE)
    object REGISTRY : ConfigData(RegistryConfig.INSTANCE)
    object SCREEN_SHAKE : ConfigData(ScreenShakeConfig.INSTANCE)
    object SPLASH_TEXT : ConfigData(SplashTextConfig.INSTANCE)
    object SURFACE_RULE : ConfigData(SurfaceRuleConfig.INSTANCE)
    object WORLD : ConfigData(WorldConfig.INSTANCE)

    fun modify(Consumer<T> modification) {
        ConfigRegistry.register(ConfigModification(config, modification))
    }
}