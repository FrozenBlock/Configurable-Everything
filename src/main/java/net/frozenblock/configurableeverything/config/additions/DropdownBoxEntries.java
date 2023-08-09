package net.frozenblock.configurableeverything.config.additions;

import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import java.util.function.Function;

public class DropdownBoxEntries {

	public static final ItemStack BARRIER = new ItemStack(Items.BARRIER);

	@Nullable
	public static final Function<String, SoundEvent> SOUND_FUNCTION = (str) -> {
		try {
			return BuiltInRegistries.SOUND_EVENT.getOptional(new ResourceLocation(str)).orElse(null);
		} catch (Exception var2) {
			return null;
		}
	};

	public static DropdownBoxEntry.SelectionTopCellElement<SoundEvent> ofSoundEventObject(SoundEvent soundEvent) {
		return new DropdownBoxEntry.DefaultSelectionTopCellElement<>(soundEvent, SOUND_FUNCTION, (i) -> {
			return Component.literal(BuiltInRegistries.SOUND_EVENT.getKey(i).toString());
		}) {
			public void render(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int width, int height, float delta) {
				this.textFieldWidget.setX(x + 4);
				this.textFieldWidget.setY(y + 6);
				this.textFieldWidget.setWidth(width - 4 - 20);
				this.textFieldWidget.setEditable(this.getParent().isEditable());
				this.textFieldWidget.setTextColor(this.getPreferredTextColor());
				this.textFieldWidget.render(graphics, mouseX, mouseY, delta);
				if (this.hasConfigError()) {
					graphics.renderItem(BARRIER, x + width - 18, y + 2);
				}
			}
		};
	}



	public static DropdownBoxEntry.SelectionCellCreator<SoundEvent> ofSoundEvent() {
		return ofSoundEvent(20, 146, 7);
	}

	public static DropdownBoxEntry.SelectionCellCreator<SoundEvent> ofSoundEvent(int maxItems) {
		return ofSoundEvent(20, 146, maxItems);
	}

	public static DropdownBoxEntry.SelectionCellCreator<SoundEvent> ofSoundEvent(final int cellHeight, final int cellWidth, final int maxItems) {
		return new DropdownBoxEntry.DefaultSelectionCellCreator<>() {
			public DropdownBoxEntry.SelectionCellElement<SoundEvent> create(SoundEvent selection) {
				return new DropdownBoxEntry.DefaultSelectionCellElement<>(selection, this.toTextFunction) {
					public void render(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int width, int height, float delta) {
						this.rendering = true;
						this.x = x;
						this.y = y;
						this.width = width;
						this.height = height;
						boolean b = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
						if (b) {
							graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, -15132391);
						}
						graphics.drawString(Minecraft.getInstance().font, this.toTextFunction.apply(this.r).getVisualOrderText(), x + 6 + 18, y + 6, b ? 16777215 : 8947848);
					}
				};
			}

			public int getCellHeight() {
				return cellHeight;
			}

			public int getCellWidth() {
				return cellWidth;
			}

			public int getDropBoxMaxHeight() {
				return this.getCellHeight() * maxItems;
			}
		};
	}

}
