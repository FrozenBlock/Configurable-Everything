package net.frozenblock.configurableeverything.scripting.util.remap

import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import net.fabricmc.mappingio.MappedElementKind
import net.fabricmc.mappingio.MappingVisitor

data class MappingsHolder(
    val classes: Int2ObjectMap<String> = Int2ObjectMaps.synchronize(Int2ObjectOpenHashMap()),
    val methods: Int2ObjectMap<String> = Int2ObjectMaps.synchronize(Int2ObjectOpenHashMap()),
    val fields: Int2ObjectMap<String> = Int2ObjectMaps.synchronize(Int2ObjectOpenHashMap()),
) : MappingVisitor {

    private var dstNamespaceId: Int = 0

    private var srcClassName: String? = null
    private var srcMethodName: String? = null
    private var srcFieldName: String? = null

    override fun visitNamespaces(srcNamespace: String?, dstNamespaces: MutableList<String>) {
        val namedIndex = dstNamespaces.indexOf("named")
        if (namedIndex != -1) this.dstNamespaceId = namedIndex
    }

    override fun visitClass(srcName: String): Boolean {
        this.srcClassName = srcName
        return true
    }

    override fun visitField(srcName: String, srcDesc: String?): Boolean {
        this.srcFieldName = srcName
        return true
    }

    override fun visitMethod(srcName: String, srcDesc: String?): Boolean {
        this.srcMethodName = srcName
        return true
    }

    override fun visitMethodArg(argPosition: Int, lvIndex: Int, srcName: String?): Boolean {
        return false
    }

    override fun visitMethodVar(
        lvtRowIndex: Int,
        lvIndex: Int,
        startOpIdx: Int,
        srcName: String?
    ): Boolean {
        return false
    }

    override fun visitDstName(targetKind: MappedElementKind?, namespace: Int, name: String?) {
        var name = name
        if (this.dstNamespaceId != namespace) {
            return
        }

        when (targetKind) {
            MappedElementKind.CLASS -> {
                var srcName = this.srcClassName
                if (srcName == name) {
                    return
                }
                if (!srcName!!.startsWith("net/minecraft/class_")) {
                    // I don't know why, but in some versions (tested in 1.14.2) the
                    // names are just switched, without the namespaces being switched
                    if (name!!.startsWith("net/minecraft/class_")) {
                        val ogName = name
                        name = srcName
                        srcName = ogName
                    } else {
                        return
                    }
                }

                var classIdStr: String
                var innerClassSeparatorIndex = srcName.lastIndexOf('$')
                if (innerClassSeparatorIndex != -1) {
                    classIdStr = srcName.substring(innerClassSeparatorIndex + 1)
                    if (!classIdStr.startsWith("class_")) {
                        return  // don't save it if it is a lambda
                    }
                    classIdStr = classIdStr.substring("class_".length)
                } else {
                    classIdStr = srcName.substring("net/minecraft/class_".length)
                }

                innerClassSeparatorIndex = name!!.indexOf('$')
                name = if (innerClassSeparatorIndex != -1) {
                    name.substring(innerClassSeparatorIndex + 1)
                } else {
                    name.replace('/', '.')
                }
                classes[classIdStr.toInt()] = name
            }

            MappedElementKind.METHOD -> {
                var srcName = this.srcMethodName
                if (srcName == name) {
                    return
                }
                if (!srcName!!.startsWith("method_")) {
                    if (name!!.startsWith("method_")) {
                        val ogName = name
                        name = srcName
                        srcName = ogName
                    } else {
                        return
                    }
                }

                val methodId = srcName.substring("method_".length).toInt()
                methods[methodId] = name!!
            }

            MappedElementKind.FIELD -> {
                var srcName = this.srcFieldName
                if (srcName == name) {
                    return
                }
                if (!srcName!!.startsWith("field_")) {
                    if (name!!.startsWith("field_")) {
                        val ogName = name
                        name = srcName
                        srcName = ogName
                    } else {
                        return
                    }
                }

                val fieldId = srcName.substring("field_".length).toInt()
                fields[fieldId] = name!!
            }
            else -> {}
        }
    }

    override fun visitComment(targetKind: MappedElementKind?, comment: String?) {
    }
}
