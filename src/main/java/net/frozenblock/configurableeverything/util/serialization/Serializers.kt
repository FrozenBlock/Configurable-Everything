@file:OptIn(ExperimentalSerializationApi::class)

package net.frozenblock.configurableeverything.util.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.Music
import net.minecraft.sounds.SoundEvent
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.*
import net.minecraft.world.level.biome.Biome.ClimateSettings
import net.minecraft.world.level.biome.Biome.TemperatureModifier
import net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import java.util.*
import kotlin.enums.EnumEntries
import kotlin.jvm.optionals.getOrNull

fun <T : Any> T?.optional(): Optional<T> = if (this != null) Optional.of(this) else Optional.empty()

fun Encoder.encodeNullableBoolean(value: Boolean?) = this.encodeNullableSerializableValue(Boolean.serializer(), value)
fun Encoder.encodeNullableInt(value: Int?) = this.encodeNullableSerializableValue(Int.serializer(), value)
fun Encoder.encodeNullableFloat(value: Float?) = this.encodeNullableSerializableValue(Float.serializer(), value)
fun Encoder.encodeNullableDouble(value: Double?) = this.encodeNullableSerializableValue(Double.serializer(), value)
fun Encoder.encodeNullableString(value: String?) = this.encodeNullableSerializableValue(String.serializer(), value)


fun Encoder.encodeResourceLocation(value: ResourceLocation?) {
    this.encodeString(value.toString())
}

fun Decoder.decodeResourceLocation(): ResourceLocation = ResourceLocation(this.decodeString())

fun Encoder.encodeResourceKey(value: ResourceKey<out Any?>) {
    this.encodeResourceLocation(value.registry())
    this.encodeResourceLocation(value.location())
}

inline fun <reified T> Decoder.decodeResourceKey(): ResourceKey<T> = ResourceKey.create(this.decodeResourceLocation(), this.decodeResourceLocation())

fun Encoder.encodeTagKey(value: TagKey<out Any?>) {
    this.encodeResourceKey(value.registry())
    this.encodeResourceLocation(value.location)
}

fun Decoder.decodeTagKey(): TagKey<out Any?> = TagKey.create<Any?>(this.decodeResourceKey<Registry<Any?>>(), this.decodeResourceLocation())

fun Encoder.encodeClimateSettings(value: ClimateSettings) {
    this.encodeBoolean(value.hasPrecipitation)
    this.encodeFloat(value.temperature)
    this.encodeSerializableValue(TemperatureModifierSerializer, value.temperatureModifier)
    this.encodeFloat(value.downfall)
}

fun Decoder.decodeClimateSettings(): ClimateSettings = ClimateSettings(this.decodeBoolean(), this.decodeFloat(), this.decodeSerializableValue(TemperatureModifierSerializer), this.decodeFloat())

object ResourceLocationSerializer : KSerializer<ResourceLocation> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(ResourceLocation::class.simpleName!!, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ResourceLocation = decoder.decodeResourceLocation()

    override fun serialize(encoder: Encoder, value: ResourceLocation) = encoder.encodeResourceLocation(value)

}

object ResourceKeySerializer : KSerializer<ResourceKey<out Any?>> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(ResourceKey::class.simpleName!!, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ResourceKey<out Any?> = decoder.decodeResourceKey()

    override fun serialize(encoder: Encoder, value: ResourceKey<out Any?>) = encoder.encodeResourceKey(value)
}

object TagKeySerializer : KSerializer<TagKey<out Any?>> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(TagKey::class.simpleName!!, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): TagKey<out Any?> = decoder.decodeTagKey()

    override fun serialize(encoder: Encoder, value: TagKey<out Any?>) = encoder.encodeTagKey(value)

}

object TemperatureModifierSerializer : KSerializer<TemperatureModifier> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(TemperatureModifier::class.simpleName!!, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): TemperatureModifier = TemperatureModifier.valueOf(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: TemperatureModifier) = encoder.encodeString(value.name)
}

open class EnumSerializer<T>(private val enum: EnumEntries<T>) : KSerializer<T> where T : Enum<T> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(Enum::class.simpleName!!, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): T {
        val `class` = enum[0].javaClass
        return java.lang.Enum.valueOf(`class`, decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: T) {
        TODO("Not yet implemented")
    }

}

object ClimateSettingsSerializer : KSerializer<ClimateSettings> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(ClimateSettings::class.simpleName!!) {
        element<Boolean>("has_precipitation")
        element<Float>("temperature")
        element("temperature_modifier", TemperatureModifierSerializer.descriptor, isOptional = true)
        element<Float>("downfall")
        val enumS = EnumSerializer(TemperatureModifier.entries)
    }

    override fun deserialize(decoder: Decoder): ClimateSettings = decoder.decodeClimateSettings()

    override fun serialize(encoder: Encoder, value: ClimateSettings) = encoder.encodeClimateSettings(value)
}

object AmbientParticleSettingsSerializer : KSerializer<AmbientParticleSettings> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(AmbientParticleSettings::class.simpleName!!) {


    }

    override fun deserialize(decoder: Decoder): AmbientParticleSettings {
        TODO("Not yet implemented")
    }

    override fun serialize(encoder: Encoder, value: AmbientParticleSettings) {
        TODO("Not yet implemented")
    }

}

object SoundEventSerializer : KSerializer<SoundEvent?> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(SoundEvent::class.simpleName!!, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): SoundEvent? = BuiltInRegistries.SOUND_EVENT[decoder.decodeResourceKey()]

    override fun serialize(encoder: Encoder, value: SoundEvent?) {
        encoder.encodeResourceLocation(BuiltInRegistries.SOUND_EVENT.getKey(value))
    }

}

object AmbientMoodSettingsSerializer : KSerializer<AmbientMoodSettings> {
    override val descriptor: SerialDescriptor
        get() = TODO("Not yet implemented")

    override fun deserialize(decoder: Decoder): AmbientMoodSettings {
        TODO("Not yet implemented")
    }

    override fun serialize(encoder: Encoder, value: AmbientMoodSettings) {
        TODO("Not yet implemented")
    }

}

object AmbientAdditionsSettingsSerializer : KSerializer<AmbientAdditionsSettings> {
    override val descriptor: SerialDescriptor
        get() = TODO("Not yet implemented")

    override fun deserialize(decoder: Decoder): AmbientAdditionsSettings {
        TODO("Not yet implemented")
    }

    override fun serialize(encoder: Encoder, value: AmbientAdditionsSettings) {
        TODO("Not yet implemented")
    }

}

object BiomeSpecialEffectsSerializer : KSerializer<BiomeSpecialEffects> {
    object GrassColorModifierSerializer : EnumSerializer<GrassColorModifier>(GrassColorModifier.entries)

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(BiomeSpecialEffects::class.simpleName!!) {
        element<Int>("fog_color")
        element<Int>("water_color")
        element<Int>("water_fog_color")
        element<Int>("sky_color")
        element<Int>("foliage_color", isOptional = true)
        element<Int>("grass_color", isOptional = true)
        element("grass_color_modifier", GrassColorModifierSerializer.descriptor, isOptional = true)
        element("particle", AmbientParticleSettingsSerializer.descriptor, isOptional = true)
        element("ambient_sound", SoundEventSerializer.descriptor, isOptional = true)
        element("mood_sound", AmbientMoodSettingsSerializer.descriptor, isOptional = true)
        element("additions_sound", AmbientAdditionsSettingsSerializer.descriptor, isOptional = true)
        element("music", MusicSerializer.descriptor, isOptional = true)
    }

    override fun deserialize(decoder: Decoder): BiomeSpecialEffects = BiomeSpecialEffects(
        decoder.decodeInt(),
        decoder.decodeInt(),
        decoder.decodeInt(),
        decoder.decodeInt(),
        Optional.of(decoder.decodeInt()),
        Optional.of(decoder.decodeInt()),
        decoder.decodeNullableSerializableValue(GrassColorModifierSerializer) ?: GrassColorModifier.NONE,
        Optional.of(decoder.decodeSerializableValue(AmbientParticleSettingsSerializer)),
        Optional.of(BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(decoder.decodeSerializableValue(SoundEventSerializer)).orElseThrow())),
        Optional.of(decoder.decodeSerializableValue(AmbientMoodSettingsSerializer)),
        Optional.of(decoder.decodeSerializableValue(AmbientAdditionsSettingsSerializer)),
        Optional.of(decoder.decodeSerializableValue(MusicSerializer))
    )

    override fun serialize(encoder: Encoder, value: BiomeSpecialEffects) {
        encoder.encodeInt(value.fogColor)
        encoder.encodeInt(value.waterColor)
        encoder.encodeInt(value.waterFogColor)
        encoder.encodeInt(value.skyColor)
        encoder.encodeNullableInt(value.foliageColorOverride.getOrNull())
        encoder.encodeNullableInt(value.grassColorOverride.getOrNull())
        encoder.encodeNullableSerializableValue(GrassColorModifierSerializer, value.grassColorModifier)
        encoder.encodeNullableSerializableValue(AmbientParticleSettingsSerializer, value.ambientParticleSettings.getOrNull())
        encoder.encodeNullableSerializableValue(SoundEventSerializer, value.ambientLoopSoundEvent.getOrNull()?.value())
        encoder.encodeNullableSerializableValue(AmbientMoodSettingsSerializer, value.ambientMoodSettings.getOrNull())
        encoder.encodeNullableSerializableValue(AmbientAdditionsSettingsSerializer, value.ambientAdditionsSettings.getOrNull())
        encoder.encodeNullableSerializableValue(MusicSerializer, value.backgroundMusic.getOrNull())
    }
}

object BiomeSerializer : KSerializer<Biome> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(Biome::class.simpleName!!) {
        // climate settings
        element<Boolean>("has_precipitation")
        element<Float>("temperature")
        element("temperature_modifier", TemperatureModifierSerializer.descriptor)
        element<Float>("downfall")

        element("effects", BiomeSpecialEffectsSerializer.descriptor)
    }
    override fun deserialize(decoder: Decoder): Biome = Biome(
        decoder.decodeClimateSettings(),
        decoder.decodeSerializableValue(BiomeSpecialEffectsSerializer),
        TODO(),
        TODO()
    )

    override fun serialize(encoder: Encoder, value: Biome) {
        TODO("Not yet implemented")
    }
}

object PlacedFeatureSerializer : KSerializer<PlacedFeature> {
    override val descriptor: SerialDescriptor
        get() = TODO("Not yet implemented")

    override fun deserialize(decoder: Decoder): PlacedFeature {
        TODO("Not yet implemented")
    }

    override fun serialize(encoder: Encoder, value: PlacedFeature) {
        TODO("Not yet implemented")
    }
}

object MusicSerializer : KSerializer<Music> {
    override fun deserialize(decoder: Decoder): Music {
        TODO("Not yet implemented")
    }

    override val descriptor: SerialDescriptor
        get() = TODO("Not yet implemented")

    override fun serialize(encoder: Encoder, value: Music) {
        TODO("Not yet implemented")
    }
}

val module = SerializersModule {
    contextual(ResourceLocation::class, ResourceLocationSerializer)
    contextual(ResourceKey::class, ResourceKeySerializer)
    contextual(TagKey::class, TagKeySerializer)
    contextual(Biome::class, BiomeSerializer)
    contextual(Music::class, MusicSerializer)
    contextual(PlacedFeature::class, PlacedFeatureSerializer)
}.apply {
    Json.serializersModule.plus(this)
}
