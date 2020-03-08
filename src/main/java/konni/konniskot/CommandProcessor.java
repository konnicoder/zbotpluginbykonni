/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import konni.konniskot.BotEdit.TaskPyramid;
import konni.konniskot.BotEdit.TaskPyramidBFS;
import zedly.zbot.BlockFace;

/**
 *
 * @author Konstantin
 */
class CommandProcessor {

    private static TaskCraftGoldingots goldingotcraft;

    private static TaskCraftKelpBlocks craftkelpblocks;
    private static TaskGrindpigmen pigmentask;
    private static final File scrambleComplimentFile = new File(Main.instance.getDataFolder(), "compliments.txt");
    private static final ArrayList<String> compliments = new ArrayList<>();
    private static TaskGetSand taskgetsand;
    private String status = "-v";

    static void onCommand(String user, String command, boolean pm) throws Exception {
        String[] args = command.split(" ");
        loadComplimentsDictionary();

        switch (args[0]) {

            case "Testt":

                new TaskTest().start();

                break;

            case "hallo":
                Main.self.sendChat("Hallo " + user + ", nice to meet you!");
                break;

            case "mario":
                Main.self.sendChat("everything is tested");
                break;

            case "lil":
                Main.self.sendChat("give me dah weed!!");
                break;

            case "compliment":
                String comp = compliments.get(Main.rnd.nextInt(compliments.size()));
                Main.self.sendChat(comp);
                break;

        }
        if (!Main.admins.contains(user)) {
            return;

        }
        switch (args[0]) {
            case "floor":
                new TaskFloor().start();
                break;

            case "dig":
                if (args.length == 1) {
                    Main.self.sendChat("prefix dig x1 y1 z1 x2 y2 z2 clicks_per_slice sliceskip");
                }
                if (args.length == 9) {
                    int x1 = Integer.parseInt(args[1]);
                    int y1 = Integer.parseInt(args[2]);
                    int z1 = Integer.parseInt(args[3]);
                    int x2 = Integer.parseInt(args[4]);
                    int y2 = Integer.parseInt(args[5]);
                    int z2 = Integer.parseInt(args[6]);
                    int sh = Integer.parseInt(args[7]);
                    int sk = Integer.parseInt(args[8]);
                    new TaskDig(x1, y1, z1, x2, y2, z2, sh, sk).start();
                }

                break;

            case "strip":
                new TaskStripMine().start();
                break;

            case "pk":
                new TaskConvertPumpkins().start();
                break;

            case "mode":

                Main.self.sneak(true);
                Main.self.placeBlock(Main.self.getLocation(), BlockFace.EAST);
                Main.self.sneak(false);

                break;

            case "afk":
                Main.self.sendChat("/afk");
                break;

            case "skulls":
                new TaskWitherSkulls().start();
                break;

            case "bfspyr":
                if (args.length == 5) {
                    int x = Integer.parseInt(args[1]);
                    int y = Integer.parseInt(args[2]);
                    int z = Integer.parseInt(args[3]);
                    int size = Integer.parseInt(args[4]);
                    new TaskPyramidBFS(x, y, z, size).start();
                }
                break;

            case "hpyr":
                if (args.length == 5) {
                    int x = Integer.parseInt(args[1]);
                    int y = Integer.parseInt(args[2]);
                    int z = Integer.parseInt(args[3]);
                    int size = Integer.parseInt(args[4]);
                    new TaskPyramid(x, y, z, size).start();
                }
                break;

            case "get":
                if (args.length == 2) {

                    taskgetsand = new TaskGetSand();
                    taskgetsand.start();
                }
                break;

            case "stop":
                taskgetsand.cancel();
                break;

            case "kelp":
                if (args.length == 1) {
                    Main.self.sendChat("(prefix) kelp (amount in Tesseract) (verbose)");
                }
                if (args.length == 2) {
                    int x = Integer.parseInt(args[1]);
                    craftkelpblocks = new TaskCraftKelpBlocks(x, false);
                    craftkelpblocks.start();
                }
                if (args.length == 3) {
                    switch (args[2]) {
                        case "-v":
                            int x = Integer.parseInt(args[1]);
                            craftkelpblocks = new TaskCraftKelpBlocks(x, true);
                            craftkelpblocks.start();
                            break;
                    }
                }

                break;

            case "trash":
                new TaskTrashcan().start();
                break;

            case "xp":
                Main.self.sendChat("/home xp");
                break;

            case "test":
                new TaskFixfarm().start();
                break;

            case "grind":
                if (args.length == 1) {
                    pigmentask = new TaskGrindpigmen(false, false);
                    pigmentask.start();
                }
                if (args.length == 2) {
                    switch (args[1]) {
                        case "-v":
                            pigmentask = new TaskGrindpigmen(true, true);
                            pigmentask.start();
                            break;
                    }
                }
                break;

            case "stopgrind":
                if (pigmentask != null) {
                    pigmentask.cancel();
                    System.out.println("stopped TaskGrindpigmen");
                } else {
                    System.out.println("Task läuft nicht");
                }
                break;

            case "midas":
                if (args.length == 1) {
                    Main.self.sendChat("grind, craft_gold, craft_goldblocks");
                }
                if (args.length == 2) {
                    switch (args[1]) {
                        case "grind":
                            new TaskMidas(1).start();
                            break;
                        case "craft_gold":
                            new TaskMidas(2).start();
                            break;
                        case "craft_goldblocks":
                            new TaskMidas(3).start();
                            break;
                    }
                }
                break;

            case "extract_books":
                new TaskExtractBooks().start();
                break;
                
            case "count":
                new TaskCountGold().start();
                break;

            case "craft_gold":
                new TaskCraftGoldingots(false).start();
                break;

            case "craft_gold_stop":
                if (goldingotcraft != null) {
                    goldingotcraft.cancel();
                    System.out.println("stopped TaskGoldcraften");
                } else {
                    System.out.println("Task läuft nicht");
                }
                break;

            case "craft_goldblocks":
                new TaskCraftGoldblocks().start();
                break;

            case "inventory":
                new TaskCheckInventory().start();
                break;

            case "concrete":
                if (args.length == 1) {
                    Main.self.sendChat("(prefix) concrete (colour) (amount)");
                }
                if (args.length == 3) {
                    int stacks = Integer.parseInt(args[2]);
                    String block = args[1];
                    new TaskConcrete(block, stacks).start();
                }
                break;
        }
    }

    private static void loadComplimentsDictionary() {
        try (BufferedReader br = new BufferedReader(new FileReader(scrambleComplimentFile))) {
            String t;
            while ((t = br.readLine()) != null) {
                compliments.add(t);
            }
            br.close();
        } catch (IOException ex) {
        }
    }
}
