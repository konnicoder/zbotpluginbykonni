package konni.konniskot;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import zedly.zbot.BlockFace;
import zedly.zbot.EntityType;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.block.TileSign;
import zedly.zbot.entity.Entity;

/**
 *
 * @author Konstantin
 */
public class TaskDailyRoutine extends Task {

    private static final Location home = new Location(295, 137, -8702).centerHorizontally();
    private static final Location BUTTON_LOC = new Location(293, 138, -8702).centerHorizontally();
    private static final Location PLAYER_FUEL_LOC = new Location(295.5, 137, -8704.5);
    private static final Location BACKUP_LOC = new Location(295, 137, -8699);

    private static final Location TRASH_CHEST_LOC = new Location(298, 137, -8701).centerHorizontally();
    private static final Location TRASH_WALK_LOC = new Location(299, 137, -8701).centerHorizontally();

    private static final Location SHULKER_WALK = new Location(290, 137, -8721).centerHorizontally();
    private static final Location BUTTON_SHULKER_LOC = new Location(289, 137, -8721).centerHorizontally();
    private static final Location SHULKER_OUTPUT_LOC = new Location(290, 138, -8723).centerHorizontally();
    private static final Location NUGGET_TESSERACT_WALK = new Location(299, 137, -8713).centerHorizontally();
    private static final Location NUGGET_TESSERACT_LOC = new Location(300, 138, -8713).centerHorizontally();

    private static final Location GOLD_INGOT_CHEST_WALK = new Location(298, 133, -8716).centerHorizontally();
    private static final Location GOLD_INGOT_CHEST_LOC = new Location(298, 132, -8716).centerHorizontally();
    private static final Location GOLD_INGOT_TESS_WALK = new Location(299, 137, -8714).centerHorizontally();
    private static final Location GOLD_INGOT_TESS_LOC = new Location(300, 138, -8714).centerHorizontally();

    private static final Location FEATHER_CHEST_WALK = new Location(296, 133, -8716).centerHorizontally();
    private static final Location FEATHER_CHEST_LOC = new Location(296, 132, -8716).centerHorizontally();
    private static final Location FEATHER_TESS_WALK = new Location(299, 137, -8717).centerHorizontally();
    private static final Location FEATHER_TESS_LOC = new Location(300, 138, -8717).centerHorizontally();

    private static final Location COOCKED_CHICKEN_CHEST_WALK = new Location(288, 137, -8715).centerHorizontally();
    private static final Location COOCKED_CHICKEN_CHEST_LOC = new Location(288, 137, -8715).centerHorizontally();

    private static final Location COAL_CHEST_WALK = new Location(299, 132, -8717).centerHorizontally();
    private static final Location COAL_CHEST_LOC = new Location(300, 132, -8717).centerHorizontally();

    private static final Location KELP_INPUT_WALK = new Location(255, 137, -8603).centerHorizontally();
    private static final Location KELP_INPUT_TESS = new Location(256, 138, -8603).centerHorizontally();
    private static final Location KELP_CRAFTING_WALK = new Location(255, 137, -8602).centerHorizontally();
    private static final Location KELP_CRAFTING_LOC = new Location(257, 138, -8602).centerHorizontally();
    private static final Location KELP_OUTPUT_WALK = new Location(255, 137, -8604).centerHorizontally();
    private static final Location KELP_OUTPUT_TESS = new Location(256, 138, -8604).centerHorizontally();
    private static final Location KELP_OUTPUT_CHEST = new Location(255, 137, -8601).centerHorizontally();
    private static final Location waypoint1 = new Location(229, 137, -8711).centerHorizontally();
    private static final Location waypoint2 = new Location(229, 137, -8631).centerHorizontally();
    private static final Location signloc = new Location(256, 138, -8604).centerHorizontally();

    private static final Location CHICK_TESS_WALK = new Location(299, 137, -8716).centerHorizontally();
    private static final Location CHICK_TESS = new Location(300, 138, -8716).centerHorizontally();

    private static final HashSet<Material> TRASH_MATERIALS = new HashSet<>();
    private static final HashSet<Material> Fuel = new HashSet<>();

    private static boolean playerPresent = false;
    private static boolean grinding = false;
    private static boolean shulkerempty = false;
    private static boolean daily = true;
    private static int nuggettransfer = 0;
    private static int goldingottransfer = 0;
    private static int grd;
    private static int grinds = Main.config.getInt("grinds", 0);
    private static double fuellevel;
    private static int goldfarmruns;

    public TaskDailyRoutine() {
        super(100);
    }

    public void run() {

        try {
            Main.self.sendChat("/home xp");
            ai.tick(5);
            while (daily) {

                if (getFoodLevel() <= 0) {
                    ai.moveTo(CHICK_TESS_WALK);
                    ai.tick();
                    ai.clickBlock(CHICK_TESS);
                }
                goldfarmruns = 0;
                while (goldfarmruns <= 5) {
                    goldfarm();
                    goldfarmruns++;
                }
            }

        } catch (InterruptedException ehmm) {
        }
        unregister();

    }

    public int getFoodLevel() {
        int food = InventoryUtil.count(Material.COOKED_CHICKEN, true, false);
        return food;
    }

    public void craftKelpBlocks(int repeats) throws InterruptedException {
        for (int i = 0; i < repeats; i++) {
            ai.tick();
            craftBlock(Material.DRIED_KELP, KELP_CRAFTING_WALK, KELP_CRAFTING_LOC, KELP_INPUT_TESS, KELP_INPUT_WALK, KELP_OUTPUT_TESS, KELP_OUTPUT_WALK);
            ai.tick();
            System.out.println("done crafting");

            ai.moveTo(KELP_INPUT_WALK);
            ai.tick(3);
            Main.self.sneak(true);
            ai.tick();
            Main.self.placeBlock(KELP_INPUT_TESS, BlockFace.EAST);
            ai.tick();
            Main.self.sneak(false);
        }

    }

    public void storeKelp() throws InterruptedException {
        ai.moveTo(KELP_CRAFTING_LOC);
        ai.openContainer(KELP_OUTPUT_CHEST);
        ai.tick();
        emptyChest();
        ai.tick();
        ai.closeContainer();
        ai.tick();
        ai.moveTo(KELP_INPUT_WALK);
        ai.tick(3);
        Main.self.sneak(true);
        ai.tick();
        Main.self.placeBlock(KELP_INPUT_TESS, BlockFace.EAST);
        ai.tick();
        Main.self.sneak(false);

    }

    public void craftBlock(Material matin, Location walkcraft, Location craftingbench, Location inputtess, Location walkinput, Location outputtess, Location walkoutput) throws InterruptedException {
        ai.moveTo(walkinput);
        ai.tick();
        while (InventoryUtil.count(matin, true, false) < 9 * 64) {
            Main.self.clickBlock(inputtess);
            ai.tick(3);
        }
        ai.moveTo(walkcraft);

        ai.openContainer(craftingbench);
        int staticOffset = Main.self.getInventory().getStaticOffset();
        for (int crafting = 1; crafting <= 9; crafting++) {
            for (int local = staticOffset; local < staticOffset + 36; local++) {
                if (Main.self.getInventory().getSlot(local) != null
                        && Main.self.getInventory().getSlot(local).getType() == matin) {
                    ai.transferItem(local, crafting);
                    System.out.println("MIAUUUU " + local + " " + crafting);

                    ai.tick(2);
                    break;
                }
            }
        }
        Main.self.getInventory().click(0, 1, 0);
        ai.closeContainer();

        ai.moveTo(walkoutput);
        ai.tick(3);
        Main.self.sneak(true);
        ai.tick();
        Main.self.placeBlock(outputtess, BlockFace.EAST);
        ai.tick();
        Main.self.sneak(false);
    }

    public void goldfarm() throws InterruptedException {
        grd = 0;
        grinding = true;
        ai.tick();
        Main.self.selectSlot(1);
        if (Main.self.getLocation().distanceTo(home) > 0.1) {
            ai.moveTo(home);
        }
        while (grinding) {
            grindpigmen();
            System.out.println(grd);

            if (grd >= grinds) {
                grinding = false;
                System.out.println("done grinding");
            }
        }
        ai.tick(2);
        //Trashcan start
        ai.moveTo(TRASH_WALK_LOC);
        ai.tick(20);
        System.out.println("wait done, deposit trash");
        dumpTrash();
        shulkerempty = true;
        //Trashcan end

        ai.tick();
        ai.moveTo(SHULKER_WALK);
        ai.tick(2);
        checkshulkers();

        while (shulkerempty) {
            shulkers();
            ai.tick();
            System.out.println(nuggettransfer);
        }
        ai.tick();
        serviceoutput();
        ai.tick();
        Main.self.sendChat("/msg ImakeYourBedrock " + nuggettransfer);

    }

    public void cancel() {
        daily = false;
        grinding = false;
        shulkerempty = false;
        Main.self.sendChat("I transfered " + nuggettransfer + " goldnuggets and " + goldingottransfer + " goldingots into tesseracts.");
    }

    public void serviceoutput() throws InterruptedException {
        serviceChest(GOLD_INGOT_CHEST_WALK, GOLD_INGOT_CHEST_LOC, GOLD_INGOT_TESS_WALK, GOLD_INGOT_TESS_LOC);
        ai.tick();
        serviceChest(FEATHER_CHEST_WALK, FEATHER_CHEST_LOC, FEATHER_TESS_WALK, FEATHER_TESS_LOC);
        ai.tick();
        EmptyChickens();
        ai.moveTo(COAL_CHEST_WALK);
        ai.tick();
        ai.openContainer(COAL_CHEST_LOC);
        ai.tick();
        fuellevel = InventoryUtil.count(Material.DRIED_KELP_BLOCK, false, true);
        fuellevel = fuellevel / 3456;
        fuellevel = fuellevel * 100;
        ai.tick();
        ai.closeContainer();
        ai.tick();
        System.out.println(fuellevel);
        ai.tick();
        Main.self.sendChat("/msg Konni999 fuellevel at goldfarm ist at " + fuellevel + "%");
        if (fuellevel < 20) {
            System.out.println("Fuellevel low: " + fuellevel + "getting fuel...");
            if (Main.self.getLocation().distanceTo(KELP_CRAFTING_LOC) > 10) {
                System.out.println("Fuellevel low: " + fuellevel + "getting fuel...");
                getkelp();
            }
        } else {
            System.out.println("Fuellevel O.K.: " + fuellevel);
        }

        ai.tick();
    }

    public void getkelp() throws InterruptedException {
        Main.self.sendChat("/home xp");
        ai.tick(5);
        ai.moveTo(waypoint1);
        ai.tick(5);
        ai.moveTo(waypoint2);
        ai.tick(5);
        ai.moveTo(KELP_INPUT_WALK);
        while (InventoryUtil.countFreeStorageSlots(true, false) >= 1) {
            ai.clickBlock(KELP_OUTPUT_TESS);
            ai.tick(3);
        }

        Main.self.sendChat("/home xp");
        ai.tick(5);
        ai.moveTo(COAL_CHEST_WALK);
        ai.tick();
        ai.openContainer(COAL_CHEST_LOC);

        for (int slot = 54; slot <= 89; slot++) {
            if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == Material.DRIED_KELP_BLOCK) {
                ai.depositSlot(slot);
                ai.tick();
            }
        }
        ai.closeContainer();
        ai.tick();
    }

    public void serviceChest(Location walkchest, Location chestloc, Location tesswalk, Location tessloc) throws InterruptedException {
        System.out.println("service chest");
        ai.moveTo(walkchest);
        ai.tick(3);
        ai.openContainer(chestloc);
        ai.tick(3);
        emptyChest();
        ai.tick();
        ai.closeContainer();
        ai.tick(3);
        Main.self.sendChat("/home xp");
        ai.tick(5);
        ai.moveTo(tesswalk);
        ai.tick();
        Main.self.sneak(true);
        ai.tick();
        Main.self.placeBlock(tessloc, BlockFace.EAST);
        ai.tick();
        Main.self.sneak(false);
    }

    public void checkshulkers() throws InterruptedException {
        System.out.println("checking shulkers...");
        if (Main.self.getEnvironment().getBlockAt(290, 138, -8723).getType() != Material.SHULKER_BOX) {
            System.out.println("shulker not found");
            ai.tick(5);
            System.out.println("press button");
            Main.self.placeBlock(BUTTON_SHULKER_LOC, BlockFace.EAST);
            ai.tick(20);
        } else {
            System.out.println("shulker avaliable");
        }
    }

    public void shulkers() throws InterruptedException {
        ai.tick();
        ai.moveTo(SHULKER_WALK);
        ai.tick();
        if (Main.self.getEnvironment().getBlockAt(290, 138, -8723).getType() == Material.SHULKER_BOX) {
            ai.openContainer(SHULKER_OUTPUT_LOC);
            ai.tick();
            if (InventoryUtil.count(Material.GOLDEN_SWORD, false, true) > 0 || InventoryUtil.count(Material.GOLD_INGOT, false, true) > 0 || InventoryUtil.count(Material.FEATHER, false, true) > 0) {
                System.out.println("ERROR");
                Main.self.sendChat("/msg Konni999 Error at the Shulkeroutput, ding konni");
            }
            ai.tick();
            emptyChest();

            ai.tick(10);
            ai.closeContainer();
            ai.tick();
            nuggettransfer = nuggettransfer + InventoryUtil.count(Material.GOLD_NUGGET, true, false);
            ai.tick();
            ai.moveTo(NUGGET_TESSERACT_WALK);
            ai.tick(0);
            Main.self.sneak(true);
            ai.tick();
            Main.self.placeBlock(NUGGET_TESSERACT_LOC, BlockFace.EAST);
            ai.tick();
            Main.self.sneak(false);
        } else {
            System.out.println("no shulker found");

            shulkerempty = false;
        }

    }

    public static boolean testPlayerPresent() {
        try {

            for (Entity ent : Main.self.getEnvironment().getEntities()) {
                if (ent.getType() != EntityType.PLAYER) {
                    continue;
                }
                if (ent.getLocation().distanceTo(PLAYER_FUEL_LOC) < 3) {
                    playerPresent = true;
                    return true;
                }
            }
        } catch (ConcurrentModificationException ex) {
            System.err.println("CME in getEntities() :( :(");
            return false;
        }
        playerPresent = false;
        return true;
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
    

    private void grind() throws InterruptedException {

        ai.tick();
        tryAttack(home, home);
        //System.out.println("hit");
        //System.out.println("press button");
        ai.tick(3);
        Main.self.placeBlock(BUTTON_LOC, BlockFace.EAST);
        grd++;
        ai.tick(5);
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

    private boolean emptyChest() throws InterruptedException {
        for (int i = 0; i < Main.self.getInventory().getStaticOffset(); i++) {
            if (InventoryUtil.findFreeStorageSlot(true) == -1) {
                return true;
            }
            if (Main.self.getInventory().getSlot(i) != null) {
                ai.withdrawSlot(i);
            }
        }
        return false;
    }

    public void EmptyChickens() throws InterruptedException {
        ai.tick();

    }

    private void grindpigmen() throws InterruptedException {
        try {
            //Main.self.sendChat("Don't forget to turn on the deadman-switch-system!");

            testPlayerPresent();
            ai.tick();
            if (playerPresent) {
                System.out.println("player present");

                if (Main.self.getLocation().distanceTo(BACKUP_LOC) > 0.1) {
                    Main.self.sendChat("I can see you!");
                    tryAttack(home, home);
                    ai.tick(40);
                    tryAttack(home, home);
                    ai.tick();
                    ai.moveTo(BACKUP_LOC);

                    Main.self.sendChat("Let me make some space for you!");
                    System.out.println("backing up");

                }

            } else {
                if (Main.self.getLocation().distanceTo(home) > 0.1) {
                    ai.moveTo(home);
                    System.out.println("komme zurück");
                }
                grind();
            }

        } catch (InterruptedException ehmm) {
        }
    }

    static {
        TRASH_MATERIALS.add(Material.ROTTEN_FLESH);
        TRASH_MATERIALS.add(Material.GOLD_NUGGET);
        TRASH_MATERIALS.add(Material.GOLD_INGOT);
        TRASH_MATERIALS.add(Material.GOLDEN_SWORD);
        TRASH_MATERIALS.add(Material.CHICKEN);
        TRASH_MATERIALS.add(Material.FEATHER);
        Fuel.add(Material.DRIED_KELP_BLOCK);
    }
}
