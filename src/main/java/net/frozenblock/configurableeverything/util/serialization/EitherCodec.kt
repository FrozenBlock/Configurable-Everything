package net.frozenblock.configurableeverything.util.serialization

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import java.util.*

fun <F : Any, S : Any> either(left: Codec<F>, right: Codec<S>): EitherCodec<F, S> = EitherCodec(left, right)

class EitherCodec<F : Any, S : Any>(private val first: Codec<F>, private val second: Codec<S>) : Codec<Either<F, S>> {
    override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<Either<F, S>, T>>? {
        val firstRead = first.decode(ops, input).map { vo: Pair<F, T> ->
            vo.mapFirst<Either<F, S>> { value: F ->
                Either.left(
                    value
                )
            }
        }
        return if (firstRead.result().isPresent) {
            firstRead
        } else second.decode(ops, input).map { vo: Pair<S, T> ->
            vo.mapFirst { value: S ->
                Either.right(value)
            }
        }
    }

    override fun <T> encode(input: Either<F, S>, ops: DynamicOps<T>, prefix: T): DataResult<T> {
        return input.map(
            { value1: F -> first.encode(value1, ops, prefix) }
        ) { value2: S -> second.encode(value2, ops, prefix) }
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val eitherCodec = o as EitherCodec<*, *>
        return first == eitherCodec.first && second == eitherCodec.second
    }

    override fun hashCode(): Int {
        return Objects.hash(first, second)
    }

    override fun toString(): String {
        return "EitherCodec[$first, $second]"
    }
}

