package net.frozenblock.configurableeverything.config.gui.main

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.lib.FrozenBools
import net.minecraft.client.gui.screens.Screen

@Environment(EnvType.CLIENT)
class ModMenuIntegration : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<Screen> {
        return if (FrozenBools.HAS_CLOTH_CONFIG) {
            ConfigScreenFactory { parent: Screen? ->
                ConfigurableEverythingConfigGui.buildScreen(parent)
            }
        } else ConfigScreenFactory { _ -> null }
    }
}
