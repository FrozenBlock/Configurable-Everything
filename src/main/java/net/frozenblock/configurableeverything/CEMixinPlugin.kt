package net.frozenblock.configurableeverything

import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.frozenblock.configurableeverything.config.MixinsConfig
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

class CEMixinPlugin : IMixinConfigPlugin {
    override fun onLoad(mixinPackage: String?) {}
    override fun getRefMapperConfig(): String? = null

    override fun shouldApplyMixin(targetClassName: String, mixinClassName: String): Boolean {
        val config = MixinsConfig.get()
        val isClient = FabricLoader.getInstance().environmentType === EnvType.CLIENT
        if (mixinClassName.contains("biome_placement.mixin")) return config.biome_placement
        if (mixinClassName.contains("datafixer.mixin")) return config.datafixer
        if (mixinClassName.contains("datapack.mixin")) return config.datapack
        if (mixinClassName.contains("entity.mixin.zombie")) return config.entity_zombie
        if (mixinClassName.contains("entity.mixin")) return config.entity
        if (mixinClassName.contains("fluid.mixin")) return config.fluid
        if (mixinClassName.contains("game.mixin.client")) return config.game_client
        if (mixinClassName.contains("game.mixin")) return config.game
        if (mixinClassName.contains("item.mixin")) return config.item
        if (mixinClassName.contains("loot.mixin")) return config.loot
        if (mixinClassName.contains("screenshake.mixin.client")) return config.screenshake_client
        if (mixinClassName.contains("screenshake.mixin")) return config.screenshake
        if (mixinClassName.contains("sculk_spreading.mixin")) return config.sculk_spreading
        if (mixinClassName.contains("splash_text.mixin")) return isClient && config.splash_text
        if (mixinClassName.contains("structure.mixin")) return config.structure
        if (mixinClassName.contains("world.mixin.client")) return config.world_client
        if (mixinClassName.contains("world.mixin")) return config.world
        return true
    }

    override fun acceptTargets(myTargets: Set<String>, otherTargets: Set<String>) {}

    override fun getMixins(): List<String>? = null

    override fun preApply(
        targetClassName: String,
        targetClass: ClassNode,
        mixinClassName: String,
        mixinInfo: IMixinInfo
    ) {}

    override fun postApply(
        targetClassName: String,
        targetClass: ClassNode,
        mixinClassName: String,
        mixinInfo: IMixinInfo
    ) {}
}
