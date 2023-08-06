package net.frozenblock.configurableeverything.entity.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Shadow
	public abstract AttributeMap getAttributes();

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(EntityType entityType, Level level, CallbackInfo ci) {
		AttributeMap attributes = this.getAttributes();
		var attribute = attributes.getInstance(Attributes.MOVEMENT_SPEED);
		attribute.addPermanentModifier(new AttributeModifier("Configurable Everything speed upgrade", 1.5, AttributeModifier.Operation.MULTIPLY_TOTAL));
	}
}
