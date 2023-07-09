package net.frozenblock.configurableeverything.mixin;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.frozenblock.configurableeverything.util.BiomeSourceExtension;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
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
	public void updateBiomesList(List<Holder<Biome>> biomesToAdd, List<Holder<Biome>> biomesToRemove) {
		List<Holder<Biome>> biomeList = new ArrayList<>(this.possibleBiomes.get());

		biomeList.addAll(biomesToAdd);
		biomeList.removeAll(biomesToRemove);

		this.possibleBiomes = () -> new ObjectLinkedOpenHashSet<>(biomeList);
	}
}
