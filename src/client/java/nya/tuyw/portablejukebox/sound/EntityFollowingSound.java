package nya.tuyw.portablejukebox.sound;

import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;

public class EntityFollowingSound extends MovingSoundInstance {

    private final Entity entity;

    public EntityFollowingSound(Entity entityIn, SoundEvent soundIn) {
        super(soundIn, SoundCategory.NEUTRAL, Random.create());
        this.entity = entityIn;
        this.repeat = false;
        this.repeatDelay = 0;
        this.volume = 1.0F;
    }

    public void tick() {
        if (!this.entity.isAlive()) {
            this.setDone();
        } else {
            this.x = (float) this.entity.getX();
            this.y = (float) this.entity.getY();
            this.z = (float) this.entity.getZ();
        }
    }
}
