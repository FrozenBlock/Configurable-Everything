package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.screenshake.util.SoundScreenShake
import net.frozenblock.configurableeverything.util.CONFIG_JSONTYPE
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.Config
import net.frozenblock.lib.config.api.instance.json.JsonConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.monster.warden.WardenAi

data class ScreenShakeConfig(
    @JvmField
    var soundScreenShakes: TypedEntry<List<SoundScreenShake?>>? = TypedEntry(
        SOUND_SCREEN_SHAKE,
        listOf(
            SoundScreenShake(
                SoundEvents.ENDER_DRAGON_GROWL.location,
                2.5f,
                140,
                90,
                48f
            ),
            SoundScreenShake(
                SoundEvents.ENDER_DRAGON_FLAP.location,
                0.5f,
                8,
                1,
                26f
            ),
            SoundScreenShake(
                SoundEvents.RAVAGER_ROAR.location,
                1f,
                17,
                1,
                23f
            ),
            SoundScreenShake(
                SoundEvents.WARDEN_EMERGE.location,
                0.75f,
                WardenAi.EMERGE_DURATION - 30,
                1,
                20f
            ),
            SoundScreenShake(
                SoundEvents.WARDEN_ROAR.location,
                1.5f,
                WardenAi.ROAR_DURATION - 25,
                1,
                19f
            ),
            SoundScreenShake(
                SoundEvents.WARDEN_ATTACK_IMPACT.location,
                1f,
                3,
                1,
                2f
            ),
            SoundScreenShake(
                SoundEvents.WARDEN_SONIC_BOOM.location,
                2f,
                25,
                1,
                18f
            )
        )
    ),

    @JvmField
    var dragonRespawnScreenShake: Boolean? = true,

    @JvmField
    var explosionScreenShake: Boolean? = true
) {
    companion object {
        private val SOUND_SCREEN_SHAKE : TypedEntryType<List<SoundScreenShake?>> = ConfigRegistry.register(
            TypedEntryType(
                MOD_ID,
                SoundScreenShake.CODEC.listOf()
            )
        )

        @JvmField
        internal val INSTANCE: Config<ScreenShakeConfig> = ConfigRegistry.register(
            JsonConfig(
                MOD_ID,
                ScreenShakeConfig::class.java,
                makeConfigPath("screen_shake"),
                CONFIG_JSONTYPE
            )
        )

        @JvmStatic
        fun get(): ScreenShakeConfig = INSTANCE.config()
    }
}
