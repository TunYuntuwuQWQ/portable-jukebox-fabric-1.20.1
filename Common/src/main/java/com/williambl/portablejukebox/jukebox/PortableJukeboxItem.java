package com.williambl.portablejukebox.jukebox;

import com.williambl.portablejukebox.DiscHelper;
import com.williambl.portablejukebox.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PortableJukeboxItem extends Item {

    private static final String DISC_TAG_KEY = "Disc";

    private List<ItemStack> jukeboxes = null;

    public PortableJukeboxItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand handIn) {

        ItemStack stack = player.getItemInHand(handIn);

        ItemStack discStack = this.getDiscStack(stack);

        if (!(discStack.getItem() instanceof RecordItem disc)) {
            return InteractionResultHolder.pass(stack);
        }

        if (player.isCrouching()) {
            if (!world.isClientSide()) {
                Services.MESSAGES.sendMessage(PortableJukeboxMessage.stop(player, DiscHelper.getSoundEvent(discStack)), player);
            }

            this.setDiscStack(stack, ItemStack.EMPTY);

            if (!player.addItem(discStack)) {
                player.drop(discStack, true);
            }

            return InteractionResultHolder.success(stack);
        }

        if (!world.isClientSide()) {
            Services.MESSAGES.sendMessage(PortableJukeboxMessage.start(player, DiscHelper.getSoundEvent(discStack)), player);
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        var discStack = this.getDiscStack(stack);
        if (DiscHelper.isDisc(discStack)) {
            tooltip.add(Component.literal("Disc: ").append(DiscHelper.getName(discStack)).withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.literal("Empty").withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public void fillItemCategory(@Nonnull CreativeModeTab group, @Nonnull NonNullList<ItemStack> items) {
        if (this.allowedIn(group)) {
            items.add(this.getDefaultInstance());
            items.addAll(this.getJukeboxes());
        }
    }

    //Overrides on Forge
    public boolean onDroppedByPlayer(ItemStack stack, Player player) {
        if (!player.level.isClientSide()) {
            var discStack = this.getDiscStack(stack);
            if (DiscHelper.isDisc(discStack)) {
                Services.MESSAGES.sendMessage(PortableJukeboxMessage.stop(player, DiscHelper.getSoundEvent(discStack)), player);
            }
        }
        return true;
    }

    private ItemStack getDiscStack(ItemStack stack) {
        if (stack.getOrCreateTag().getTagType(DISC_TAG_KEY) != Tag.TAG_COMPOUND) {
            return ItemStack.EMPTY;
        }

        return ItemStack.of(stack.getOrCreateTagElement(DISC_TAG_KEY));
    }

    private void setDiscStack(ItemStack stack, ItemStack discStack) {
        stack.getOrCreateTag().put(DISC_TAG_KEY, discStack.save(new CompoundTag()));
    }

    private List<ItemStack> getJukeboxes() {
        if (this.jukeboxes == null || this.jukeboxes.isEmpty()) {
            this.jukeboxes = Registry.ITEM.stream()
                    .filter(it -> it instanceof RecordItem)
                    .map(it -> {
                        ItemStack stack = Services.REGISTRY.portableJukeboxItem().get().getDefaultInstance();
                        this.setDiscStack(stack, it.getDefaultInstance());
                        return stack;
                    })
                    .toList();
        }
        return this.jukeboxes;
    }
}
