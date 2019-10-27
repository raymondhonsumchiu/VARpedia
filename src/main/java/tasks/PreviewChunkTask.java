package main.java.tasks;

import javafx.concurrent.Task;

/**
 * This Background task is used to keep track of the preview chunk process
 * Once the process ends, the VARpediaController can set the preview's button back from stop to preview
 */
public class PreviewChunkTask extends Task<Void> {
    private Process process;

    public PreviewChunkTask(Process process){
        this.process = process;
    }

    @Override
    protected Void call() throws Exception {
        //loop stays active, keeping task alive as long as process is alive
        while (process.isAlive()) {
        }
        return null;
    }
}
