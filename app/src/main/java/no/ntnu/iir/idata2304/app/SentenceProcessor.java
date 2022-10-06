package no.ntnu.iir.idata2304.app;

import lombok.extern.slf4j.Slf4j;
import no.ntnu.iir.idata2304.shared.ClientResponseHandler;

@Slf4j
public class SentenceProcessor implements ClientResponseHandler {
  
  @Override
  public byte[] onResponse(byte[] responseData, int responseLength) {
    log.info("Received response from server!");

    byte[] result = new byte[]{};
    String type;

    String sentence = new String(responseData, 0, responseLength);
    if (!sentence.equals("error") && !sentence.equals("ok")) {
      int wordAmount = 0;

      if (sentence.endsWith("?")) {
        type = "question";
      } else {
        type = "statement";
      }
      
      if (!sentence.replaceAll("^[?.]$", "").isBlank()) {
        wordAmount = sentence.split("\\s+").length;
      }
    
      result = (type + " " + wordAmount).getBytes();
    }

    return result;
  }
}