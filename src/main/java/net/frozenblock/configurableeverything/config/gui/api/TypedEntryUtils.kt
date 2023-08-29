package net.frozenblock.configurableeverything.config.gui.api

import me.shedaniel.clothconfig2.api.*
import me.shedaniel.clothconfig2.gui.entries.*
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.lib.config.api.entry.TypedEntry
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier

@Environment(EnvType.CLIENT)
object TypedEntryUtils {

    @JvmStatic
    fun setupVec3TypedEntries(entryBuilder: ConfigEntryBuilder, entrySupplier: Supplier<TypedEntry<List<Vec3>>>, setterConsumer: Consumer<TypedEntry<List<Vec3>>>, title: Component, entryTitle: Component): NestedListListEntry<Vec3, AbstractConfigListEntry<Vec3>> {
        return NestedListListEntry(
            //Name
            title,
            //Value
            entrySupplier.get()?.value() ?: listOf(Vec3(1.0, 1.0, 1.0), Vec3(2.0, 2.0, 2.0), Vec3(69.0, 420.0, 5.0)),
            //Expanded By Default
            false,
            //Tooltip Supplier
            {
                Optional.empty()
            },
            //Save Consumer
            {
                newValue -> setterConsumer.accept(TypedEntry(entrySupplier.get().type, newValue))
            },
            //Default Value
            {
                listOf(Vec3(1.0, 1.0, 1.0), Vec3(2.0, 2.0, 2.0), Vec3(69.0, 420.0, 5.0))
            },
            //Reset Button
            entryBuilder.resetButtonKey,
            //Delete Button Enabled
            true,
            //Insert In Front
            true,
            //New Cell Creation
            {
            element, nestedListListEntry ->
                val newDefault = Vec3(1.0, 1.0, 1.0)
                val usedValue = element ?: newDefault
                MultiElementListEntry(
                    //Name
                    entryTitle,
                    //Default Value
                    usedValue,
                    listOf(
                        entryBuilder.startDoubleField(Component.literal("x"), usedValue.x).setDefaultValue(1.0).build(),
                        entryBuilder.startDoubleField(Component.literal("y"), usedValue.y).setDefaultValue(1.0).build(),
                        entryBuilder.startDoubleField(Component.literal("z"), usedValue.z).setDefaultValue(1.0).build()
                    ),
                    //Expanded By Default
                    true
                )
            }
        )
    }

}
