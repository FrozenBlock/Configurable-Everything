package net.frozenblock.configurableeverything.structure.mixin;

import java.util.List;
import net.frozenblock.configurableeverything.structure.util.StructureConfigUtil;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkGeneratorStructureState.class)
public class ChunkGeneratorStructureStateMixin {

	@Shadow
	@Final
	@Mutable
	private List<Holder<StructureSet>> possibleStructureSets;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(RandomState randomState, BiomeSource biomeSource, long levelSeed, long cocentricRingsSeed, List<Holder<StructureSet>> possibleStructureSets, CallbackInfo ci) {
		this.possibleStructureSets = StructureConfigUtil.modifyStructureSetList(possibleStructureSets);
	}
}
