import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Submission implements Comparable<Submission> {
    private String savedFileName;
    private LocalDateTime uploadTime;

    public Submission(String savedFileName) {
        this.savedFileName = savedFileName;
        this.uploadTime = LocalDateTime.now();
    }

    public String getSavedFileName() { return savedFileName; }

    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return uploadTime.format(formatter);
    }

    @Override
    public int compareTo(Submission other) {
        return other.uploadTime.compareTo(this.uploadTime); 
    }

    @Override
    public String toString() {
        return savedFileName + " [" + getFormattedTime() + "]";
    }
}