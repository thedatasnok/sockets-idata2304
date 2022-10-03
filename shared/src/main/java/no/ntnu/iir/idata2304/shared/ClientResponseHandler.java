package no.ntnu.iir.idata2304.shared;

public interface ClientResponseHandler {
  
  /**
   * Called when single response is received in the client.
   * 
   * @param responseData the data the server responded with
   * @param responseLength the length of the received response
   * 
   * @return a new request to the server, or an empty array to not send a new request
   */
  byte[] onResponse(byte[] responseData, int responseLength);

}
