package nya.tuyw.portablejukebox.jukebox;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import nya.tuyw.portablejukebox.PortableJukeboxMod;

import java.util.ArrayList;
import java.util.List;

public class PortableJukeboxItem extends Item {

    private static List<ItemStack> jukeboxes = null;

    public PortableJukeboxItem(Settings settings) {
        super(settings);
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand handIn) {

        ItemStack stack = player.getStackInHand(handIn);

        NbtCompound tag = stack.getOrCreateSubNbt("Disc");
        Item discItem = ItemStack.fromNbt(tag).getItem();

        if (!(discItem instanceof MusicDiscItem disc))
            return TypedActionResult.pass(player.getStackInHand(handIn));

        if (player.isSneaking()) {
            stack.removeSubNbt("Disc");
            stack.getOrCreateNbt().put("Disc", ItemStack.EMPTY.writeNbt(new NbtCompound()));
            player.giveItemStack(new ItemStack(disc));

            if (!world.isClient) {
                ((ServerChunkManager)player.getEntityWorld().getChunkManager()).sendToNearbyPlayers(player, ServerPlayNetworking.createS2CPacket(new Identifier("portablejukebox:stop"), PacketByteBufs.create().writeUuid(player.getUuid())));
            }

            return new TypedActionResult<>(ActionResult.SUCCESS, stack);
        }

        if (!world.isClient) {
            ((ServerChunkManager)player.getEntityWorld().getChunkManager()).sendToNearbyPlayers(player, ServerPlayNetworking.createS2CPacket(new Identifier("portablejukebox:play"), PacketByteBufs.create().writeUuid(player.getUuid()).writeString(Registries.ITEM.getId(disc).toString())));
        }
        return new TypedActionResult<>(ActionResult.SUCCESS, stack);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        NbtCompound tag = stack.getOrCreateSubNbt("Disc");

        ItemStack discStack = ItemStack.fromNbt(tag);

        if (discStack.getItem() != Items.AIR)
            tooltip.add(Text.literal("Disc: ").append(((MusicDiscItem) discStack.getItem()).getDescription()));
        else
            tooltip.add(Text.literal("Empty"));
    }

    public void onDroppedByPlayer(ItemStack stack, PlayerEntity player) {
        if (!player.getWorld().isClient) {
            ((ServerChunkManager)player.getEntityWorld().getChunkManager()).sendToNearbyPlayers(player, ServerPlayNetworking.createS2CPacket(new Identifier("portablejukebox:stop"), PacketByteBufs.create().writeUuid(player.getUuid())));
        }
    }





    public static List<ItemStack> getJukeboxes() {
        if (jukeboxes == null) {
            jukeboxes = new ArrayList<>();
            Registries.ITEM.stream()
                    .filter(it -> it instanceof MusicDiscItem)
                    .map(item -> item.getDefaultStack().writeNbt(new NbtCompound()))
                    .forEach(tag -> {
                        ItemStack stack = new ItemStack(PortableJukeboxMod.PORTABLE_JUKEBOX);
                        stack.getOrCreateNbt().put("Disc", tag);
                        jukeboxes.add(stack);
                    });
        }
        return jukeboxes;
    }
}
