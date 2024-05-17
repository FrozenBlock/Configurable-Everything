// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.
package net.frozenblock.configurableeverything.util

import com.google.common.collect.ImmutableList
import com.mojang.datafixers.util.Pair
import com.mojang.datafixers.util.Unit
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.codecs.ListCodec
import org.apache.commons.lang3.mutable.MutableObject
import java.util.*
import java.util.stream.Stream

inline fun <T> Codec<T>.mutListOf(): Codec<MutableList<T>>
    = mutList(this)

inline fun <T> mutList(elementCodec: Codec<T>): Codec<MutableList<T>>
    = MutableListCodec(elementCodec)

/**
 * Based on Mojang's ListCodec
 */
data class MutableListCodec<E>(private val elementCodec: Codec<E>) : Codec<MutableList<E>> {
    private val delegate = ListCodec(elementCodec)

    override fun <T> encode(input: MutableList<E>, ops: DynamicOps<T>, prefix: T): DataResult<T> {
        return delegate.encode(input, ops, prefix)
    }

    override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<MutableList<E>, T>> {
        return ops.getList(input).setLifecycle(Lifecycle.stable()).flatMap { stream ->
            val read = ImmutableList.builder<E>()
            val failed = Stream.builder<T>()
            val result = MutableObject<DataResult<Unit>>(DataResult.success(Unit.INSTANCE, Lifecycle.stable()))

            stream.accept { t ->
                val element = elementCodec.decode(ops, t)
                element.error().ifPresent { failed.add(t) }
                result.value = result.value.apply2stable({ r, v ->
                    read.add(v.first)
                    return@apply2stable r
                }, element)
            }

            val elements = read.build()
            val errors = ops.createList(failed.build())

            val pair = Pair.of(elements.toMutableList(), errors)

            return@flatMap result.value.map { pair }.setPartial(pair)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other === null || javaClass !== other.javaClass) {
            return false
        }
        return Objects.equals(elementCodec, (other as MutableListCodec<*>).elementCodec)
    }

    override fun toString(): String
        = "MutableListCodec[${elementCodec}]"
}
