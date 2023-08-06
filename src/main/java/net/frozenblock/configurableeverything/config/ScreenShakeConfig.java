package net.frozenblock.configurableeverything.config;

import net.frozenblock.configurableeverything.screenshake.util.SoundScreenShake;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingSharedConstants;
import net.frozenblock.configurableeverything.util.ConfigurableEverythingUtils;
import net.frozenblock.lib.config.api.entry.TypedEntry;
import net.frozenblock.lib.config.api.entry.TypedEntryType;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.json.JsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.monster.warden.WardenAi;

import java.util.List;

public final class ScreenShakeConfig {

	private static final TypedEntryType<List<SoundScreenShake>> SOUND_SCREEN_SHAKE = ConfigRegistry.register(
		new TypedEntryType<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			SoundScreenShake.CODEC.listOf()
		)
	);

	private static final Config<ScreenShakeConfig> INSTANCE = ConfigRegistry.register(
		new JsonConfig<>(
			ConfigurableEverythingSharedConstants.MOD_ID,
			ScreenShakeConfig.class,
			ConfigurableEverythingUtils.makePath("screen_shake", true),
			true
		)
	);

	public TypedEntry<List<SoundScreenShake>> soundScreenShakes = new TypedEntry<>(
		SOUND_SCREEN_SHAKE,
		List.of(
				new SoundScreenShake(
						SoundEvents.ENDER_DRAGON_GROWL.getLocation(),
						2.5F,
						140,
						90,
						48
				),
				new SoundScreenShake(
						SoundEvents.ENDER_DRAGON_FLAP.getLocation(),
						0.5F,
						8,
						1,
						26
				),
				new SoundScreenShake(
					SoundEvents.RAVAGER_ROAR.getLocation(),
					1F,
					17,
					1,
					23
				),
				new SoundScreenShake(
						SoundEvents.WARDEN_EMERGE.getLocation(),
						0.75F,
						WardenAi.EMERGE_DURATION - 30,
						1,
						20
				),
				new SoundScreenShake(
						SoundEvents.WARDEN_ROAR.getLocation(),
						1.5F,
						WardenAi.ROAR_DURATION - 25,
						1,
						19
				),
				new SoundScreenShake(
						SoundEvents.WARDEN_ATTACK_IMPACT.getLocation(),
						1F,
						3,
						1,
						2
				),
				new SoundScreenShake(
						SoundEvents.WARDEN_SONIC_BOOM.getLocation(),
						2F,
						25,
						1,
						18
				)
		)
	);

	public boolean dragonRespawnScreenShake = true;

	public boolean explosionScreenShake = true;

	public static ScreenShakeConfig get() {
		return INSTANCE.config();
	}

	public static Config<ScreenShakeConfig> getConfigInstance() {
		return INSTANCE;
	}
}
