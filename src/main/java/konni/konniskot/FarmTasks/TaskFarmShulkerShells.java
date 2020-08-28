/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot.FarmTasks;

import java.util.ConcurrentModificationException;
import konni.konniskot.Main;
import konni.konniskot.Task;
import zedly.zbot.EntityType;
import zedly.zbot.Location;
import zedly.zbot.entity.Entity;

/**
 *
 * @author Konstantin
 */
public class TaskFarmShulkerShells extends Task {

    private static final Location savepos = new Location(12, 1, -391).centerHorizontally();

    public TaskFarmShulkerShells(String user) {
        super(50);
    }

    public void run() {
        herbert();
    }

    public void herbert() {
        try {
            while (true) {
                if (Main.self.getHealth() <= 6) {
                    doHeal();
                }
                if (getEnderLoc() != null) {
                    transformEndermen(getEnderLoc());
                    ai.tick();
                }
                if (getShulkerLoc() != null) {
                    killShulker();
                    ai.tick();
                }
                if (getEnderLoc() == null && getShulkerLoc() == null) {
                    spawnwalk();
                }

            }
        } catch (InterruptedException ex) {
        }
    }

    public void doHeal() throws InterruptedException {
        Location temp = Main.self.getLocation();
        ai.moveTo(savepos);
        while (Main.self.getHealth() <= 19) {
            ai.tick(10);
        }
        ai.tick();
       
    }

    public void killShulker() throws InterruptedException {
        Main.self.selectSlot(1);
        while (getShulkerLoc() != null) {
            ai.moveTo(getShulkerLoc());
            attackShulker(getShulkerLoc());
            ai.tick(7);
        }
    }

    public void spawnwalk() {
        System.out.println("spawnwalk");
    }

    public void transformEndermen(Location loc) throws InterruptedException {
        Main.self.selectSlot(0);
        ai.moveTo(loc);
        if (getEnderLoc().distanceTo(Main.self.getLocation()) > 2) {
            ai.moveTo(getEnderLoc());
            ai.tick();
        }
        Main.self.selectSlot(0);
        tryAttack(loc);
        ai.tick();
    }

    private Location getEnderLoc() {
        for (Entity ent : Main.self.getEnvironment().getEntities()) {
            if (ent.getType() != EntityType.ENDERMAN) {
                continue;
            }
            if (ent.getLocation().getBlockY() != Main.self.getLocation().getBlockY()) {
                continue;
            }

            if (ent.getType() == EntityType.ENDERMAN) {
                Location target = ent.getLocation();
                return target;
            }

        }

        return null;
    }

    private Location getShulkerLoc() {
        for (Entity ent : Main.self.getEnvironment().getEntities()) {
            if (ent.getType() != EntityType.SHULKER) {
                continue;
            }
            if (ent.getType() == EntityType.SHULKER) {
                Location target = ent.getLocation();
                return target;
            }

        }

        return null;
    }

    private boolean tryAttack(Location attackLoc) throws InterruptedException {
        Entity skeleton = tryGetEnemy(attackLoc, EntityType.ENDERMAN);
        if (skeleton != null) {

            ai.tick();
            Main.self.attackEntity(skeleton);
            ai.tick(5);
            return true;
        }
        return false;
    }

    private boolean attackShulker(Location attackLoc) throws InterruptedException {
        Entity skeleton = tryGetEnemy(attackLoc, EntityType.SHULKER);
        if (skeleton != null) {

            ai.tick();
            Main.self.attackEntity(skeleton);
            ai.tick(5);
            return true;
        }
        return false;
    }

    private Entity tryGetEnemy(Location attackLoc, EntityType ente) throws InterruptedException {
        try {
            int annoyingEntities = 0;
            Entity nearestAnnoying = null;
            for (Entity ent : Main.self.getEnvironment().getEntities()) {
                if (ent.getType() != ente) {
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
