package com.glare;

import javax.sound.sampled.*;
import java.io.*;


public class Microphone extends Thread{


    private static final int SAMPLE_RATE = 44100; // Hz
    private static final int SAMPLE_SIZE_IN_BITS = 16; // bits
    private static final int CHANNELS = 1; // mono
    private static final boolean SIGNED = true;
    private static final boolean BIG_ENDIAN = false;
    private static final float SILENCE_THRESHOLD = 30f; // Adjust this!  Lower = more sensitive
    private static final int SILENCE_DURATION_MS = 500; // Adjust this! Milliseconds of silence to trigger stop
    private String status = "";

    public void run(){
        record();
    }

    public String getStatus(){
        return status;
    }

    public void record(){
        boolean recording = false;
        status = "";
        System.out.println("listening");
        try {
            AudioFormat format = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, SIGNED, BIG_ENDIAN);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                return;
            }

            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int numBytesRead;
            byte[] data = new byte[line.getBufferSize() / 5]; // Read in chunks

            long silenceStartTime = 0;

            while (true) {
                System.out.println(status);
                numBytesRead = line.read(data, 0, data.length);
                if (numBytesRead == -1) {
                    break;
                }
                out.write(data, 0, numBytesRead);


                // Check for silence
                boolean isSilent = isSilent(data);
                if(recording){
                    // System.out.println("recording");
                    if (isSilent) {
                        if (silenceStartTime == 0) {
                            silenceStartTime = System.currentTimeMillis();
                        } else if (System.currentTimeMillis() - silenceStartTime >= SILENCE_DURATION_MS) {
                            System.out.println("Saved audio");
                            break; // Enough silence detected
                        }
                    } else {
                        silenceStartTime = 0; // Reset silence timer
                    }
                }else{
                    if(isSilent == false){
                        recording = true;
                    }else{
                        out = new ByteArrayOutputStream();
                    }
                }
            }

            line.stop();
            line.close();

            // Save the recorded audio (optional - adjust file path as needed)
            saveAudioToFile(out.toByteArray(), "recorded_audio.wav");
            System.out.println("Recording saved to recorded_audio.wav");

        } catch (LineUnavailableException | IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static boolean isSilent(byte[] data) {
        int sum = 0;
        for (byte b : data) {
            sum += Math.abs(b); // Sum of absolute values
        }
        float avg = (float) sum / data.length;
        return avg < SILENCE_THRESHOLD; // Adjust this threshold
    }


    private void saveAudioToFile(byte[] audioData, String filePath) throws IOException {
        AudioFormat format = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, SIGNED, BIG_ENDIAN);
        ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
        AudioInputStream ais = new AudioInputStream(bais, format, audioData.length);
    
        File file = new File(filePath);
        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
        ais.close();
        bais.close();
    }
}
