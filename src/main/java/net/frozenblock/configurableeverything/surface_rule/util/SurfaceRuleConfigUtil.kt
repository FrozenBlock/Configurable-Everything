package net.frozenblock.configurableeverything.surface_rule.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.config.SurfaceRuleConfig
import net.frozenblock.lib.worldgen.surface.api.FrozenDimensionBoundRuleSource
import net.frozenblock.lib.worldgen.surface.api.SurfaceRuleEvents
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.SurfaceRules.RuleSource

object SurfaceRuleConfigUtil {

    @JvmField
    val DIMENSION_SURFACE_RULE_CODEC: Codec<FrozenDimensionBoundRuleSource> = RecordCodecBuilder.create { instance ->
        instance.group(
            ResourceLocation.CODEC.fieldOf("dimension").forGetter(FrozenDimensionBoundRuleSource::dimension),
            RuleSource.CODEC.fieldOf("rule").forGetter(FrozenDimensionBoundRuleSource::ruleSource)
        ).apply(instance, ::FrozenDimensionBoundRuleSource)
    };

    @JvmStatic
    fun init() {
        val config = SurfaceRuleConfig.get();
        if (MainConfig.get().surface_rule) {
            SurfaceRuleEvents.MODIFY_GENERIC.register { context ->
                run {
                    if (config.addedSurfaceRule?.value != null) {
                        context.addAll(config.addedSurfaceRule.value)
                    }
                }
            }
        }
    }
}
