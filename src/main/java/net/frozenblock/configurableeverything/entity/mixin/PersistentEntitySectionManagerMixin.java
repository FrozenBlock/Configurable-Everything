package net.frozenblock.configurableeverything.entity.mixin;

import net.frozenblock.configurableeverything.entity.util.EntityConfigUtil;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PersistentEntitySectionManager.class)
public class PersistentEntitySectionManagerMixin<T extends EntityAccess> {

	@Inject(method = "addEntity", at = @At(value = "RETURN", ordinal = 1))
	private void addEntity(T entityAccess, boolean worldGenSpawned, CallbackInfoReturnable<Boolean> cir) {
		EntityConfigUtil.addAttributeAmplifiers$ConfigurableEverything(entityAccess);
	}
}
