package net.frozenblock.configurableeverything.structure.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.frozenblock.configurableeverything.structure.util.StructureConfigUtil;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import java.util.List;

@Mixin(StructureSet.class)
public class StructureSetMixin {

	@ModifyReturnValue(method = "structures", at = @At("RETURN"))
	private List<StructureSet.StructureSelectionEntry> structures(List<StructureSet.StructureSelectionEntry> original) {
		return StructureConfigUtil.modifyStructureList(original);
	}
}
