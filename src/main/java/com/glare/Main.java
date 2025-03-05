package com.glare;

// import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Gemini deepSeek = new Gemini();
        Transcriber transcriber = new Transcriber();
        Microphone mic = new Microphone();
        // mic.start();
        mic.record();
        System.out.println("thinking...");
        String result = deepSeek.getResponse(transcriber.transcribe());
        System.out.println(result);

        // TextToSpeech tts = new TextToSpeech();

        // // System.out.println(result);

        // tts.speak("hello my name is gavin");
        
    }
}