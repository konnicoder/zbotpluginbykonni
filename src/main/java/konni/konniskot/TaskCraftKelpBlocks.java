/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.logging.Level;
import java.util.logging.Logger;
import zedly.zbot.BlockFace;
import zedly.zbot.Location;
import zedly.zbot.Material;

/**
 *
 * @author Konstantin
 */
public class TaskCraftKelpBlocks extends Task {

    private static final Location CRAFING_LOC = new Location(255, 137, -8599).centerHorizontally();
    private static final Location TESS_PRECRAFT_LOC = new Location(256, 137, -8599).centerHorizontally();
    private static final Location CRAFTINGBENCH = new Location(255, 137, -8600).centerHorizontally();
    private static final Location TESS_AFTERCRAFT_LOC = new Location(255, 137, -8598).centerHorizontally();

    private static final Location waypoint1 = new Location(229, 137, -8711).centerHorizontally();
    private static final Location waypoint2 = new Location(229, 137, -8631).centerHorizontally();

    private boolean verbose;
    private boolean runkelptask = true;
    private int tesseractfill;
    private int cycles;

    public TaskCraftKelpBlocks(int tesseractfilllevel, boolean verbose) {
        super(100);
        this.verbose = verbose;
        tesseractfill = tesseractfilllevel;
    }

    public void run() {
        System.out.println("starting TaskCraftKelpblocks");
        if (verbose) {
            System.out.println("verbose active");
        }
        getNumber(tesseractfill);
        try {
            if (Main.self.getLocation().distanceTo(CRAFING_LOC) > 20) {
                Main.self.sendChat("/home xp");

                ai.tick(5);
                ai.moveTo(waypoint1);
                ai.tick(5);
                ai.moveTo(waypoint2);
                ai.tick(5);
            }

            for (int i = cycles; i > 0; i--) {
                if (Main.self.getLocation().distanceTo(CRAFING_LOC) > 0.1) {
                    ai.tick();
                    ai.moveTo(CRAFING_LOC);
                    ai.tick();
                }
                KaiTools.CraftFullBlock(Material.DRIED_KELP, TESS_PRECRAFT_LOC, TESS_AFTERCRAFT_LOC, CRAFTINGBENCH, ai);
                ai.tick();
                System.out.println(i);
            }
            System.out.println("done");
        } catch (InterruptedException ex) {
        }
        unregister();
    }

    public void getNumber(int itemintesseract) {
        double number = itemintesseract;
        number = number / 9;
        int ergebnis = (int) number;
        cycles = ergebnis;
        System.out.println("cycles: " + cycles);
    }

    public void cancel() throws InterruptedException {
        runkelptask = false;
        System.out.println("stopped TaskCraftKelpblocks");
    }
}
