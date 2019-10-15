package main.java.tasks;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.*;
import javafx.concurrent.Task;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static main.java.VARpedia.TEMPIMGS;
import static main.java.VARpedia.deleteDirectory;

public class FlickrTask extends Task<Integer> {
    private String query;

    public FlickrTask(String query) {
        this.query = query;
    }

    @Override
    protected Integer call() {
        // Returns 0 if completed successfully, 1 if interrupted, and 2 if no images are found or an exception is thrown.

        deleteDirectory(TEMPIMGS);
        TEMPIMGS.mkdirs();
        int i = 1;

        try {
            // Obtain API keys from text file
            String apiKey = getAPIKey("apiKey");
            String sharedSecret = getAPIKey("sharedSecret");

            // Use Flickr API
            Flickr flickr = new Flickr(apiKey, sharedSecret, new REST());

            // Only need one page of images
            int page = 0;

            PhotosInterface photos = flickr.getPhotosInterface();
            SearchParameters params = new SearchParameters();
            params.setSort(SearchParameters.RELEVANCE);
            params.setMedia("photos");
            params.setText(query);

            PhotoList<Photo> results = photos.search(params, 12, page);

            for (Photo photo: results) {
                if (Thread.currentThread().isInterrupted()) { return 1; }
                try {
                    BufferedImage image = photos.getImage(photo, Size.LARGE);
                    String filename = i + ".jpg";

                    // Downloads images into temp directory
                    File outputfile = new File(TEMPIMGS.toString(),filename);
                    ImageIO.write(image, "jpg", outputfile);
                    System.out.println(filename + " downloaded");
                    i++;
                } catch (FlickrException fe) {
                    System.err.println("Ignoring image " + photo.getId() + ": " + fe.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 2;
        }
        if (i < 13) {
            return 2;
        }
        return 0;
    }

    /**
     * Helper Method used to extract keys from text file
     * @param key
     * @return
     * @throws Exception
     */
    public static String getAPIKey(String key) throws Exception {

        // Change to working directory
        String config = System.getProperty("user.dir")
                + System.getProperty("file.separator")+ "flickr-api-keys.txt";

        File file = new File(config);
        BufferedReader br = new BufferedReader(new FileReader(file));

        // Extract key strings
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
