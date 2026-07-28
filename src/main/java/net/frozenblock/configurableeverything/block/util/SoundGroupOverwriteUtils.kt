package net.frozenblock.configurableeverything.block.util;

import com.mojang.datafixers.util.Function7
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.frozenblock.lib.block.api.sound.SoundTypeCodecs
import net.frozenblock.lib.block.impl.sound.SoundTypeOverride
import net.frozenblock.lib.config.v2.entry.predicates.ConfigPredicate
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.IdMap
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.VarInt
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import java.util.*
import java.util.function.Function
import kotlin.jvm.optionals.getOrNull

@Suppress("UnstableApiUsage")
data class MutableBlockSoundGroupOverwrite(
    var blockId: Identifier,
    var soundOverwrite: MutableSoundType,
    var condition: Optional<ConfigPredicate>
) {
    companion object {
        @JvmField
        val CODEC: Codec<MutableBlockSoundGroupOverwrite> = RecordCodecBuilder.create { instance ->
            instance.group(
                Identifier.CODEC.fieldOf("id").forGetter(MutableBlockSoundGroupOverwrite::blockId),
                MutableSoundType.CODEC.fieldOf("sound_type").forGetter(MutableBlockSoundGroupOverwrite::soundOverwrite)
            ).apply(instance) { id, soundType -> MutableBlockSoundGroupOverwrite(id, soundType, Optional.empty()) } // TODO EXPLORE CONFIG PREDICATES
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, MutableBlockSoundGroupOverwrite> = StreamCodec.composite(
            Identifier.STREAM_CODEC, MutableBlockSoundGroupOverwrite::blockId,
            MutableSoundType.STREAM_CODEC, MutableBlockSoundGroupOverwrite::soundOverwrite,
            { id, soundOverwrite -> MutableBlockSoundGroupOverwrite(id, soundOverwrite, Optional.empty()) }
        )
    }

    fun immutable(): SoundTypeOverride? {
        val block: Block = BuiltInRegistries.BLOCK.getOptional(this.blockId).getOrNull() ?: return null
        return SoundTypeOverride(HolderSet.direct(block.builtInRegistryHolder()), this.soundOverwrite.immutable(), this.condition)
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
            SOUND_EVENT_STREAM_CODEC, MutableSoundType::breakSound,
            SOUND_EVENT_STREAM_CODEC, MutableSoundType::stepSound,
            SOUND_EVENT_STREAM_CODEC, MutableSoundType::placeSound,
            SOUND_EVENT_STREAM_CODEC, MutableSoundType::hitSound,
            SOUND_EVENT_STREAM_CODEC, MutableSoundType::fallSound,
            ::MutableSoundType
        )
    }

    fun immutable(): SoundType
        = SoundType(volume, pitch, breakSound, stepSound, placeSound, hitSound, fallSound)
}

fun SoundType.mutable(): MutableSoundType
    = MutableSoundType(volume, pitch, breakSound, stepSound, placeSound, hitSound, fallSound)


// TODO REMOVE ALL THIS STUFF ONCE FROZENLIB IS UPDATED
val SOUND_EVENT_STREAM_CODEC = holderValue(
    Registries.SOUND_EVENT,
    SoundEvent.DIRECT_STREAM_CODEC
)
val SOUND_TYPE_STREAM_CODEC =
    StreamCodec.composite(
        ByteBufCodecs.FLOAT, { obj: SoundType? -> obj!!.getVolume() },
        ByteBufCodecs.FLOAT, { obj: SoundType? -> obj!!.getPitch() },
        SOUND_EVENT_STREAM_CODEC, { obj: SoundType? -> obj!!.breakSound },
        SOUND_EVENT_STREAM_CODEC, { obj: SoundType? -> obj!!.stepSound },
        SOUND_EVENT_STREAM_CODEC, { obj: SoundType? -> obj!!.placeSound },
        SOUND_EVENT_STREAM_CODEC, { obj: SoundType? -> obj!!.hitSound },
        SOUND_EVENT_STREAM_CODEC, { obj: SoundType? -> obj!!.fallSound },
        { volume: Float?, pitch: Float?, breakSound: SoundEvent?, stepSound: SoundEvent?, placeSound: SoundEvent?, hitSound: SoundEvent?, fallSound: SoundEvent? ->
            SoundType(
                volume!!,
                pitch!!,
                breakSound!!,
                stepSound!!,
                placeSound!!,
                hitSound!!,
                fallSound!!
            )
        }
    )

fun <T : Any> holderValue(
    registryKey: ResourceKey<out Registry<T>>, directCodec: StreamCodec<in RegistryFriendlyByteBuf, T>
): StreamCodec<RegistryFriendlyByteBuf, T> {
    return object : StreamCodec<RegistryFriendlyByteBuf, T> {
        private val DIRECT_HOLDER_ID = 0

        fun getRegistryOrThrow(input: RegistryFriendlyByteBuf): IdMap<Holder<T>> {
            return input.registryAccess().lookupOrThrow<T>(registryKey).asHolderIdMap()
        }

        override fun decode(input: RegistryFriendlyByteBuf): T {
            val id = VarInt.read(input)
            return if (id == DIRECT_HOLDER_ID) directCodec.decode(input) else this.getRegistryOrThrow(input)
                .byIdOrThrow(id - 1).value()
        }

        override fun encode(output: RegistryFriendlyByteBuf, value: T) {
            val lookup = output.registryAccess().lookupOrThrow<T>(registryKey)
            val holder = lookup.wrapAsHolder(value)
            when (holder.kind()) {
                Holder.Kind.REFERENCE -> {
                    val id = this.getRegistryOrThrow(output).getIdOrThrow(holder)
                    VarInt.write(output, id + 1)
                }

                Holder.Kind.DIRECT -> {
                    VarInt.write(output, DIRECT_HOLDER_ID)
                    directCodec.encode(output, holder.value())
                }
            }
        }
    }
}
