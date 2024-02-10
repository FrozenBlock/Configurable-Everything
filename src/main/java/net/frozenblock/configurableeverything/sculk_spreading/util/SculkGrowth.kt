package net.frozenblock.configurableeverything.sculk_spreading.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.block.state.BlockState

data class SculkGrowth(
    @JvmField var restrictedToWorldgen: Boolean?,
    @JvmField var rarity: Int?,
    @JvmField var blockState: BlockState?
) {

    companion object {
        @JvmField
        val CODEC: Codec<SculkGrowth> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.BOOL.fieldOf("restrictedToWorldgen").forGetter(SculkGrowth::restrictedToWorldgen),
                Codec.INT.fieldOf("rarity").forGetter(SculkGrowth::rarity),
                BlockState.CODEC.fieldOf("block").forGetter(SculkGrowth::blockState)
            ).apply(instance, ::SculkGrowth)
        }
    }
}
