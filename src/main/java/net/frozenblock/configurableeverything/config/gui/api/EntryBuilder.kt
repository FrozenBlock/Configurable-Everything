package net.frozenblock.configurableeverything.config.gui.api

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.frozenblock.configurableeverything.util.text
import net.minecraft.network.chat.Component
import java.util.function.Consumer

@Environment(EnvType.CLIENT)
data class EntryBuilder<T>(
    @JvmField val title: Component,
    @JvmField val value: T?,
    @JvmField val defaultValue: T,
    @JvmField val saveConsumer: Consumer<T>,
    @JvmField val tooltip: Component? = null,
    @JvmField val requiresRestart: Boolean? = false
) {

    fun build(entryBuilder: ConfigEntryBuilder): AbstractConfigListEntry<out Any> {
        val usedValue: T = value ?: defaultValue
        return when (usedValue) {
            is Boolean -> {
                entryBuilder.startBooleanToggle(title, usedValue)
                    .setDefaultValue(defaultValue as Boolean)
                    .setSaveConsumer(saveConsumer as Consumer<Boolean>)
                    .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
                    .let {
                        tooltip?.let { tooltip -> it.setTooltip(tooltip) }
                        requiresRestart?.let { requiresRestart -> it.requireRestart(requiresRestart) }
                        it
                    }.build()
            }
            is Int -> {
                entryBuilder.startIntField(title, usedValue)
                    .setDefaultValue(defaultValue as Int)
                    .setSaveConsumer(saveConsumer as Consumer<Int>)
                    .let {
                        tooltip?.let { tooltip -> it.setTooltip(tooltip) }
                        requiresRestart?.let { requiresRestart -> it.requireRestart(requiresRestart) }
                        it
                    }.build()
            }
            is Long -> {
                entryBuilder.startLongField(title, usedValue)
                    .setDefaultValue(defaultValue as Long)
                    .setSaveConsumer(saveConsumer as Consumer<Long>)
                    .let {
                        tooltip?.let { tooltip -> it.setTooltip(tooltip) }
                        requiresRestart?.let { requiresRestart -> it.requireRestart(requiresRestart) }
                        it
                    }.build()
            }
            is Float -> {
                entryBuilder.startFloatField(title, usedValue)
                    .setDefaultValue(defaultValue as Float)
                    .setSaveConsumer(saveConsumer as Consumer<Float>)
                    .let {
                        tooltip?.let { tooltip -> it.setTooltip(tooltip) }
                        requiresRestart?.let { requiresRestart -> it.requireRestart(requiresRestart) }
                        it
                    }.build()
            }
            is Double -> {
                entryBuilder.startDoubleField(title, usedValue)
                    .setDefaultValue(defaultValue as Double)
                    .setSaveConsumer(saveConsumer as Consumer<Double>)
                    .let {
                        tooltip?.let { tooltip -> it.setTooltip(tooltip) }
                        requiresRestart?.let { requiresRestart -> it.requireRestart(requiresRestart) }
                        it
                    }.build()
            }
            is String -> {
                entryBuilder.startStrField(title, usedValue)
                    .setDefaultValue(defaultValue as String)
                    .setSaveConsumer(saveConsumer as Consumer<String>)
                    .let {
                        tooltip?.let { tooltip -> it.setTooltip(tooltip) }
                        requiresRestart?.let { requiresRestart -> it.requireRestart(requiresRestart) }
                        it
                    }.build()
            }
            is Color -> {
                entryBuilder.startColorField(title, usedValue.color)
                    .setDefaultValue((defaultValue as Color).color)
                    .setSaveConsumer(saveConsumer as Consumer<Int>)
                    .let {
                        tooltip?.let { tooltip -> it.setTooltip(tooltip) }
                        requiresRestart?.let { requiresRestart -> it.requireRestart(requiresRestart) }
                        it
                    }.build()
            }
            else -> throw IllegalArgumentException("Unsupported type: ${usedValue!!::class.java}")
        }
    }
}
