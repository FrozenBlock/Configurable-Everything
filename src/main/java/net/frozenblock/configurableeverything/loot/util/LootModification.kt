package net.frozenblock.configurableeverything.loot.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.frozenblock.configurableeverything.util.mutListOf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.storage.loot.LootPool
import java.util.*
import kotlin.jvm.optionals.getOrNull

data class LootModification(var id: ResourceLocation, var pool: LootPool? = null, var removals: MutableList<ResourceLocation?>? = mutableListOf()) {

    constructor(id: ResourceLocation, pool: Optional<LootPool>?, removals: Optional<MutableList<ResourceLocation?>>?) : this(id, pool?.getOrNull(), removals?.getOrNull())

    companion object {
        @JvmField
        val CODEC: Codec<LootModification> = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(LootModification::id),
                LootPool.CODEC.optionalFieldOf("addition_pool").forGetter { Optional.ofNullable(it.pool) },
                ResourceLocation.CODEC.mutListOf().optionalFieldOf("removals").forGetter { Optional.ofNullable(it.removals) }
            ).apply(instance, ::LootModification)
        }
    }
}
