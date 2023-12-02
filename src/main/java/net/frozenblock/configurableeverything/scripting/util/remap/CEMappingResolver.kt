package net.frozenblock.configurableeverything.scripting.util.remap

import net.fabricmc.loader.api.MappingResolver
import net.fabricmc.mappingio.tree.MappingTree
import java.util.Collections

class CEMappingResolver(
    private val mappings: MappingTree,
    private val targetNamespace: String
) : MappingResolver {

    private val targetNamespaceId = this.mappings.getNamespaceId(this.targetNamespace)

    override fun getNamespaces(): Collection<String?> {
        val set = mutableSetOf(this.mappings.srcNamespace)
        set.addAll(this.mappings.dstNamespaces)
        return Collections.unmodifiableSet(set)
    }

    override fun getCurrentRuntimeNamespace(): String {
        return this.targetNamespace
    }

    override fun mapClassName(namespace: String?, className: String?): String {
        checkNotNull(namespace) { "Namespace cannot be null" }
        checkNotNull(className) { "Class name cannot be null" }
        require(!className.contains('/')) { "Class names must be provided in dot format: $className" }

        return this.mappings.mapClassName(className, this.targetNamespaceId)
    }

    override fun unmapClassName(targetNamespace: String?, className: String?): String {
        checkNotNull(targetNamespace) { "Namespace cannot be null" }
        checkNotNull(className) { "Class name cannot be null" }
        require(!className.contains('/')) { "Class names must be provided in dot format: $className" }

        return this.mappings.mapClassName(className.inSlashFormat, this.targetNamespaceId, this.mappings.getNamespaceId(targetNamespace)).inDotFormat
    }

    override fun mapFieldName(namespace: String?, owner: String?, name: String?, descriptor: String?): String {
        checkNotNull(namespace) { "Namespace cannot be null" }
        checkNotNull(owner) { "Owner cannot be null" }
        checkNotNull(name) { "Name cannot be null" }
        checkNotNull(descriptor) { "Descriptor cannot be null" }
        require(!owner.contains('/')) { "Class names must be provided in dot format: $owner" }

        val field = this.mappings.getField(owner.inSlashFormat, name, descriptor, this.mappings.getNamespaceId(namespace))
        return field?.getName(targetNamespaceId) ?: name
    }

    override fun mapMethodName(namespace: String?, owner: String?, name: String?, descriptor: String?): String {
        checkNotNull(namespace) { "Namespace cannot be null" }
        checkNotNull(owner) { "Owner cannot be null" }
        checkNotNull(name) { "Name cannot be null" }
        checkNotNull(descriptor) { "Descriptor cannot be null" }
        require(!owner.contains('/')) { "Class names must be provided in dot format: $owner" }

        val method = this.mappings.getMethod(owner.inSlashFormat, name, descriptor, this.mappings.getNamespaceId(namespace))
        return method?.getName(targetNamespaceId) ?: name
    }
}

private val String.inDotFormat
    get() = this.replace('/', '.')

private val String.inSlashFormat
    get() = this.replace('.', '/')
