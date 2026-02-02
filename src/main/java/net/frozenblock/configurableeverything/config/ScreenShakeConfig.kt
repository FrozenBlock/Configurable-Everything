package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.screenshake.util.SoundScreenShake
import net.frozenblock.configurableeverything.util.*
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.SyncBehavior
import net.frozenblock.lib.config.v2.entry.ConfigEntry
import net.frozenblock.lib.config.v2.entry.EntryType
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.monster.warden.WardenAi

private val SOUND_SCREEN_SHAKE : EntryType<SoundScreenShake> = EntryType.create(
    SoundScreenShake.CODEC,
    SoundScreenShake.STREAM_CODEC,
)

// UNSYNCABLE
object ScreenShakeConfig : CEConfig("screen_shake") {
    @JvmField
    var soundScreenShakes: ConfigEntry<MutableList<SoundScreenShake>> = this.unsyncableEntry("soundScreenShakes",
        SOUND_SCREEN_SHAKE.asList(),
        mutableListOf(
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
    )

    @JvmField
    var dragonRespawnScreenShake: ConfigEntry<Boolean> = this.unsyncableEntry("dragonRespawnScreenShake", EntryType.BOOL, true)

    @JvmField
    var explosionScreenShake: ConfigEntry<Boolean> = this.unsyncableEntry("explosionScreenShake", EntryType.BOOL, true)
}
