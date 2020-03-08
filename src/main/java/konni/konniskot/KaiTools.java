/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.function.Predicate;
import zedly.zbot.BlockFace;
import zedly.zbot.ConcurrentLinkedQueue;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.event.ChatEvent;
import zedly.zbot.event.SlotUpdateEvent;

/**
 *
 * @author Konstantin
 */
public class KaiTools {

    public static Location Scan(Material searchmat, int x, int y, int z, int yscanrange, int searchrange) {

        int ScanAnker1X = x + 1;
        int ScanAnker1Y = y + yscanrange;
        int ScanAnker1Z = z - 1;

        int ScanAnker2X = x - 1;
        int ScanAnker2Y = y - yscanrange;
        int ScanAnker2Z = z + 1;

        int durchsuchte_bloecke = 0;
        for (int shelllevel = 1; shelllevel <= searchrange; shelllevel++) {

            for (int X = ScanAnker1X; X >= ScanAnker2X; X--) {
                for (int Z = ScanAnker1Z; Z <= ScanAnker2Z; Z++) {
                    for (int Y = ScanAnker1Y; Y >= ScanAnker2Y; Y--) {
                        durchsuchte_bloecke++;
                        if (Main.self.getEnvironment().getBlockAt(X, Y, Z).getType() == Material.BIRCH_PLANKS) {
                            break;
                        }
                        if (Main.self.getEnvironment().getBlockAt(X, Y, Z).getType() == searchmat) {
                            return new Location(X, Y, Z);
                        }

                    }
                }
            }
            ScanAnker1X++;
            ScanAnker1Z--;

            ScanAnker2X--;
            ScanAnker2Z++;
        }
        System.out.println("shell zuende");
        System.out.println("blöcke durchsucht: " + durchsuchte_bloecke);

        return null;
    }

    public static Location ScanArea(Material searchmat, int x1, int y1, int z1, int x2, int y2, int z2) {

        int durchsuchte_bloecke = 0;
        for (int yachse = y1; yachse > y2; yachse--) {
            for (int xachse = x1; xachse > x2; xachse--) {
                for (int zachse = z1; zachse < z2; zachse++) {
                    if (Main.self.getEnvironment().getBlockAt(x2, y2, z2).getType() == searchmat) {

                        return new Location(xachse, yachse, zachse);
                    }
                }
            }
        }

        System.out.println("blöcke durchsucht: " + durchsuchte_bloecke);

        return null;
    }

    public static boolean modeSelect(String mode, BlockingAI ai) throws InterruptedException {
        while (true) {
            Main.self.sneak(true);
            Main.self.placeBlock(Main.self.getLocation(), BlockFace.EAST);
            Main.self.sneak(false);
            ChatEvent e = ai.waitForEvent(ChatEvent.class, 5000);
            if (e == null) {
                System.out.println("Error in select mode");
                return false;
            }
            if (e.getMessage().matches("^(3x Wide|3x Long|3x Tall|Ore|1x Normal) Mode$")) {
                if (e.getMessage().toLowerCase().contains(mode.toLowerCase())) {
                    return true;
                }
            }

        }
    }

    public static boolean checkIfBlocksAbove(int x, int y, int z, int maxsearchheight) {
        for (int Y = y; y <= maxsearchheight; y++) {
            Location scanloc = new Location(x, Y, z);
            if (Main.self.getEnvironment().getBlockAt(scanloc).getType() != Material.AIR) {
                return true;
            }
        }
        return false;
    }

    public static void CraftFullBlock(Material mat, Location tessvor, Location tessdone, Location craft, BlockingAI ai) throws InterruptedException {

        while (InventoryUtil.countFullStacks(mat, 9, 44) < 9) {
            Main.self.clickBlock(tessvor);
            ai.tick();
        }

        ai.openContainer(craft);
        int staticOffset = Main.self.getInventory().getStaticOffset();
        for (int crafting = 1; crafting <= 9; crafting++) {
            for (int local = staticOffset; local < staticOffset + 36; local++) {
                if (Main.self.getInventory().getSlot(local) != null
                        && Main.self.getInventory().getSlot(local).getType() == mat) {
                    ai.transferItem(local, crafting);
                    //System.out.println("MIAUUUU " + local + " " + crafting);
                    //ai.tick(1);
                    break;
                }
            }
        }
        Main.self.getInventory().click(0, 1, 0);
        ai.closeContainer();
        fillTesseract(tessdone);
    }

    public static void CraftFullBlockSpeed(Material mat, Location tessvor, Location tessdone, Location craft, BlockingAI ai) throws InterruptedException {
        while (InventoryUtil.countFullStacks(mat, 9, 44) < 9) {
            Main.self.clickBlock(tessvor);
            ai.tick();
        }

        ai.openContainer(craft);
        int staticOffset = Main.self.getInventory().getStaticOffset();
        for (int crafting = 1; crafting <= 9; crafting++) {
            for (int local = staticOffset; local < staticOffset + 36; local++) {
                if (Main.self.getInventory().getSlot(local) != null
                        && Main.self.getInventory().getSlot(local).getType() == mat) {
                    ai.transferItem(local, crafting);
                    Main.self.clickBlock(tessvor);

                    break;
                }
            }
        }
        Main.self.getInventory().click(0, 1, 0);
        ai.closeContainer();
        fillTesseract(tessdone);
    }

    public static Location BFSScan(Predicate<Location> pred, int x, int y, int z, int viewrange) {
        HashSet<Location> searched_blocks = new HashSet<>();
        final BlockFace[] searchdirections = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};

        LinkedList<Location> scanborder = new LinkedList<>();

        Location origin = new Location(x, y, z);
        searched_blocks.add(origin);
        scanborder.add(origin);

        while (!scanborder.isEmpty()) {
            Location loc = scanborder.removeFirst();

            if (pred.test(loc)) {
                return loc;
            }
            for (BlockFace face : searchdirections) {
                Location relatives = loc.getRelative(face.getDirection());
                if (searched_blocks.contains(relatives)) {
                    continue;
                }
                if (relatives.distanceTo(origin) > viewrange) {
                    continue;
                }
                searched_blocks.add(relatives);
                scanborder.add(relatives);
            }

        }
        return null;
    }

    public static Location BFSScan(Material mat, int x, int y, int z, int viewrange) {
        return BFSScan((loc) -> {
            return Main.self.getEnvironment().getBlockAt(loc).getType() == mat;
        }, x, y, z, viewrange);
    }

    public static boolean testStackAvaliable(Material mat, int slot) {
        if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == mat) {
            return true;
        } else {
            return false;
        }
    }

    public static void CraftFullBlockSuper(String recipeId, Material matvor, Tesseract tessvor, Tesseract tessdone, Location craft, BlockingAI ai) throws InterruptedException {
        ai.openContainer(craft);
        Main.self.recipeBookStatus(true, false, true, false, true, false, true, false);
        while (tessvor.getAmount() > 9 * 64) {
            int slotsToGet = 11 - InventoryUtil.countFullStacks(matvor, 9, 44);
            for (int i = 0; i < slotsToGet; i++) {
                Main.self.clickBlock(tessvor.getLocation());
            }
            Main.self.requestRecipe(recipeId, true);
            ai.waitForEvent(SlotUpdateEvent.class, (e) -> {
                return e.getSlotId() == 0;
            }, 5000);
            Main.self.getInventory().click(0, 1, 0);
            fillTesseract(tessdone.getLocation());
            ai.tick();
        }
    }

    public static void fillTesseract(Location TessLoc) {
        Main.self.placeBlock(TessLoc, BlockFace.EAST);
        Main.self.placeBlock(TessLoc, BlockFace.EAST);
    }

}
