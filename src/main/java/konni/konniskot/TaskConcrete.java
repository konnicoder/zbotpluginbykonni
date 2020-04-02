/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.NBTTagCompound;
import zedly.zbot.BlockFace;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.inventory.ItemStack;

/**
 *
 * @author Konstantin
 */
//24 by 24
public class TaskConcrete extends Task {

    private static final Location CONCRETE_FARM_LOC = new Location(209, 138, -8667).centerHorizontally();
    private static final Location CONCRETE_FARM_WALK = new Location(210, 137, -8667).centerHorizontally();

    private static final Location GRAVEL_TESSERACT_LOC = new Location(215, 138, -8665).centerHorizontally();
    private static final Location GRAVEL_TESSERACT_WALK = new Location(215, 137, -8666).centerHorizontally();
    private static final Location SAND_TESSERACT_LOC = new Location(216, 138, -8665).centerHorizontally();
    private static final Location SAND_TESSERACT_WALK = new Location(216, 137, -8666).centerHorizontally();

    private static final Location DYE_CHEST_WALK = new Location(217, 137, -8665).centerHorizontally();
    private static final Location DYE_CHEST_LOC = new Location(217, 137, -8664).centerHorizontally();

    private static final Location POWDER_CRAFTING_LOC = new Location(214, 138, -8664).centerHorizontally();
    private static final Location POWDER_CRAFTING_WALK = new Location(214, 137, -8666).centerHorizontally();

    private static final Location POWDER_CHEST_LOC = new Location(219, 137, -8664).centerHorizontally();
    private static final Location POWDER_CHEST_WALK = new Location(219, 137, -8665).centerHorizontally();

    private static final Location CONCRETE_CHEST_LOC = new Location(209, 137, -8670).centerHorizontally();
    private static final Location CONCRETE_CHEST_WALK = new Location(210, 137, -8670).centerHorizontally();

    private static final Location XP_FARM_LOC = new Location(295, 137, -8705).centerHorizontally();

    private static final Location TRASH_CHEST_LOC = new Location(298, 137, -8701).centerHorizontally();
    private static final Location TRASH_CHEST_WALK = new Location(299, 137, -8701).centerHorizontally();
    private static final HashSet<Material> TRASH_MATERIALS = new HashSet<>();

    private static Material colourpowder = null;
    private static Material colourdye = null;

    private static final HashSet<Material> powder = new HashSet<>();
    private static final HashSet<Material> dyes = new HashSet<>();
    private static final HashSet<Material> concrete = new HashSet<>();
    private int neededpowder = 0;
    private int powderslot = 37;
    private int concretemined = 0;
    private int goal = 0;
    private String blockcolour = null;
    private boolean abbruch = false;

    public TaskConcrete(String blocktype, int stacks) {
        super(100);
        goal = stacks * 64;
        blockcolour = blocktype;
    }

    public void run() {

        selectColour();
        try {

            System.out.println("Dye set to: " + colourdye);
            System.out.println("Powder selected: " + colourpowder);
            neededpowder = goal;

            walkToWork();
            
            ai.tick();
            Main.self.selectSlot(0);

            while (concretemined < neededpowder && checkToolHealth() == true && abbruch == false) {
                restock();
                while (KaiTools.testStackAvaliable(colourpowder, powderslot) == true && concretemined < neededpowder) {
                    mineConcrete();
                    System.out.println(concretemined);
                }
                cleanUp();
                if (checkToolHealth() == false) {
                    Main.self.sendChat("my tool needs repairing, sir");
                    healTool();
                    ai.tick();
                    dumpTrash();
                    ai.tick();
                    walkToWork();
                }
            }

            ai.tick();
        } catch (InterruptedException ex) {
        }
        if (concretemined >= neededpowder) {
            Main.self.sendChat("done");
        } else {
            Main.self.sendChat("Task cancelled");
        }
        unregister();
    }

    public boolean restock() throws InterruptedException {
        if (KaiTools.testStackAvaliable(colourpowder, powderslot) == false) {
            if (KaiTools.lookInInventoryAndMove(colourpowder, powderslot, ai) == false) {
                if (getPowderFromChest() == false) {
                    Main.self.sendChat("can`t find a fitting powder, is the chest empty?");
                    abbruch = true;
                    return false;
                }
            }
        }
        return true;
    }

//checks if powder is in definded powderchest and if so, gets 1 stack
    public boolean getPowderFromChest() throws InterruptedException {
        ai.moveTo(POWDER_CHEST_WALK);
        ai.tick();
        ai.openContainer(POWDER_CHEST_LOC);
        // if no suitable powder is in chest, returns false
        if (KaiTools.lookInDoubleChestAndMove(colourpowder, powderslot, ai) == false) {
            ai.closeContainer();
            return false;
        }
        ai.closeContainer();
        return true;
    }

    public void walkToWork() throws InterruptedException {
        if (Main.self.getLocation().distanceTo(CONCRETE_FARM_WALK) > 20) {
            Main.self.sendChat("/home xp");
            ai.tick(10);
            ai.navigateTo(CONCRETE_FARM_WALK);
        } else {
            ai.moveTo(CONCRETE_FARM_WALK);
        }

    }

    public void cleanUp() throws InterruptedException {
        ai.moveTo(POWDER_CHEST_WALK);
        ai.openContainer(POWDER_CHEST_LOC);
        for (int slot = 54; slot < 89; slot++) {
            if (Main.self.getInventory().getSlot(slot) == null) {
                continue;
            }
            if (powder.contains(Main.self.getInventory().getSlot(slot).getType())) {
                ai.depositSlot(slot);
            }
        }
        ai.closeContainer();
        ai.tick();

        ai.moveTo(DYE_CHEST_WALK);
        ai.openContainer(DYE_CHEST_LOC);
        for (int slot = 54; slot < 89; slot++) {
            if (Main.self.getInventory().getSlot(slot) == null) {
                continue;
            }
            if (dyes.contains(Main.self.getInventory().getSlot(slot).getType())) {
                ai.depositSlot(slot);
            }
        }
        ai.closeContainer();

        ai.tick();

        ai.moveTo(CONCRETE_CHEST_WALK);
        ai.openContainer(CONCRETE_CHEST_LOC);
        for (int slot = 54; slot < 89; slot++) {
            if (Main.self.getInventory().getSlot(slot) == null) {
                continue;
            }
            if (concrete.contains(Main.self.getInventory().getSlot(slot).getType())) {
                ai.depositSlot(slot);

            }
        }
        ai.closeContainer();

    }

    public boolean checkToolHealth() {

        ItemStack is = Main.self.getInventory().getItemInHand();
        if (is.getNbt() instanceof NBTTagCompound) {
            NBTTagCompound nbt = (NBTTagCompound) is.getNbt();
            int damage = nbt.getInteger("Damage");
            System.out.println("tooldamage:" + damage);

            if (damage < 1400) {
                return true;
            }
        }
        return false;
    }

    public void healTool() throws InterruptedException {
        Main.self.sendChat("/home xp");
        ai.tick(5);
        ai.moveTo(XP_FARM_LOC);
        ai.tick(3);
        Main.self.selectSlot(0);
        while (KaiTools.checkToolFullHealth() == false) {
            if (Main.self.getLocation().distanceTo(XP_FARM_LOC) > 0.1) {
                ai.moveTo(XP_FARM_LOC);
            }
            ai.tick(10);
        }

    }

    private boolean dumpTrash() throws InterruptedException {
        ai.tick();
        ai.moveTo(TRASH_CHEST_WALK);
        ai.tick(3);
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

    public void selectColour() {
        switch (blockcolour) {
            case "black":
                System.out.println("Material set to: " + blockcolour);
                colourpowder = Material.BLACK_CONCRETE_POWDER;
                colourdye = Material.BLACK_DYE;
                break;
            case "red":
                System.out.println("Material set to: " + blockcolour);
                colourpowder = Material.RED_CONCRETE_POWDER;
                colourdye = Material.RED_DYE;
                break;
            case "green":
                System.out.println("Material set to: " + blockcolour);
                colourpowder = Material.GREEN_CONCRETE_POWDER;
                colourdye = Material.GREEN_DYE;
                break;
            case "brown":
                System.out.println("Material set to: " + blockcolour);
                colourpowder = Material.BROWN_CONCRETE_POWDER;
                colourdye = Material.BROWN_DYE;
                break;
            case "blue":
                System.out.println("Material set to: " + blockcolour);
                colourpowder = Material.BLUE_CONCRETE_POWDER;
                colourdye = Material.BLUE_DYE;
                break;
            case "purple":
                System.out.println("Material set to: " + blockcolour);
                colourpowder = Material.PURPLE_CONCRETE_POWDER;
                colourdye = Material.PURPLE_DYE;
                break;
            case "cyan":
                System.out.println("Material set to: " + blockcolour);
                colourpowder = Material.CYAN_CONCRETE_POWDER;
                colourdye = Material.CYAN_DYE;
                break;
            case "light_gray":
                System.out.println("Material set to: " + blockcolour);
                colourpowder = Material.LIGHT_GRAY_CONCRETE_POWDER;
                colourdye = Material.LIGHT_GRAY_DYE;
                break;
            case "gray":
                System.out.println("Material set to: " + blockcolour);
                colourpowder = Material.GRAY_CONCRETE_POWDER;
                colourdye = Material.GRAY_DYE;
                break;
            case "pink":
                System.out.println("Material set to: " + blockcolour);
                colourpowder = Material.PINK_CONCRETE_POWDER;
                colourdye = Material.PINK_DYE;
                break;
            case "lime":
                System.out.println("Material set to: " + blockcolour);
                colourpowder = Material.LIME_CONCRETE_POWDER;
                colourdye = Material.LIME_DYE;
                break;
            case "yellow":
                System.out.println("Material set to: " + blockcolour);
                colourpowder = Material.YELLOW_CONCRETE_POWDER;
                colourdye = Material.YELLOW_DYE;
                break;
            case "light_blue":
                System.out.println("Material set to: " + blockcolour);
                colourpowder = Material.LIGHT_BLUE_CONCRETE_POWDER;
                colourdye = Material.LIGHT_BLUE_DYE;
                break;
            case "magenta":
                System.out.println("Material set to: " + blockcolour);
                colourpowder = Material.MAGENTA_CONCRETE_POWDER;
                colourdye = Material.MAGENTA_DYE;
                break;
            case "orange":
                System.out.println("Material set to: " + blockcolour);
                colourpowder = Material.ORANGE_CONCRETE_POWDER;
                colourdye = Material.ORANGE_DYE;
                break;
            case "white":
                System.out.println("Material set to: " + blockcolour);
                colourpowder = Material.WHITE_CONCRETE_POWDER;
                colourdye = Material.WHITE_DYE;
                break;
        }

    }

    public void mineConcrete() throws InterruptedException {
        if (Main.self.getLocation().distanceTo(CONCRETE_FARM_WALK) > 0.1) {
            ai.moveTo(CONCRETE_FARM_WALK);
            ai.tick();
        }
        Main.self.selectSlot(1);
        Main.self.placeBlock(CONCRETE_FARM_LOC, BlockFace.UP);
        ai.tick();
        Main.self.selectSlot(0);
        ai.breakBlock(CONCRETE_FARM_LOC, 200);
        concretemined++;
    }

    static {
        concrete.add(Material.BLACK_CONCRETE);
        powder.add(Material.BLACK_CONCRETE_POWDER);
        concrete.add(Material.RED_CONCRETE);
        powder.add(Material.RED_CONCRETE_POWDER);
        concrete.add(Material.GREEN_CONCRETE);
        powder.add(Material.GREEN_CONCRETE_POWDER);
        concrete.add(Material.BLUE_CONCRETE);
        powder.add(Material.BLUE_CONCRETE_POWDER);
        concrete.add(Material.PURPLE_CONCRETE);
        powder.add(Material.PURPLE_CONCRETE_POWDER);
        concrete.add(Material.CYAN_CONCRETE);
        powder.add(Material.CYAN_CONCRETE_POWDER);
        concrete.add(Material.LIGHT_GRAY_CONCRETE);
        powder.add(Material.LIGHT_GRAY_CONCRETE_POWDER);
        concrete.add(Material.GRAY_CONCRETE);
        powder.add(Material.GRAY_CONCRETE_POWDER);
        concrete.add(Material.PINK_CONCRETE);
        powder.add(Material.PINK_CONCRETE_POWDER);
        concrete.add(Material.LIME_CONCRETE);
        powder.add(Material.LIME_CONCRETE_POWDER);
        concrete.add(Material.YELLOW_CONCRETE);
        powder.add(Material.YELLOW_CONCRETE_POWDER);
        concrete.add(Material.LIGHT_BLUE_CONCRETE);
        powder.add(Material.LIGHT_BLUE_CONCRETE_POWDER);
        concrete.add(Material.MAGENTA_CONCRETE);
        powder.add(Material.MAGENTA_CONCRETE_POWDER);
        concrete.add(Material.ORANGE_CONCRETE);
        powder.add(Material.ORANGE_CONCRETE_POWDER);
        concrete.add(Material.WHITE_CONCRETE);
        powder.add(Material.WHITE_CONCRETE_POWDER);
        dyes.add(Material.BLACK_DYE);
        dyes.add(Material.RED_DYE);
        dyes.add(Material.GREEN_DYE);
        dyes.add(Material.BLUE_DYE);
        dyes.add(Material.PURPLE_DYE);
        dyes.add(Material.CYAN_DYE);
        dyes.add(Material.LIGHT_GRAY_DYE);
        dyes.add(Material.GRAY_DYE);
        dyes.add(Material.PINK_DYE);
        dyes.add(Material.LIME_DYE);
        dyes.add(Material.YELLOW_DYE);
        dyes.add(Material.LIGHT_BLUE_DYE);
        dyes.add(Material.MAGENTA_DYE);
        dyes.add(Material.ORANGE_DYE);
        dyes.add(Material.WHITE_DYE);
        TRASH_MATERIALS.add(Material.ROTTEN_FLESH);
        TRASH_MATERIALS.add(Material.GOLD_NUGGET);
        TRASH_MATERIALS.add(Material.GOLD_INGOT);
        TRASH_MATERIALS.add(Material.GOLDEN_SWORD);
        TRASH_MATERIALS.add(Material.CHICKEN);
        TRASH_MATERIALS.add(Material.FEATHER);
    }
}
