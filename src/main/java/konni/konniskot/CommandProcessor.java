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

/**
 *
 * @author Konstantin
 */
class CommandProcessor {

    private static TaskCraftGoldingots goldingotcraft;
    private static TaskCraftGoldblocks goldblockcraft;
    private static TaskCraftKelpBlocks craftkelpblocks;
    private static TaskGrindpigmen pigmentask;
    private static TaskShulkerleeren shulkerrun;
    private static TaskGoldfarm goldfarm;
    private static TaskDailyRoutine dailyroutine;
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
                Main.self.sendChat("give me dah weed!");
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

            case "afk":
                Main.self.sendChat("/afk");
                break;

            case "trench":
                new TaskDigTrench().start();
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

            case "readsign":
                new TaskReadSign().start();
                break;

            case "1":
                Main.self.sendChat("1");
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

            case "daily_routine":
                dailyroutine = new TaskDailyRoutine();
                dailyroutine.start();
                break;

            case "daily_routine_stop":
                dailyroutine.cancel();
                break;

            case "empty_shulkers":
                shulkerrun = new TaskShulkerleeren();
                shulkerrun.start();
                break;

            case "extract_books":
                new TaskExtractBooks().start();
                break;

            case "gold":
                goldfarm = new TaskGoldfarm();
                goldfarm.start();
                break;

            case "gold_stop":
                if (goldfarm != null) {
                    goldfarm.cancel();
                    System.out.println("stopped TaskGoldfarm");
                } else {
                    System.out.println("Task läuft nicht");
                }
                break;

            case "craft_gold":
                goldingotcraft = new TaskCraftGoldingots(true);
                goldingotcraft.start();
                Main.self.sendChat("crafting goldingots");
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
                goldblockcraft = new TaskCraftGoldblocks();
                goldblockcraft.start();
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
