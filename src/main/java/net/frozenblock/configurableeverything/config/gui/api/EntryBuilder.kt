package net.frozenblock.configurableeverything.config.gui.api

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.util.text
import net.minecraft.network.chat.Component
import java.util.function.Consumer

@Environment(EnvType.CLIENT)
data class EntryBuilder<T>(val title: Component, val value: T?, val defaultValue: T, val saveConsumer: Consumer<T> val tooltip: Component? = null) {

    fun build(entryBuilder: ConfigEntryBuilder): AbstractConfigListEntry<out Any>? {
        val usedValue: T = value ?: defaultValue
        return when (usedValue) {
            is Boolean -> {
                val field = entryBuilder.startBooleanToggle(title, usedValue)
                    .setDefaultValue(defaultValue as Boolean)
                    .setSaveConsumer(saveConsumer as Consumer<Boolean>)
                    .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
                tooltip?.let { field.setTooltip(it) }
                field.build()
            }
            is Int -> {
                val field = entryBuilder.startIntField(title, usedValue)
                    .setDefaultValue(defaultValue as Int)
                    .setSaveConsumer(saveConsumer as Consumer<Int>)
                tooltip?.let { field.setTooltip(it) }
                field.build()
            }
            is Float -> {
                val field = entryBuilder.startFloatField(title, usedValue)
                    .setDefaultValue(defaultValue as Float)
                    .setSaveConsumer(saveConsumer as Consumer<Float>)
                tooltip?.let { field.setTooltip(it) }
                field.build()
            }
            is Double -> {
                val field = entryBuilder.startDoubleField(title, usedValue)
                    .setDefaultValue(defaultValue as Double)
                    .setSaveConsumer(saveConsumer as Consumer<Double>)
                tooltip?.let { field.setTooltip(it) }
                field.build()
            }
            is String -> {
                val field = entryBuilder.startStrField(title, usedValue)
                    .setDefaultValue(defaultValue as String)
                    .setSaveConsumer(saveConsumer as Consumer<String>)
                tooltip?.let { field.setTooltip(it) }
                field.build()
            }
            else -> throw IllegalArgumentException("Unsupported type: ${usedValue::class.java}")
        }
    }
}
