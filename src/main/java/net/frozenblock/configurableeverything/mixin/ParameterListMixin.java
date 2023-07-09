package net.frozenblock.configurableeverything.mixin;

import com.mojang.datafixers.util.Pair;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.frozenblock.configurableeverything.util.ParameterListExtension;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
		var addedBiomes = ConfigurableEverythingUtils.biomeAdditions(registryAccess.lookupOrThrow(Registries.BIOME), dimension);
		var removedBiomes = ConfigurableEverythingUtils.biomeRemovals(dimension);

		try {
			var biomeValues = (List<Pair<Climate.ParameterPoint, Holder<Biome>>>) (List) this.values;
			List<Pair<Climate.ParameterPoint, Holder<Biome>>> newParameters = new ArrayList<>(biomeValues);

			newParameters.addAll(addedBiomes);
			newParameters.removeAll(
				newParameters.stream().filter(pair ->
					removedBiomes.contains(pair.getSecond().unwrapKey().orElseThrow())
				).toList()
			);

			this.values = List.copyOf((List<Pair<Climate.ParameterPoint, T>>) (List) newParameters);
			this.index = Climate.RTree.create(this.values);
		} catch (ClassCastException ignored) {
		}
	}
}
