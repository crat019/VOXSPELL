package models;

/**
 * Created by ratterz on 18/09/16.
 */
public class Festival {

    /**
     * This method takes in a sentence and relays it to festival
     * @param phrase
     */
    public static void festivalTTS(String phrase) {
        try {
            String command = "echo " + phrase + " | festival --tts";
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
            Process p = pb.start();
            p.waitFor();
        } catch (Exception e) {

        }
    }
}
