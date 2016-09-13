package sample;

/**
 * Created by ratterz on 8/09/16.
 */
public class Festival {
    public void festivalTTS(String word) {
        try {
            String command = "echo " + word + " | festival --tts";
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
            Process p = pb.start();
            p.waitFor();
        } catch (Exception e) {

        }
    }
}
