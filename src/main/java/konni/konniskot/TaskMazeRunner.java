/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import static java.lang.Math.random;
import java.util.ConcurrentModificationException;
import java.util.Random;
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
public class TaskMazeRunner extends Task {

    private static final Location HOMEPOINT = new Location(1366, 65, -8955).centerHorizontally();
    int temp = 0;
    int terri;
    int miau;
    private Location TERRYTORY = new Location();

    public TaskMazeRunner(int territorium) {
        super(40);
        terri = territorium;
    }

    public void run() {
//13517      997
        selectTerritory();
        Main.self.sendChat("/home territory");

        while (true) {
            try {
                ai.tick();
                if (checkIfHuntTime() == true) {
                    Main.self.sendChat("/msg Konni999 HUNT START");
                }
                while (checkIfHuntTime() == true) {
                    miau = 0;
                    goHunt();
                }
                if (checkIfHuntTime() == false && miau == 0) {
                    Main.self.sendChat("/msg Konni999 HUNT STOP");
                    miau = 1;
                    Main.self.sendChat("/home home");
                }
                ai.tick();
                

            } catch (InterruptedException ex) {
            }
            if (temp == 100) {
                break;
            }
        }

        unregister();
    }

    public boolean checkIfHuntTime() {
        if (getDayTime() > 0 && getDayTime() < 13500) {
            return false;
        }
        return true;
    }

    public void goHunt() throws InterruptedException {
        if (Main.self.getLocation().distanceTo(TERRYTORY) > 120) {
            Main.self.sendChat("/home territory");
            ai.tick(3);
        }
        Main.self.sneak(true);
        try {
            ai.tick();
            if (getPlayerLoc() == null) {
                walk(randomDirection());
            }

            while (getPlayerLoc() != null) {
                Main.self.sneak(true);
                ai.moveToDynamic(findWalkableLocation(getPlayerLoc()), 1);
                attack();
                ai.tick();
                break;
            }
            ai.tick();
        } catch (InterruptedException ex) {
        }

    }

    public Location findWalkableLocation(Location loc) {
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        for (int Y = y; Y > 0; Y--) {
            if (Main.self.getEnvironment().getBlockAt(x, Y + 2, z).getType() == Material.AIR
                    && Main.self.getEnvironment().getBlockAt(x, Y - 1, z).getType() != Material.AIR) {
                Location ziel = new Location(x, Y, z);
                return ziel;
            }
        }
        return null;
    }

    public void selectTerritory() {
        if (terri == 0) {
            TERRYTORY = new Location(1364, 64, -8826);
        }
        if (terri == 1) {
            TERRYTORY = new Location(1353, 64, -9100);
        }
        if (terri == 2) {
            TERRYTORY = new Location(1232,64,-8954);
        }
        if (terri == 3) {
            TERRYTORY = new Location();
        }

        System.out.println("Territory center set to" + TERRYTORY);

    }

    public void attack() throws InterruptedException {
        if (getPlayerLoc() != null) {
            if (Main.self.getLocation().distanceTo(getPlayerLoc()) > 5) {
                ai.moveTo(getPlayerLoc());
            }
        }

        tryAttack(Main.self.getLocation(), Main.self.getLocation());
        ai.tick(5);
    }

    public int randomDirection() {
        Random rnd = new Random();
        int randomInt = rnd.nextInt(99);
        System.out.println(randomInt);
        return randomInt;
    }

    public int randomStepSize() {
        Random rnd = new Random();
        int randomInt = rnd.nextInt(20);
        System.out.println(randomInt);
        return randomInt + 5;
    }

    public void walk(int direction) throws InterruptedException {
        if (direction >= 0 && direction < 25) {
            cleverWalk(Main.self.getLocation().getRelative(randomStepSize(), 0, 0));
            System.out.println("a");
        }
        if (direction >= 25 && direction < 50) {
            cleverWalk(Main.self.getLocation().getRelative(-randomStepSize(), 0, 0));
            System.out.println("b");
        }
        if (direction >= 50 && direction < 75) {
            cleverWalk(Main.self.getLocation().getRelative(0, 0, randomStepSize()));
            System.out.println("c");
        }
        if (direction >= 75 && direction < 99) {
            cleverWalk(Main.self.getLocation().getRelative(1, 0, -randomStepSize()));
            System.out.println("d");
        }

    }

    public void cleverWalk(Location loc) throws InterruptedException {
        if (Main.self.getEnvironment().getBlockAt(loc).getType() == Material.AIR) {
            if (checkIfBlocksAbove(loc) == false) {
                ai.moveTo(loc);
            }

        }
    }

    public boolean checkIfBlocksAbove(Location checkloc) {
        int x = checkloc.getBlockX();
        int y = checkloc.getBlockY();
        int z = checkloc.getBlockZ();
        for (int Y = y; Y < 120; Y++) {
            if (Main.self.getEnvironment().getBlockAt(x, Y, z).getType() != Material.AIR) {
                return true;
            }
        }
        return false;
    }

    public int getDayTime() {
        long time = Main.self.getEnvironment().getTimeOfDay() % 24000;
        int daytime = (int) time;
        System.out.println(daytime);
        return daytime;
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

    private Location getPlayerLoc() {
        for (Entity ent : Main.self.getEnvironment().getEntities()) {
            if (ent.getType() != EntityType.PLAYER) {
                continue;
            }
            if (ent.getType() == EntityType.PLAYER) {
                Location target = ent.getLocation();
                return target;
            }

        }

        return null;
    }

    private Entity tryGetEnemy(Location attackLoc) throws InterruptedException {
        try {
            int annoyingEntities = 0;
            Entity nearestAnnoying = null;
            for (Entity ent : Main.self.getEnvironment().getEntities()) {
                if (ent.getType() != EntityType.PLAYER) {
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
