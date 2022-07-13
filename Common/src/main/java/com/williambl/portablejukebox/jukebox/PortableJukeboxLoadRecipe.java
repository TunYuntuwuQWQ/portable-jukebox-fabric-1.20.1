package com.williambl.portablejukebox.jukebox;

import com.williambl.portablejukebox.platform.Services;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

@MethodsReturnNonnullByDefault
public class PortableJukeboxLoadRecipe extends CustomRecipe {
    public PortableJukeboxLoadRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        ItemStack jukebox = ItemStack.EMPTY;
        ItemStack disc = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stackInSlot = inv.getItem(i);
            if (!stackInSlot.isEmpty()) {
                if (stackInSlot.getItem() == Services.REGISTRY.portableJukeboxItem().get()) {
                    if (!jukebox.isEmpty()) { //There can only be one!
                        return false;
                    }
                    if (jukebox.hasTag()) {
                        if (!ItemStack.of(jukebox.getOrCreateTagElement("Disc")).isEmpty()) {
                            return false;
                        }
                    }
                    jukebox = stackInSlot;
                } else {
                    if (stackInSlot.is(ItemTags.MUSIC_DISCS) || stackInSlot.getItem() instanceof RecordItem) {
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
    public ItemStack assemble(CraftingContainer inv) {
        ItemStack jukebox = ItemStack.EMPTY;
        ItemStack disc = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stackInSlot = inv.getItem(i);
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
                    if (stackInSlot.getItem() instanceof RecordItem) {
                        if (!disc.isEmpty()) { //There can only be one!
                            return ItemStack.EMPTY;
                        }
                        disc = stackInSlot.copy();
                    }
                }
            }
        }

        if (!jukebox.isEmpty() && !disc.isEmpty()) {
            jukebox.getOrCreateTag().put("Disc", disc.save(new CompoundTag()));
            return jukebox;
        } else {
            return ItemStack.EMPTY;
        }
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Services.REGISTRY.portableJukeboxLoadRecipeSerializer().get();
    }
}