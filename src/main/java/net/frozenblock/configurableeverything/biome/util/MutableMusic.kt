package net.frozenblock.configurableeverything.biome.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.Music
import net.minecraft.sounds.SoundEvent
import java.util.*
import kotlin.jvm.optionals.getOrNull

data class MutableMusic(
    @JvmField var event: Holder<SoundEvent>?,
    @JvmField var minDelay: Int?,
    @JvmField var maxDelay: Int?,
    @JvmField var replaceCurrentMusic: Boolean?
) {
    companion object {
        @JvmField
        val CODEC: Codec<MutableMusic> = RecordCodecBuilder.create { instance ->
            instance.group(
                SoundEvent.CODEC.optionalFieldOf("sound").forGetter { Optional.ofNullable(it.event) },
                Codec.INT.fieldOf("min_delay").forGetter(MutableMusic::minDelay),
                Codec.INT.fieldOf("max_delay").forGetter(MutableMusic::maxDelay),
                Codec.BOOL.fieldOf("replace_current_music").forGetter(MutableMusic::replaceCurrentMusic)
            ).apply(instance) { event, min, max, replace ->
                MutableMusic(event.getOrNull(), min, max, replace)
            }
        }
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
