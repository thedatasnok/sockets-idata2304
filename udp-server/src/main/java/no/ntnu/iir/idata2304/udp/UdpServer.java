package no.ntnu.iir.idata2304.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import lombok.extern.slf4j.Slf4j;
import no.ntnu.iir.idata2304.shared.Server;
import no.ntnu.iir.idata2304.shared.ServerRequestHandler;
import no.ntnu.iir.idata2304.shared.ValidationUtils;

@Slf4j
public class UdpServer implements Server {
  private int port;
  private ServerRequestHandler requestHandler;

  public UdpServer(int port) {
    this.port = ValidationUtils.validatePort(port);
  }

  @Override
  public void run() {
    log.info("Starting UDP Server on port: {}", this.port);
    byte[] receiveBuffer = new byte[1024];

    try (DatagramSocket socket = new DatagramSocket(this.port)) {
      boolean error = false;
      
      while (!error) {
        error = this.handleRequest(receiveBuffer, socket);
      }

    } catch (Exception e) {
      log.error("Failed binding socket, shutting down...", e);
    }
  }

  @Override
  public void setRequestHandler(Class<? extends ServerRequestHandler> handlerClass) {
    try {
      this.requestHandler = (ServerRequestHandler) handlerClass.getDeclaredConstructors()[0].newInstance();
    } catch (Exception e) {
      log.error("An error occured instantiating the request handler.", e);
    }
  }

  /**
   * Handles an individual request from the client.
   * 
   * @param buffer the buffer to use to receive data to
   * @param socket the socket to receive data on
   * 
   * @return true if processing the request resulted in an error
   */
  private boolean handleRequest(byte[] buffer, DatagramSocket socket) {
    boolean error = false;

    try {
      DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);
      socket.receive(incomingPacket);

      log.info(
        "Received UDP packet from {}, port {}", 
        incomingPacket.getAddress().getHostAddress(), 
        incomingPacket.getPort()
      );

      byte[] response = this.requestHandler.onRequest(
        incomingPacket.getData(), 
        incomingPacket.getLength(),
        incomingPacket.getAddress()
      );

      DatagramPacket responsePacket = new DatagramPacket(
        response, 
        response.length, 
        incomingPacket.getAddress(), 
        incomingPacket.getPort()
      );

      socket.send(responsePacket);
    } catch (Exception e) {
      error = true; 
      log.error("An error occured receiving packet data from the client!", e);
    }

    return error;
  }
  
}
