package net.frozenblock.configurableeverything.config.gui

import com.mojang.datafixers.util.Either
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.api.Requirement
import me.shedaniel.clothconfig2.gui.entries.NestedListListEntry
import net.frozenblock.lib.config.api.client.gui.*;
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Supplier
import kotlin.jvm.optionals.getOrNull
import kotlin.math.exp

fun <T : Any> String.toEitherKeyOrTag(registry: ResourceKey<Registry<T>>): Either<ResourceKey<T>, TagKey<T>> {
    return if (this.startsWith('#'))
        Either.right(TagKey.create(registry, Identifier.parse(this.substring(1))))
    else
        Either.left(ResourceKey.create(registry, Identifier.parse(this)))
}

fun <T : Any> Either<ResourceKey<T>, TagKey<T>>?.toStr(): String {
    var string = ""
    this?.ifLeft { key -> string = key?.identifier().toString() }
    this?.ifRight { tag -> string = "#${tag?.location.toString()}" }
    return string
}

inline fun <T : Any> String.toKey(registry: ResourceKey<out Registry<T>>): ResourceKey<T> = ResourceKey.create(registry, Identifier.parse(this))

inline fun <T : Any> ResourceKey<T>?.toStr(): String = this?.identifier()?.toString() ?: ""

inline fun <T : Any> String.toHolder(registry: Registry<T>): Holder<T>? = registry.get(this.toKey(registry.key())).getOrNull()

inline fun <T : Any> Holder<T>.toStr(): String = this.unwrapKey().getOrNull()?.toStr() ?: ""

@Suppress("UNCHECKED_CAST", "UnstableApiUsage")
fun <T> typedEntryList(
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
        Supplier { entrySupplier?.invoke() } as Supplier<TypedEntry<List<T>>?>?,
        Supplier { defaultValue() } as Supplier<TypedEntry<List<T>>>,
        expandedByDefault,
        tooltip,
        { entry -> setterConsumer(entry.let {
            if (entry.value() is MutableList<T>) return@let entry as TypedEntry<MutableList<T>>
            entry.setValue(entry.value().toMutableList())
            return@let entry as TypedEntry<MutableList<T>>
        }) },
        { a, b -> cellCreator(a, b) },
        requiresRestart,
        requirement
    )
}

@Suppress("UNCHECKED_CAST", "UnstableApiUsage")
fun <T> nestedList(
    entryBuilder: ConfigEntryBuilder,
    title: Component,
    entrySupplier: (() -> MutableList<T>?)?,
    defaultValue: () -> MutableList<T>,
    expandedByDefault: Boolean = false,
    tooltip: Component,
    setterConsumer: (MutableList<T>) -> Unit,
    cellCreator: (T, NestedListListEntry<T, AbstractConfigListEntry<T>>) -> AbstractConfigListEntry<T>,
    requiresRestart: Boolean = false,
    requirement: Requirement? = null
): NestedListListEntry<T, out AbstractConfigListEntry<T>> {
    return nestedList(
        entryBuilder,
        title,
        Supplier { entrySupplier?.invoke() } as Supplier<List<T>?>?,
        Supplier { defaultValue() } as Supplier<List<T>>,
        expandedByDefault,
        tooltip,
        { entry -> setterConsumer(entry.let {
            if (entry is MutableList<T>) return@let entry
            return@let entry.toMutableList()
        }) },
        cellCreator,
        requiresRestart,
        requirement
    )
}
