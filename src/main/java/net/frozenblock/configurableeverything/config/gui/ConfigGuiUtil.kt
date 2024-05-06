package net.frozenblock.configurableeverything.config.gui

import com.mojang.datafixers.util.Either
import net.frozenblock.lib.config.api.client.gui.*;
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import kotlin.jvm.optionals.getOrNull

fun <T : Any> String.toEitherKeyOrTag(registry: ResourceKey<Registry<T>>): Either<ResourceKey<T>, TagKey<T>> {
    return if (this.startsWith('#'))
        Either.right(TagKey.create(registry, ResourceLocation(this.substring(1))))
    else
        Either.left(ResourceKey.create(registry, ResourceLocation(this)))
}

fun <T : Any> Either<ResourceKey<T>, TagKey<T>>?.toStr(): String {
    var string = ""
    this?.ifLeft { key -> string = key?.location().toString() }
    this?.ifRight { tag -> string = "#${tag?.location.toString()}" }
    return string
}

inline fun <T : Any> String.toKey(registry: ResourceKey<out Registry<T>>): ResourceKey<T> = ResourceKey.create(registry, ResourceLocation(this))

inline fun <T : Any> ResourceKey<T>?.toStr(): String = this?.location()?.toString() ?: ""

inline fun <T : Any> String.toHolder(registry: Registry<T>): Holder<T>? = registry.getHolder(this.toKey(registry.key())).getOrNull()

inline fun <T : Any> Holder<T>.toStr(): String = this.unwrapKey().getOrNull()?.toStr() ?: ""

inline fun <T> typedEntryList(
    entryBuilder: ConfigEntryBuilder,
    title: Component,
    entrySupplier: (() -> TypedEntry<MutableList<T>>?)?,
    defaultValue: () -> TypedEntry<MutableList<T>>,
    expandedByDefault: Boolean = false,
    tooltip: Component,
    setterConsumer: (TypedEntry<MutableList<T>>) -> Unit,
    cellCreator: (T, NestedListListEntry<T, AbstractConfigListEntry<T>>) -> AbstractConfigListEntry<T>,
    requiresRestart: Boolean = false,
    requirement: Requirement? = null
): NestedListListEntry<T, AbstractConfigListEntry<T>> {
    return typedEntryList(
        entryBuilder,
        title,
        Supplier { entrySupplier() } as Supplier<TypedEntry<List<T>>?>?,
        Supplier { defaultValue() } as Supplier<TypedEntry<List<T>>>,
        expandedByDefault,
        tooltip,
        Consumer { list -> setterConsumer(list.toMutableList()) },
        BiFunction { a, b -> cellCreator(a, b) },
        requiresRestart,
        requirement
    )
}