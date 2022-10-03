package no.ntnu.iir.idata2304.app;

import lombok.extern.slf4j.Slf4j;
import no.ntnu.iir.idata2304.shared.ClientResponseHandler;

@Slf4j
public class SentenceProcessor implements ClientResponseHandler {
  
  @Override
  public byte[] onResponse(byte[] responseData, int responseLength) {
    log.info("Received response from server!");

    // TODO: implement this method
    
    return new byte[] { };
  }

}
