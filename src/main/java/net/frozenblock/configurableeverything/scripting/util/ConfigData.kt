package net.frozenblock.configurableeverything.scripting.util

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.*
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.ConfigModification
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import java.util.function.Consumer

/**
 * Simplified modification of CE configs
 * @since 1.1
 */
@Suppress("unused", "ClassName")
sealed class ConfigData<T : Any>(@JvmField val config: Config<T>) {
    data object MAIN : ConfigData<MainConfig>(MainConfig)
    data object BIOME : ConfigData<BiomeConfig>(BiomeConfig)
    data object BIOME_PLACEMENT : ConfigData<BiomePlacementConfig>(BiomePlacementConfig)
    data object BLOCK : ConfigData<BlockConfig>(BlockConfig)
    data object DATAFIXER : ConfigData<DataFixerConfig>(DataFixerConfig)
    data object ENTITY : ConfigData<EntityConfig>(EntityConfig)
    data object FLUID : ConfigData<FluidConfig>(FluidConfig)
    data object GAME : ConfigData<GameConfig>(GameConfig)
    data object GRAVITY : ConfigData<GravityConfig>(GravityConfig)
    data object ITEM : ConfigData<ItemConfig>(ItemConfig)
    data object LOOT : ConfigData<LootConfig>(LootConfig)
    data object REGISTRY : ConfigData<RegistryConfig>(RegistryConfig)
    data object SCREEN_SHAKE : ConfigData<ScreenShakeConfig>(ScreenShakeConfig)
    data object SCULK_SPREADING : ConfigData<SculkSpreadingConfig>(SculkSpreadingConfig)
    @Environment(EnvType.CLIENT)
    data object SPLASH_TEXT : ConfigData<SplashTextConfig>(SplashTextConfig)
    data object STRUCTURE : ConfigData<StructureConfig>(StructureConfig)
    data object SURFACE_RULE : ConfigData<SurfaceRuleConfig>(SurfaceRuleConfig)
    data object WORLD : ConfigData<WorldConfig>(WorldConfig)

    fun get(): T = config.config()

    fun modify(modification: ConfigModification<T>)
        = config.apply { ConfigRegistry.register(this, modification) }

    @Suppress("NOTHING_TO_INLINE")
    inline fun modify(modification: Consumer<T>)
        = modify(ConfigModification(modification))
}

