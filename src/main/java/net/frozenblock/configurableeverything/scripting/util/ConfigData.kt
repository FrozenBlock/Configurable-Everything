package net.frozenblock.configurableeverything.scripting.util

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.*
import net.frozenblock.configurableeverything.util.ifExperimental
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.ConfigModification
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import java.util.function.Consumer

sealed class StableConfigData<T : Any>(override val config: Config<T>): ConfigData<T>(config) {
    override fun get(): T = super.get()!!
}

/**
 * Simplified modification of CE configs
 * @since 1.1
 */
@Suppress("unused", "ClassName")
sealed class ConfigData<T : Any>(open val config: Config<T>?) {
    data object MAIN : StableConfigData<MainConfig>(MainConfig)
    data object BIOME : StableConfigData<BiomeConfig>(BiomeConfig)
    data object BIOME_PLACEMENT : StableConfigData<BiomePlacementConfig>(BiomePlacementConfig)
    data object BLOCK : StableConfigData<BlockConfig>(BlockConfig)
    data object DATAFIXER : StableConfigData<DataFixerConfig>(DataFixerConfig)
    data object ENTITY : StableConfigData<EntityConfig>(EntityConfig)
    data object FLUID : StableConfigData<FluidConfig>(FluidConfig)
    data object GRAVITY : StableConfigData<GravityConfig>(GravityConfig)
    data object ITEM : StableConfigData<ItemConfig>(ItemConfig)
    data object LOOT : StableConfigData<LootConfig>(LootConfig)
    data object REGISTRY : StableConfigData<RegistryConfig>(RegistryConfig)
    data object SCREEN_SHAKE : StableConfigData<ScreenShakeConfig>(ScreenShakeConfig)
    data object SCULK_SPREADING : StableConfigData<SculkSpreadingConfig>(SculkSpreadingConfig)
    @Environment(EnvType.CLIENT)
    data object SPLASH_TEXT : StableConfigData<SplashTextConfig>(SplashTextConfig)
    data object STRUCTURE : StableConfigData<StructureConfig>(StructureConfig)
    data object SURFACE_RULE : StableConfigData<SurfaceRuleConfig>(SurfaceRuleConfig)
    data object TAG : ConfigData<TagConfig>(ifExperimental { TagConfig })
    data object WORLD : StableConfigData<WorldConfig>(WorldConfig)

    open fun get(): T? = config?.config()

    fun modify(modification: ConfigModification<T>)
        = config?.also { ConfigRegistry.register(it, modification) }

    @Suppress("NOTHING_TO_INLINE")
    inline fun modify(modification: Consumer<T>)
        = modify(ConfigModification(modification))

    inline fun modify(crossinline modification: (T) -> Unit)
        = modify(Consumer { config -> modification(config) })
}

