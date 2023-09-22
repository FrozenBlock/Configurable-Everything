package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.id
import net.frozenblock.configurableeverything.util.vanillaId
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.sound.api.block_sound_group.*
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents

data class BlockConfig(
    @JvmField
    var soundGroupOverwrites: TypedEntry<List<BlockSoundGroupOverwrite?>?>? = TypedEntry(
        SOUND_GROUP_OVERWRITE,
        listOf(
            vanillaId("grass_block"),
            SoundType(
                SoundEvents.ENTITY_HORSE_DEATH,
                SoundEvents.ENTITY_HORSE_DEATH,
                SoundEvents.ENTITY_HORSE_DEATH,
                SoundEvents.ENTITY_HORSE_DEATH,
                SoundEvents.ENTITY_HORSE_DEATH,
                100F,
                1F
            ),
            {true}
        )
    )
) {
    companion object {
        private val SOUND_GROUP_OVERWRITES: TypedEntryType<List<BlockSoundGroupOverwrite?>?> = ConfigRegistry.register(
            TypedEntryType(
                MOD_ID,
                Codec.list(SoundCodecs.SOUND_GROUP_OVERWRITE)
            )
        )

        @JvmField
        internal val INSTANCE: Config<BlockConfig> = ConfigRegistry.register(
            JsonConfig(
                MOD_ID,
                BlockConfig::class.java,
                makeConfigPath("block"),
                CONFIG_JSONTYPE
            )
        )

        @JvmStatic
        fun get(): BlockConfig = INSTANCE.config()
    }
}
