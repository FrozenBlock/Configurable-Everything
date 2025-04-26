package net.frozenblock.configurableeverything.block.util;

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.frozenblock.lib.block.sound.impl.overwrite.HolderSetBlockSoundTypeOverwrite
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import java.util.function.BooleanSupplier
import kotlin.jvm.optionals.getOrNull

@Suppress("UnstableApiUsage")
data class MutableBlockSoundGroupOverwrite(
    var blockId: ResourceLocation,
    var soundOverwrite: MutableSoundType,
    var condition: BooleanSupplier
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
    }

    fun immutable(): SoundType
        = SoundType(volume, pitch, breakSound, stepSound, placeSound, hitSound, fallSound)
}

fun SoundType.mutable(): MutableSoundType
    = MutableSoundType(volume, pitch, breakSound, stepSound, placeSound, hitSound, fallSound)
