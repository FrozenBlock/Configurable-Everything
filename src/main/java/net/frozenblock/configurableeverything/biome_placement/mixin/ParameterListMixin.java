package net.frozenblock.configurableeverything.biome_placement.mixin;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import net.frozenblock.configurableeverything.biome_placement.util.BiomePlacementUtils;
import net.frozenblock.configurableeverything.biome_placement.util.ParameterListExtension;
import net.frozenblock.configurableeverything.datagen.ConfigurableEverythingDataGenerator;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = Climate.ParameterList.class, priority = 993)
public class ParameterListMixin<T> implements ParameterListExtension {
	@Mutable
	@Shadow
	@Final
	private List<Pair<Climate.ParameterPoint, T>> values;

	@Mutable
	@Shadow
	@Final
	private Climate.RTree<T> index;

	@Override
	public void updateBiomesList(RegistryAccess registryAccess, ResourceKey<DimensionType> dimension) {
		var addedBiomes = BiomePlacementUtils.biomeAdditions(registryAccess.lookupOrThrow(Registries.BIOME), dimension);
		var removedBiomes = BiomePlacementUtils.biomeRemovals(dimension, registryAccess);

		try {
			var biomeValues = (List<Pair<Climate.ParameterPoint, Holder<Biome>>>) (List) this.values;
			List<Pair<Climate.ParameterPoint, Holder<Biome>>> newParameters = new ArrayList<>(biomeValues);

			// remove biomes first to allow replacing biome parameters
			newParameters.removeAll(
				newParameters.stream().filter(pair ->
					removedBiomes.contains(pair.getSecond().unwrapKey().orElseThrow())
				).toList()
			);

			newParameters.addAll(addedBiomes);

			if (newParameters.isEmpty()) {
				newParameters.add(
					Pair.of(
						Climate.parameters(0F, 0F, 0F, 0F, 0F, 0F, 0F),
						registryAccess.registryOrThrow(Registries.BIOME).getHolderOrThrow(ConfigurableEverythingDataGenerator.BLANK_BIOME)
					)
				);
			}

			this.values = List.copyOf((List<Pair<Climate.ParameterPoint, T>>) (List) newParameters);
			this.index = Climate.RTree.create(this.values);
		} catch (ClassCastException ignored) {
		}
	}
}
