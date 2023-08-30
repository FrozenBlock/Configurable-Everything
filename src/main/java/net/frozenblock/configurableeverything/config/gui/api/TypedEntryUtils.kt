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
    fun <T> makeTypedEntryList(entryBuilder: ConfigEntryBuilder, title: Component, entrySupplier: Supplier<TypedEntry<List<T>>?>?, defaultValue: Supplier<TypedEntry<List<T>>>, expandedByDefault: Boolean, tooltip: Component, setterConsumer: Consumer<TypedEntry<List<T>>>, cellCreator: BiFunction<T, NestedListListEntry<T, AbstractConfigListEntry<T>>, AbstractConfigListEntry<T>>): NestedListListEntry<T, AbstractConfigListEntry<T>> {
        val typedEntry: TypedEntry<T> = entrySupplier?.get() ?: defaultValue.get()

        return NestedListListEntry(
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
            {
                defaultValue.get().value
            },
            // Reset Button
            entryBuilder.resetButtonKey,
            // Delete Button Enabled
            true,
            // Insert In Front
            true,
            // New Cell Creation
            cellCreator
        )
    }

    @JvmStatic
    fun vec3Element(entryBuilder: ConfigEntryBuilder, title: Component, value: MutableVec3?, defaultValue: MutableVec3, expandedByDefault: Boolean = true): AbstractConfigListEntry<MutableVec3> {
        val usedValue = value ?: defaultValue
        return MultiElementBuilder(
            title,
            usedValue, // Default Value
            listOf(
                EntryBuilder(Component.literal("x"), usedValue.x,
                    0.0,
                    null,
                    usedValue::setX
                ).build(entryBuilder),
                EntryBuilder(Component.literal("y"), usedValue.y,
                    0.0,
                    null,
                    usedValue::setY
                ).build(entryBuilder),
                EntryBuilder(Component.literal("z"), usedValue.z
                    0.0,
                    null,
                    usedValue::setZ
                )
            ),
            expandedByDefault
        )
    }

    @JvmStatic
    fun setupVec3TypedEntries(entryBuilder: ConfigEntryBuilder, entrySupplier: Supplier<TypedEntry<List<MutableVec3>>?>?, setterConsumer: Consumer<TypedEntry<List<MutableVec3>>>, title: Component, entryTitle: Component): NestedListListEntry<MutableVec3, AbstractConfigListEntry<MutableVec3>> {
        return NestedListListEntry(
            //Name
            title,
            //Value
            entrySupplier?.get()?.value() ?: listOf(MutableVec3(1.0, 1.0, 1.0), MutableVec3(2.0, 2.0, 2.0), MutableVec3(69.0, 420.0, 5.0)),
            //Expanded By Default
            false,
            //Tooltip Supplier
            {
                Optional.empty()
            },
            //Save Consumer
            {
                newValue -> setterConsumer.accept(TypedEntry(entrySupplier?.get()?.type, newValue))
            },
            //Default Value
            {
                listOf(MutableVec3(1.0, 1.0, 1.0), MutableVec3(2.0, 2.0, 2.0), MutableVec3(69.0, 420.0, 5.0))
            },
            //Reset Button
            entryBuilder.resetButtonKey,
            //Delete Button Enabled
            true,
            //Insert In Front
            true
        )
        //New Cell Creation
        { element, nestedListListEntry ->
            val usedValue = element ?: MutableVec3(1.0, 1.0, 1.0)
            MultiElementListEntry(
                entryTitle,
                usedValue, //Default Value
                listOf(
                    EntryBuilder(Component.literal("x"), usedValue.x,
                        1.0,
                        null,
                        usedValue::setX
                    ).build(entryBuilder),
                    EntryBuilder(Component.literal("y"), usedValue.y,
                        1.0,
                            null,
                        usedValue::setY
                    ).build(entryBuilder),
                    EntryBuilder(Component.literal("z"), usedValue.z,
                        1.0,
                        null,
                        usedValue::setZ
                    ).build(entryBuilder),
                ),
                true
            )
        }
    }

    fun <T> makeMultiElementEntry(title: Component, value: T, defaultValue: T, list: List<AbstractConfigListEntry<out Any>, defaultExpanded: Boolean = true): MultiElementListEntry<T> {
        val usedEntry = value ?: defaultValue

        return MultiElementListEntry(
            title,
            defaultValue, // Default Value
            list,
            defaultExpanded
        )
    }
}
