package net.frozenblock.configurableeverything.entity.mixin;

import net.frozenblock.configurableeverything.config.EntityConfig;
import net.frozenblock.configurableeverything.config.MainConfig;
import net.frozenblock.configurableeverything.entity.util.EntitySpottingIcon;
import net.frozenblock.lib.spotting_icons.api.SpottingIconPredicate;
import net.frozenblock.lib.spotting_icons.impl.EntitySpottingIconInterface;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {


	@Inject(method = "<init>", at = @At(value = "TAIL"))
	public void init(EntityType<?> entityType, Level level, CallbackInfo ci) {
		var config = EntityConfig.get();
		if (MainConfig.get().entity) {
			if (config.entitySpottingIcons != null && config.entitySpottingIcons.value() != null) {
				var entitySpottingIcons = config.entitySpottingIcons.value();
				for (EntitySpottingIcon spottingIcon : entitySpottingIcons) {
					if (spottingIcon.entity().location().equals(BuiltInRegistries.ENTITY_TYPE.getKey(entityType))) {
						((EntitySpottingIconInterface) Entity.class.cast(this)).getSpottingIconManager()
							.setIcon(spottingIcon.texture(), spottingIcon.startFade(), spottingIcon.endFade(), SpottingIconPredicate.DEFAULT_ID);
					}
				}
			}
		}
	}
}
