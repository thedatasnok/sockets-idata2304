package no.ntnu.iir.idata2304.shared;

import java.net.InetAddress;

public interface ServerRequestHandler {

  /**
   * Called when the server receives a request from a client.
   * 
   * @param requestData the request data received from the client
   * @param requestLength the length of the received request data
   * @param clientAddress the requesting clients address
   * 
   * @return the response encoded as an array of bytes, or an empty array to not send a response
   */
  public abstract byte[] onRequest(byte[] requestData, int requestLength, InetAddress clientAddress);

}
