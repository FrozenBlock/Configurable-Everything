package net.frozenblock.configurableeverything.mixin;

import com.mojang.datafixers.util.Pair;
import net.frozenblock.configurableeverything.config.BiomeConfig;
import net.frozenblock.configurableeverything.util.BiomeParameters;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList;
import java.util.List;

@Mixin(MultiNoiseBiomeSourceParameterList.class)
public class MultiNoiseBiomeSourceParameterListMixin {

	@Mutable
	@Shadow
	@Final
	private Climate.ParameterList<Holder<Biome>> parameters;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(MultiNoiseBiomeSourceParameterList.Preset preset, HolderGetter<Biome> biomeProvider, CallbackInfo ci) {
		var biomeConfig = BiomeConfig.get();
		var addedBiomes = biomeConfig.addedBiomes;
		var removedBiomes = biomeConfig.removedBiomes;
		List<Pair<Climate.ParameterPoint, Holder<Biome>>> newParameters = new ArrayList<>(this.parameters.values());

		if (removedBiomes != null && removedBiomes.value() != null) {
			newParameters.removeAll(
				this.parameters.values().stream().filter(pair ->
					removedBiomes.value().contains(pair.getSecond().unwrapKey().orElseThrow())
				).toList()
			);
		}

		if (addedBiomes != null && addedBiomes.value() != null) {
			for (BiomeParameters parameters : addedBiomes.value()) {
				var optionalHolder = biomeProvider.get(parameters.biome());
				if (optionalHolder.isPresent()) {
					newParameters.add(Pair.of(parameters.parameters(), optionalHolder.get()));
				} else {
					ConfigurableEverythingSharedConstants.LOGGER.error("Invalid biome id in biome config: " + parameters.biome().location());
				}
			}
		}

		this.parameters = new Climate.ParameterList<>(newParameters);
	}
}
