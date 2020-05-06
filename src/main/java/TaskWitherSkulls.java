/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import konni.konniskot.Task;
import zedly.zbot.EntityType;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.entity.Entity;
import konni.konniskot.Main;
import konni.konniskot.InventoryUtil;


/**
 *
 * @author Konstantin
 */
public class TaskWitherSkulls extends Task {

    private static final Location ITEM_SORTER_WALK = new Location(245, 10, -8781).centerHorizontally();
    private static final Location ITEM_SORTER_INPUT = new Location(246, 11, -8781).centerHorizontally();
    private static final Location SMELTER_WALK = new Location(240, 6, -8788).centerHorizontally();
    private static final Location SMELTER_INPUT = new Location(240, 9, -8790).centerHorizontally();
    private static final Location SKULL_FARMING_WALK = new Location(239, 6, -8786)
            ;

    private static final HashSet<Material> SMELTABLES = new HashSet<>();
    private static final HashSet<Material> SORTABLES = new HashSet<>();

    public TaskWitherSkulls() {
        super(100);
    }

    public void run() {
        try {
            Main.self.selectSlot(8);
            ai.moveTo(SKULL_FARMING_WALK);
            ai.tick(3);
            while (true) {
                ai.moveTo(SKULL_FARMING_WALK);
                ai.tick(3);
                while (InventoryUtil.countFreeStorageSlots(true, false) > 0) {
                    tryAttack(SKULL_FARMING_WALK, SKULL_FARMING_WALK);
                    ai.tick(5);
                }
                ai.tick();
                storeItems();
                ai.tick();
            }
        } catch (InterruptedException ex) {

        }

    }

    public void storeItems() throws InterruptedException {
        ai.moveTo(SMELTER_WALK);
        ai.tick();
        ai.openContainer(SMELTER_INPUT);
        for (int slot = 54; slot <= 89; slot++) {
            if (Main.self.getInventory().getSlot(slot) != null
                    && SMELTABLES.contains(Main.self.getInventory().getSlot(slot).getType())) {
                ai.depositSlot(slot);
                ai.tick();
            }
        }
        ai.tick();
        ai.closeContainer();

        ai.tick();
        ai.moveTo(ITEM_SORTER_WALK);
        ai.tick();
        ai.openContainer(ITEM_SORTER_INPUT);
        for (int slot = 54; slot <= 89; slot++) {
            if (Main.self.getInventory().getSlot(slot) != null
                    && SORTABLES.contains(Main.self.getInventory().getSlot(slot).getType())) {
                ai.depositSlot(slot);
                ai.tick();
            }
        }
        ai.tick();
        ai.closeContainer();
        ai.tick();
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
                if (ent.getType() != EntityType.SKELETON && ent.getType() != EntityType.WITHER_SKELETON) {
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

    static {
        SORTABLES.add(Material.STONE_SWORD);
        SORTABLES.add(Material.ARROW);
        SORTABLES.add(Material.BOW);
        SORTABLES.add(Material.WITHER_SKELETON_SKULL);
        SORTABLES.add(Material.COAL);
        SORTABLES.add(Material.BONE);
        SORTABLES.add(Material.LEATHER_BOOTS);
        SORTABLES.add(Material.LEATHER_LEGGINGS);
        SORTABLES.add(Material.LEATHER_CHESTPLATE);
        SORTABLES.add(Material.LEATHER_HELMET);
        SORTABLES.add(Material.SKELETON_SKULL);

        SMELTABLES.add(Material.CHAINMAIL_BOOTS);
        SMELTABLES.add(Material.CHAINMAIL_LEGGINGS);
        SMELTABLES.add(Material.CHAINMAIL_CHESTPLATE);
        SMELTABLES.add(Material.CHAINMAIL_HELMET);

        SMELTABLES.add(Material.IRON_BOOTS);
        SMELTABLES.add(Material.IRON_LEGGINGS);
        SMELTABLES.add(Material.IRON_CHESTPLATE);
        SMELTABLES.add(Material.IRON_HELMET);

        SMELTABLES.add(Material.GOLDEN_BOOTS);
        SMELTABLES.add(Material.GOLDEN_LEGGINGS);
        SMELTABLES.add(Material.GOLDEN_CHESTPLATE);
        SMELTABLES.add(Material.GOLDEN_HELMET);
    }

}
