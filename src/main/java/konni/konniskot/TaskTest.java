/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import zedly.zbot.Location;
import zedly.zbot.Material;

/**
 *
 * @author Konstantin
 */
public class TaskTest extends Task {

    private static final Location POWDER_CRAFTING_LOC = new Location(214, 138, -8664).centerHorizontally();
    private static final Location POWDER_CRAFTING_WALK = new Location(214, 137, -8666).centerHorizontally();

    private static final HashSet<Material> powder = new HashSet<>();

    private static final Location POWDER_CHEST_LOC = new Location(216, 137, -8676).centerHorizontally();
    private static final Location POWDER_CHEST_WALK = new Location(216, 137, -8674).centerHorizontally();

    private static final Location GRAVEL_TESSERACT_LOC = new Location(215, 138, -8665).centerHorizontally();
    private static final Location GRAVEL_TESSERACT_WALK = new Location(215, 137, -8666).centerHorizontally();
    private static final Location SAND_TESSERACT_LOC = new Location(216, 138, -8665).centerHorizontally();
    private static final Location SAND_TESSERACT_WALK = new Location(216, 137, -8666).centerHorizontally();
    private static final Location DYE_CHEST_WALK = new Location(218, 137, -8674).centerHorizontally();
    private static final Location DYE_CHEST_LOC = new Location(218, 137, -8676).centerHorizontally();
   
    

    public TaskTest() {
        super(100);

    }

    public void run() {
        try {
            //craftConcretePowder(Material.BLACK_DYE, Material.BLACK_CONCRETE_POWDER);
            getCraftingMaterials(Material.BLACK_DYE);
            ai.tick();
            craftConcretePowder(Material.BLACK_DYE, Material.BLACK_CONCRETE_POWDER);
            ai.tick();
        } catch (InterruptedException ex) {

        }

    }

    public void getCraftingMaterials(Material dye) throws InterruptedException {

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

        ai.moveTo(DYE_CHEST_WALK);
        ai.tick();
        ai.openContainer(DYE_CHEST_LOC);

        for (int slot = 0; slot <= 53; slot++) {
            if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == dye) {
                ai.withdrawSlot(slot);
                slot = 54;
            }
            if (slot == 53 && InventoryUtil.countFullStacks(dye, 54, 89) == 0) {
                Main.self.sendChat("is the chest empty?");
            }
        }
        ai.tick();
        ai.closeContainer();

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
            ai.tick(4);

        }
        ai.tick(5);
        Main.self.getInventory().click(0, 1, 0);
        ai.tick();
        ai.closeContainer();
        Main.self.sendChat("done");
        ai.moveTo(POWDER_CHEST_WALK);
        ai.tick();
        ai.openContainer(POWDER_CHEST_LOC);
        for (int slot = 54; slot <= 89; slot++) {
            if (Main.self.getInventory().getSlot(slot) != null
                    && Main.self.getInventory().getSlot(slot).getType() == powder) {
                ai.depositSlot(slot);
                ai.tick();
            }
        }
        ai.closeContainer();
    }

    static {
        powder.add(Material.BLACK_CONCRETE);
    }

}
