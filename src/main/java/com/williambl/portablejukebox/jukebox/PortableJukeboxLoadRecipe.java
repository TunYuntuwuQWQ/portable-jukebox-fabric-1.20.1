package com.williambl.portablejukebox.jukebox;

import com.williambl.portablejukebox.PortableJukeboxMod;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

@MethodsReturnNonnullByDefault
public class PortableJukeboxLoadRecipe extends SpecialRecipe {
    public PortableJukeboxLoadRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        ItemStack jukebox = ItemStack.EMPTY;
        ItemStack disc = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stackInSlot = inv.getStackInSlot(i);
            if (!stackInSlot.isEmpty()) {
                if (stackInSlot.getItem() == PortableJukeboxMod.PORTABLE_JUKEBOX.get()) {
                    if (!jukebox.isEmpty()) { //There can only be one!
                        return false;
                    }
                    if (jukebox.hasTag()) {
                        if (ItemStack.read(jukebox.getOrCreateChildTag("Disc")).getItem() != Items.AIR)
                            return false;
                    }
                    jukebox = stackInSlot;
                } else {
                    if (ItemTags.getCollection().getTagByID(new ResourceLocation("minecraft:music_discs")).contains(stackInSlot.getItem()) || stackInSlot.getItem() instanceof MusicDiscItem) {
                        if (!disc.isEmpty()) { //There can only be one!
                            return false;
                        }
                        disc = stackInSlot;
                    }
                }
            }
        }

        return !jukebox.isEmpty() && !disc.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack jukebox = ItemStack.EMPTY;
        ItemStack disc = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stackInSlot = inv.getStackInSlot(i);
            if (!stackInSlot.isEmpty()) {
                Item item = stackInSlot.getItem();
                if (item instanceof PortableJukeboxItem) {
                    if (!jukebox.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    if (jukebox.hasTag()) {
                        if (jukebox.getTag().contains("Disc"))
                            return ItemStack.EMPTY;
                    }

                    jukebox = stackInSlot.copy();
                } else {
                    if (ItemTags.getCollection().getTagByID(new ResourceLocation("minecraft:music_discs")).contains(stackInSlot.getItem()) || stackInSlot.getItem() instanceof MusicDiscItem) {
                        if (!disc.isEmpty()) { //There can only be one!
                            return ItemStack.EMPTY;
                        }
                        disc = stackInSlot.copy();
                    }
                }
            }
        }

        if (!jukebox.isEmpty() && !disc.isEmpty()) {
            jukebox.getOrCreateTag().put("Disc", disc.serializeNBT());
            return jukebox;
        } else {
            return ItemStack.EMPTY;
        }
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return PortableJukeboxMod.PORTABLE_JUKEBOX_LOAD.get();
    }
}