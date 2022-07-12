package com.williambl.portablejukebox.jukebox;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PortableJukeboxItem extends Item {

    private List<ItemStack> jukeboxes = null;

    public PortableJukeboxItem(Item.Properties properties) {
        super(properties);
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand handIn) {

        ItemStack stack = player.getItemInHand(handIn);

        CompoundTag tag = stack.getOrCreateTagElement("Disc");
        Item discItem = ItemStack.of(tag).getItem();

        if (!(discItem instanceof RecordItem disc))
            return InteractionResultHolder.pass(stack);

        if (player.isCrouching()) {
            stack.removeTagKey("Disc");
            stack.getOrCreateTag().put("Disc", ItemStack.EMPTY.save(new CompoundTag()));
            player.addItem(new ItemStack(disc));

            if (!world.isClientSide()) {
                //PortableJukeboxMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new PortableJukeboxMessage(false, player.getUniqueID(), disc.getRegistryName()));
            }

            return InteractionResultHolder.success(stack);
        }

        if (!world.isClientSide()) {
            //PortableJukeboxMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new PortableJukeboxMessage(true, player.getUniqueID(), disc.getRegistryName()));
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        CompoundTag tag = stack.getOrCreateTagElement("Disc");

        ItemStack discStack = ItemStack.of(tag);

        if (discStack.getItem() != Items.AIR) {
            tooltip.add(Component.literal("Disc: ").append(discStack.getItem().getDescription()));
        } else {
            tooltip.add(Component.literal("Empty"));
        }
    }

    @Override
    public void fillItemCategory(@Nonnull CreativeModeTab group, @Nonnull NonNullList<ItemStack> items) {
        if (this.allowedIn(group)) {
            items.add(new ItemStack(this));
            items.addAll(this.getJukeboxes());
        }
    }

    /*@Override
    public boolean onDroppedByPlayer(ItemStack stack, Player player) {
        if (!player.level.isClientSide()) {
            //PortableJukeboxMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new PortableJukeboxMessage(false, player.getUniqueID(), ItemStack.read(stack.getOrCreateChildTag("Disc")).getItem().getRegistryName()));
        }
        return true;
    }*/

    private List<ItemStack> getJukeboxes() {
        if (this.jukeboxes == null || this.jukeboxes.isEmpty()) {
            this.jukeboxes = new ArrayList<>();
            Registry.ITEM.stream()
                    .filter(it -> it instanceof RecordItem)
                    .map(it -> it.getDefaultInstance().save(new CompoundTag()))
                    .forEach(nbt -> {
                        /*ItemStack stack = new ItemStack(PortableJukeboxMod.PORTABLE_JUKEBOX.get());
                        stack.getOrCreateTag().put("Disc", nbt);
                        jukeboxes.add(stack);*/
                    });
        }
        return this.jukeboxes;
    }
}
