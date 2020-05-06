/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot.Useful;

import java.util.logging.Level;
import java.util.logging.Logger;
import konni.konniskot.Task;

/**
 *
 * @author Konstantin
 */
public class TaskWalkToCommonLocations extends Task {

    public TaskWalkToCommonLocations() {
        super(100);
    }
    public void run(){
        try {
            ai.tick();
        } catch (InterruptedException ex) {          
        }
    unregister();
    }
}
