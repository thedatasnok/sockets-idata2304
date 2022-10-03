package no.ntnu.iir.idata2304.tcp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import lombok.extern.slf4j.Slf4j;
import no.ntnu.iir.idata2304.shared.ServerRequestHandler;

@Slf4j
public class TcpClientHandler implements Runnable {
  private Socket clientSocket;
  private InputStream inStream;
  private OutputStream outStream;
  private ServerRequestHandler requestHandler;
  private Character terminator;

  public TcpClientHandler(
      Socket clientSocket, 
      Class<? extends ServerRequestHandler> requestHandlerClass,
      Character terminator
  ) {
    try {
      this.clientSocket = clientSocket;
      this.inStream = this.clientSocket.getInputStream();
      this.outStream = this.clientSocket.getOutputStream();
      this.terminator = terminator;
      
      // hacky - but this way we don't really have to consider thread safety in the request handler (i think?)
      this.requestHandler = (ServerRequestHandler) requestHandlerClass.getDeclaredConstructors()[0].newInstance();
    } catch (Exception e) {
      throw new IllegalStateException("Something went wrong starting client handler!", e);
    }
  }

  @Override
  public void run() {
    log.info("Handling client {}[:{}]", clientSocket.getInetAddress().getHostName(), clientSocket.getPort());
    boolean clientAlive = true;

    while (clientAlive) {
      StringBuilder buffer = new StringBuilder();
      Character character = this.readCharacter();

      while (character != null && this.isLegalCharacter(character) && !this.isTerminatingCharacter(character)) {
        buffer.append(character);
        character = this.readCharacter();
      }
  
      byte[] request = buffer.toString().getBytes();
      
      
      if (this.requestHandler != null) {
        byte[] response = this.requestHandler.onRequest(request, request.length, clientSocket.getInetAddress());

        try {
          this.outStream.write(response);
          this.outStream.write(this.terminator.toString().getBytes());
          this.outStream.flush();
        } catch (Exception e) {
          log.error("Failed to write response to client in socket!", e);
          clientAlive = false;
        }
      }

      if (request.length == 0) {
        clientAlive = false;
      }
    }

    log.info("Client socket closed, shutting down client handler...");
  }

  /**
   * Reads a single character from the input stream.
   * 
   * @return the read character from the input stream
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
   * Determines whether a character is a legal character.
   * 
   * @param character the character to determine the validity of
   * 
   * @return true if the character is valid
   */
  private boolean isLegalCharacter(Character character) {
    return (int) character != 65535;
  }

  /**
   * Determines whether a character is a terminating character.
   * 
   * @param character the character to determine if it's a terminator
   * 
   * @return true if the character was a terminator
   */
  private boolean isTerminatingCharacter(Character character) {
    return character.equals(this.terminator);
  }
}
