@file:Environment(EnvType.CLIENT)

package net.frozenblock.configurableeverything.config.gui.main

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

// TODO: Re-enable when modmenu is unobfuscated
/*import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.lib.FrozenBools
import net.minecraft.client.gui.screens.Screen

/**
 * The entrypoint for Mod Menu to build the config screen.
 */
class ModMenuIntegration : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<Screen> {
        return if (FrozenBools.HAS_CLOTH_CONFIG) {
            ConfigScreenFactory { parent: Screen? ->
                ConfigurableEverythingConfigGui.buildScreen(parent)
            }
        } else ConfigScreenFactory { _ -> null }
    }
}
*/
