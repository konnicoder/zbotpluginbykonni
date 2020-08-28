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
public class TaskFarmWitherSkelletons extends Task {

    boolean run;
    private static final Location GRINDING_LOC = new Location(-1122, 23, -505).centerHorizontally();
    private static final Location BACKUP_LOC = new Location(-1128, 23, -505).centerHorizontally();

    public TaskFarmWitherSkelletons() {
        super(100);
    }

    public void run() {
        try {
            setup();
            while (run == true) {

                tryAttack(Main.self.getLocation());
                ai.tick(10);

            }
        } catch (InterruptedException ex) {

        }
        unregister();
    }

    public void setup() throws InterruptedException {
        run = true;
        ai.moveTo(BACKUP_LOC);
        ai.tick();
        ai.moveTo(GRINDING_LOC);
    }
    
    public void cancel() {
        run = false;
    }

    private Entity tryGetEnemy(Location attackLoc) throws InterruptedException {
        try {
            int annoyingEntities = 0;
            Entity nearestAnnoying = null;
            for (Entity ent : Main.self.getEnvironment().getEntities()) {
                if (ent.getType() != EntityType.WITHER_SKELETON) {
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

    private boolean tryAttack(Location attackLoc) throws InterruptedException {
        Entity skeleton = tryGetEnemy(attackLoc);
        if (skeleton != null) {

            Main.self.attackEntity(skeleton);
            ai.tick(5);
            return true;
        }
        return false;
    }

    
}
