package net.frozenblock.configurableeverything.datafixer.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstantsKt;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtilsKt;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public record RegistryFixer(ResourceLocation registryKey, List<Fixer> fixers) {

	public static final Codec<RegistryFixer> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					ResourceLocation.CODEC.fieldOf("registry_key").forGetter(RegistryFixer::registryKey),
					Fixer.CODEC.listOf().fieldOf("fixers").forGetter(RegistryFixer::fixers)
			).apply(instance, RegistryFixer::new)
	);

	public static ResourceLocation getFixedValueInRegistry(Registry<?> registry, @Nullable ResourceLocation name) {
		if (name != null) {
			var registryFixers = DataFixerUtils.getRegistryFixers();
			for (RegistryFixer registryFixer : registryFixers) {
				if (registryFixer.registryKey().equals(registry.key().location())) {
					for (Fixer fixer : registryFixer.fixers()) {
						if (fixer.oldId().equals(name)) {
							ConfigurableEverythingUtilsKt.log("Successfully changed old ID " + name + " to new ID " + fixer.newId(), ConfigurableEverythingSharedConstantsKt.UNSTABLE_LOGGING);
							return fixer.newId();
						}
					}
				}
			}
		}
		return null;
	}
}
