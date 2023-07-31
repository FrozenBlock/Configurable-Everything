package net.frozenblock.configurableeverything.biome_placement.mixin;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.frozenblock.configurableeverything.biome_placement.util.BiomeSourceExtension;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Mixin(value = BiomeSource.class, priority = 993)
public class BiomeSourceMixin implements BiomeSourceExtension {
	@Mutable
	@Shadow
	@Final
	private Supplier<Set<Holder<Biome>>> possibleBiomes;

	@Override
	public void updateBiomesList(@NotNull List<? extends Holder<Biome>> biomesToAdd, @NotNull List<? extends Holder<Biome>> biomesToRemove) {
		List<Holder<Biome>> biomeList = new ArrayList<>(this.possibleBiomes.get());

		// remove biomes first to allow replacing biome parameters
		biomeList.removeAll(biomesToRemove);
		biomeList.addAll(biomesToAdd);

		this.possibleBiomes = () -> new ObjectLinkedOpenHashSet<>(biomeList);
	}
}
