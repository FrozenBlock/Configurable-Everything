package net.frozenblock.configurableeverything.biome.util

import net.fabricmc.fabric.api.biome.v1.*
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.frozenblock.configurableeverything.config.BiomeConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.id
import net.minecraft.server.packs.PackType
import java.util.function.Consumer

object BiomeConfigUtil {

	@JvmStatic
	fun init() {
        val config = BiomeConfig.get()
        if (MainConfig.get().biome == true) {
            val biomeChange = BiomeChange(config.addedFeatures.value, config.removedFeatures.value, config.replacedFeatures.value, config.musicReplacements.value)
            BiomeChanges.addChange(id("config"), biomeChange)

            val resourceLoader = ResourceManagerHelper.get(PackType.SERVER_DATA)
            resourceLoader?.registerReloadListener(BiomeChangeManager.INSTANCE)
        }
    }

    // should only be run if the config is enabled, since this is only called from the datapack manager
    @JvmStatic
    fun applyModifications(changes: Collection<BiomeChange?>?) {
        val modification: BiomeModification = BiomeModifications.create(id("feature_modifications"))
        changes?.forEach { change ->
            change?.let {
                // FEATURES
                initAddedFeatures(it, modification)
                initRemovedFeatures(it, modification)
                initReplacedFeatures(it, modification)

                // EFFECTS
                initReplacedMusic(it, modification)
            }
        }
    }

    private fun initAddedFeatures(change: BiomeChange, modification: BiomeModification) {
        val addedFeatures = change.addedFeatures
        if (addedFeatures != null) {
            for (list in addedFeatures) {
                val biome = list.biome
                val features = list.features
                val consumer: Consumer<BiomeModificationContext> = Consumer<BiomeModificationContext> { context ->
                    for (decorationFeature in features) {
                        for (placedFeature in decorationFeature.placedFeatures) {
                            context.generationSettings.addFeature(decorationFeature.decoration, placedFeature)
                        }
                    }
                }
                biome.ifLeft { modification.add(ModificationPhase.ADDITIONS, BiomeSelectors.includeByKey(it), consumer) }
                biome.ifRight { modification.add(ModificationPhase.ADDITIONS, BiomeSelectors.tag(it), consumer) }
            }
        }
    }

    private fun initRemovedFeatures(change: BiomeChange, modification: BiomeModification) {
        val removedFeatures = change.removedFeatures
        if (removedFeatures != null) {
            for (list in removedFeatures) {
                val biome = list.biome
                val features = list.features
                val consumer: Consumer<BiomeModificationContext> = Consumer<BiomeModificationContext> { context ->
                    for (decorationFeature in features) {
                        for (placedFeature in decorationFeature.placedFeatures) {
                            context.generationSettings.removeFeature(decorationFeature.decoration, placedFeature)
                        }
                    }
                }
                biome?.ifLeft { modification.add(ModificationPhase.REMOVALS, BiomeSelectors.includeByKey(it), consumer) }
                biome?.ifRight { modification.add(ModificationPhase.REMOVALS, BiomeSelectors.tag(it), consumer) }
            }
        }
    }

    private fun initReplacedFeatures(change: BiomeChange, modification: BiomeModification) {
        val replacedFeatures = change.replacedFeatures
        if (replacedFeatures != null) {
            for (list in replacedFeatures) {
                val biome = list.biome
                val replacements = list.replacements
                val consumer: Consumer<BiomeModificationContext> = Consumer<BiomeModificationContext> { context ->
                    for (replacement in replacements) {
                        context.generationSettings.removeFeature(replacement.replacement.decoration, replacement.original)
                        for (placedFeature in replacement.replacement.placedFeatures) {
                            context.generationSettings.addFeature(replacement.replacement.decoration, placedFeature)
                        }
                    }
                }
                biome?.ifLeft { modification.add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(it), consumer) }
                biome?.ifRight { modification.add(ModificationPhase.REPLACEMENTS, BiomeSelectors.tag(it), consumer) }
            }
        }
    }

    private fun initReplacedMusic(change: BiomeChange, modification: BiomeModification) {
        val replacedMusic = change.musicReplacements
        if (replacedMusic != null) {
            for (list in replacedMusic) {
                val biome = list.biome
                val music = list.music
                val consumer: Consumer<BiomeModificationContext> = Consumer<BiomeModificationContext> { context ->
                    context.effects.setMusic(music)
                }
                biome?.ifLeft { modification.add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(it), consumer) }
                biome?.ifRight { modification.add(ModificationPhase.REPLACEMENTS, BiomeSelectors.tag(it), consumer) }
            }
        }
    }
}
