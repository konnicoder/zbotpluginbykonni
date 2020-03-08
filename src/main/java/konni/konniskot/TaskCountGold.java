/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import zedly.zbot.Location;

/**
 *
 * @author Konstantin
 */
public class TaskCountGold extends Task {

    private static final Location craftingbench = new Location(295, 130, -8720).centerHorizontally();
    private static final Location walk = new Location(297, 130, -8718).centerHorizontally();
    private static final Tesseract nuggetTesseract1 = new Tesseract(297, 132, -8717);
    private static final Tesseract nuggetTesseract2 = new Tesseract(295, 132, -8717);
    private static final Tesseract nuggetTesseract3 = new Tesseract(293, 132, -8717);
    private static final Tesseract nuggetTesseractsmelter = new Tesseract(303, 131, -8719);
    private static final Tesseract ingotTesseractfinal = new Tesseract(295, 132, -8721);
    private static final Tesseract ingotTesseractdropper = new Tesseract(300, 132, -8718);

    private static final Tesseract Goldblocktesseract = new Tesseract(299, 132, -8721);

    public TaskCountGold() {
        super(100);

    }

    public void run() {
        int nuggetsinpipeline = (int) (nuggetTesseract1.getAmount() + nuggetTesseract2.getAmount() + nuggetTesseract3.getAmount() + nuggetTesseractsmelter.getAmount());
        int ingotsinpipeline = (int) (ingotTesseractdropper.getAmount() + ingotTesseractfinal.getAmount());
        Main.self.sendChat("Nuggets in pipeline: " + nuggetsinpipeline + " = " + nuggetsinpipeline / 64 + " Stacks");
        Main.self.sendChat("Ingots in pipeline: " + ingotsinpipeline + " = " + ingotsinpipeline / 64 + " Stacks");
        int totalnuggetsinpipeline = nuggetsinpipeline + ingotsinpipeline * 9;
        

        int goldblockstotal = (int) Goldblocktesseract.getAmount();
        int goldafteradding = goldblockstotal * 81 + totalnuggetsinpipeline;
        Main.self.sendChat("Goldblocks before adding: " + goldblockstotal + " = " + goldblockstotal / 64 + " Stacks");
        Main.self.sendChat("Goldblocks after adding: " + goldafteradding / 81 + " = " + (goldafteradding / 81) / 64 + " Stacks");
        Main.self.sendChat("Total Nuggets: " + goldafteradding + " = " + (goldafteradding / 64) + " Stacks");

    }

}
