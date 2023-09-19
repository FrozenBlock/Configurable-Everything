package net.frozenblock.configurableeverything.entity.mixin;

import net.frozenblock.configurableeverything.config.EntityConfig;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.entity.util.AttributeAmplifier;
import net.frozenblock.configurableeverything.entity.util.EntityAttributeAmplifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;

@Mixin(PersistentEntitySectionManager.class)
public class PersistentEntitySectionManagerMixin<T extends EntityAccess> {

	@Inject(method = "addEntity", at = @At(value = "RETURN", ordinal = 1))
	private void addEntity(T entityAccess, boolean worldGenSpawned, CallbackInfoReturnable<Boolean> cir) {
		var config = EntityConfig.get();
		if (entityAccess instanceof LivingEntity entity
			&& (MainConfig.get().entity == true && (config.entityAttributeAmplifiers != null && config.entityAttributeAmplifiers.value() != null))) {
				List<EntityAttributeAmplifier> entityAttributeMultipliers = config.entityAttributeAmplifiers.value();
				for (EntityAttributeAmplifier entityAttributeAmplifier : entityAttributeMultipliers) {
					if (entityAttributeAmplifier.entity.location().equals(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()))) {
						Component entityName = Component.literal(entityAttributeAmplifier.entityName);
						if (entityName.getString().isEmpty() || entityName.equals(entity.getName())) {
							AttributeMap attributes = entity.getAttributes();
							for (AttributeAmplifier amplifier : entityAttributeAmplifier.amplifiers) {
								AttributeInstance attribute = attributes.getInstance(BuiltInRegistries.ATTRIBUTE.get(amplifier.attribute));
								attribute.addTransientModifier(
									new AttributeModifier(
										"Configurable Everything Entity Config " + amplifier.attribute.location() + " change to " + entity.getName(),
										amplifier.amplifier - 1.0,
										AttributeModifier.Operation.MULTIPLY_TOTAL
									)
								);
							}
						}
					}
				}

		}
	}
}
