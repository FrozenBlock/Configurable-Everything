package net.frozenblock.configurableeverything.loot.mixin;

import com.google.common.collect.ImmutableList;
import net.frozenblock.configurableeverything.loot.util.CETableBuilder;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LootTable.Builder.class)
public class LootTableBuilderMixin implements CETableBuilder {

	@Shadow
	@Final
	private ImmutableList.Builder<LootPool> pools;

	@Unique
	@NotNull
	@Override
	public LootTable.Builder configurable_Everything$withPool(@NotNull LootPool pool) {
		this.pools.add(pool);
		return (LootTable.Builder) (Object) this;
	}
}
