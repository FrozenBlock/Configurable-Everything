package net.frozenblock.configurableeverything.scripting.util

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.config.*
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.ConfigModification
import net.frozenblock.lib.config.api.registry.ConfigRegistry

sealed class StableConfigData<T : Any, C>(override val config: Config<T>): ConfigData<T, C>(config) where C : ConfigWrapper<T> {
    override fun get(): T = super.get()!!
}

/**
 * Simplified modification of CE configs
 * @since 1.1
 */
@Suppress("unused", "ClassName")
sealed class ConfigData<T : Any, C>(open val config: Config<T>?) where C : ConfigWrapper<T> {
    data object MAIN : StableConfigData<MainConfig, MainWrapper>(MainConfig) {
        override fun modify(modification: (MainWrapper) -> Unit) {
            ConfigRegistry.register(MainConfig, ConfigModification { modification(MainWrapper(it)) })
        }
    }
    data object BIOME : StableConfigData<BiomeConfig, BiomeWrapper>(BiomeConfig) {
        override fun modify(modification: (BiomeWrapper) -> Unit) {
            ConfigRegistry.register(BiomeConfig, ConfigModification { modification(BiomeWrapper(it)) })
        }
    }
    data object BIOME_PLACEMENT : StableConfigData<BiomePlacementConfig, BiomePlacementWrapper>(BiomePlacementConfig) {
        override fun modify(modification: (BiomePlacementWrapper) -> Unit) {
            ConfigRegistry.register(BiomePlacementConfig, ConfigModification { modification(BiomePlacementWrapper(it)) })
        }
    }
    data object BLOCK : StableConfigData<BlockConfig, BlockWrapper>(BlockConfig) {
        override fun modify(modification: (BlockWrapper) -> Unit) {
            ConfigRegistry.register(BlockConfig, ConfigModification { modification(BlockWrapper(it)) })
        }
    }
    data object DATAFIXER : StableConfigData<DataFixerConfig, DataFixerWrapper>(DataFixerConfig) {
        override fun modify(modification: (DataFixerWrapper) -> Unit) {
            ConfigRegistry.register(DataFixerConfig, ConfigModification { modification(DataFixerWrapper(it)) })
        }
    }
    data object ENTITY : StableConfigData<EntityConfig, EntityWrapper>(EntityConfig) {
        override fun modify(modification: (EntityWrapper) -> Unit) {
            ConfigRegistry.register(EntityConfig, ConfigModification { modification(EntityWrapper(it)) })
        }
    }
    data object FLUID : StableConfigData<FluidConfig, FluidWrapper>(FluidConfig) {
        override fun modify(modification: (FluidWrapper) -> Unit) {
            ConfigRegistry.register(FluidConfig, ConfigModification { modification(FluidWrapper(it)) })
        }
    }
    data object GRAVITY : StableConfigData<GravityConfig, GravityWrapper>(GravityConfig) {
        override fun modify(modification: (GravityWrapper) -> Unit) {
            ConfigRegistry.register(GravityConfig, ConfigModification { modification(GravityWrapper(it)) })
        }
    }
    data object ITEM : StableConfigData<ItemConfig, ItemWrapper>(ItemConfig) {
        override fun modify(modification: (ItemWrapper) -> Unit) {
            ConfigRegistry.register(ItemConfig, ConfigModification { modification(ItemWrapper(it)) })
        }
    }
    data object LOOT : StableConfigData<LootConfig, LootWrapper>(LootConfig) {
        override fun modify(modification: (LootWrapper) -> Unit) {
            ConfigRegistry.register(LootConfig, ConfigModification { modification(LootWrapper(it)) })
        }
    }
    data object REGISTRY : StableConfigData<RegistryConfig, RegistryWrapper>(RegistryConfig) {
        override fun modify(modification: (RegistryWrapper) -> Unit) {
            ConfigRegistry.register(RegistryConfig, ConfigModification { modification(RegistryWrapper(it)) })
        }
    }
    data object SCREEN_SHAKE : StableConfigData<ScreenShakeConfig, ScreenShakeWrapper>(ScreenShakeConfig) {
        override fun modify(modification: (ScreenShakeWrapper) -> Unit) {
            ConfigRegistry.register(ScreenShakeConfig, ConfigModification { modification(ScreenShakeWrapper(it)) })
        }
    }
    data object SCULK_SPREADING : StableConfigData<SculkSpreadingConfig, SculkSpreadingWrapper>(SculkSpreadingConfig) {
        override fun modify(modification: (SculkSpreadingWrapper) -> Unit) {
            ConfigRegistry.register(SculkSpreadingConfig, ConfigModification { modification(SculkSpreadingWrapper(it)) })
        }
    }
    @Environment(EnvType.CLIENT)
    data object SPLASH_TEXT : StableConfigData<SplashTextConfig, SplashTextWrapper>(SplashTextConfig) {
        override fun modify(modification: (SplashTextWrapper) -> Unit) {
            ConfigRegistry.register(SplashTextConfig, ConfigModification { modification(SplashTextWrapper(it)) })
        }
    }
    data object STRUCTURE : StableConfigData<StructureConfig, StructureWrapper>(StructureConfig) {
        override fun modify(modification: (StructureWrapper) -> Unit) {
            ConfigRegistry.register(StructureConfig, ConfigModification { modification(StructureWrapper(it)) })
        }
    }
    data object SURFACE_RULE : StableConfigData<SurfaceRuleConfig, SurfaceRuleWrapper>(SurfaceRuleConfig) {
        override fun modify(modification: (SurfaceRuleWrapper) -> Unit) {
            ConfigRegistry.register(SurfaceRuleConfig, ConfigModification { modification(SurfaceRuleWrapper(it)) })
        }
    }
    data object TAG : StableConfigData<TagConfig, TagWrapper>(TagConfig) {
        override fun modify(modification: (TagWrapper) -> Unit) {
            ConfigRegistry.register(TagConfig, ConfigModification { modification(TagWrapper(it)) })
        }
    }
    data object WORLD : StableConfigData<WorldConfig, WorldWrapper>(WorldConfig) {
        override fun modify(modification: (WorldWrapper) -> Unit) {
            ConfigRegistry.register(WorldConfig, ConfigModification { modification(WorldWrapper(it)) })
        }
    }

    abstract fun modify(modification: (C) -> Unit)

    open fun get(): T? = config?.config()
}

open class ConfigWrapper<T : Any>(protected val config: T)
