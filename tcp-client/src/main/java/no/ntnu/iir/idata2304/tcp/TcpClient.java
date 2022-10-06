package no.ntnu.iir.idata2304.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import lombok.extern.slf4j.Slf4j;
import no.ntnu.iir.idata2304.shared.Client;
import no.ntnu.iir.idata2304.shared.ClientResponseHandler;
import no.ntnu.iir.idata2304.shared.ValidationUtils;

@Slf4j
public class TcpClient implements Client {
  private InetAddress address;
  private int port;
  private Socket socket;
  private OutputStream outStream;
  private InputStream inStream;
  private Character terminator;
  private ClientResponseHandler responseHandler;

  public TcpClient(InetAddress address, int port, Character terminator) throws IOException {
    this.address = address;
    this.port = ValidationUtils.validatePort(port);
    this.terminator = terminator;
    this.socket = new Socket(this.address, this.port);
    this.outStream = this.socket.getOutputStream();
    this.inStream = this.socket.getInputStream();
  }

  @Override
  public void send(byte[] requestData) throws IOException {
    this.outStream.write(requestData);
    this.outStream.write(terminator.toString().getBytes());
    StringBuilder buffer = new StringBuilder();
    Character character = this.readCharacter();

    while (character != null && this.isLegalCharacter(character) && !this.isTerminatingCharacter(character)) {
      buffer.append(character);
      character = this.readCharacter();
    }

    byte[] response = buffer.toString().getBytes();

    if (this.responseHandler != null) {
      byte[] newRequest = this.responseHandler.onResponse(response, response.length);
      if (newRequest.length > 0) this.send(newRequest);
    }
  }

  @Override
  public void setResponseHandler(ClientResponseHandler handler) {
    this.responseHandler = handler;
  }

  /**
   * Reads a character from the clients input stream.
   * 
   * @return the read character from the input stream - or null
   */
  private Character readCharacter() {
    Character character = null;

    try {
      character = (char) this.inStream.read();
    } catch (Exception e) {
      log.error("Failed to read character from input stream", e);
    }

    return character;
  }

  /**
   * Checks whether the character is legal and returns true if it is.
   * 
   * @param character the character to check if its legal
   * 
   * @return true if the character is a legal character
   */
  private boolean isLegalCharacter(Character character) {
    return (int) character != 65535;
  }

  /**
   * Checks whether a character is the terminating character for the client.
   * 
   * @param character the character to check if is a terminator
   * 
   * @return true if the character is a terminator
   */
  private boolean isTerminatingCharacter(Character character) {
    return character.equals(this.terminator);
  }
}
