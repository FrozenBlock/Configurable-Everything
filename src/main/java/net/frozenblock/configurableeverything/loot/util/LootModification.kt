package net.frozenblock.configurableeverything.loot.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.storage.loot.LootPool
import java.util.*
import kotlin.jvm.optionals.getOrNull

data class LootModification(var id: ResourceLocation, var removals: List<ResourceLocation?>? = listOf()) {

    constructor(id: ResourceLocation, removals: Optional<List<ResourceLocation?>>?) : this(id, pool?.getOrNull(), removals?.getOrNull())

    companion object {
        @JvmField
        val CODEC: Codec<LootModification> = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(LootModification::id),
                ResourceLocation.CODEC.listOf().optionalFieldOf("removals").forGetter { Optional.ofNullable(it.removals) }
            ).apply(instance, ::LootModification)
        }
    }
}
