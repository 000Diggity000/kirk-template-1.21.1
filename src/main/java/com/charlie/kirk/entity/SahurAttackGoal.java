package com.charlie.kirk.entity;

import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Zombie;

public class SahurAttackGoal extends MeleeAttackGoal {
    private final SahurMob SahurMob;
    private int raiseArmTicks;

    public SahurAttackGoal(SahurMob sahurMob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(sahurMob, speedModifier, followingTargetEvenIfNotSeen);
        this.SahurMob = sahurMob;
    }

    public void start() {
        super.start();
        this.raiseArmTicks = 0;
    }

    public void stop() {
        super.stop();
        this.SahurMob.setAggressive(false);
    }

    public void tick() {
        super.tick();
        ++this.raiseArmTicks;
        if (this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2) {
            this.SahurMob.setAggressive(true);
        } else {
            this.SahurMob.setAggressive(false);
        }

    }
}
