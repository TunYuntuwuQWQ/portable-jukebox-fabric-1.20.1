package com.williambl.portablejukebox.noteblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

public class PortableNoteBlockItem extends Item {

    public PortableNoteBlockItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();

        world.playSound(null, player.blockPosition(), getInstrumentFromBlock(world, pos), SoundSource.RECORDS, 3.0F, getPitchFromPosition(pos));
        world.addParticle(ParticleTypes.NOTE, player.getX(), player.getEyeY(), player.getZ(), 1.0F, 0F, 0F);
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        worldIn.playSound(null, playerIn.blockPosition(), NoteBlockInstrument.HARP.getSoundEvent(), SoundSource.RECORDS, 3.0F, getPitchFromPosition(playerIn.blockPosition()));
        worldIn.addParticle(ParticleTypes.NOTE, playerIn.getX(), playerIn.getEyeY(), playerIn.getZ(), 1.0F, 0F, 0F);
        return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
    }

    private static float getPitchFromPosition(BlockPos pos) {
        return (float) pos.getY() / 128;
    }

    private static SoundEvent getInstrumentFromBlock(Level worldIn, BlockPos pos) {
        return NoteBlockInstrument.byState(worldIn.getBlockState(pos)).getSoundEvent();
    }
}
