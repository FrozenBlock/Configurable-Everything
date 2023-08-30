package net.frozenblock.configurableeverything.config.gui.api

import me.shedaniel.clothconfig2.api.*
import me.shedaniel.clothconfig2.gui.entries.*
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.minecraft.network.chat.Component
import java.util.*
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Supplier

@Environment(EnvType.CLIENT)
object TypedEntryUtils {

    @JvmStatic
    fun <T> makeTypedEntryList(entryBuilder: ConfigEntryBuilder, title: Component, entrySupplier: Supplier<TypedEntry<List<T>>?>?, defaultValue: Supplier<TypedEntry<List<T>>>, expandedByDefault: Boolean = false, tooltip: Component, setterConsumer: Consumer<TypedEntry<List<T>>>, cellCreator: BiFunction<T, NestedListListEntry<T, AbstractConfigListEntry<T>>, AbstractConfigListEntry<T>>, requiresRestart: Boolean = false): NestedListListEntry<T, AbstractConfigListEntry<T>> {
        val typedEntry: TypedEntry<List<T>> = entrySupplier?.get() ?: defaultValue.get()

        val entry = NestedListListEntry(
            // Name
            title,
            // Value
            typedEntry.value,
            // Expanded By Default
            expandedByDefault,
            // Tooltip Supplier
            {
                Optional.of(arrayOf(tooltip))
            },
            // Save Consumer
            {
                newValue -> setterConsumer.accept(TypedEntry(typedEntry.type, newValue))
            },
            // Default Value
            defaultValue.get()::value,
            // Reset Button
            entryBuilder.resetButtonKey,
            // Delete Button Enabled
            true,
            // Insert In Front
            true,
            // New Cell Creation
            cellCreator
        )
        if (requiresRestart) entry.isRequiresRestart = true
        return entry
    }

    @JvmStatic
    fun <T> makeNestedList(entryBuilder: ConfigEntryBuilder, title: Component, entrySupplier: Supplier<List<T>?>?, defaultValue: Supplier<List<T>>, expandedByDefault: Boolean = false, tooltip: Component, setterConsumer: Consumer<List<T>>, cellCreator: BiFunction<T, NestedListListEntry<T, AbstractConfigListEntry<T>>, AbstractConfigListEntry<T>>, requiresRestart: Boolean = false): NestedListListEntry<T, AbstractConfigListEntry<T>> {
        val value: List<T> = entrySupplier?.get() ?: defaultValue.get()

        return NestedListListEntry(
            // Name
            title,
            // Value
            value,
            // Expanded By Default
            expandedByDefault,
            // Tooltip Supplier
            {
                Optional.of(arrayOf(tooltip))
            },
            // Save Consumer
            {
                    newValue -> setterConsumer.accept(value)
            },
            // Default Value
            defaultValue::get,
            // Reset Button
            entryBuilder.resetButtonKey,
            // Delete Button Enabled
            true,
            // Insert In Front
            true,
            // New Cell Creation
            cellCreator
        ).let {it.isRequiresRestart = requiresRestart; it}
    }

    @JvmStatic
    fun vec3Element(entryBuilder: ConfigEntryBuilder, title: Component, value: MutableVec3?, defaultValue: MutableVec3, expandedByDefault: Boolean = true): AbstractConfigListEntry<MutableVec3> {
        val usedValue = value ?: defaultValue
        return makeMultiElementEntry(
            title,
            usedValue,
            expandedByDefault,
            EntryBuilder(Component.literal("x"), usedValue.x,
                0.0,
                usedValue::setX,
                null
            ).build(entryBuilder),
            EntryBuilder(Component.literal("y"), usedValue.y,
                0.0,
                usedValue::setY,
                null
            ).build(entryBuilder),
            EntryBuilder(Component.literal("z"), usedValue.z,
                0.0,
                usedValue::setZ,
                null
            ).build(entryBuilder)
        )
    }

    fun <T> makeMultiElementEntry(title: Component, value: T, defaultExpanded: Boolean = true, vararg entries: AbstractConfigListEntry<out Any>, requiresRestart: Boolean = false): MultiElementListEntry<T> =
        MultiElementListEntry(
            title,
            value, // Default Value
            entries.asList(),
            defaultExpanded
        ).let { it.isRequiresRestart = requiresRestart; it }
}
