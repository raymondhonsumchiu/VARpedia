package main.java.tasks;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.*;
import javafx.concurrent.Task;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

import static main.java.VARpedia.*;

public class CreateTask extends Task<Void> {
    private String name, query;
    private List<String> chunkList;
    int numImages;

    public CreateTask(String name, String query, List<String> chunkList, int numImages) {
        this.name = name;
        this.query = query;
        this.chunkList = chunkList;
        this.numImages = numImages;
    }

    @Override
    protected Void call() throws Exception {

        TEMP.mkdirs();

        try {
            //obtain API keys from txt file
            String apiKey = getAPIKey("apiKey");
            String sharedSecret = getAPIKey("sharedSecret");

            //use Flickr API
            Flickr flickr = new Flickr(apiKey, sharedSecret, new REST());

            //only need one pages images
            int page = 0;

            PhotosInterface photos = flickr.getPhotosInterface();
            SearchParameters params = new SearchParameters();
            params.setSort(SearchParameters.RELEVANCE);
            params.setMedia("photos");
            params.setText(query);

            PhotoList<Photo> results = photos.search(params, numImages, page);
            System.out.println("Retrieving " + results.size()+ " results");

            int i = 0;
            for (Photo photo: results) {
                updateProgress(i,results.size());
                try {
                    BufferedImage image = photos.getImage(photo, Size.LARGE);
                    String filename = query.trim().replace(' ', '-')+"-"+System.currentTimeMillis()+"-"+photo.getId()+".jpg";

                    //downloads images into tmp directory
                    File outputfile = new File("tmp",filename);
                    ImageIO.write(image, "jpg", outputfile);
                    System.out.println("tmp "+filename);
                    i++;
                } catch (FlickrException fe) {
                    System.err.println("Ignoring image " +photo.getId() +": "+ fe.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Combine audio chunks
        String chunkString = "";
        for (String s : chunkList) {
            chunkString = chunkString + s + " ";
        }
        ProcessBuilder b5 = new ProcessBuilder("/bin/bash", "-c", "sox " + chunkString + "../tmp/temp.wav");
        b5.directory(CHUNKS);
        Process p5 = b5.start();
        p5.waitFor();

        // Get length of audio file
        ProcessBuilder b6 = new ProcessBuilder("/bin/bash", "-c", "soxi -D temp.wav");
        b6.directory(TEMP);
        Process p6 = b6.start();
        p6.waitFor();
        InputStream out6 = p6.getInputStream();
        BufferedReader stdout6 = new BufferedReader(new InputStreamReader(out6));
        double length = Double.parseDouble(stdout6.readLine());

        //create video slideshow
        double frameRate = 1;
        if (numImages != 1) {
            frameRate = numImages / length;
        }
        String vidCmd = "cat *.jpg | ffmpeg -f image2pipe -framerate " + frameRate + " -i - -t " + length + " -c:v libx264 -pix_fmt yuv420p -vf \"scale=560:480\" -r 25 -max_muxing_queue_size 1024 -y temp.mp4";
        ProcessBuilder b7 = new ProcessBuilder("/bin/bash", "-c", vidCmd);
        b7.directory(TEMP);
        Process p7 = b7.start();
        p7.waitFor();

        // Merge video with audio for the final Creation
        ProcessBuilder b8 = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -i temp.mp4 -i temp.wav -c:v copy -c:a aac -strict experimental temp1.mp4");
        b8.directory(TEMP);
        Process p8 = b8.start();
        p8.waitFor();

        //add text overlay to vid
        ProcessBuilder b9 = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -i ../tmp/temp1.mp4 -vf drawtext=\"fontfile=../myfont.ttf: text='" + query + "': fontcolor=white: fontsize=24: box=1: boxcolor=black@0.5: boxborderw=5: x=(w-text_w)/2: y=(h-text_h)/2\" -codec:a copy " + name + ".mp4");
        b9.directory(CREATIONS);
        CREATIONS.mkdirs();
        Process p9 = b9.start();
        p9.waitFor();


        updateProgress(1,1);
        deleteDirectory(TEMP);
        return null;
    }

    /**
     * Helper Method used to extract keys from text file
     * @param key
     * @return
     * @throws Exception
     */
    public static String getAPIKey(String key) throws Exception {

        //goes to working dir
        String config = System.getProperty("user.dir")
                + System.getProperty("file.separator")+ "flickr-api-keys.txt";

        File file = new File(config);
        BufferedReader br = new BufferedReader(new FileReader(file));

        //extract key strings
        String line;
        while ( (line = br.readLine()) != null ) {
            if (line.trim().startsWith(key)) {
                br.close();
                return line.substring(line.indexOf("=")+1).trim();
            }
        }
        br.close();
        throw new RuntimeException("Couldn't find " + key +" in config file "+file.getName());
    }



}
