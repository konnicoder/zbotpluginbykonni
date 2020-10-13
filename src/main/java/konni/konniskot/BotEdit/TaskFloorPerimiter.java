/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot.BotEdit;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import konni.konniskot.InventoryUtil;
import konni.konniskot.KaiTools;
import konni.konniskot.Main;
import konni.konniskot.Task;
import zedly.zbot.BlockFace;
import zedly.zbot.EntityType;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.entity.Entity;

/**
 *
 * @author Konstantin
 */
public class TaskFloorPerimiter extends Task {

    //settings
    int blockslot = 0;
    int swordslot = 4;
    int foodslot = 8;
    boolean usezenchantments = false;
    boolean defenderactive = true;
    Material blockmat = Material.GLASS;

    //variable declaration, do not change
    private static final HashSet<EntityType> enemies = new HashSet<>();
    int x1;
    int y1;
    int z1;

    int x2;
    int y2;
    int z2;

    public TaskFloorPerimiter(int x1, int y1, int z1, int x2, int y2, int z2) {
        super(20);
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
    }

    public void run() {
        System.out.println("Starting TaskFloor");
        Main.self.selectSlot(blockslot);
        try {
            for (int xachse = x1; xachse > x2; xachse = xachse - 2) {
               // Location walk1 = new Location(xachse, y1 + 1, z1).centerHorizontally();
              //  ai.navigateTo(walk1);
                lookForShulker();

                for (int zachse = z1; zachse <= z2; zachse++) {
//                    Location scanloc = new Location(xachse, y1, zachse).centerHorizontally();
//                    if (Main.self.getEnvironment().getBlockAt(scanloc).getType() == Material.GLASS) {
//                        continue;
//                    }
                    doStep(xachse, zachse);
                }
                for (int zachse = z2; zachse >= z1; zachse--) {
//                    Location scanloc = new Location(xachse, y1, zachse).centerHorizontally();
//                    if (Main.self.getEnvironment().getBlockAt(scanloc).getType() == Material.GLASS) {
//                        continue;
//                    }
                    doStep(xachse-1, zachse);
                }

                ai.tick();
                if (Main.self.getFoodLevel() < 20) {
                    System.out.println("eating");
                    Main.self.sendChat("/msg Konni999 eating");
                    Main.self.selectSlot(foodslot);
                    ai.eat(0);
                    ai.tick(10);
                }
            }

        } catch (InterruptedException ex) {
        }
        unregister();
    }

    public void doStep(int xachse, int zachse) throws InterruptedException {
        if (testIfSave() == false && defenderactive == true) {
            defender();
        }
        int blocksleft = InventoryUtil.count(blockmat, true, false);
        System.out.println(blocksleft);
        Main.self.selectSlot(blockslot);
        if (Main.self.getInventory().getSlot(36) == null || Main.self.getInventory().getSlot(36).getType() != blockmat) {
            for (int i = 9; i < 44; i++) {
                if (Main.self.getInventory().getSlot(i) != null && Main.self.getInventory().getSlot(i).getType() == blockmat) {
                    ai.swapItems(i, 36);
                    ai.tick(10);
                    break;
                }
            }
        }

        Location placeloc = new Location(xachse, y1, zachse).centerHorizontally();
        Main.self.placeBlock(placeloc, BlockFace.EAST);     
        ai.moveTo(placeloc.getRelative(0, +1, 0));

    }

    public boolean testIfSave() throws InterruptedException {
        if (tryGetEnemy(Main.self.getLocation()) != null) {
            return false;
        }
        return true;
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

    private Entity tryGetEnemy(Location attackLoc) throws InterruptedException {
        try {
            int annoyingEntities = 0;
            Entity nearestAnnoying = null;
            for (Entity ent : Main.self.getEnvironment().getEntities()) {
                if (enemies.contains(ent.getType()) == false) {
                    continue;
                }

                if (ent.getLocation().distanceTo(attackLoc) < 5) {
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

    public void defender() throws InterruptedException {
        if (tryGetEnemy(Main.self.getLocation()) != null) {
            System.out.println("defend");
            Main.self.selectSlot(swordslot);
            while (tryGetEnemy(Main.self.getLocation()) != null) {
                tryAttack(Main.self.getLocation());
                ai.tick(40);
            }
        }
    }

    public void lookForShulker() throws InterruptedException {
        Location org = Main.self.getLocation();
        if (KaiTools.Scan(Material.SHULKER_BOX, org.getBlockX(), org.getBlockY(), org.getBlockZ(), 2, 4) != null) {
            Location shulkerbox = KaiTools.Scan(Material.SHULKER_BOX, org.getBlockX(), org.getBlockY(), org.getBlockZ(), 2, 4);
            ai.openContainer(shulkerbox);

            for (int slot = 0; slot <= 26; slot++) {
                if (Main.self.getInventory().getSlot(slot) == null) {
                    continue;
                }
                if (InventoryUtil.countFreeStorageSlots(true, false) <= 1) {
                    break;
                }
                if (Main.self.getInventory().getSlot(slot).getType() == blockmat) {
                    ai.withdrawSlot(slot);
                }
            }

            ai.closeContainer();
        } else {
            System.out.println("no shulkerbox found!");
        }

    }

    public boolean findAndPlace(Material mat, int x, int y, int z) throws InterruptedException {
        if (Main.self.getInventory().getSlot(36) != null && Main.self.getInventory().getSlot(36).getType() == mat) {
            Main.self.placeBlock(x, y, z, BlockFace.UP);
            return true;
        } else {
            System.out.println("scanning for items in inventory");
            for (int i = 9; i < 44; i++) {
                if (Main.self.getInventory().getSlot(36) != null && Main.self.getInventory().getSlot(i).getType() == mat) {
                    ai.swapItems(i, 36);
                    break;
                }
            }
            Main.self.placeBlock(x, y, z, BlockFace.UP);
            return true;
        }

    }

    static {
        enemies.add(EntityType.SLIME);
        enemies.add(EntityType.CREEPER);
        enemies.add(EntityType.SKELETON);
        enemies.add(EntityType.ZOMBIE);
        enemies.add(EntityType.DROWNED);
        enemies.add(EntityType.CAVE_SPIDER);
        enemies.add(EntityType.SPIDER);
        enemies.add(EntityType.PILLAGER);
        enemies.add(EntityType.WITCH);
        enemies.add(EntityType.BAT);
        enemies.add(EntityType.WANDERING_TRADER);
        enemies.add(EntityType.TRADER_LLAMA);
    }
}
