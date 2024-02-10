package net.frozenblock.configurableeverything.loot.mixin;

import net.frozenblock.configurableeverything.loot.util.ConfigurableLootItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.function.Consumer;

@Mixin(LootItem.class)
public class LootItemMixin implements ConfigurableLootItem {

	@Unique
	private boolean disabled = false;


	@Inject(method = "createItemStack", at = @At("HEAD"), cancellable = true)
	private void removeItem(Consumer<ItemStack> stackConsumer, LootContext lootContext, CallbackInfo ci) {
		if (disabled) ci.cancel();
	}

	@Override
	public void configurableEverything$disable() {
		this.disabled = true;
	}
}
