package net.frozenblock.configurableeverything.biome.util

import net.fabricmc.fabric.api.biome.v1.*
import net.frozenblock.configurableeverything.config.BiomeConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils
import java.util.function.Consumer

object BiomeConfigUtil {
	@JvmStatic
	fun init() {
        val config = BiomeConfig.get()
        if (MainConfig.get().biome) {
            val modification = BiomeModifications.create(ConfigurableEverythingUtils.id("feature_modifications"))

            // FEATURES
            initAddedFeatures(config, modification)
            initRemovedFeatures(config, modification)
            initReplacedFeatures(config, modification)

            // EFFECTS
            initReplacedMusic(config, modification)
        }
    }

    private fun initAddedFeatures(config: BiomeConfig, modification: BiomeModification) {
        val addedFeatures = config.addedFeatures
        if (addedFeatures?.value() != null) {
            for (list in addedFeatures.value()!!) {
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

    private fun initRemovedFeatures(config: BiomeConfig, modification: BiomeModification) {
        val removedFeatures = config.removedFeatures
        if (removedFeatures?.value() != null) {
            for (list in removedFeatures.value()!!) {
                val biome = list.biome
                val features = list.features
                val consumer: Consumer<BiomeModificationContext> = Consumer<BiomeModificationContext> { context ->
                    for (decorationFeature in features) {
                        for (placedFeature in decorationFeature.placedFeatures) {
                            context.generationSettings.removeFeature(decorationFeature.decoration, placedFeature)
                        }
                    }
                }
                biome.ifLeft { modification.add(ModificationPhase.REMOVALS, BiomeSelectors.includeByKey(it), consumer) }
                biome.ifRight { modification.add(ModificationPhase.REMOVALS, BiomeSelectors.tag(it), consumer) }
            }
        }
    }

    private fun initReplacedFeatures(config: BiomeConfig, modification: BiomeModification) {
        val replacedFeatures = config.replacedFeatures
        if (replacedFeatures?.value() != null) {
            for (list in replacedFeatures.value()!!) {
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
                biome.ifLeft { modification.add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(it), consumer) }
                biome.ifRight { modification.add(ModificationPhase.REPLACEMENTS, BiomeSelectors.tag(it), consumer) }
            }
        }
    }

    private fun initReplacedMusic(config: BiomeConfig, modification: BiomeModification) {
        val replacedMusic = config.musicReplacements
        if (replacedMusic?.value() != null) {
            for (list in replacedMusic.value()!!) {
                val biome = list.biome
                val music = list.music
                val consumer: Consumer<BiomeModificationContext> = Consumer<BiomeModificationContext> { context ->
                    context.effects.setMusic(music)
                }
                biome.ifLeft { modification.add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(it), consumer) }
                biome.ifRight { modification.add(ModificationPhase.REPLACEMENTS, BiomeSelectors.tag(it), consumer) }
            }
        }
    }
}
