package net.frozenblock.configurableeverything.loot.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.frozenblock.configurableeverything.util.mutListOf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.world.level.storage.loot.LootPool
import java.util.*
import kotlin.jvm.optionals.getOrNull

data class LootModification(var id: Identifier, var pool: LootPool? = null, var removals: MutableList<Identifier>? = mutableListOf()) {

    constructor(id: Identifier, pool: Optional<LootPool>?, removals: Optional<MutableList<Identifier>>?) : this(id, pool?.getOrNull(), removals?.getOrNull())

    companion object {
        @JvmField
        val CODEC: Codec<LootModification> = RecordCodecBuilder.create { instance ->
            instance.group(
                Identifier.CODEC.fieldOf("id").forGetter(LootModification::id),
                LootPool.CODEC.optionalFieldOf("addition_pool").forGetter { Optional.ofNullable(it.pool) },
                Identifier.CODEC.mutListOf().optionalFieldOf("removals").forGetter { Optional.ofNullable(it.removals) }
            ).apply(instance, ::LootModification)
        }

        // todo more compact stream codec
        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, LootModification> = StreamCodec.composite(
            Identifier.STREAM_CODEC,
            LootModification::id,
            ByteBufCodecs.optional(ByteBufCodecs.fromCodec(LootPool.CODEC)),
            { Optional.ofNullable(it.pool) },
            ByteBufCodecs.optional(Identifier.STREAM_CODEC.apply(ByteBufCodecs.list())),
            { Optional.ofNullable(it.removals) },
            ::LootModification
        )
    }
}
