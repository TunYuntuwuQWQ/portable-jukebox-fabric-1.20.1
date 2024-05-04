package nya.tuyw.portablejukebox.jukebox;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import nya.tuyw.portablejukebox.PortableJukeboxMod;

public class PortableJukeboxLoadRecipe extends SpecialCraftingRecipe {
    public PortableJukeboxLoadRecipe(Identifier idIn, CraftingRecipeCategory category) {
        super(idIn, category);
    }

    @Override
    public boolean matches(RecipeInputInventory inv, World worldIn) {
        ItemStack jukebox = ItemStack.EMPTY;
        ItemStack disc = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); ++i) {
            ItemStack stackInSlot = inv.getStack(i);
            if (!stackInSlot.isEmpty()) {
                if (stackInSlot.getItem() == PortableJukeboxMod.PORTABLE_JUKEBOX) {
                    if (!jukebox.isEmpty()) { //There can only be one!
                        return false;
                    }
                    if (jukebox.hasNbt()) {
                        if (ItemStack.fromNbt(jukebox.getSubNbt("Disc")).getItem() != Items.AIR)
                            return false;
                    }
                    jukebox = stackInSlot;
                } else {
                    if (stackInSlot.isIn(ItemTags.MUSIC_DISCS) || stackInSlot.getItem() instanceof MusicDiscItem) {
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
    public ItemStack craft(RecipeInputInventory inv, DynamicRegistryManager registryManager) {
        ItemStack jukebox = ItemStack.EMPTY;
        ItemStack disc = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); ++i) {
            ItemStack stackInSlot = inv.getStack(i);
            if (!stackInSlot.isEmpty()) {
                Item item = stackInSlot.getItem();
                if (item instanceof PortableJukeboxItem) {
                    if (!jukebox.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    if (jukebox.getOrCreateNbt().contains("Disc"))
                        return ItemStack.EMPTY;

                    jukebox = stackInSlot.copy();
                } else {
                    if (stackInSlot.getItem() instanceof MusicDiscItem) {
                        if (!disc.isEmpty()) { //There can only be one!
                            return ItemStack.EMPTY;
                        }
                        disc = stackInSlot.copy();
                    }
                }
            }
        }
        if (!jukebox.isEmpty() && !disc.isEmpty()) {
            jukebox.getOrCreateNbt().put("Disc", disc.writeNbt(new NbtCompound()));
            return jukebox;
        } else {
            return ItemStack.EMPTY;
        }
    }


    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return PortableJukeboxMod.PORTABLE_JUKEBOX_LOAD;
    }
}