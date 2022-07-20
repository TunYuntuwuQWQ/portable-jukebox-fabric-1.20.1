package com.williambl.portablejukebox;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class DiscHelper {

    private static final List<Predicate<ItemStack>> DISC_PREDICATES = new ArrayList<>();
    private static final List<Function<ItemStack, @Nullable SoundEvent>> DISC_TO_SOUND_EVENT_FUNCTIONS = new ArrayList<>();
    private static final List<Function<ItemStack, @Nullable Component>> DISC_TO_NAME_FUNCTIONS = new ArrayList<>();

    public static void registerDiscPredicate(Predicate<ItemStack> predicate) {
        DISC_PREDICATES.add(predicate);
    }

    public static boolean isDisc(ItemStack stack) {
        for (var predicate : DISC_PREDICATES) {
            if (predicate.test(stack)) {
                return true;
            }
        }

        return false;
    }

    public static void registerDiscToSoundEventFunction(Function<ItemStack, @Nullable SoundEvent> func) {
        DISC_TO_SOUND_EVENT_FUNCTIONS.add(func);
    }

    public static SoundEvent getSoundEvent(ItemStack stack) {
        for (var func : DISC_TO_SOUND_EVENT_FUNCTIONS) {
            var res = func.apply(stack);
            if (res != null) {
                return res;
            }
        }

        return SoundEvents.EXPERIENCE_ORB_PICKUP; // trol
    }

    public static void registerDiscToNameFunction(Function<ItemStack, @Nullable Component> func) {
        DISC_TO_NAME_FUNCTIONS.add(func);
    }

    public static Component getName(ItemStack stack) {
        for (var func : DISC_TO_NAME_FUNCTIONS) {
            var res = func.apply(stack);
            if (res != null) {
                return res;
            }
        }

        return Component.literal("????").withStyle(ChatFormatting.OBFUSCATED);
    }

    static {
        registerDiscPredicate(s -> s.getItem() instanceof RecordItem);
        registerDiscToSoundEventFunction(s -> s.getItem() instanceof RecordItem recordItem ? recordItem.getSound() : null);
        registerDiscToNameFunction(s -> s.getItem() instanceof RecordItem recordItem ? recordItem.getDisplayName() : null);
    }
}
