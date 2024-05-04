package nya.tuyw.portablejukebox;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import nya.tuyw.portablejukebox.sound.EntityFollowingSound;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public class PortableJukeboxModClient implements ClientModInitializer {

	private static final Map<UUID, SoundInstance> PLAYING_SOUNDS = new HashMap<>();

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(new Identifier("portablejukebox:play"), ((client, handler, buf, responseSender) -> {
			World world = MinecraftClient.getInstance().world;
			if (world == null) return;
			UUID uuid = buf.readUuid();
			Identifier discId = new Identifier(buf.readString());

			client.send(() -> {
				SoundInstance oldSound = PLAYING_SOUNDS.get(uuid);
				if (oldSound != null)
					client.getSoundManager().stop(oldSound);

				EntityFollowingSound sound = new EntityFollowingSound(
						world.getPlayerByUuid(uuid),
						((MusicDiscItem) Objects.requireNonNull(Registries.ITEM.get(discId))).getSound()
				);
				client.getSoundManager().play(sound);
				client.getMusicTracker().stop();
				PLAYING_SOUNDS.put(uuid, sound);
			});
		}));

		ClientPlayNetworking.registerGlobalReceiver(new Identifier("portablejukebox:stop"), ((client, handler, buf, responseSender) -> {
			UUID uuid = buf.readUuid();
			client.send(() -> {
				SoundInstance oldSound = PLAYING_SOUNDS.get(uuid);
				if (oldSound != null)
					client.getSoundManager().stop(oldSound);
				PLAYING_SOUNDS.remove(uuid);
			});
		}));
	}
}
