package net.frozenblock.configurableeverything.loot.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.storage.loot.LootPool

data class LootModification(var id: ResourceLocation?, var pool: LootPool? = null, var removals: List<ResourceLocation?>? = listOf()) {

    companion object {
        @JvmField
        val CODEC: Codec<LootModification> = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(LootModification::id),
                LootPool.CODEC.fieldOf("addition_pool").forGetter(LootModification::pool),
                ResourceLocation.CODEC.listOf().fieldOf("removals").forGetter(LootModification::removals)
            ).apply(instance, ::LootModification)
        }
    }
}
