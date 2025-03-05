package com.glare;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import org.json.JSONObject;



public class TextToSpeech{

  private final String API_KEY = "*******";
  private final String URL = "https://api.ttsopenai.com/uapi/v1/text-to-speech";

  public void speak(String message) throws Exception{
    JSONObject payload = new JSONObject();

    payload.put("model", "tts-1");
    payload.put("voice", "OAOO5");
    payload.put("speed", 1);
    payload.put("input", message);



    HttpRequest request = HttpRequest.newBuilder()
      .uri(new URI(URL))
      .header("x-api-key", API_KEY)
      .header("Content-Type", "applcation/json")
      .POST(BodyPublishers.ofString(payload.toString()))
      .build();

    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

    System.out.println(response.body());
  }



}

// import java.util.Locale;

// import javax.speech.AudioException;
// import javax.speech.Central;
// import javax.speech.EngineException;
// import javax.speech.EngineStateError;
// import javax.speech.synthesis.Synthesizer;
// import javax.speech.synthesis.SynthesizerModeDesc;
// //camb ai 22907804-e307-462f-8812-e47060c53c6d
// public class TextToSpeech{

//     public void speak(String message){

//       System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_time_awb.AlanVoiceDirectory");

//       try {

//         Central.registerEngineCentral("com.sun.speech.freetts" + 
//           ".jsapi.FreeTTSEngineCentral");
//         Synthesizer synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));

//         synthesizer.allocate();
//         synthesizer.resume();
        
//         synthesizer.speakPlainText(message, null);
//         synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
//         synthesizer.deallocate();

//       } catch (EngineException e) {
//         e.printStackTrace();
//       } catch (AudioException e) {
//         e.printStackTrace();
//       } catch (EngineStateError e) {
//         e.printStackTrace();
//       } catch (IllegalArgumentException e) {
//               e.printStackTrace();
//             } catch (InterruptedException e) {
//               e.printStackTrace();
//             }
//     }




//     // public void speak2(String text){
//     //   String voiceName = "kevin16";

//     //   VoiceManager voiceManager
//     // }


// }