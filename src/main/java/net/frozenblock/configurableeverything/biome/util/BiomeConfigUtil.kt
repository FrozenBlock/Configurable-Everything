package net.frozenblock.configurableeverything.biome.util

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.fabricmc.fabric.api.biome.v1.*
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.frozenblock.configurableeverything.config.BiomeConfig
import net.frozenblock.configurableeverything.config.MainConfig
import net.frozenblock.configurableeverything.util.id
import net.minecraft.server.packs.PackType
import java.util.function.Consumer

object BiomeConfigUtil {

	fun init() {
        val config = BiomeConfig.get()
        if (MainConfig.get().biome == true) {
            val biomeChange = BiomeChange(config.addedFeatures?.value, config.removedFeatures?.value, config.replacedFeatures?.value, config.musicReplacements?.value)
            BiomeChanges.addChange(id("config"), biomeChange)

            val resourceLoader = ResourceManagerHelper.get(PackType.SERVER_DATA)
            resourceLoader?.registerReloadListener(BiomeChangeManager)
        }
    }

    // should only be run if the config is enabled, since this is only called from the datapack manager
    @JvmStatic
    fun applyModifications(changes: Collection<BiomeChange?>?) = runBlocking {
        val modification: BiomeModification = BiomeModifications.create(id("feature_modifications"))
        changes?.forEach { change -> launch {
            change?.apply {
                // FEATURES
                launch {
                    initAddedFeatures(this@apply, modification)
                }
                launch {
                    initRemovedFeatures(this@apply, modification)
                }
                launch {
                    initReplacedFeatures(this@apply, modification)
                }

                // EFFECTS
                launch {
                    initReplacedMusic(this@apply, modification)
                }
            }
        } }
    }

    private suspend fun initAddedFeatures(change: BiomeChange, modification: BiomeModification) {
        val addedFeatures = change.addedFeatures ?: return
        for (list in addedFeatures) { launch {
            val biome = list?.biome ?: continue
            val features = list.features ?: continue
            val consumer = Consumer<BiomeModificationContext> { context -> runBlocking {
                for (decorationFeature in features) { launch {
                    val placedFeatures = decorationFeature?.placedFeatures ?: continue
                    for (placedFeature in placedFeatures) { launch {
                        placedFeature?.apply {
                            context.generationSettings.addFeature(
                                decorationFeature.decoration,
                                this
                            )
                        }
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

    private suspend fun initRemovedFeatures(change: BiomeChange, modification: BiomeModification) {
        val removedFeatures = change.removedFeatures ?: return
        for (list in removedFeatures) { launch {
            val biome = list?.biome ?: return@launch
            val features = list.features ?: return@launch
            val consumer = Consumer<BiomeModificationContext> { context -> runBlocking {
                for (decorationFeature in features) { launch {
                    val placedFeatures = decorationFeature?.placedFeatures ?: continue
                    for (placedFeature in placedFeatures) { launch {
                        placedFeature?.apply {
                            context.generationSettings.removeFeature(
                                decorationFeature.decoration,
                                this
                            )
                        }
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

    private suspend fun initReplacedFeatures(change: BiomeChange, modification: BiomeModification) {
        val replacedFeatures = change.replacedFeatures ?: return
        for (list in replacedFeatures) { launch {
            val biome = list?.biome ?: continue
            val replacements = list.replacements ?: continue
            val consumer: Consumer<BiomeModificationContext> = Consumer<BiomeModificationContext> { context -> runBlocking {
                for (replacement in replacements) { launch {
                    val original = replacement?.original ?: continue
                    val decoration = replacement.replacement?.decoration ?: continue
                    val placedFeatures = replacement.replacement?.placedFeatures ?: continue
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

    private suspend fun initReplacedMusic(change: BiomeChange, modification: BiomeModification) {
        val replacedMusic = change.musicReplacements ?: return
        for (musicReplacement in replacedMusic) { launch {
            val biome = musicReplacement?.biome ?: continue
            val music = musicReplacement.music?.immutable() ?: continue
            val consumer: Consumer<BiomeModificationContext> = Consumer<BiomeModificationContext> { context ->
                context.effects.setMusic(music)
            }
            biome.ifLeft { modification.add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(it), consumer) }
            biome.ifRight { modification.add(ModificationPhase.REPLACEMENTS, BiomeSelectors.tag(it), consumer) }
        } }
    }
}
