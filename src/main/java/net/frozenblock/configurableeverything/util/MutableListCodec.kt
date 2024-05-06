// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.
package net.frozenblock.configurableeverything.util

import com.mojang.datafixers.util.Pair
import com.mojang.datafixers.util.Unit as DFUUnit
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.ListBuilder
import com.mojang.serialization.codecs.ListCodec
import java.util.Stream

inline fun <T> Codec<T>.mutListOf(minSize: Int = 0, maxSize: Int = Int.MAX_VALUE): Codec<MutableList<T>>
    = mutList(this, minSize, maxSize)

inline fun <T> mutList(elementCodec: Codec<T>, minSize: Int = 0, maxSize: Int = Int.MAX_VALUE): Codec<MutableList<T>>
    = ListCodec(elementCodec, minSize, maxSize)

/**
 * Based on Mojang's ListCodec
 */
data class MutableListCodec<E>(private val elementCodec: Codec<E>, private val minSize: Int = 0, private val maxSize: Int = Int.MAX_VALUE) : Codec<MutableList<E>> {
    private val delegate = ListCodec(elementCodec, minSize, maxSize)

    private fun <R> createTooShortError(size: Int): DataResult<R>
        = DataResult.error { "List is too short: $size, expected range [${minSize}-${maxSize}]" }

    private fun <R> createTooLongError(size: Int): DataResult<R>
        = DataResult.error { "List is too long: $size, expected range [${minSize}-${maxSize}]" }

    override fun <T> encode(input: MutableList<E>, ops: DynamicOps<T>, prefix: T): DataResult<T> {
        return delegate.encode(input, ops, prefix)
    }

    override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<MutableList<E>, T>> {
        return ops.getList(input).setLifecycle(Lifecycle.stable()).flatMap { stream ->
            val decoder: DecoderState<T> = DecoderState(ops)
            stream.accept(decoder::accept)
            return@flatMap decoder.build()
        }
    }

    override fun toString(): String
        = "MutableListCodec[${elementCodec}]"

    companion object {
        private val INITIAL_RESULT: DataResult<DFUUnit> = DataResult.success(DFUUnit.INSTANCE, Lifecycle.stable())
    }

    private inner class DecoderState<T>(private val ops: DynamicOps<T>) {

        private val elements: MutableList<E> = mutableListOf()
        private val failed: Steam.Builder<T> = Stream.builder();
        private var result: DataResult<DFUUnit> = INITIAL_RESULT
        private var totalCount: Int

        fun accept(value: T) {
            totalCount++
            if (elements.size() >= maxSize) {
                failed.add(value)
                return
            }
            val elementResult = elementCodec.decode(ops, value)
            elementResult.error().ifPresent { error -> failed.add(value) }
            elementResult.resultOrPartial().ifPresent { pair -> elements.add(pair.getFirst()) }
            result = result.apply2stable({ result, element -> result }, elementResult)
        }

        fun build(): DataResult<Pair<MutableList<E>, T>> {
            if (elements.size() < minSize) {
                return createTooShortError(elements.size())
            }
            val errors = ops.createList(failed.build())
            val pair: Pair<MutableList<E>, T> = Pair.of(elements.toMutableList(), errors)
            if (totalCount > maxSize) {
                result = createTooLongError(totalCount)
            }
            return result.map { ignored -> pair }.setPartial(pair)
        }
    }
}
