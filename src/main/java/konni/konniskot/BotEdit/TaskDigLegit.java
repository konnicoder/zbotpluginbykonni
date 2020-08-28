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
import net.minecraft.server.NBTTagCompound;
import zedly.zbot.BlockFace;
import zedly.zbot.EntityType;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.entity.Entity;
import zedly.zbot.inventory.ItemStack;

/**
 *
 * @author Konstantin
 */
public class TaskDigLegit extends Task {
//          msg Edwin_Hubble__ di 594 32 -7990 523 45 -7831 16

    private static final Location TRASH_CHEST_LOC = new Location(298, 137, -8701).centerHorizontally();
    private static final Location TRASH_WALK_LOC = new Location(299, 137, -8701).centerHorizontally();
    private static final Location REPAIR_LOC = new Location(295, 137, -8705).centerHorizontally();
    private static final Location BED_LOC = new Location(321, 137, -8707).centerHorizontally();
    private static final Location BED_WALK = new Location(320, 137, -8707).centerHorizontally();

    private static final HashSet<Material> TRASH_MATERIALS = new HashSet<>();
    public Location anker1;
    public Location anker2;
    public int X;
    public int Y;
    public int Z;
    public int X2;
    public int Y2;
    public int Z2;
    public boolean collect;
    int th;

    boolean firstslicefinder = false;
    private static final HashSet<Material> ores = new HashSet<>();
    private static final HashSet<Material> nonusables = new HashSet<>();
    private static final HashSet<Material> shovelables = new HashSet<>();
    private static final HashSet<Material> dangerous = new HashSet<>();
    private static final HashSet<EntityType> enemies = new HashSet<>();
    int pickaxeslot = 0;
    int shovelslot = 1;
    int axeslot = 2;
    int blockslot = 3;
    int swordslot = 4;
    int foodslot = 8;

    int backupslot = 9;

    public TaskDigLegit(int x1, int y1, int z1, int x2, int y2, int z2, int targetheight) {
        super(10);
        X = x1;
        Y = y1;
        Z = z1;
        X2 = x2;
        Y2 = y2;
        Z2 = z2;
        anker1 = new Location(x1, y1, z1).centerHorizontally();
        anker2 = new Location(x2, y2, z2).centerHorizontally();
        System.out.println("anker1 set to: " + anker1);
        System.out.println("anker2 set to: " + anker2);
        th = targetheight;
    }

    public void run() {
        System.out.println("start");
        try {
            ai.navigateTo(anker1);
            ai.tick();
            if (Main.self.getEnvironment().getBlockAt(anker1.getRelative(0, -1, 0)).isSolid() == false) {
                ai.moveTo(anker1.getRelative(0, -1, 0));
                firstslicefinder = true;
            }
            for (int yhöhe = Y - 1; yhöhe >= th; yhöhe--) {
                for (int xachsenpunkt = X; xachsenpunkt >= X2; xachsenpunkt = xachsenpunkt - 2) {
                    if (firstslicefinder == true && findFirstSlice() == false) {
                        Location walkloc = new Location(xachsenpunkt - 2, Main.self.getLocation().getBlockY(), Z);
                        ai.moveTo(walkloc);
                        ai.tick(5);
                        continue;
                    } else {
                        if (firstslicefinder == true) {
                            firstslicefinder = false;
                        }
                        for (int zachsenpunkt = Z; zachsenpunkt <= Z2; zachsenpunkt++) {
                            Location mine = new Location(xachsenpunkt, yhöhe, zachsenpunkt).centerHorizontally();
                            if (zachsenpunkt == Z && xachsenpunkt == X) {
                                digDown(mine);
                                System.out.println("dig down");
                            } else {
                                if (Main.self.getEnvironment().getBlockAt(mine).getType() != Material.AIR) {
                                    mineClever(mine);
                                }
                                if (Main.self.getEnvironment().getBlockAt(mine.getRelative(0, +1, 0)).getType() != Material.AIR) {
                                    mineDLC(mine.getRelative(0, +1, 0));
                                }

                            }
                            walkSave(mine);
                            checkForBedtime();
                            if (testIfSave() == false) {
                                defender();
                            }
                        }

                        System.out.println(KaiTools.getDayTime());
                        for (int zachsenpunkt = Z2; zachsenpunkt >= Z; zachsenpunkt--) {
                            if (xachsenpunkt == X2) {
                                break;
                            }

                            Location mine = new Location(xachsenpunkt - 1, yhöhe, zachsenpunkt).centerHorizontally();

                            if (Main.self.getEnvironment().getBlockAt(mine).getType() != Material.AIR) {
                                mineClever(mine);
                                if (Main.self.getEnvironment().getBlockAt(mine.getRelative(0, +1, 0)).getType() != Material.AIR) {
                                    mineDLC(mine.getRelative(0, +1, 0));
                                }
                            } else {
                                ai.tick(5);
                            }

                            walkSave(mine);
                            if (testIfSave() == false) {
                                defender();
                            }
                        }

                        Location walkloc = new Location(xachsenpunkt - 1, Main.self.getLocation().getBlockY(), Z);
                        ai.moveTo(walkloc);
                    }

                }
                Location wa = new Location(X, Main.self.getLocation().getBlockY(), Z);
                ai.navigateTo(wa);
            }
            Main.self.sendChat("done, ding konni");
            Main.self.sendChat("/spawn");
        } catch (InterruptedException ex) {
        }

        unregister();
    }

    public boolean findFirstSlice() throws InterruptedException {
        ai.tick(5);
        Location loc = new Location(Main.self.getLocation().getBlockX() - 3, Y - 1, Z);
        System.out.println(loc);
        if (Main.self.getEnvironment().getBlockAt(loc).isSolid() == true) {
            System.out.println("true");
            return true;
        }

        System.out.println("false");
        return false;
    }

    public void walkSave(Location walk) throws InterruptedException {
        if (Main.self.getEnvironment().getBlockAt(walk.getRelative(0, -1, 0)).isSolid() == false) {
            Main.self.selectSlot(blockslot);
            if (KaiTools.testStackAvaliable(Material.STONE, 39) == false) {
                System.out.println("looking");
                KaiTools.lookInInventoryAndMove(Material.STONE, 39, ai);
            }
            Main.self.placeBlock(walk.getRelative(0, -1, 0), BlockFace.DOWN);
        }
        ai.moveTo(walk);
    }

    public void digDown(Location loc) throws InterruptedException {
        ai.moveTo(loc.getRelative(0, +1, +1));
        mineClever(loc);
    }

    public void mineClever(Location loc) throws InterruptedException {
        mineDLC(loc);
        while (Main.self.getEnvironment().getBlockAt(loc.getRelative(0, -1, 0)).isSolid() == false) {
            //Main.self.sendChat("bridge");
            Main.self.selectSlot(blockslot);
            ai.tick();
            if (KaiTools.testStackAvaliable(Material.STONE, 39) == false) {
                System.out.println("looking");
                KaiTools.lookInInventoryAndMove(Material.STONE, 39, ai);
            }
            Main.self.placeBlock(loc.getRelative(0, -1, 0), BlockFace.SOUTH);
        }

        if (testifReadyToMine() == false) {
            if (InventoryUtil.countFreeStorageSlots(true, false) == 0) {
                //empty Inventory
            }
        }

    }

    public boolean testifReadyToMine() {
        if (InventoryUtil.countFreeStorageSlots(true, false) == 0) {
            return false;
        }

        return true;

    }

    public boolean mineDLC(Location loc) throws InterruptedException {

        if (shovelables.contains(Main.self.getEnvironment().getBlockAt(loc).getType())) {
            Main.self.selectSlot(shovelslot);
            if (KaiTools.checkToolHealth() == false) {
                while (KaiTools.checkToolHealth() == false) {
                    System.out.println("TOOL HEALTH LOW");
                    ai.tick(200);
                }
            }
            ai.breakBlock(loc, 2);
        } else {
            Main.self.selectSlot(pickaxeslot);
            if (Main.self.getInventory().getSlot(36).getType() == Material.DIAMOND_PICKAXE) {
                if (checkToollHealth(36) == false) {
                    while (checkToollHealth(36) == false) {
                        getNewTool();
                    }
                }
                ai.mineBlock(loc);
            } else {
                while (true) {
                    Main.self.sendChat("/spawn");
                    ai.tick(10);
                    System.out.println("ERROR");
                    ai.tick(100);

                }
            }

        }

        return true;
    }

    public boolean getNewTool() throws InterruptedException {
        Main.self.sendChat("Toolhealth low, searching for new tool");

        for (int slot = 9; slot < 44; slot++) {
            if (slot == 36) {
                continue;
            }
            if (Main.self.getInventory().getSlot(slot).getType() != Material.DIAMOND_PICKAXE) {
                continue;
            }
            if (checkToollHealth(slot) == false) {
                continue;
            }
            if (checkToollHealth(slot) == true) {
                ai.swapItems(36, slot);
            }
        }

        return true;
    }

    public void checkForBedtime() throws InterruptedException {
        int time = KaiTools.getDayTime();
        if (time > 12000 || time <= 1) {
            eveningRoutine();

        }

    }

    public void eveningRoutine() throws InterruptedException {
        Main.self.sendChat("/sethome temp");
        ai.tick(20);
        Main.self.sendChat("/home xp");
        ai.tick(1500);
        checkFood();
        ai.tick(60);
        healtools();
        ai.tick(20);
        while (KaiTools.getDayTime() < 13000) {
            ai.tick(200);
            System.out.println("waiting for bedtime");
        }
        goSleep();
        ai.tick(5);
        while (KaiTools.getDayTime() > 12000 || KaiTools.getDayTime() <= 300) {
            ai.tick(400);
            System.out.println("waiting for daytime");
        }
        ai.tick(40);
        Main.self.sendChat("/home temp");
        ai.tick(1000);
    }

    public void checkFood() throws InterruptedException {
        if (Main.self.getFoodLevel() < 20) {
            System.out.println("eating");
            Main.self.selectSlot(foodslot);
            ai.eat(0);
            ai.tick();
        }
    }

    public void checkHealth() throws InterruptedException {
        if (Main.self.getHealth() < 10) {
            Main.self.sendChat("/sethome temp");
            ai.tick();
            Main.self.sendChat("/home xp");
            while (Main.self.getHealth() < 20) {
                ai.tick(10);
                System.out.println("regenerating health");

            }
        }
    }

    public static boolean checkToollHealth(int slot) {

        ItemStack is = Main.self.getInventory().getSlot(slot);
        if (is.getNbt() instanceof NBTTagCompound) {
            NBTTagCompound nbt = (NBTTagCompound) is.getNbt();
            int damage = nbt.getInteger("Damage");
            //System.out.println("tooldamage:" + damage);

            if (damage < 1400) {
                return true;
            }
        }
        return false;
    }

    public void goSleep() throws InterruptedException {
        Main.self.sendChat("going to Bed, pls type /afk so that we can skip the night!");
        ai.tick(3);
        ai.moveTo(BED_WALK);
        ai.tick(60);
        Main.self.placeBlock(BED_LOC, BlockFace.UP);
        ai.tick(10);
    }

    public boolean testIfSave() throws InterruptedException {
        if (tryGetEnemy(Main.self.getLocation()) != null) {
            return false;
        }
        return true;
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

    public void healtools() throws InterruptedException {

        ai.moveTo(REPAIR_LOC);
        healpickaxes();
        ai.tick();
        healOtherTools();
        ai.moveTo(TRASH_WALK_LOC);
        ai.tick(60);
        dumpTrash();
    }

    public void healOtherTools() throws InterruptedException {
        Main.self.selectSlot(shovelslot);
        while (KaiTools.checkToolFullHealth() == false) {
            ai.tick(40);
        }
        ai.tick(20);
        Main.self.selectSlot(axeslot);
        while (KaiTools.checkToolFullHealth() == false) {
            ai.tick(40);
        }
        ai.tick(20);
    }

    public void healpickaxes() throws InterruptedException {
        Main.self.selectSlot(pickaxeslot);
        ai.tick();
        while (KaiTools.checkToolFullHealth() == false) {
            ai.tick(40);
        }
        ai.tick(20);

        for (int slot = 9; slot < 44; slot++) {
            if (slot == 36) {
                continue;
            }
            if (Main.self.getInventory().getSlot(slot).getType() != Material.DIAMOND_PICKAXE) {
                continue;
            }
            if (checkToollHealth(slot) == true) {
                continue;
            }
            if (checkToollHealth(slot) == false) {
                ai.swapItems(36, slot);
                ai.tick(10);
                while (KaiTools.checkToolFullHealth() == false) {
                    ai.tick(40);
                }
                ai.tick(20);
            }
        }

    }

    private boolean dumpTrash() throws InterruptedException {
        if (InventoryUtil.findItem((i) -> i != null && TRASH_MATERIALS.contains(i.getType())) == -1) {
            return true;
        }

        if (!ai.openContainer(TRASH_CHEST_LOC)) {
            System.err.println("Can't open disposal");
            ai.tick(50);
            return false;
        }

        int staticOffset = Main.self.getInventory().getStaticOffset();
        boolean hasTrash;
        do {
            hasTrash = false;
            for (int i = staticOffset; i < staticOffset + 36; i++) {
                if (Main.self.getInventory().getSlot(i) != null
                        && TRASH_MATERIALS.contains(Main.self.getInventory().getSlot(i).getType())) {
                    ai.depositSlot(i);
                    hasTrash = true;
                }
            }
        } while (hasTrash);

        ai.closeContainer();

        return true;
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
        dangerous.add(Material.AIR);
        dangerous.add(Material.CAVE_AIR);
        dangerous.add(Material.VOID_AIR);
        dangerous.add(Material.LAVA);
        dangerous.add(Material.WATER);
        shovelables.add(Material.CLAY);
        shovelables.add(Material.COARSE_DIRT);
        shovelables.add(Material.DIRT);
        shovelables.add(Material.FARMLAND);
        shovelables.add(Material.GRASS_BLOCK);
        shovelables.add(Material.GRAVEL);
        shovelables.add(Material.MYCELIUM);
        shovelables.add(Material.PODZOL);
        shovelables.add(Material.RED_SAND);
        shovelables.add(Material.SAND);
        shovelables.add(Material.SOUL_SAND);
        ores.add(Material.COAL_ORE);
        ores.add(Material.IRON_ORE);
        ores.add(Material.GOLD_ORE);
        ores.add(Material.LAPIS_ORE);
        ores.add(Material.REDSTONE_ORE);
        ores.add(Material.EMERALD_ORE);
        ores.add(Material.DIAMOND_ORE);
        nonusables.add(Material.STONE);
        nonusables.add(Material.DIRT);
        nonusables.add(Material.ANDESITE);
        nonusables.add(Material.DIORITE);
        nonusables.add(Material.GRANITE);
        nonusables.add(Material.GRAVEL);
        nonusables.add(Material.COBBLESTONE);

        TRASH_MATERIALS.add(Material.ROTTEN_FLESH);
        TRASH_MATERIALS.add(Material.GOLD_NUGGET);
        TRASH_MATERIALS.add(Material.GOLD_INGOT);
        TRASH_MATERIALS.add(Material.GOLDEN_SWORD);
        TRASH_MATERIALS.add(Material.CHICKEN);
        TRASH_MATERIALS.add(Material.FEATHER);
    }
}
