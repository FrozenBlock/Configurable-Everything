package net.frozenblock.configurableeverything.loot.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.frozenblock.configurableeverything.util.mutListOf
import net.minecraft.resources.ResourceLocation
import java.util.*
import kotlin.jvm.optionals.getOrNull

data class LootModification(var id: ResourceLocation, var removals: MutableList<ResourceLocation?>? = listOf()) {

    constructor(id: ResourceLocation, removals: Optional<MutableList<ResourceLocation?>>?) : this(id, pool?.getOrNull(), removals?.getOrNull())

    companion object {
        @JvmField
        val CODEC: Codec<LootModification> = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(LootModification::id),
                ResourceLocation.CODEC.mutListOf().optionalFieldOf("removals").forGetter { Optional.ofNullable(it.removals) }
            ).apply(instance, ::LootModification)
        }
    }
}
