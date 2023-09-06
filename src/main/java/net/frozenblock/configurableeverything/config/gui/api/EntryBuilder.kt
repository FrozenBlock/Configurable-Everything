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
                val consumer = saveConsumer as? Consumer<Boolean> ?: throw IllegalArgumentException("Invalid consumer")
                entryBuilder.startBooleanToggle(title, usedValue)
                    .setDefaultValue(defaultValue as Boolean)
                    .setSaveConsumer(consumer)
                    .setYesNoTextSupplier { bool: Boolean -> text(bool.toString()) }
                    .let {
                        tooltip?.let { tooltip -> it.setTooltip(tooltip) }
                        requiresRestart?.let { requiresRestart -> it.requireRestart(requiresRestart) }
                        it
                    }.build()
            }
            is Int -> {
                val consumer = saveConsumer as? Consumer<Int> ?: throw IllegalArgumentException("Invalid consumer")
                entryBuilder.startIntField(title, usedValue)
                    .setDefaultValue(defaultValue as Int)
                    .setSaveConsumer(consumer)
                    .let {
                        tooltip?.let { tooltip -> it.setTooltip(tooltip) }
                        requiresRestart?.let { requiresRestart -> it.requireRestart(requiresRestart) }
                        it
                    }.build()
            }
            is Long -> {
                val consumer = saveConsumer as? Consumer<Long> ?: throw IllegalArgumentException("Invalid consumer")
                entryBuilder.startLongField(title, usedValue)
                    .setDefaultValue(defaultValue as Long)
                    .setSaveConsumer(consumer)
                    .let {
                        tooltip?.let { tooltip -> it.setTooltip(tooltip) }
                        requiresRestart?.let { requiresRestart -> it.requireRestart(requiresRestart) }
                        it
                    }.build()
            }
            is Float -> {
                val consumer = saveConsumer as? Consumer<Float> ?: throw IllegalArgumentException("Invalid consumer")
                entryBuilder.startFloatField(title, usedValue)
                    .setDefaultValue(defaultValue as Float)
                    .setSaveConsumer(consumer)
                    .let {
                        tooltip?.let { tooltip -> it.setTooltip(tooltip) }
                        requiresRestart?.let { requiresRestart -> it.requireRestart(requiresRestart) }
                        it
                    }.build()
            }
            is Double -> {
                val consumer = saveConsumer as? Consumer<Double> ?: throw IllegalArgumentException("Invalid consumer")
                entryBuilder.startDoubleField(title, usedValue)
                    .setDefaultValue(defaultValue as Double)
                    .setSaveConsumer(consumer)
                    .let {
                        tooltip?.let { tooltip -> it.setTooltip(tooltip) }
                        requiresRestart?.let { requiresRestart -> it.requireRestart(requiresRestart) }
                        it
                    }.build()
            }
            is String -> {
                val consumer = saveConsumer as? Consumer<String> ?: throw IllegalArgumentException("Invalid consumer")
                entryBuilder.startStrField(title, usedValue)
                    .setDefaultValue(defaultValue as String)
                    .setSaveConsumer(consumer)
                    .let {
                        tooltip?.let { tooltip -> it.setTooltip(tooltip) }
                        requiresRestart?.let { requiresRestart -> it.requireRestart(requiresRestart) }
                        it
                    }.build()
            }
            is Color -> {
                val consumer = saveConsumer as? Consumer<Int> ?: throw IllegalArgumentException("Invalid consumer")
                entryBuilder.startColorField(title, usedValue.color)
                    .setDefaultValue((defaultValue as Color).color)
                    .setSaveConsumer(consumer)
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
