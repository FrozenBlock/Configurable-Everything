package net.frozenblock.configurableeverything.block.util;

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.frozenblock.lib.block.sound.api.SoundTypeCodecs
import net.frozenblock.lib.block.sound.impl.overwrite.HolderSetBlockSoundTypeOverwrite
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import java.util.function.BooleanSupplier
import kotlin.jvm.optionals.getOrNull

@Suppress("UnstableApiUsage")
data class MutableBlockSoundGroupOverwrite(
    var blockId: Identifier,
    var soundOverwrite: MutableSoundType,
    var condition: BooleanSupplier
) {
    companion object {
        @JvmField
        val CODEC: Codec<MutableBlockSoundGroupOverwrite> = RecordCodecBuilder.create { instance ->
            instance.group(
                Identifier.CODEC.fieldOf("id").forGetter(MutableBlockSoundGroupOverwrite::blockId),
                MutableSoundType.CODEC.fieldOf("sound_type").forGetter(MutableBlockSoundGroupOverwrite::soundOverwrite)
            ).apply(instance) { id, soundType -> MutableBlockSoundGroupOverwrite(id, soundType) { true } }
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, MutableBlockSoundGroupOverwrite> = StreamCodec.composite(
            Identifier.STREAM_CODEC, MutableBlockSoundGroupOverwrite::blockId,
            MutableSoundType.STREAM_CODEC, MutableBlockSoundGroupOverwrite::soundOverwrite,
            { id, soundOverwrite -> MutableBlockSoundGroupOverwrite(id, soundOverwrite) { true } }
        )
    }

    fun immutable(): HolderSetBlockSoundTypeOverwrite? {
        val block: Block = BuiltInRegistries.BLOCK.getOptional(this.blockId).getOrNull() ?: return null
        return HolderSetBlockSoundTypeOverwrite(HolderSet.direct(block.builtInRegistryHolder()), this.soundOverwrite.immutable(), this.condition)
    }
}

//fun HolderSetBlockSoundTypeOverwrite.mutable(): MutableBlockSoundGroupOverwrite
//    = MutableBlockSoundGroupOverwrite(this.blockId, this.soundOverwrite.mutable(), this.condition)


data class MutableSoundType(
    var volume: Float,
    var pitch: Float,
    var breakSound: SoundEvent,
    var stepSound: SoundEvent,
    var placeSound: SoundEvent,
    var hitSound: SoundEvent,
    var fallSound: SoundEvent
) {
    companion object {
        @JvmField
        val CODEC: Codec<MutableSoundType> = RecordCodecBuilder.create { instance ->
            val soundCodec = BuiltInRegistries.SOUND_EVENT.byNameCodec()
            instance.group(
                Codec.FLOAT.fieldOf("volume").forGetter(MutableSoundType::volume),
                Codec.FLOAT.fieldOf("pitch").forGetter(MutableSoundType::pitch),
                soundCodec.fieldOf("break_sound").forGetter(MutableSoundType::breakSound),
                soundCodec.fieldOf("step_sound").forGetter(MutableSoundType::stepSound),
                soundCodec.fieldOf("place_sound").forGetter(MutableSoundType::placeSound),
                soundCodec.fieldOf("hit_sound").forGetter(MutableSoundType::hitSound),
                soundCodec.fieldOf("fall_sound").forGetter(MutableSoundType::fallSound)
            ).apply(instance, ::MutableSoundType)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, MutableSoundType> = StreamCodec.composite(
            ByteBufCodecs.FLOAT, MutableSoundType::volume,
            ByteBufCodecs.FLOAT, MutableSoundType::pitch,
            SoundTypeCodecs.SOUND_EVENT_STREAM_CODEC, MutableSoundType::breakSound,
            SoundTypeCodecs.SOUND_EVENT_STREAM_CODEC, MutableSoundType::stepSound,
            SoundTypeCodecs.SOUND_EVENT_STREAM_CODEC, MutableSoundType::placeSound,
            SoundTypeCodecs.SOUND_EVENT_STREAM_CODEC, MutableSoundType::hitSound,
            SoundTypeCodecs.SOUND_EVENT_STREAM_CODEC, MutableSoundType::fallSound,
            ::MutableSoundType
        )
    }

    fun immutable(): SoundType
        = SoundType(volume, pitch, breakSound, stepSound, placeSound, hitSound, fallSound)
}

fun SoundType.mutable(): MutableSoundType
    = MutableSoundType(volume, pitch, breakSound, stepSound, placeSound, hitSound, fallSound)
