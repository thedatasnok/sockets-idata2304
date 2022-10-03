package no.ntnu.iir.idata2304.tcp;

import java.net.ServerSocket;
import java.net.Socket;

import lombok.extern.slf4j.Slf4j;
import no.ntnu.iir.idata2304.shared.Server;
import no.ntnu.iir.idata2304.shared.ServerRequestHandler;
import no.ntnu.iir.idata2304.shared.ValidationUtils;

@Slf4j
public class TcpServer implements Server {
  private int threadCount;
  private int port;
  private Class<? extends ServerRequestHandler> handlerClass;
  private ServerSocket serverSocket;
  private boolean error;
  private Character terminator;


  public TcpServer(
      int port,
      Class<? extends ServerRequestHandler> handlerClass,
      Character terminator
  ) {
    this.threadCount = 0;
    this.port = ValidationUtils.validatePort(port);
    this.handlerClass = handlerClass;
    this.error = false;
    this.terminator = terminator;
  }

  @Override
  public void run() {
    log.info("Starting TCP Server on port: {}", this.port);

    try {
      this.serverSocket = new ServerSocket(this.port);
    } catch (Exception e) {
      log.error("Failed to run the TCP server!", e);
      throw new IllegalStateException("Starting the TCP server failed!", e);
    }

    while (!error) {
      Socket clientSocket = this.waitForClientConnection();
      if (clientSocket != null) {
        this.spawnSocketThread(clientSocket);
      }
    }
  }

  @Override
  public void setRequestHandler(Class<? extends ServerRequestHandler> handlerClass) {
    this.handlerClass = handlerClass;
  }

  /**
   * Waits until a client opens a connection to the server.
   * 
   * @return the client socket connection, or null if noone connected
   */
  private Socket waitForClientConnection() {
    Socket clientSocket = null;

    try {
      clientSocket = serverSocket.accept();
    } catch (Exception e) {
      log.error("Failed to accept incoming client connection!", e);
    }

    return clientSocket;
  }

  /**
   * Spawns a thread for a single client socket.
   * 
   * @param clientSocket the socket to spawn a thread for
   */
  private void spawnSocketThread(Socket clientSocket) {
    try {
      TcpClientHandler clientHandler = new TcpClientHandler(clientSocket, this.handlerClass, this.terminator);
      Thread clientThread = new Thread(clientHandler, "tcp-server-work-" + this.threadCount);
      this.threadCount++;
      clientThread.start();
    } catch (Exception e) {
      log.error("Error while initializing client connection!", e);
    }
  }

  
}
