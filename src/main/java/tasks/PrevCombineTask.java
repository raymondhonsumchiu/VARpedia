package main.java.tasks;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static main.java.VARpedia.*;

public class PrevCombineTask extends Task<Void> {
    private String name, query, music;
    private List<String> chunkList;
    private int numImages;
    private List<String> listImages;

    public PrevCombineTask(String query, List<String> chunkList, List<String> listImages, String music) {
        this.name = "prevCreation";
        this.query = query;
        this.chunkList = chunkList;
        this.listImages = listImages;
        this.numImages = listImages.size();

        if (music.equals("Funny Piano")){
            this.music = "funny_piano.mp3";
        }else if(music.equals("Happy Piano")){
            this.music = "happy_piano.mp3";
        }else if(music.equals("Groovy Music")){
            this.music = "groovy_music.mp3";
        }else{
            this.music = null;
        }
    }

    @Override
    protected Void call() throws Exception {

        deleteDirectory(SELIMGS);

        CHUNKS.mkdirs();
        CREATIONS.mkdirs();
        TEMPIMGS.mkdirs();
        SELIMGS.mkdir();

        //move a copy of all selected images to a new directory
        for (String img: listImages) {
            ProcessBuilder b1 = new ProcessBuilder("/bin/bash", "-c", "cp " + TEMPIMGS.toString() + img + " " + SELIMGS.toString() + img);
            Process p1 = b1.start();
            p1.waitFor();
        }
        System.out.println("all imgs moved");

        ProcessBuilder b2 = new ProcessBuilder("/bin/bash", "-c", "rm -f prevCreation.mp4 temp.mp4 temp.wav temp1.mp4 temp1.mp3");
        b2.directory(TEMP);
        Process p2 = b2.start();
        p2.waitFor();

        // Combine audio chunks
        String chunkString = "";
        for (String s : chunkList) {
            chunkString += s + "/" + s + " ";
        }
        ProcessBuilder b5 = new ProcessBuilder("/bin/bash", "-c", "sox " + chunkString + "../temp/temp.wav");
        b5.directory(CHUNKS);
        Process p5 = b5.start();
        p5.waitFor();
        System.out.println("chunks combined");

        // Get length of audio file
        ProcessBuilder b6 = new ProcessBuilder("/bin/bash", "-c", "soxi -D temp.wav");
        b6.directory(TEMP);
        Process p6 = b6.start();
        p6.waitFor();
        InputStream out6 = p6.getInputStream();
        BufferedReader stdout6 = new BufferedReader(new InputStreamReader(out6));
        double length = Double.parseDouble(stdout6.readLine());

        //add bg music into chunk file
        String audFile = "temp.wav";
        if(music != null) {
            System.out.println("start music add");
            ProcessBuilder bm = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -i temp.wav -i ../src/main/resources/music/" + music + " -filter_complex amerge -ac 2 -c:a libmp3lame -q:a 4 temp1.mp3");
            bm.directory(TEMP);
            Process pm = bm.start();
            pm.waitFor();
            System.out.println("music added");
            audFile = "temp1.mp3";
        }

        // Create video slideshow and send output to TEMP
        double frameRate = numImages == 1 ? 1 : numImages / length;

        String vidCmd = "cat *.jpg | ffmpeg -f image2pipe -framerate " + frameRate + " -i - -t " + length + " -c:v libx264 -pix_fmt yuv420p -vf \"scale=560:480\" -r 25 -max_muxing_queue_size 1024 -y " + TEMP.toString() + "/" + "temp.mp4";
        ProcessBuilder b7 = new ProcessBuilder("/bin/bash", "-c", vidCmd);
        b7.directory(SELIMGS);
        Process p7 = b7.start();
        p7.waitFor();
        System.out.println("slideshow made");

        // Merge video with audio for the final Creation
        ProcessBuilder b8 = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -i temp.mp4 -i " + audFile + " -c:v copy -c:a aac -strict experimental temp1.mp4");
        b8.directory(TEMP);
        Process p8 = b8.start();
        p8.waitFor();
        System.out.println("merged");

        // Add text overlay to vid
        ProcessBuilder b9 = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -i ../temp/temp1.mp4 -vf drawtext=\"fontfile=../../resources/fonts/Questrial-Regular.ttf: text='" + query + "': fontcolor=white: fontsize=24: box=1: boxcolor=black@0.5: boxborderw=5: x=(w-text_w)/2: y=(h-text_h)/2\" -codec:a copy " + name + ".mp4");
        b9.directory(TEMP);
        Process p9 = b9.start();
        p9.waitFor();

        return null;
    }
}
