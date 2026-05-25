import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AlarmClock implements Runnable {

    private final LocalTime alarmTime;
    private final String filePath;
    private final Scanner scanner;

    AlarmClock(LocalTime alarmTime, String filePath, Scanner scanner) {
        this.alarmTime = alarmTime;
        this.filePath = filePath;
        this.scanner = scanner;

    }

    @Override
    public void run() {

        // now is < alarmTime
        while (LocalTime.now().isBefore(alarmTime)) {
            try {
                Thread.sleep(1000);

                LocalTime now = LocalTime.now();

                // to replace the current line
                System.out.printf("\r%02d:%2d:%02d", now.getHour(), now.getMinute(), now.getSecond());
            } catch (InterruptedException e) {
                System.out.println("Thread was interuppted");
            }
        }

        System.out.println("Alarm noises");
        try {
            playSound(filePath);
        } catch (LineUnavailableException e) {
            System.out.println("Audio is unavailable");
        }

    }

    private void playSound(String filePath) throws LineUnavailableException {
        File audioFile = new File(filePath);
        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile)) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

            System.out.print("Press Enter to stop the alarm :");
            scanner.nextLine();

            clip.stop();

            scanner.close();

        } catch (UnsupportedAudioFileException e) {
            System.out.println("Audio not supported");
        } catch (IOException e) {
            System.out.println("Error reading audio file");
        }
    }
}
