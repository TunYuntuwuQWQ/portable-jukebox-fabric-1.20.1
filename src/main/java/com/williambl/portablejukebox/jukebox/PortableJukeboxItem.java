package com.williambl.portablejukebox.jukebox;

import com.williambl.portablejukebox.PortableJukeboxMod;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

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
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {

        ItemStack stack = player.getHeldItem(handIn);

        CompoundNBT tag = stack.getOrCreateChildTag("Disc");
        Item discItem = ItemStack.read(tag).getItem();

        if (!(discItem instanceof MusicDiscItem))
            return ActionResult.resultPass(player.getHeldItem(handIn));
        MusicDiscItem disc = (MusicDiscItem) discItem;

        if (player.isSneaking()) {
            stack.removeChildTag("Disc");
            stack.getOrCreateTag().put("Disc", ItemStack.EMPTY.serializeNBT());
            player.addItemStackToInventory(new ItemStack(disc));

            if (!world.isRemote) {
                PortableJukeboxMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new PortableJukeboxMessage(false, player.getUniqueID(), disc.getRegistryName()));
            }

            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }

        if (!world.isRemote) {
            PortableJukeboxMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new PortableJukeboxMessage(true, player.getUniqueID(), disc.getRegistryName()));
        }
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT tag = stack.getOrCreateChildTag("Disc");

        ItemStack discStack = ItemStack.read(tag);

        if (discStack.getItem() != Items.AIR)
            tooltip.add(new StringTextComponent("Disc: ").append(((MusicDiscItem) discStack.getItem()).getDescription()));
        else
            tooltip.add(new StringTextComponent("Empty"));
    }

    @Override
    public void fillItemGroup(@Nonnull ItemGroup group, @Nonnull NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            items.add(new ItemStack(this));
            items.addAll(getJukeboxes());
        }
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack stack, PlayerEntity player) {
        if (!player.world.isRemote) {
            PortableJukeboxMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new PortableJukeboxMessage(false, player.getUniqueID(), ItemStack.read(stack.getOrCreateChildTag("Disc")).getItem().getRegistryName()));
        }
        return true;
    }

    private List<ItemStack> getJukeboxes() {
        if (jukeboxes == null || jukeboxes.isEmpty()) {
            jukeboxes = new ArrayList<>();
            ForgeRegistries.ITEMS.getValues().stream()
                    .filter(it -> it instanceof MusicDiscItem)
                    .map(it -> it.getDefaultInstance().serializeNBT())
                    .forEach(nbt -> {
                        ItemStack stack = new ItemStack(PortableJukeboxMod.PORTABLE_JUKEBOX.get());
                        stack.getOrCreateTag().put("Disc", nbt);
                        jukeboxes.add(stack);
                    });
        }
        return jukeboxes;
    }
}
