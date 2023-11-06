package net.frozenblock.configurableeverything.biome.util

import net.minecraft.sounds.Music
import net.minecraft.sounds.SoundEvent

data class MutableMusic(
    @JvmField var sound: Holder<SoundEvent>?,
    @JvmField var minDelay: Int?,
    @JvmField var maxDelay: Int?,
    @JvmField var replaceCurrentMusic: Boolean?
) {
    companion object {
        @JvmField
        val CODEC: Codec<MutableMusic> = Music.CODEC.xmap({ music -> music.mutable()}, { mutMusic -> mutMusic.immutable() })
    }
}

fun Music.mutable(): MutableMusic = MutableMusic(this.sound, this.minDelay, this.maxDelay, this.replaceCurrentMusic)

fun MutableMusic.immutable(): Music? {
    val sound = this.sound
    val minDelay = this.minDelay
    val maxDelay = this.maxDelay
    val replaceCurrentMusic = this.replaceCurrentMusic
    if (sound == null || minDelay == null || maxDelay == null || replaceCurrentMusic == null)
        return null

    return Music(sound, minDelay, maxDelay, replaceCurrentMusic)
}