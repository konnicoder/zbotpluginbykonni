/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.logging.Level;
import java.util.logging.Logger;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.event.SlotUpdateEvent;

/**
 *
 * @author Konstantin
 */
public class TaskCraftSeaLanterns extends Task {

    private static final Tesseract SHARD_TESS_LOC = new Tesseract(50, 42, -990);
    private static final Tesseract CRYSTAL_TESS_LOC = new Tesseract(49, 42, -989);
    private static final Tesseract LANTERN_TESS_LOC = new Tesseract(48, 42, -990);

    private static final Location CRAFTINGBENCH_LOC = new Location(47, 41, -990);
    private static final Location WALK_LOC = new Location(49, 41, -990).centerHorizontally();

    public TaskCraftSeaLanterns() {
        super(100);
    }

    public void run() {
        try {
            ai.moveTo(WALK_LOC);
            CraftSeaLantern(SHARD_TESS_LOC, CRYSTAL_TESS_LOC, LANTERN_TESS_LOC, CRAFTINGBENCH_LOC);
        } catch (InterruptedException ex) {

        }
    }

    public void CraftSeaLantern(Tesseract tessshards, Tesseract tesscrystals, Tesseract tessdone, Location craft) throws InterruptedException {
        ai.openContainer(craft);
        Main.self.recipeBookStatus(true, false, true, false, true, false, true, false);
        while (true) {

            int slotsToGet = 6 - InventoryUtil.countFullStacks(Material.PRISMARINE_SHARD, 9, 44);
            for (int i = 0; i < slotsToGet; i++) {
                Main.self.clickBlock(tessshards.getLocation());
            }
            int slotsToGetcrys = 7 - InventoryUtil.countFullStacks(Material.PRISMARINE_CRYSTALS, 9, 44);
            for (int i = 0; i < slotsToGetcrys; i++) {
                Main.self.clickBlock(tesscrystals.getLocation());
            }

            Main.self.requestRecipe("sea_lantern", true);
            ai.waitForEvent(SlotUpdateEvent.class, (e) -> {
                return e.getSlotId() == 0;
            }, 5000);
            Main.self.getInventory().click(0, 1, 0);
            KaiTools.fillTesseract(tessdone.getLocation());
            ai.tick();
        }
    }
}
