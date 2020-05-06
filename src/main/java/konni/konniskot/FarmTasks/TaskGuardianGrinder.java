/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot.FarmTasks;

import java.util.ConcurrentModificationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import konni.konniskot.Main;
import konni.konniskot.Task;
import zedly.zbot.EntityType;
import zedly.zbot.Location;
import zedly.zbot.entity.Entity;

/**
 *
 * @author Konstantin
 */
public class TaskGuardianGrinder extends Task {

    private static final Location home = new Location(50, 47, -988);

    public TaskGuardianGrinder() {
        super(100);
    }

    public void run() {

        while (true) {
            try {
                tryAttack(home, home);
                ai.tick(8);
            } catch (InterruptedException ex) {

            }

        }
    }

    private boolean tryAttack(Location home, Location attackLoc) throws InterruptedException {
        Entity skeleton = tryGetEnemy(attackLoc);
        if (skeleton != null) {

            Main.self.attackEntity(skeleton);
            ai.tick(3);
            return true;
        }
        return false;
    }

    private Entity tryGetEnemy(Location attackLoc) throws InterruptedException {
        try {
            int annoyingEntities = 0;
            Entity nearestAnnoying = null;
            for (Entity ent : Main.self.getEnvironment().getEntities()) {
                if (ent.getType() != EntityType.GUARDIAN) {
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
