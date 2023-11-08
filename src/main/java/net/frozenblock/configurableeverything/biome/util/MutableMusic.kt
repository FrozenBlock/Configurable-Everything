package net.frozenblock.configurableeverything.biome.util

import com.mojang.serialization.Codec
import net.minecraft.core.Holder
import net.minecraft.sounds.Music
import net.minecraft.sounds.SoundEvent

data class MutableMusic(
    @JvmField var event: Holder<SoundEvent>?,
    @JvmField var minDelay: Int?,
    @JvmField var maxDelay: Int?,
    @JvmField var replaceCurrentMusic: Boolean?
) {
    companion object {
        @JvmField
        val CODEC: Codec<MutableMusic> = Music.CODEC.xmap({ music -> music.mutable()}, { mutMusic -> mutMusic.immutable() })
    }
}

fun Music.mutable(): MutableMusic = MutableMusic(this.event, this.minDelay, this.maxDelay, this.replaceCurrentMusic())

fun MutableMusic?.immutable(): Music? {
    if (this == null) return null
    val event = this.event
    val minDelay = this.minDelay
    val maxDelay = this.maxDelay
    val replaceCurrentMusic = this.replaceCurrentMusic
    if (event == null || minDelay == null || maxDelay == null || replaceCurrentMusic == null)
        return null

    return Music(event, minDelay, maxDelay, replaceCurrentMusic)
}
