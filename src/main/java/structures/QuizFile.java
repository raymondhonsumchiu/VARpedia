package main.java.structures;

import java.io.File;

/**
 * Custom data structure for use in the Quiz component of VARpedia.
 * Simply stores an associated String "answer" with each file.
 */
public class QuizFile {
    private File file;
    private String answer;

    /**
     * @param file File to play in the quiz tab
     * @param answer Answer associated with the file
     */
    public QuizFile(File file, String answer) {
        this.file = file;
        this.answer = answer;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
