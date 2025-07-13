package net.frozenblock.configurableeverything.block.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.BlockTypes

data class DataBlock(
    @JvmField var id: String,
    @JvmField var block: Block
) {
    companion object {
        @JvmField
        val CODEC: Codec<DataBlock> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.STRING.fieldOf("id").forGetter(DataBlock::id),
                BlockTypes.CODEC.fieldOf("block").forGetter(DataBlock::block)
            ).apply(instance, ::DataBlock)
        }
    }
}
