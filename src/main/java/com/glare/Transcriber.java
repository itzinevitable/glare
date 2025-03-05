package com.glare;

import java.io.File;

import org.json.JSONObject;

import com.assemblyai.api.AssemblyAI;
import com.assemblyai.api.resources.files.types.UploadedFile;
import com.assemblyai.api.resources.transcripts.requests.TranscriptParams;
import com.assemblyai.api.resources.transcripts.types.Transcript;
import com.assemblyai.api.resources.transcripts.types.TranscriptStatus;

public class Transcriber {

    private final String API_KEY = "********";
    private final String FILE_PATH = "recorded_audio.wav";

    public String transcribe() throws Exception{
        AssemblyAI client = AssemblyAI.builder()
            .apiKey(API_KEY)
            .build();

        UploadedFile uploadedFile = client.files().upload(new File(FILE_PATH));
        String audioURL = uploadedFile.getUploadUrl();

        TranscriptParams transcriptParams = TranscriptParams.builder()
            .audioUrl(audioURL)
            .build();

        Transcript transcript = client.transcripts().transcribe(transcriptParams);

        if(transcript.getStatus() == TranscriptStatus.ERROR){
            throw new Exception("Transcript failed with error: " + transcript.getError().get());
        }

        JSONObject json = new JSONObject(transcript.toString());

        String result = json.getString("text");
        System.out.println(result);
        return result;
    }

}
