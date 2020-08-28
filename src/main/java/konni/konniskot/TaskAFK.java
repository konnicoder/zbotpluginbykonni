/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import konni.konniskot.Task;
import zedly.zbot.event.ChatEvent;

/**
 *
 * @author Konstantin
 */
public class TaskAFK extends Task {

    private static final String TPS_REGEX = "^TPS from last 1m, 5m, 15m: ([0-9\\.]+), ([0-9\\.]+), ([0-9\\.]+)$";
    private static final Pattern TPS_PATTERN = Pattern.compile(TPS_REGEX);

    public TaskAFK() {
        super(1000);
    }

    public void run() {
        try {
            Main.self.sendChat("" + getTPS());
            while (true) {
                if (getTPS() <= 15) {
                    Main.self.sendChat("/spawn");
                    ai.tick(20);
                }
                if (getTPS() >=18 ) {
                    Main.self.sendChat("/home dj");
                    ai.tick(60);
                }
            }

        } catch (InterruptedException ex) {

        }
    }

    public double getTPS() throws InterruptedException {
        Main.self.sendChat("/tps");
        ChatEvent c = ai.waitForEvent(ChatEvent.class, (e) -> {
            return e.getMessage().matches(TPS_REGEX);
        }, 5000);
        if (c == null) {
            System.out.println("Error in TPS message");
            return 0;
        } else {
            String msg = c.getMessage();
            return tpsFromServerMsg(msg, 1);
        }
    }

    public double tpsFromServerMsg(String serverMsg, int minuteaverage) {
        String AvgGroup = "error";
        Matcher m = TPS_PATTERN.matcher(serverMsg);
        if (!m.find()) {
            return -1;
        }
        if (minuteaverage == 1) {
            AvgGroup = m.group(1);
        }
        if (minuteaverage == 5) {
            AvgGroup = m.group(2);
        }
        if (minuteaverage == 15) {
            AvgGroup = m.group(3);
        }

        return Double.parseDouble(AvgGroup);
    }

}
