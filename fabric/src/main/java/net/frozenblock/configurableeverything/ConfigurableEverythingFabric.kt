package net.frozenblock.configurableeverything

import net.fabricmc.api.ModInitializer
import net.frozenblock.configurableeverything.entity.util.EntityConfigUtil
import net.frozenblock.configurableeverything.gravity.util.GravityConfigUtil
import net.frozenblock.configurableeverything.loot.util.LootConfigUtil
import net.frozenblock.configurableeverything.registry.util.RegistryConfigUtil
import net.frozenblock.configurableeverything.splash_text.util.SplashTextConfigUtil
import net.frozenblock.configurableeverything.surface_rule.util.SurfaceRuleConfigUtil
import net.frozenblock.configurableeverything.util.ifClient
import net.frozenblock.configurableeverything.util.log
import net.frozenblock.configurableeverything.world.util.WorldConfigUtil
import kotlin.system.measureNanoTime

/**
 * Mod initializer for Configurable Everything.
 */
class ConfigurableEverythingFabric : ModInitializer {

    override fun onInitialize() {
        ConfigurableEverything.init()
        ConfigurableEverything.setup()
        val time = measureNanoTime {
            //ConfigurableEverythingIntegrations.init()

            // run functionality AFTER scripts have run
            EntityConfigUtil.init()
            GravityConfigUtil.init()
            LootConfigUtil.init()
            RegistryConfigUtil.init()
            ifClient {
                SplashTextConfigUtil.init()
            }
            SurfaceRuleConfigUtil.init()
            WorldConfigUtil.init()
        }

        log("Configurable Everything Fabric took $time nanoseconds")
    }

}
