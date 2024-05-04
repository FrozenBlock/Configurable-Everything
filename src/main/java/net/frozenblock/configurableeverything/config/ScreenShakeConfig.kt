package net.frozenblock.configurableeverything.config

import net.frozenblock.configurableeverything.screenshake.util.SoundScreenShake
import net.frozenblock.configurableeverything.util.CEConfig
import net.frozenblock.configurableeverything.util.CONFIG_FORMAT
import net.frozenblock.configurableeverything.util.MOD_ID
import net.frozenblock.configurableeverything.util.makeConfigPath
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.frozenblock.lib.config.api.entry.TypedEntryType
import net.frozenblock.lib.config.api.instance.xjs.XjsConfig
import net.frozenblock.lib.config.api.registry.ConfigRegistry
import net.frozenblock.lib.config.api.sync.SyncBehavior
import net.frozenblock.lib.config.api.sync.annotation.EntrySyncData
import net.frozenblock.lib.config.api.sync.annotation.UnsyncableConfig
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.monster.warden.WardenAi

private val SOUND_SCREEN_SHAKE : TypedEntryType<List<SoundScreenShake>> = ConfigRegistry.register(
    TypedEntryType(
        MOD_ID,
        SoundScreenShake.CODEC.listOf()
    )
)

@UnsyncableConfig
data class ScreenShakeConfig(
    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    var soundScreenShakes: TypedEntry<List<SoundScreenShake>> = TypedEntry.create(
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
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    var dragonRespawnScreenShake: Boolean = true,

    @JvmField
    @EntrySyncData(behavior = SyncBehavior.UNSYNCABLE)
    var explosionScreenShake: Boolean = true
) {
    companion object : CEConfig<ScreenShakeConfig>(
        ScreenShakeConfig::class,
        "screen_shake"
    ) {

        init {
            ConfigRegistry.register(this)
        }

        @JvmStatic
        @JvmOverloads
        fun get(real: Boolean = false): ScreenShakeConfig = if (real) this.instance() else this.config()
    }
}
