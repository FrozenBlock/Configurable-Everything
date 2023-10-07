package net.frozenblock.configurableeverything.entity.mixin;

import java.util.List;
import net.frozenblock.configurableeverything.config.EntityConfig;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.entity.util.EntityHurtEffects;
import net.frozenblock.configurableeverything.entity.util.MobEffectHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingEntity.class, priority = 1005)
public abstract class LivingEntityMixin extends Entity {

	private LivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@ModifyArg(method = "dropExperience", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ExperienceOrb;award(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"), index = 2)
	private int dropExperience(int amount) {
		var config = EntityConfig.get();
		if (MainConfig.get().entity == true && config.experienceOverrides != null && config.experienceOverrides.value() != null) {
			var experienceOverrides = config.experienceOverrides.value();
			for (var override : experienceOverrides) {
				if (override.entity.location().equals(BuiltInRegistries.ENTITY_TYPE.getKey(this.getType()))) {
					return override.amount;
				}
			}
		}
		return amount;
	}

	@Inject(method = "setLastHurtMob", at = @At("TAIL"))
	private void setLastHurtMob(Entity entity, CallbackInfo ci) {
		var config = EntityConfig.get();
		if (entity instanceof LivingEntity livingEntity) {
			if (MainConfig.get().entity == true) {
				if (config.entityHurtEffects != null && config.entityHurtEffects.value() != null) {
					var entityHurtEffects = config.entityHurtEffects.value();
					for (EntityHurtEffects hurtEffects : entityHurtEffects) {
						if (hurtEffects.entity.equals(BuiltInRegistries.ENTITY_TYPE.getKey(this.getType()))) {
							var name = hurtEffects.entityName;
							if (name.isEmpty() || livingEntity.getName().getString().equals(name)) {
								List<MobEffectHolder> effects = hurtEffects.effects;
								for (MobEffectHolder effect : effects) {
									var mobEffect = BuiltInRegistries.MOB_EFFECT.getOrThrow(effect.effect);
									var duration = effect.duration;
									livingEntity.addEffect(new MobEffectInstance(mobEffect, duration == -1 ? duration : duration * 20, effect.amplifier, effect.ambient, effect.visible, effect.showIcon), this);
								}
							}
						}
					}
				}
			}
		}
	}
}
