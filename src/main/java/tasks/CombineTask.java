package main.java.tasks;

import javafx.concurrent.Task;

import java.io.*;
import java.util.List;

import static main.java.VARpedia.*;

/**
 * Task to be run in background thread to create preview and proper creations
 */
public class CombineTask extends Task<Void> {
    private String name, query, music;
    private List<String> chunkList;
    private int numImages;
    private List<String> listImages;
    private boolean isPreview;

    public CombineTask(String name, String query, List<String> chunkList, List<String> listImages, String music, boolean isPreview) {
        //set all fields
        this.name = name;
        this.query = query;
        this.chunkList = chunkList;
        this.listImages = listImages;
        this.numImages = listImages.size();
        this.isPreview = isPreview;

        //decide on music choice
        if (music.equals("Funny Piano")) {
            this.music = "funny_piano.mp3";
        } else if (music.equals("Happy Piano")) {
            this.music = "happy_piano.mp3";
        } else if (music.equals("Groovy Music")) {
            this.music = "groovy_music.mp3";
        } else {
            this.music = null;
        }
    }

    @Override
    protected Void call() throws Exception {

        //delete any prior selected images
        deleteDirectory(SELIMGS);

        //create if necessary, the folders for the creation process
        CHUNKS.mkdirs();
        CREATIONS.mkdirs();
        TEMPIMGS.mkdirs();
        SELIMGS.mkdir();


        //move a copy of all selected images to a new directory
        for (String img : listImages) {
            String copyCmd = "cp " + TEMPIMGS.toString() + img + " " + SELIMGS.toString() + img;
            bashCommand(copyCmd, null);
        }


        //Clean up any leftover files
        String removeCmd = "rm -f prevCreation.mp4 temp.mp4 temp.wav temp1.mp4 temp1.mp3";
        bashCommand(removeCmd, TEMP);


        // Combine audio chunks
        String chunkString = "";
        for (String s : chunkList) {
            chunkString += s + "/" + s + " ";
        }
        String audioCmd = "sox " + chunkString + "../temp/temp.wav";
        bashCommand(audioCmd, CHUNKS);


        // Get length of audio file, can't use bashCommand() due to the need to utilise the process outside of the basic running of the bash command
        ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "soxi -D temp.wav");
        pb.directory(TEMP);
        Process p = pb.start();
        p.waitFor();
        InputStream out = p.getInputStream();
        BufferedReader stdout = new BufferedReader(new InputStreamReader(out));
        double length = Double.parseDouble(stdout.readLine());


        //add bg music into chunk file
        String audFile = "temp.wav";
        if (music != null) {
            String musicCmd = "ffmpeg -i temp.wav -i ../src/main/resources/music/" + music + " -filter_complex amerge -ac 2 -c:a libmp3lame -q:a 4 temp1.mp3";
            bashCommand(musicCmd, TEMP);
            audFile = "temp1.mp3";
        }


        // Create video slideshow and send output to TEMP
        double frameRate = (double) numImages / length;
        String vidCmd = "cat *.jpg | ffmpeg -f image2pipe -framerate " + frameRate + " -i - -t " + length + " -c:v libx264 -pix_fmt yuv420p -vf \"scale=w=800:h=800:force_original_aspect_ratio=1,pad=800:800:(ow-iw)/2:(oh-ih)/2\" -r 25 -max_muxing_queue_size 1024 -y " + TEMP.toString() + "/" + "temp.mp4";
        bashCommand(vidCmd, SELIMGS);


        // Merge video with audio for the final Creation
        String mergeCmd = "ffmpeg -i temp.mp4 -i " + audFile + " -c:v copy -c:a aac -strict experimental temp1.mp4";
        bashCommand(mergeCmd, TEMP);


        // If the creation is not a preview, create the creation file which should include the creation and additional quiz media
        if(!isPreview) {
            // create the new creation's folder
            File NEWCREATION = new File(CREATIONS.toString() + "/" + name);
            NEWCREATION.mkdirs();


            // Add text overlay to vid
            String addTextCmd = "ffmpeg -i ../../temp/temp1.mp4 -vf drawtext=\"fontfile=../../resources/fonts/Questrial-Regular.ttf: text='" + query + "': fontcolor=white: fontsize=24: box=1: boxcolor=black@0.5: boxborderw=5: x=(w-text_w)/2: y=(h-text_h)/2\" -codec:a copy " + name + ".mp4";
            bashCommand(addTextCmd, NEWCREATION);


            // create 20sec audio for quiz
            String quizAudioCmd = "ffmpeg -stream_loop -1 -i " + TEMP.toString() + "/temp.wav -vcodec copy -ss 00:00:00.000 -t 00:00:20.000 audio.wav";
            bashCommand(quizAudioCmd, NEWCREATION);


            // create 20sec video
            frameRate = (double) numImages / 20;
            String quizVidCmd = "cat *.jpg | ffmpeg -f image2pipe -framerate " + frameRate + " -i - -t 20 -c:v libx264 -pix_fmt yuv420p -vf \"scale=w=800:h=800:force_original_aspect_ratio=1,pad=800:800:(ow-iw)/2:(oh-ih)/2\" -r 25 -max_muxing_queue_size 1024 -y " + "../../Creations/" + name + "/" + "video.mp4";
            bashCommand(quizVidCmd, SELIMGS);


            // Merge to get 20sec both vid
            String mergeQuizCmd = "ffmpeg -i video.mp4 -i audio.wav -c:v copy -c:a aac -strict experimental both.mp4";
            bashCommand(mergeQuizCmd, NEWCREATION);


            // Add text file for term.txt so that quiz can compare answers to real term
            String quizTermCmd = "touch " + query.replaceAll("\\s+", "_") + ".txt";
            bashCommand(quizTermCmd, NEWCREATION);

        }else {
            // if the creation is a preview, create preview video without the quiz media
            String prevVidCmd = "ffmpeg -i ../temp/temp1.mp4 -vf drawtext=\"fontfile=../../resources/fonts/Questrial-Regular.ttf: text='" + query + "': fontcolor=white: fontsize=24: box=1: boxcolor=black@0.5: boxborderw=5: x=(w-text_w)/2: y=(h-text_h)/2\" -codec:a copy " + name + ".mp4";
            bashCommand(prevVidCmd, TEMP);
        }

        return null;
    }

    /**
     * Helper method for running bash commands using process builder
     * @param cmd bash command to run
     * @param dir directory for bash process to run in
     */
    private void bashCommand(String cmd, File dir) throws IOException, InterruptedException {
        ProcessBuilder pBuilder = new ProcessBuilder("/bin/bash", "-c", cmd);
        //run builder in the input directory if not null
        if (dir != null){
            pBuilder.directory(dir);
        }
        Process process = pBuilder.start();
        process.waitFor();

    }
}