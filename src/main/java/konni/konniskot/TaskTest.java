/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import zedly.zbot.BlockFace;
import zedly.zbot.EntityType;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.entity.Entity;
import konni.konniskot.Main;

/**
 *
 * @author Konstantin
 */
public class TaskTest extends Task {

    private static final File test = new File(Main.instance.getDataFolder(), "test.txt");

    public TaskTest() {
        super(100);

    }

    public void run() {
        Main.self.sendChat("Doing a test!");
        writetofile();

    }

    private void writetofile() {
        String s = "hallo";
        try {
            FileOutputStream fos = new FileOutputStream(test);

            fos.write(s.getBytes());
            fos.write("\r\n".getBytes());

            fos.close();
        } catch (IOException ex) {
        }
    }
}
