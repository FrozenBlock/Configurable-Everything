package net.frozenblock.configurableeverything.config.gui

import com.mojang.datafixers.util.Either
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import kotlin.jvm.optionals.getOrNull

fun <T : Any> String.toEitherKeyOrTag(registry: ResourceKey<Registry<T>>): Either<ResourceKey<T>, TagKey<T>>? {
    return if (this.startsWith('#'))
        Either.right(TagKey.create(registry, ResourceLocation(this.substring(1))))
    else
        Either.left(ResourceKey.create(registry, ResourceLocation(this)))
}

fun <T : Any> Either<ResourceKey<T>, TagKey<T>>?.toStr(): String {
    var string = ""
    this?.ifLeft { key -> string = key.location().toString() }
    this?.ifRight { tag -> string = tag.location.toString() }
    return string
}

fun <T : Any> String.toKey(registry: ResourceKey<out Registry<T>>): ResourceKey<T> = ResourceKey.create(registry, ResourceLocation(this))

fun <T : Any> ResourceKey<T>?.toStr(): String = this?.location()?.toString() ?: ""

fun <T : Any> String.toHolder(registry: Registry<T>): Holder<T>? = registry.getHolder(this.toKey(registry.key())).getOrNull()

fun <T : Any> Holder<T>.toStr(): String = this.unwrapKey().getOrNull()?.toStr() ?: ""
