/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.ConcurrentModificationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import zedly.zbot.EntityType;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.entity.Entity;

/**
 *
 * @author Konstantin
 */
public class TaskMidas extends Task {

    private static final Location home = new Location(2726, 181, -8574).centerHorizontally();
    private static final Location CRAFT_LOC = new Location(2726, 172, -8573).centerHorizontally();
    private static final Location NUGGET_TESS_LOC = new Location(2726, 173, -8572).centerHorizontally();
    private static final Location INGOT_TESS_LOC = new Location(2727, 173, -8573).centerHorizontally();
    private static final Location GOLDBLOCK_TESS_LOC = new Location(2727, 172, -8573).centerHorizontally();
    private static final Location CRAFTINGBENCH_LOC = new Location(2728, 172, -8572).centerHorizontally();

    private int mode = 0;

    public TaskMidas(int mode) {
        super(100);
        this.mode = mode;

    }

    public void run() {
        System.out.println("starting Task TaskGrindPigmenMidas");
        try {
            if (mode == 1) {
                Main.self.sendChat("grinding pigmen");
                grinder();
            }
            if (mode == 2) {
                ai.moveTo(CRAFT_LOC);
                Main.self.sendChat("crafting goldingots");
                KaiTools.CraftFullBlockSuper("gold_ingot_from_nuggets", Material.GOLD_NUGGET, NUGGET_TESS_LOC, INGOT_TESS_LOC, CRAFTINGBENCH_LOC, ai);             
            }
            if (mode == 3) {
                ai.moveTo(CRAFT_LOC);
                Main.self.sendChat("crafting goldblocks");
                KaiTools.CraftFullBlockSuper("gold_block", Material.GOLD_INGOT, INGOT_TESS_LOC, GOLDBLOCK_TESS_LOC, CRAFTINGBENCH_LOC, ai);
            }

        } catch (InterruptedException ex) {
        }

    }

    private void grinder() throws InterruptedException {
        ai.tick();
        if (Main.self.getLocation().distanceTo(home) > 0.1) {
            ai.moveTo(home);
        }
        while (true) {
            ai.tick(3);
            tryAttack(home, home);
            ai.tick(5);
        }
    }

    private boolean tryAttack(Location home, Location attackLoc) throws InterruptedException {
        Entity skeleton = tryGetEnemy(attackLoc);
        if (skeleton != null) {
            ai.moveTo(home);
            ai.tick();
            Main.self.attackEntity(skeleton);
            ai.tick(5);
            return true;
        }
        return false;
    }

    private Entity tryGetEnemy(Location attackLoc) throws InterruptedException {
        try {
            int annoyingEntities = 0;
            Entity nearestAnnoying = null;
            for (Entity ent : Main.self.getEnvironment().getEntities()) {
                if (ent.getType() != EntityType.CHICKEN
                        && ent.getType() != EntityType.PIG_ZOMBIE) {
                    continue;
                }
                if (ent.getLocation().distanceTo(attackLoc) < 4) {
                    annoyingEntities++;
                    if (nearestAnnoying == null
                            || ent.getLocation().distanceTo(attackLoc)
                            < nearestAnnoying.getLocation().distanceTo(attackLoc)) {
                        nearestAnnoying = ent;
                    }
                }
            }
            if (nearestAnnoying != null) {
                return nearestAnnoying;
            }
        } catch (ConcurrentModificationException ex) {
            System.err.println("CME in getEntities() :( :(");
        }
        return null;
    }
}
