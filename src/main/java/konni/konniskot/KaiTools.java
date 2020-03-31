/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.function.Predicate;
import net.minecraft.server.NBTTagCompound;
import zedly.zbot.BlockFace;
import zedly.zbot.ConcurrentLinkedQueue;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.environment.Block;
import zedly.zbot.event.ChatEvent;
import zedly.zbot.event.SlotUpdateEvent;
import zedly.zbot.inventory.ItemStack;
import zedly.zbot.util.CartesianVector;
import zedly.zbot.util.SphericalVector;
import zedly.zbot.util.Vector;

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

    public static Block RayCast(Location origin, Vector aim, double radius, Predicate<Block> searchmethod) {
        double x = origin.getX();
        double y = origin.getY();
        double z = origin.getZ();

        aim = aim.normalize();
        double dx = aim.getX();
        double dy = aim.getY();
        double dz = aim.getZ();

        double stepX = Math.signum(dx);
        double stepY = Math.signum(dy);
        double stepZ = Math.signum(dz);

        double tMaxX = intbound(origin.getX(), dx);
        double tMaxY = intbound(origin.getY(), dy);
        double tMaxZ = intbound(origin.getZ(), dz);

        double tDeltaX = stepX / dx;
        double tDeltaY = stepY / dy;
        double tDeltaZ = stepZ / dz;

        if (dx == 0 && dy == 0 && dz == 0) {
            throw new IllegalArgumentException("Raycast in zero direction!");
        }

        radius = radius / Math.sqrt(dx * dx + dy * dy + dz * dz);
        ///HIER DER FEHLER
        Location testblocloc = new Location(x, y, z);
        Block testblock = Main.self.getEnvironment().getBlockAt(testblocloc);

        while (testblock.isLoaded()) {
            testblocloc = new Location(x, y, z);
            testblock = Main.self.getEnvironment().getBlockAt(testblocloc);
            if (searchmethod.test(testblock)) {
                return testblock;
            }
//            Location testloc = new Location(x, y, z);
//            if (Main.self.getEnvironment().getBlockAt(testloc).getType() != Material.AIR) {
//                Location goal = new Location(x, y, z).centerHorizontally();
//                Main.self.sendChat("block found at: " + goal);
//                Block foundblock = Main.self.getEnvironment().getBlockAt(goal);
//                Main.self.sendChat("" + foundblock);
//                return foundblock;
//            }
            if (tMaxX < tMaxY) {
                if (tMaxX < tMaxZ) {
                    if (tMaxX > radius) {
                        break;
                    }

                    x += stepX;

                    tMaxX += tDeltaX;

                } else {
                    if (tMaxZ > radius) {
                        break;
                    }
                    z += stepZ;
                    tMaxZ += tDeltaZ;

                }
            } else {
                if (tMaxY < tMaxZ) {
                    if (tMaxY > radius) {
                        break;
                    }
                    y += stepY;
                    tMaxY += tDeltaY;

                } else {

                    if (tMaxZ > radius) {
                        break;
                    }
                    z += stepZ;
                    tMaxZ += tDeltaZ;
                }
            }
            System.out.println(x + " " + y + " " + z);
        }
        return null;
    }

    public static boolean isVisible(Block block) {
        System.out.println("zielblock" + block);
        System.out.println("isvisible raycast" + RayCast(Main.self.getLocation(), block.getLocation(), 30, (b) -> {
            return b.getType() != Material.AIR && b.getType() != Material.CAVE_AIR;
        }));
        if (RayCast(Main.self.getLocation(), block.getLocation(), 30, (b) -> {
            return b.getType() != Material.AIR && b.getType() != Material.CAVE_AIR;
        }) == block) {
            return true;

        } else {
            return false;
        }

//        return RayCast(Main.self.getLocation(), block.getLocation(), 30, (b) -> {
//            return b.getType() != Material.AIR && b.getType() != Material.CAVE_AIR;
//        }) == block;
    }

    public static boolean isVis(Block block, Location loc) {
        System.out.println("zielblock" + loc);
        System.out.println("isvisible raycast" + RayCast(Main.self.getLocation(), loc, 30, (b) -> {
            return b.getType() != Material.AIR && b.getType() != Material.CAVE_AIR;
        }));
        if (RayCast(Main.self.getLocation(), loc, 30, (b) -> {
            return b.getType() != Material.AIR && b.getType() != Material.CAVE_AIR;
        }) == block) {
            return true;

        } else {
            return false;
        }

    }

    public static Block RayCast(Location origin, double radius, Predicate<Block> searchmethod) {

        SphericalVector aim = new SphericalVector(origin.getYaw() * Math.PI / 180.0, origin.getPitch() * Math.PI / 180.0, 1);
        return RayCast(origin, aim, radius, searchmethod);
    }

    public static Block RayCast(Location origin, Location dest, double radius, Predicate<Block> searchmethod) {
        Vector aim = origin.vectorTo(dest);

        return RayCast(origin, aim, radius, searchmethod);
    }

    private static double intbound(double s, double ds) {
        if (ds < 0) {
            return intbound(-s, -ds);
        } else {
            s = modFloatPositive(s, 1);
            return (1 - s) / ds;
        }

    }

    private static double modFloatPositive(double value, double modulus) {
        return modFloat((modFloat(value, modulus) + modulus), modulus);
    }

    private static double modFloat(double number, double modulus) {
        int intFraction = (int) (number / modulus);
        double remainder = number - intFraction * modulus;
        return remainder;
    }

    public static boolean doesRayColideWithBlock(Location origin, Vector aim, Location testblock) {

        double originX = origin.getX();
        double originY = origin.getY();
        double originZ = origin.getZ();

        double testblockX = testblock.getBlockX();
        double testblockY = testblock.getBlockY();
        double testblockZ = testblock.getBlockZ();

        double dx = aim.getX();
        double dy = aim.getY();
        double dz = aim.getZ();

        double tminX = 0;
        double tmaxX = 0;

        double tminY = 0;
        double tmaxY = 0;

        double tminZ = 0;
        double tmaxZ = 0;

        if (dx == 0) {
            if ((int) originX != (int) testblockX) {
                return false;
            }

        }
        if (dy == 0) {
            if ((int) originY != (int) testblockY) {
                return false;
            }

        }
        if (dz == 0) {
            if ((int) originZ != (int) testblockZ) {
                return false;
            }

        }

        tminX = (testblockX - originX) / dx;
        tmaxX = (testblockX + 1 - originX) / dx;
        if (tminX > tmaxX) {
            double tpminx = tminX;
            tminX = tmaxX;
            tmaxX = tpminx;
        }

        tminY = (testblockY - originY) / dy;
        tmaxY = (testblockY + 1 - originY) / dy;
        if (tminY > tmaxY) {
            double tpminy = tminY;
            tminY = tmaxY;
            tmaxY = tpminy;
        }

        tminZ = (testblockZ - originZ) / dz;
        tmaxZ = (testblockZ + 1 - originZ) / dz;
        if (tminZ > tmaxZ) {
            double tpminz = tminZ;
            tminZ = tmaxZ;
            tmaxZ = tpminz;
        }
        double tmin = Math.max(Math.max(tminX, tminY), tminZ);

        double tmax = Math.min(Math.max(tmaxX, tmaxY), tmaxZ);

        return tmax > 0 && tmin < tmax;

    }

    public static Block rayCastNew(Location origin, Vector aim, double radius, Predicate<Block> searchmethod) {
        Block miau = Main.self.getEnvironment().getBlockAt(BFSScan(
                (loc) -> {
                    return searchmethod.test(Main.self.getEnvironment().getBlockAt(loc));
                },
                (loc) -> {
                    return doesRayColideWithBlock(origin, aim, loc);
                },
                origin.getBlockX(), origin.getBlockY(), origin.getBlockZ(), (int) radius));

        return miau;
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

    public static Location BFSScan(Predicate<Location> pred, Predicate<Location> searchPred, int x, int y, int z, int viewrange) {
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
                if (!searchPred.test(relatives)) {
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

    public static Location BFSScan(Predicate<Location> pred, int x, int y, int z, int viewrange) {
        return BFSScan(pred, (b) -> {
            return true;
        }, x, y, z, viewrange);

    }

    public static boolean testStackAvaliable(Material mat, int slot) {
        if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == mat) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean lookInInventoryAndMove(Material mat, int targetslot, BlockingAI ai) throws InterruptedException {
        for (int slot = 9; slot < 44; slot++) {
            if (slot == targetslot) {
                continue;
            }
            if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == mat) {
                ai.swapItems(slot, targetslot);
                return true;
            }
        }
        return false;
    }

    public static boolean lookInDoubleChestAndMove(Material mat, int targetslot, BlockingAI ai) throws InterruptedException {
        int goalslot = targetslot + 45;
        for (int slot = 0; slot < 53; slot++) {
            if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == mat) {
                ai.transferItem(slot,goalslot);
                ai.tick();
                return true;
            }
        }
        return false;
    }
    
     public static boolean checkToolFullHealth() {
        ItemStack is = Main.self.getInventory().getItemInHand();
        if (is.getNbt() instanceof NBTTagCompound) {
            NBTTagCompound nbt = (NBTTagCompound) is.getNbt();
            int damage = nbt.getInteger("Damage");
            
            if (damage < 10) {
                return true;
            }
        }
        return false;
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
