package net.frozenblock.configurableeverything.tag.util

data class TagModification(
    @JvmField var tag: String,
    @JvmField var additions: List<String>,
    @JvmField var removals: List<String>
) {
    companion object {
        @JvmField
        val CODEC: Codec<TagModification> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.STRING.fieldOf("tag").forGetter(TagModification::tag),
                Codec.STRING.listOf().fieldOf("additions").forGetter(TagModification::additions),
                Codec.STRING.listOf().fieldOf("removals").forGetter(TagModification::removals)
            ).apply(instance, ::TagModification)
        }
    }
}