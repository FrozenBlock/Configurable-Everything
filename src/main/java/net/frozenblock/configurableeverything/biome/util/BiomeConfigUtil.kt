package net.frozenblock.configurableeverything.biome.util

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.fabricmc.fabric.api.biome.v1.*
import net.fabricmc.fabric.api.resource.v1.ResourceLoader
import net.frozenblock.configurableeverything.config.BiomeConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.value
import net.frozenblock.lib.sound.api.asImmutable
import net.minecraft.server.packs.PackType
import java.util.function.Consumer

internal object BiomeConfigUtil {

	internal fun init() {
        val config = BiomeConfig.get()
        if (MainConfig.get().biome) {
            val biomeChange = BiomeChange(config.addedFeatures.value, config.removedFeatures.value, config.replacedFeatures.value, config.musicReplacements.value)
            BiomeChanges.add(id("config"), biomeChange)

            val resourceLoader = ResourceLoader.get(PackType.SERVER_DATA)
            resourceLoader.registerReloader(id("biome_changes"), BiomeChanges)
        }
    }

    // should only be run if the config is enabled, since this is only called from the datapack manager
    @JvmStatic
    internal fun applyModifications(changes: Collection<BiomeChange>) = runBlocking {
        val modification: BiomeModification = BiomeModifications.create(id("feature_modifications"))
        changes.forEach { change -> launch {
            change.also { change ->
                // FEATURES
                launch {
                    initAddedFeatures(change, modification)
                }
                launch {
                    initRemovedFeatures(change, modification)
                }
                launch {
                    initReplacedFeatures(change, modification)
                }

                // EFFECTS
                launch {
                    initReplacedMusic(change, modification)
                }
            }
        } }
    }

    private suspend fun initAddedFeatures(change: BiomeChange, modification: BiomeModification) = coroutineScope {
        val addedFeatures = change.addedFeatures
        for (list in addedFeatures) { launch {
            val biome = list.biome
            val features = list.features
            val consumer = Consumer<BiomeModificationContext> { context -> runBlocking {
                for (decorationFeature in features) { launch {
                    val placedFeatures = decorationFeature.placedFeatures
                    for (placedFeature in placedFeatures) { launch {
                        context.generationSettings.addFeature(
                            decorationFeature.decoration,
                            placedFeature
                        )
                    } }
                } }
            } }

            biome.ifLeft {
                modification.add(
                    ModificationPhase.ADDITIONS,
                        BiomeSelectors.includeByKey(it),
                        consumer
                )
            }

            biome.ifRight {
                modification.add(
                    ModificationPhase.ADDITIONS,
                    BiomeSelectors.tag(it),
                    consumer
                )
            }
        } }
    }

    private suspend fun initRemovedFeatures(change: BiomeChange, modification: BiomeModification) = coroutineScope {
        val removedFeatures = change.removedFeatures
        for (list in removedFeatures) { launch {
            val biome = list.biome
            val features = list.features
            val consumer = Consumer<BiomeModificationContext> { context -> runBlocking {
                for (decorationFeature in features) { launch {
                    val placedFeatures = decorationFeature.placedFeatures
                    for (placedFeature in placedFeatures) { launch {
                        context.generationSettings.removeFeature(
                            decorationFeature.decoration,
                            placedFeature
                        )
                    } }
                } }
            } }

            biome.ifLeft {
                modification.add(
                    ModificationPhase.REMOVALS,
                    BiomeSelectors.includeByKey(it),
                    consumer
                )
            }

            biome.ifRight {
                modification.add(
                    ModificationPhase.REMOVALS,
                    BiomeSelectors.tag(it),
                    consumer
                )
            }
        } }
    }

    private suspend fun initReplacedFeatures(change: BiomeChange, modification: BiomeModification) = coroutineScope {
        val replacedFeatures = change.replacedFeatures
        for (list in replacedFeatures) { launch {
            val biome = list.biome
            val replacements = list.replacements
            val consumer: Consumer<BiomeModificationContext> = Consumer<BiomeModificationContext> { context -> runBlocking {
                for (replacement in replacements) { launch {
                    val original = replacement.original
                    val decoration = replacement.replacement.decoration
                    val placedFeatures = replacement.replacement.placedFeatures

                    context.generationSettings.removeFeature(decoration, original)

                    for (placedFeature in placedFeatures) {
                        context.generationSettings.addFeature(decoration, placedFeature)
                    }
                } }
            } }

            biome.ifLeft {
                modification.add(
                    ModificationPhase.REPLACEMENTS,
                    BiomeSelectors.includeByKey(it),
                    consumer
                )
            }

            biome.ifRight {
                modification.add(
                    ModificationPhase.REPLACEMENTS,
                    BiomeSelectors.tag(it),
                    consumer
                )
            }
        } }
    }

    private suspend fun initReplacedMusic(change: BiomeChange, modification: BiomeModification) = coroutineScope {
        val replacedMusic = change.musicReplacements
        for (musicReplacement in replacedMusic) { launch {
            val biome = musicReplacement.biome
            val music = musicReplacement.music.asImmutable ?: return@launch
            val consumer: Consumer<BiomeModificationContext> = Consumer<BiomeModificationContext> { context ->
                //context.attributes.set(EnvironmentAttributes.BACKGROUND_MUSIC, music)
                //context.effects.setMusic(music)
                // todo fix
            }
            biome.ifLeft { modification.add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(it), consumer) }
            biome.ifRight { modification.add(ModificationPhase.REPLACEMENTS, BiomeSelectors.tag(it), consumer) }
        } }
    }
}
