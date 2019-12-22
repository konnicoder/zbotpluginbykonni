/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import zedly.zbot.BlockFace;
import zedly.zbot.Location;
import zedly.zbot.Material;

/**
 *
 * @author Konstantin
 */
//24 by 24
public class TaskConcrete extends Task {

    private static final Location CONCRETE_FARM_LOC = new Location(210, 138, -8670).centerHorizontally();
    private static final Location CONCRETE_FARM_WALK = new Location(211, 137, -8670).centerHorizontally();

    private static final Location GRAVEL_TESSERACT_LOC = new Location(215, 138, -8665).centerHorizontally();
    private static final Location GRAVEL_TESSERACT_WALK = new Location(215, 137, -8666).centerHorizontally();
    private static final Location SAND_TESSERACT_LOC = new Location(216, 138, -8665).centerHorizontally();
    private static final Location SAND_TESSERACT_WALK = new Location(216, 137, -8666).centerHorizontally();
    private static final Location DYE_CHEST_WALK = new Location(218, 137, -8674).centerHorizontally();
    private static final Location DYE_CHEST_LOC = new Location(218, 137, -8676).centerHorizontally();
    private static final Location POWDER_CRAFTING_LOC = new Location(214, 138, -8664).centerHorizontally();
    private static final Location POWDER_CRAFTING_WALK = new Location(214, 137, -8666).centerHorizontally();

    private static final Location POWDER_CHEST_LOC = new Location(216, 137, -8676).centerHorizontally();
    private static final Location POWDER_CHEST_WALK = new Location(216, 137, -8674).centerHorizontally();
    private static Material colourpowder = null;
    private static Material colourdye = null;
    private static final HashSet<Material> powder = new HashSet<>();
    private static final HashSet<Material> dyes = new HashSet<>();
    private int neededpowder = 0;
    private int powderslot = 37;
    private int concretemined = 0;
    private int goal = 0;
    private String blockfarbe = null;
    private boolean abbruch = false;

    public TaskConcrete(String blocktype, int stacks) {
        super(100);
        goal = stacks;
        blockfarbe = blocktype;
    }

    public void run() {
        try {
            switch (blockfarbe) {
                case "black":
                    System.out.println("Material set to: " + blockfarbe);
                    colourpowder = Material.BLACK_CONCRETE_POWDER;
                    colourdye = Material.BLACK_DYE;
                    break;
                case "red":
                    System.out.println("Material set to: " + blockfarbe);
                    colourpowder = Material.RED_CONCRETE_POWDER;
                    colourdye = Material.RED_DYE;
                    break;
                case "green":
                    System.out.println("Material set to: " + blockfarbe);
                    colourpowder = Material.GREEN_CONCRETE_POWDER;
                    colourdye = Material.GREEN_DYE;
                    break;
                case "brown":
                    System.out.println("Material set to: " + blockfarbe);
                    colourpowder = Material.BROWN_CONCRETE_POWDER;
                    colourdye = Material.BROWN_DYE;
                    break;
                case "blue":
                    System.out.println("Material set to: " + blockfarbe);
                    colourpowder = Material.BLUE_CONCRETE_POWDER;
                    colourdye = Material.BLUE_DYE;
                    break;
                case "purple":
                    System.out.println("Material set to: " + blockfarbe);
                    colourpowder = Material.PURPLE_CONCRETE_POWDER;
                    colourdye = Material.PURPLE_DYE;
                    break;
                case "cyan":
                    System.out.println("Material set to: " + blockfarbe);
                    colourpowder = Material.CYAN_CONCRETE_POWDER;
                    colourdye = Material.CYAN_DYE;
                    break;
                case "light_gray":
                    System.out.println("Material set to: " + blockfarbe);
                    colourpowder = Material.LIGHT_GRAY_CONCRETE_POWDER;
                    colourdye = Material.LIGHT_GRAY_DYE;
                    break;
                case "gray":
                    System.out.println("Material set to: " + blockfarbe);
                    colourpowder = Material.GRAY_CONCRETE_POWDER;
                    colourdye = Material.GRAY_DYE;
                    break;
                case "pink":
                    System.out.println("Material set to: " + blockfarbe);
                    colourpowder = Material.PINK_CONCRETE_POWDER;
                    colourdye = Material.PINK_DYE;
                    break;
                case "lime":
                    System.out.println("Material set to: " + blockfarbe);
                    colourpowder = Material.LIME_CONCRETE_POWDER;
                    colourdye = Material.LIME_DYE;
                    break;
                case "yellow":
                    System.out.println("Material set to: " + blockfarbe);
                    colourpowder = Material.YELLOW_CONCRETE_POWDER;
                    colourdye = Material.YELLOW_DYE;
                    break;
                case "light_blue":
                    System.out.println("Material set to: " + blockfarbe);
                    colourpowder = Material.LIGHT_BLUE_CONCRETE_POWDER;
                    colourdye = Material.LIGHT_BLUE_DYE;
                    break;
                case "magenta":
                    System.out.println("Material set to: " + blockfarbe);
                    colourpowder = Material.MAGENTA_CONCRETE_POWDER;
                    colourdye = Material.MAGENTA_DYE;
                    break;
                case "orange":
                    System.out.println("Material set to: " + blockfarbe);
                    colourpowder = Material.ORANGE_CONCRETE_POWDER;
                    colourdye = Material.ORANGE_DYE;
                    break;
                case "white":
                    System.out.println("Material set to: " + blockfarbe);
                    colourpowder = Material.WHITE_CONCRETE_POWDER;
                    colourdye = Material.WHITE_DYE;
                    break;
            }
            System.out.println("Dye set to: "+colourdye);
            neededpowder = goal;
            while (concretemined < goal && abbruch == false) {
                Main.self.selectSlot(1);
                ai.tick(5);
                Main.self.selectSlot(0);
                if (Main.self.getLocation().distanceTo(CONCRETE_FARM_WALK) > 0.1) {
                    ai.moveTo(CONCRETE_FARM_WALK);
                }
                //System.out.println("neededpowder " + neededpowder);
                //System.out.println("concretemined " + concretemined);
                //System.out.println("goal " + goal);
                ai.tick();

                if (KaiTools.testStackAvaliable(colourpowder, powderslot)) {
                    while (KaiTools.testStackAvaliable(colourpowder, powderslot) && concretemined < goal) {

                        if (powder.contains(Main.self.getEnvironment().getBlockAt(CONCRETE_FARM_LOC).getType())) {
                            Main.self.selectSlot(0);
                            ai.breakBlock(CONCRETE_FARM_LOC, 400);
                            concretemined++;
                            System.out.println("concretemined " + concretemined);
                            neededpowder--;
                            ai.tick();
                            Main.self.selectSlot(1);
                            ai.tick();
                            if (concretemined != goal) {
                                Main.self.placeBlock(CONCRETE_FARM_LOC, BlockFace.UP);
                            }
                        } else {
                            if (Main.self.getEnvironment().getBlockAt(CONCRETE_FARM_LOC).getType() == Material.AIR) {
                                System.out.println("place block");
                                Main.self.selectSlot(1);
                                Main.self.placeBlock(CONCRETE_FARM_LOC, BlockFace.EAST);
                                ai.tick();
                            } else {
                                System.out.println("error at concrete location");
                            }
                        }
                        ai.tick(3);
                    }
                } else {
                    if (searchininventory()) {
                        System.out.println("weiter gehts");

                    } else {
                        System.out.println("get concrete");
                        getConcrete();
                        ai.tick();
                    }

                }
                ai.tick(3);
            }
            Main.self.sendChat("done");
            ai.moveTo(POWDER_CHEST_WALK);
            ai.openContainer(POWDER_CHEST_LOC);
            cleanafter();
            ai.tick();
            ai.closeContainer();

        } catch (InterruptedException ex) {
        }

    }

    public void cleanafter() throws InterruptedException {
        for (int slot = 54; slot <= 89; slot++) {
            if (Main.self.getInventory().getSlot(slot) != null
                    && powder.contains(Main.self.getInventory().getSlot(slot).getType())) {
                ai.depositSlot(slot);
                ai.tick();
            }
        }
    }

    public boolean searchininventory() throws InterruptedException {
        System.out.println("search inv");
        for (int slot = 9; slot <= 44; slot++) {
            if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == colourpowder) {
                ai.transferItem(slot, 37);
                System.out.println("item found and moved");
                return true;
            }

        }
        return false;
    }

    public void getCraftingMaterials(Material dye) throws InterruptedException {

        ai.moveTo(DYE_CHEST_WALK);
        ai.tick();
        ai.openContainer(DYE_CHEST_LOC);

        for (int slot = 0; slot <= 53; slot++) {
            if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == dye) {
                ai.withdrawSlot(slot);
                slot = 54;
            }
            if (slot >= 53 && InventoryUtil.countFullStacks(dye, 54, 89) == 0) {
                Main.self.sendChat("pls refill the dye chest with the correct dye u stupid fuck");
            }
        }
        ai.tick();
        ai.closeContainer();

        ai.moveTo(SAND_TESSERACT_WALK);
        ai.tick();
        while (InventoryUtil.countFullStacks(Material.SAND, 0, 44) < 4) {
            ai.clickBlock(SAND_TESSERACT_LOC);
            ai.tick();
        }
        ai.moveTo(GRAVEL_TESSERACT_WALK);
        ai.tick();

        //INVENTORY COUNT
        while (InventoryUtil.countFullStacks(Material.GRAVEL, 0, 44) < 4) {
            ai.clickBlock(GRAVEL_TESSERACT_LOC);
            ai.tick();
        }

    }

    public void craftConcretePowder(Material dye, Material powder) throws InterruptedException {

        if (Main.self.getLocation().distanceTo(POWDER_CRAFTING_WALK) > 0.1) {
            ai.moveTo(POWDER_CRAFTING_WALK);
        }
        ai.tick();
        ai.openContainer(POWDER_CRAFTING_LOC);
        int craftingslot = 1;
        while (craftingslot <= 9) {

            for (int slot = 10; slot <= 45; slot++) {

                if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == dye && craftingslot == 1 && Main.self.getInventory().getSlot(slot).getAmount() == 64) {
                    System.out.println("dye" + craftingslot);
                    ai.transferItem(slot, craftingslot);
                    ai.tick();

                    craftingslot++;
                }

                if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == Material.SAND && craftingslot >= 2 && craftingslot <= 5 && Main.self.getInventory().getSlot(slot).getAmount() == 64) {
                    System.out.println("sand" + craftingslot);
                    ai.transferItem(slot, craftingslot);
                    ai.tick();
                    craftingslot++;
                }
                if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == Material.GRAVEL && craftingslot >= 6 && craftingslot <= 9 && Main.self.getInventory().getSlot(slot).getAmount() == 64) {
                    System.out.println("gravel" + craftingslot);
                    ai.transferItem(slot, craftingslot);
                    ai.tick();
                    craftingslot++;
                }

            }
            ai.tick(2);

        }
        ai.tick(2);
        Main.self.getInventory().click(0, 1, 0);
        ai.tick();
        ai.closeContainer();
        Main.self.sendChat("done");
        ai.moveTo(POWDER_CHEST_WALK);
        ai.tick();
        ai.openContainer(POWDER_CHEST_LOC);
        ai.tick();
        cleanafter();
        ai.tick();
        ai.closeContainer();
        ai.tick();
    }

    public void getConcrete() throws InterruptedException {
        ai.moveTo(POWDER_CHEST_WALK);
        ai.tick();
        //ai.open

        while (neededpowder > 0 && InventoryUtil.countFreeStorageSlots(true, false) > 0 && abbruch == false) {
            System.out.println("needpowder" + neededpowder);
            ai.openContainer(POWDER_CHEST_LOC);
            for (int slot = 0; slot <= 53; slot++) {

                ai.tick();
                if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == colourpowder) {
                    System.out.println(slot + " concrete found");
                    neededpowder = neededpowder - Main.self.getInventory().getSlot(slot).getAmount();
                    ai.withdrawSlot(slot);
                    System.out.println("!!!!!!");
                    ai.tick();
                    if (slot >= 53) {
                        ai.closeContainer();
                    }
                }

                ai.tick();

                if (neededpowder > 0 && slot >= 53) {
                    ai.closeContainer();
                    Main.self.sendChat("No fitting concrete powder found, crafting new one...");
                    getCraftingMaterials(colourdye);
                    ai.tick();
                    craftConcretePowder(colourdye, colourpowder);
                }
            }
        }
        ai.tick();

    }

    static {
        powder.add(Material.BLACK_CONCRETE);
        powder.add(Material.BLACK_CONCRETE_POWDER);
        powder.add(Material.RED_CONCRETE);
        powder.add(Material.RED_CONCRETE_POWDER);
        powder.add(Material.GREEN_CONCRETE);
        powder.add(Material.GREEN_CONCRETE_POWDER);
        powder.add(Material.BLUE_CONCRETE);
        powder.add(Material.BLUE_CONCRETE_POWDER);
        powder.add(Material.PURPLE_CONCRETE);
        powder.add(Material.PURPLE_CONCRETE_POWDER);
        powder.add(Material.CYAN_CONCRETE);
        powder.add(Material.CYAN_CONCRETE_POWDER);
        powder.add(Material.LIGHT_GRAY_CONCRETE);
        powder.add(Material.LIGHT_GRAY_CONCRETE_POWDER);
        powder.add(Material.GRAY_CONCRETE);
        powder.add(Material.GRAY_CONCRETE_POWDER);
        powder.add(Material.PINK_CONCRETE);
        powder.add(Material.PINK_CONCRETE_POWDER);
        powder.add(Material.LIME_CONCRETE);
        powder.add(Material.LIME_CONCRETE_POWDER);
        powder.add(Material.YELLOW_CONCRETE);
        powder.add(Material.YELLOW_CONCRETE_POWDER);
        powder.add(Material.LIGHT_BLUE_CONCRETE);
        powder.add(Material.LIGHT_BLUE_CONCRETE_POWDER);
        powder.add(Material.MAGENTA_CONCRETE);
        powder.add(Material.MAGENTA_CONCRETE_POWDER);
        powder.add(Material.ORANGE_CONCRETE);
        powder.add(Material.ORANGE_CONCRETE_POWDER);
        powder.add(Material.WHITE_CONCRETE);
        powder.add(Material.WHITE_CONCRETE_POWDER);
        dyes.add(Material.BLACK_DYE);
        dyes.add(Material.RED_DYE);
    }
}
