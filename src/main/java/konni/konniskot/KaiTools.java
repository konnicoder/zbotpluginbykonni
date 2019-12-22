/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import zedly.zbot.Location;
import zedly.zbot.Material;

/**
 *
 * @author Konstantin
 */
public class KaiTools {

    public static Location Scan(Material searchmat, int x, int y, int z, int yscanrange, int searchrange) {
        int CenterPointX = x;
        int CenterPointY = y;
        int CenterPointZ = z;

        int ScanAnker1X = x + 1;
        int ScanAnker1Y = y + yscanrange;
        int ScanAnker1Z = z - 1;

        int ScanAnker2X = x - 1;
        int ScanAnker2Y = y - yscanrange;
        int ScanAnker2Z = z + 1;

        int durchsuchte_blöcke = 0;
        for (int shelllevel = 1; shelllevel <= searchrange; shelllevel++) {

            for (int X = ScanAnker1X; X >= ScanAnker2X; X--) {
                for (int Z = ScanAnker1Z; Z <= ScanAnker2Z; Z++) {
                    for (int Y = ScanAnker1Y; Y >= ScanAnker2Y; Y--) {
                        durchsuchte_blöcke++;
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
        System.out.println("blöcke durchsucht: " + durchsuchte_blöcke);

        return null;
    }

    public static boolean testStackAvaliable(Material mat, int slot) {
        if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == mat) {
            return true;
        } else {
            return false;
        }
    }

}
