package net.frozenblock.configurableeverything.config.gui.api

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.frozenblock.configurableeverything.util.text
import net.minecraft.network.chat.Component
import java.util.function.Consumer

@JvmRecord
data class EntryBuilder<T>(val title: Component, val value: T, val defaultValue: T, val tooltip: Component?, val saveConsumer: Consumer<T>) {

    fun build(entryBuilder: ConfigEntryBuilder): AbstractConfigListEntry<out Any>? {
        if (value == null) return null
        return when (value) {
            is Boolean -> entryBuilder.startBooleanToggle(title, value)
                .setDefaultValue(defaultValue as Boolean)
                .setTooltip(tooltip)
                .setSaveConsumer(saveConsumer as Consumer<Boolean>)
                .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
                .build()
            is String -> {
                val field = entryBuilder.startStrField(title, value)
                    .setDefaultValue(defaultValue as String)
                    .setSaveConsumer(saveConsumer as Consumer<String>)
                tooltip?.let { field.setTooltip(it) }
                field.build()
            }
            else -> throw IllegalArgumentException("Unsupported type: ${value::class.java}")
        }
    }
}
