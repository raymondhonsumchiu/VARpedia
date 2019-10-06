package main.java.tasks;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WikitTask extends Task<List<String>> {
    private String query;

    public WikitTask(String query) {
        this.query = query;
    }

    @Override
    protected List<String> call() throws Exception {
        // Run wikit command through BASH
        ProcessBuilder b = new ProcessBuilder("/bin/bash", "-c", "wikit " + query);
        Process p = b.start();
        InputStream out = p.getInputStream();
        BufferedReader stdout = new BufferedReader(new InputStreamReader(out));

        // Read wikit output
        String line = stdout.readLine();
        List<String> list = new ArrayList<String>();

        //special case when line doesn't contain full stop, just add sentence to list
        if (!line.contains(".")){
            list.add(line);
        }else {
            // Separate into sentences
            BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.UK);
            iterator.setText(line);
            int start = iterator.first();
            for (int end = iterator.next();
                 end != BreakIterator.DONE;
                 start = end, end = iterator.next()) {
                list.add(line.substring(start, end).trim() + "\n");
            }
            list.set(list.size() - 1, list.get(list.size() - 1).trim());

        }

        return list;
    }
}
