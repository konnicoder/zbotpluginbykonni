/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import konni.konniskot.FarmTasks.TaskConcrete;
import konni.konniskot.FarmTasks.TaskExtractBooks;
import konni.konniskot.FarmTasks.TaskConvertPumpkins;
import konni.konniskot.BotEdit.TaskFloorSL;
import konni.konniskot.BotEdit.TaskFloorPerimiter;
import konni.konniskot.FarmTasks.TaskGetSand;
import konni.konniskot.FarmTasks.TaskGrindpigmen;
import konni.konniskot.FarmTasks.TaskGuardianGrinder;
import konni.konniskot.FarmTasks.TaskStripMine;
import konni.konniskot.FarmTasks.TaskStripMineReal;
import konni.konniskot.Useful.TaskTrashcan;
import konni.konniskot.FarmTasks.TaskCarrotFarmer;
import konni.konniskot.Useful.TaskCheckInventory;
import konni.konniskot.BotEdit.TaskDig;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import konni.konniskot.BotEdit.TaskDigLegit;
import konni.konniskot.BotEdit.TaskFillFloor;
import konni.konniskot.BotEdit.TaskPyramid;
import konni.konniskot.BotEdit.TaskPyramidBFS;
import konni.konniskot.BotEdit.TaskWallUp;
import konni.konniskot.CraftingTasks.TaskCraftDj;
import konni.konniskot.FarmTasks.TaskFarmPigmen;
import konni.konniskot.FarmTasks.TaskFarmShulkerShells;
import konni.konniskot.FarmTasks.TaskFarmWitherSkelletons;
import konni.konniskot.FarmTasks.TaskMineEndPillars;
import konni.konniskot.FarmTasks.TaskMineOres;
import konni.konniskot.CraftingTasks.TaskCraftIron;
import zedly.zbot.BlockFace;

/**
 *
 * @author Konstantin
 */
class CommandProcessor {

    private static TaskCraftGoldingots goldingotcraft;
    private static TaskGrindpigmen pigmentask;
    private static final File scrambleComplimentFile = new File(Main.instance.getDataFolder(), "compliments.txt");
    private static final File scrambleInsultsFile = new File(Main.instance.getDataFolder(), "insults.txt");
    private static final ArrayList<String> compliments = new ArrayList<>();
    private static final ArrayList<String> insults = new ArrayList<>();
    private static TaskGetSand taskgetsand;
    private String status = "-v";

    static void onCommand(String user, String command, boolean pm) throws Exception {
        String[] args = command.split(" ");
        loadComplimentsDictionary();
        loadInsultsDictionary();

        switch (args[0]) {
            case "tps":
                new TaskAFK().start();
                break;

            case "time":
                int time = KaiTools.getDayTime();
                if (time > 0 && time < 13500) {
                    int minutesleft = 13500 - time;
                    Main.self.sendChat("time left: " + minutesleft / 1000 + " minutes");
                } else {
                    Main.self.sendChat("nope.");
                }

                break;

            case "on":
                Main.self.sendChat("/home dj");
                break;

            case "off":
                Main.self.sendChat("/spawn");
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
            case "insult":
                String ins = insults.get(Main.rnd.nextInt(insults.size()));
                Main.self.sendChat(ins);
                break;

        }
        if (!Main.admins.contains(user)) {
            return;

        }
        switch (args[0]) {
            case "ore":
                new TaskMineOres().start();
                break;
            case "pups":
                int mm = Integer.parseInt(args[1]);
                new TaskFarmPigmen(mm).start();
                break;
            case "health":
                Main.self.sendChat("" + Main.self.getHealth());
                break;
            case "shulker":
                new TaskFarmShulkerShells(user).start();
                break;
            case "st":
                if (args.length == 2) {
                    int dis = Integer.parseInt(args[1]);
                    new TaskStripMineReal(dis).start();
                }
                break;
            case "craft_iron":
                new TaskCraftIron().start();
                break;

            case "craft_dj":
                new TaskCraftDj().start();
                break;

            case "carrot":
                new TaskCarrotFarmer().start();
                break;
            case "tp":
                Main.self.sendChat("/tpa " + user);
                break;

            case "tpa":
                Main.self.sendChat("/tpaccept");
                break;

            case "grind_guardian":
                new TaskGuardianGrinder().start();
                break;

            case "Test":
                new TaskTest().start();

                break;

            case "obs":
                new TaskMineEndPillars(user).start();
                break;

            case "fl":
                new TaskFillFloor().start();
                break;

            case "farm_W":
                new TaskFarmWitherSkelletons().start();
                break;

            case "pfloor":
                if (args.length == 1) {
                    Main.self.sendChat("(prefix) pfloor (x1) (y1) (z1) (x2) (y2) (z2)");
                }
                if (args.length == 7) {
                    int xc1 = Integer.parseInt(args[1]);
                    int yc1 = Integer.parseInt(args[2]);
                    int zc1 = Integer.parseInt(args[3]);
                    int xc2 = Integer.parseInt(args[4]);
                    int yc2 = Integer.parseInt(args[5]);
                    int zc2 = Integer.parseInt(args[6]);

                    new TaskFloorPerimiter(xc1, yc1, zc1, xc2, yc2, zc2).start();
                }
                break;

            case "pfloorsl":
                if (args.length == 1) {
                    Main.self.sendChat("(prefix) pfloorsl (x1) (y1) (z1) (x2) (y2) (z2)");
                }
                if (args.length == 7) {
                    int xc1 = Integer.parseInt(args[1]);
                    int yc1 = Integer.parseInt(args[2]);
                    int zc1 = Integer.parseInt(args[3]);
                    int xc2 = Integer.parseInt(args[4]);
                    int yc2 = Integer.parseInt(args[5]);
                    int zc2 = Integer.parseInt(args[6]);

                    new TaskFloorSL(xc1, yc1, zc1, xc2, yc2, zc2).start();
                }

                break;
            case "MR":
                if (args.length == 2) {
                    int mode = Integer.parseInt(args[1]);
                    new TaskMazeRunner(mode).start();
                }

                break;

            case "wall":
                if (args.length == 1) {
                    Main.self.sendChat("(prefix) wall (ytargetheight)");
                }

                if (args.length == 2) {
                    int ytarget1 = Integer.parseInt(args[1]);
                    new TaskWallUp(ytarget1, user).start();
                }
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

            case "di":
                if (args.length == 1) {
                    Main.self.sendChat("prefix d x1 y1 z1 x2 y2 z2 targetheight");
                }
                if (args.length == 8) {
                    int x1 = Integer.parseInt(args[1]);
                    int y1 = Integer.parseInt(args[2]);
                    int z1 = Integer.parseInt(args[3]);
                    int x2 = Integer.parseInt(args[4]);
                    int y2 = Integer.parseInt(args[5]);
                    int z2 = Integer.parseInt(args[6]);
                    int th = Integer.parseInt(args[7]);

                    new TaskDigLegit(x1, y1, z1, x2, y2, z2, th).start();
                }

                break;

            case "see":
                int x1 = Integer.parseInt(args[1]);
                int y1 = Integer.parseInt(args[2]);
                int z1 = Integer.parseInt(args[3]);
                new TaskSee(x1, y1, z1).start();

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

            case "sand":
                new TaskGetSand().start();
                break;

            case "trash":
                new TaskTrashcan().start();
                break;

            case "xp":
                Main.self.sendChat("/home xp");
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
        try ( BufferedReader br = new BufferedReader(new FileReader(scrambleComplimentFile))) {
            String t;
            while ((t = br.readLine()) != null) {
                compliments.add(t);
            }
            br.close();
        } catch (IOException ex) {
        }
    }

    private static void loadInsultsDictionary() {
        try ( BufferedReader br = new BufferedReader(new FileReader(scrambleInsultsFile))) {
            String t;
            while ((t = br.readLine()) != null) {
                insults.add(t);
            }
            br.close();
        } catch (IOException ex) {
        }
    }
}
