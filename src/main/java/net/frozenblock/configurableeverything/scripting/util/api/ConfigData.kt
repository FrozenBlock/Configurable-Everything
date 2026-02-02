package net.frozenblock.configurableeverything.scripting.util.api

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.*
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.ConfigModification
import net.frozenblock.lib.config.api.registry.ConfigRegistry

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
    @Environment(EnvType.CLIENT)
    val SPLASH_TEXT = SplashTextConfig
    val STRUCTURE = StructureConfig
    val SURFACE_RULE = SurfaceRuleConfig
    val TAG = TagConfig
    val WORLD = WorldConfig
}

sealed class StableConfigV1Data<T : Any, C>(override val config: Config<T>): ConfigV1Data<T, C>(config) where C : ConfigWrapper<T> {
    override fun get(): T = super.get()!!
}

/**
 * Simplified modification of CE configs
 * @since 1.1
 */
@Suppress("unused", "ClassName")
sealed class ConfigV1Data<T : Any, C>(open val config: Config<T>?) where C : ConfigWrapper<T> {
    data object BIOME : StableConfigV1Data<BiomeConfig, BiomeWrapper>(BiomeConfig) {
        override fun modify(modification: (BiomeWrapper) -> Unit) {
            ConfigRegistry.register(BiomeConfig, ConfigModification { modification(BiomeWrapper(it)) })
        }
    }
    data object BIOME_PLACEMENT : StableConfigV1Data<BiomePlacementConfig, BiomePlacementWrapper>(BiomePlacementConfig) {
        override fun modify(modification: (BiomePlacementWrapper) -> Unit) {
            ConfigRegistry.register(BiomePlacementConfig, ConfigModification { modification(BiomePlacementWrapper(it)) })
        }
    }
    data object BLOCK : StableConfigV1Data<BlockConfig, BlockWrapper>(BlockConfig) {
        override fun modify(modification: (BlockWrapper) -> Unit) {
            ConfigRegistry.register(BlockConfig, ConfigModification { modification(BlockWrapper(it)) })
        }
    }
    data object DATAFIXER : StableConfigV1Data<DataFixerConfig, DataFixerWrapper>(DataFixerConfig) {
        override fun modify(modification: (DataFixerWrapper) -> Unit) {
            ConfigRegistry.register(DataFixerConfig, ConfigModification { modification(DataFixerWrapper(it)) })
        }
    }
    data object ENTITY : StableConfigV1Data<EntityConfig, EntityWrapper>(EntityConfig) {
        override fun modify(modification: (EntityWrapper) -> Unit) {
            ConfigRegistry.register(EntityConfig, ConfigModification { modification(EntityWrapper(it)) })
        }
    }
    data object ITEM : StableConfigV1Data<ItemConfig, ItemWrapper>(ItemConfig) {
        override fun modify(modification: (ItemWrapper) -> Unit) {
            ConfigRegistry.register(ItemConfig, ConfigModification { modification(ItemWrapper(it)) })
        }
    }
    data object LOOT : StableConfigV1Data<LootConfig, LootWrapper>(LootConfig) {
        override fun modify(modification: (LootWrapper) -> Unit) {
            ConfigRegistry.register(LootConfig, ConfigModification { modification(LootWrapper(it)) })
        }
    }
    data object REGISTRY : StableConfigV1Data<RegistryConfig, RegistryWrapper>(RegistryConfig) {
        override fun modify(modification: (RegistryWrapper) -> Unit) {
            ConfigRegistry.register(RegistryConfig, ConfigModification { modification(RegistryWrapper(it)) })
        }
    }
    data object SCREEN_SHAKE : StableConfigV1Data<ScreenShakeConfig, ScreenShakeWrapper>(ScreenShakeConfig) {
        override fun modify(modification: (ScreenShakeWrapper) -> Unit) {
            ConfigRegistry.register(ScreenShakeConfig, ConfigModification { modification(ScreenShakeWrapper(it)) })
        }
    }
    data object SCULK_SPREADING : StableConfigV1Data<SculkSpreadingConfig, SculkSpreadingWrapper>(SculkSpreadingConfig) {
        override fun modify(modification: (SculkSpreadingWrapper) -> Unit) {
            ConfigRegistry.register(SculkSpreadingConfig, ConfigModification { modification(SculkSpreadingWrapper(it)) })
        }
    }
    @Environment(EnvType.CLIENT)
    data object SPLASH_TEXT : StableConfigV1Data<SplashTextConfig, SplashTextWrapper>(SplashTextConfig) {
        override fun modify(modification: (SplashTextWrapper) -> Unit) {
            ConfigRegistry.register(SplashTextConfig, ConfigModification { modification(SplashTextWrapper(it)) })
        }
    }
    data object STRUCTURE : StableConfigV1Data<StructureConfig, StructureWrapper>(StructureConfig) {
        override fun modify(modification: (StructureWrapper) -> Unit) {
            ConfigRegistry.register(StructureConfig, ConfigModification { modification(StructureWrapper(it)) })
        }
    }
    data object SURFACE_RULE : StableConfigV1Data<SurfaceRuleConfig, SurfaceRuleWrapper>(SurfaceRuleConfig) {
        override fun modify(modification: (SurfaceRuleWrapper) -> Unit) {
            ConfigRegistry.register(SurfaceRuleConfig, ConfigModification { modification(SurfaceRuleWrapper(it)) })
        }
    }
    data object TAG : StableConfigV1Data<TagConfig, TagWrapper>(TagConfig) {
        override fun modify(modification: (TagWrapper) -> Unit) {
            ConfigRegistry.register(TagConfig, ConfigModification { modification(TagWrapper(it)) })
        }
    }

    abstract fun modify(modification: (C) -> Unit)

    open fun get(): T? = config?.config()
}

open class ConfigWrapper<T : Any>(protected val config: T)
