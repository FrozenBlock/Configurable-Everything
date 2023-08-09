package net.frozenblock.configurableeverything.entity.mixin;

import net.frozenblock.configurableeverything.config.EntityConfig;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.entity.util.AttributeAmplifier;
import net.frozenblock.configurableeverything.entity.util.EntityAttributeAmplifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	private LivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
	public abstract AttributeMap getAttributes();

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(EntityType entityType, Level level, CallbackInfo ci) {
		var config = EntityConfig.get();
		if (MainConfig.get().entity && (config.entityAttributeAmplifiers != null && config.entityAttributeAmplifiers.value() != null)) {
			List<EntityAttributeAmplifier> entityAttributeMultipliers = config.entityAttributeAmplifiers.value();
			for (EntityAttributeAmplifier entityAttributeAmplifier : entityAttributeMultipliers) {
				if (entityAttributeAmplifier.entity().location().equals(BuiltInRegistries.ENTITY_TYPE.getKey(this.getType()))) {
					AttributeMap attributes = this.getAttributes();
					for (AttributeAmplifier amplifier : entityAttributeAmplifier.amplifiers()) {
						AttributeInstance attribute = attributes.getInstance(BuiltInRegistries.ATTRIBUTE.get(amplifier.attribute()));
						attribute.addTransientModifier(
							new AttributeModifier(
								"Configurable Everything Entity Config " + amplifier.attribute().location() + " change",
								amplifier.amplifier() - 1.0,
								AttributeModifier.Operation.MULTIPLY_TOTAL
							)
						);
					}
				}
			}
		}
	}

	@ModifyArg(method = "dropExperience", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ExperienceOrb;award(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"), index = 2)
	private int dropExperience(int amount) {
		var config = EntityConfig.get();
		if (MainConfig.get().entity && config.experienceOverrides != null && config.experienceOverrides.value() != null) {
			var experienceOverrides = config.experienceOverrides.value();
			for (var override : experienceOverrides) {
				if (override.entity().location().equals(BuiltInRegistries.ENTITY_TYPE.getKey(this.getType()))) {
					return override.amount();
				}
			}
		}
		return amount;
	}
}
