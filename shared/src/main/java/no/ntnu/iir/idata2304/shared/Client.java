package no.ntnu.iir.idata2304.shared;

import java.io.IOException;

/**
 * Represents a common interface for socket clients.
 */
public interface Client {

  /**
   * Sends an array of bytes to the configured server.
   * 
   * @param requestData the array of bytes to send
   * 
   * @throws IOException if there was an error when writing data to the socket
   */
  void send(byte[] requestData) throws IOException;

  /**
   * Converts a string to bytes and sends it to the configured server. 
   * 
   * @param string the string data to send
   * 
   * @throws IOException if there was an error when writing data to the socket
   */
  default void send(String string) throws IOException {
    this.send(string.getBytes());
  }

  /**
   * Sets the response handler fo the client.
   * 
   * @param handler the response handler for the client to use
   */
  void setResponseHandler(ClientResponseHandler handler);

}