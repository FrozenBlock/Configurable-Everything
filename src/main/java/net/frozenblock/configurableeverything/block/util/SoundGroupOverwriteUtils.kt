package net.frozenblock.configurableeverything.block.util;

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.frozenblock.lib.sound.api.block_sound_group.BlockSoundGroupOverwrite
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.level.block.SoundType
import java.util.function.BooleanSupplier

data class MutableBlockSoundGroupOverwrite(
    var blockId: ResourceLocation?,
    var soundOverwrite: MutableSoundType?,
    var condition: BooleanSupplier?
) {
    companion object {
        @JvmField
        val CODEC: Codec<MutableBlockSoundGroupOverwrite> = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(MutableBlockSoundGroupOverwrite::blockId),
                MutableSoundType.CODEC.fieldOf("sound_type").forGetter(MutableBlockSoundGroupOverwrite::soundOverwrite)
            ).apply(instance) { id, soundType -> MutableBlockSoundGroupOverwrite(id, soundType) { true } }
        }
    }

    override fun toString(): String = "MutableBlockSoundGroupOverwrite[blockId=$blockId, sound_type=$soundOverwrite, condition=$condition]"

    fun immutable(): BlockSoundGroupOverwrite? {
        val blockId = this.blockId ?: return null
        val soundOverwrite = this.soundOverwrite?.immutable() ?: return null
        val condition = this.condition ?: return null
        return BlockSoundGroupOverwrite(blockId, soundOverwrite, condition)
    }
}

fun BlockSoundGroupOverwrite.mutable(): MutableBlockSoundGroupOverwrite
    = MutableBlockSoundGroupOverwrite(blockId, soundOverwrite.mutable(), condition)


data class MutableSoundType(
    var volume: Float?,
    var pitch: Float?,
    var breakSound: SoundEvent?,
    var stepSound: SoundEvent?,
    var placeSound: SoundEvent?,
    var hitSound: SoundEvent?,
    var fallSound: SoundEvent?
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
    }

    override fun toString(): String = "MutableSoundType[volume=$volume, pitch=$pitch, break_sound=$breakSound, step_sound=$stepSound, place_sound=$placeSound, hit_sound=$hitSound, fall_sound=$fallSound]"

    fun immutable(): SoundType? {
        val volume = this.volume
        val pitch = this.pitch
        val breakSound = this.breakSound
        val stepSound = this.stepSound
        val placeSound = this.placeSound
        val hitSound = this.hitSound
        val fallSound = this.fallSound
        if (volume == null || pitch == null || breakSound == null || stepSound == null || placeSound == null || hitSound == null || fallSound == null)
            return null

        return SoundType(volume, pitch, breakSound, stepSound, placeSound, hitSound, fallSound)
    }
}

fun SoundType.mutable(): MutableSoundType
    = MutableSoundType(volume, pitch, breakSound, stepSound, placeSound, hitSound, fallSound)
