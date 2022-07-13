package com.williambl.portablejukebox.client.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class MovingSound extends AbstractTickableSoundInstance {

    private final Entity entity;

    public MovingSound(Entity entityIn, SoundEvent soundIn) {
        super(soundIn, SoundSource.NEUTRAL, entityIn.level.getRandom());
        this.entity = entityIn;
        this.looping = false;
        this.delay = 0;
        this.volume = 1.0F;
    }

    @Override
    public void tick() {
        if (!this.entity.isAlive()) {
            this.stop();
        } else {
            this.x = (float) this.entity.getX();
            this.y = (float) this.entity.getY();
            this.z = (float) this.entity.getZ();
        }
    }
}
