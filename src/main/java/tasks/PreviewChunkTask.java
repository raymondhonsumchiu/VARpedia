package main.java.tasks;

import javafx.concurrent.Task;

public class PreviewChunkTask extends Task<Void> {
    //This Background task is used to keep track of the preview chunk process
    //Once the process ends, the VARpediaController can set the preview's button back from stop to preview

    private Process p1;

    public PreviewChunkTask(Process process){
        this.p1 = process;
    }

    @Override
    protected Void call() throws Exception {
            while (p1.isAlive()) {
            }
        return null;
    }
}
