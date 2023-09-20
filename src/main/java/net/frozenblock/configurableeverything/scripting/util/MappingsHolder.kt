package net.frozenblock.configurableeverything.scripting.util

import java.regex.Matcher
import java.regex.Pattern

data class MappingsHolder(val classes: MutableMap<Int, String?>, val methods: MutableMap<Int, String?>, val fields: MutableMap<Int, String?>) {
    companion object {
        val CLASS: Pattern = Pattern.compile("(net\\.minecraft\\.|net/minecraft/)?class_(\\d+)")
        val METHOD: Pattern = Pattern.compile("method_(\\d+)")
        val FIELD: Pattern = Pattern.compile("field_(\\d+)")
    }

    fun remapClass(id: Int): String?
        = this.classes[id]

    fun remapMethod(id: Int): String?
        = this.methods[id]

    fun remapField(id: Int): String?
        = this.fields[id]

    fun remapClass(string: String): String {
        return CLASS.matcher(string).replaceAll { result ->
            val id: Int = Int.parseInt(result.group(2))
            var name: String? = this.remapClass(id)
            if (name == null) return@replaceAll Matcher.quoteReplacement(result.group())

            val `package`: String = result.group(1)
            if (`package` != null) {
                if (`package`.indexOf('.') == -1)
                    name = name.replace('.', '/')
            } else {
                val index = name.lastIndexOf('.')
                if (index != -1)
                    name = name.substring(index + 1)
            }
            return@replaceAll Matcher.quoteReplacement(name)
        }
    }

    fun remapMethod(string: String): String {
        return METHOD.matcher(string).replaceAll { result ->
            val id: Int = Int.parseInt(result.group(1))
            val name: String? = this.remapMethod[id]
            return@replaceAll Matcher.quoteReplacement(name ?: result.group())
        }
    }

    fun remapField(string: String): String {
        return FIELD.matcher(string).replaceAll { result ->
            val id: Int.parseInt(result.group(1))
            val name: String? = this.remapField(id)
            return@replaceAll Matcher.quoteReplacement(name ?: result.group())
        }
    }

    fun remapString(string: String): String {
        var newString: String = string
        if (newString.contains("class_")) remapClass(newString)
        if (newString.contains("method_")) remapMethod(newString)
        if (newString.contains("field_")) remapField(newString)
        return newString
    }
}