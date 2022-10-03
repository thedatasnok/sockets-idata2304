package no.ntnu.iir.idata2304.app;

import java.net.InetAddress;

import lombok.extern.slf4j.Slf4j;
import no.ntnu.iir.idata2304.shared.Client;
import no.ntnu.iir.idata2304.shared.Server;
import no.ntnu.iir.idata2304.tcp.TcpClient;
import no.ntnu.iir.idata2304.tcp.TcpServer;
import no.ntnu.iir.idata2304.udp.UdpClient;
import no.ntnu.iir.idata2304.udp.UdpServer;

@Slf4j
public class Runner {

  private static final String ERROR_MESSAGE = "Oops, something went wrong!";

  public static void main(String[] args) {
    Runner.runTcpClientAndServer();
  }

  /**
   * Runs both the TCP and UDP clients.
   */
  public static void runTcpAndUdpClient() {
    Thread udpThread = new Thread(() -> {
      try {
        Client client = new UdpClient(InetAddress.getByName("129.241.152.12"), 1234);
        client.setResponseHandler(new SentenceProcessor());
        client.send("task");
      } catch (Exception e) {
        log.error(ERROR_MESSAGE, e);
      }
    }, "udp-client");

    Thread tcpThread = new Thread(() -> {
      try {
        Client client = new TcpClient(InetAddress.getByName("129.241.152.12"), 1234, '/');
        client.setResponseHandler(new SentenceProcessor());
        client.send("task");
      } catch (Exception e) {
        log.error(ERROR_MESSAGE, e);
      }
    }, "tcp-client");

    udpThread.start();
    tcpThread.start();
  }

  /**
   * Runs both the TCP client and server.
   */
  public static void runTcpClientAndServer() {
    Thread tcpServerThread = new Thread(() -> {
      try {
        Server server = new TcpServer(1234, TaskDistributor.class, '/');
        server.run();
      } catch (Exception e) {
        log.error(ERROR_MESSAGE, e);
      }
    }, "tcp-server");

    Thread tcpClientThread = new Thread(() -> {
      try {
        Client client = new TcpClient(InetAddress.getByName("localhost"), 1234, '/');
        client.setResponseHandler(new SentenceProcessor());
        client.send("task");
      } catch (Exception e) {
        log.error(ERROR_MESSAGE, e);
      }
    }, "tcp-client");

    tcpServerThread.start();
    tcpClientThread.start();
  }

  /**
   * Runs both the UDP client and server.
   */
  public static void runUdpClientAndServer() {
    Thread udpServerThread = new Thread(() -> {
      try {
        Server server = new UdpServer(1234);
        server.setRequestHandler(TaskDistributor.class);
        server.run();
      } catch (Exception e) {
        log.error(ERROR_MESSAGE, e);
      }
    }, "udp-server");

    Thread udpClientThread = new Thread(() -> {
      try {
        Client client = new UdpClient(InetAddress.getByName("localhost"), 1234);
        client.setResponseHandler(new SentenceProcessor());
        client.send("task");        
      } catch (Exception e) {
        log.error(ERROR_MESSAGE, e);
      }
    }, "udp-client");

    udpServerThread.start();
    udpClientThread.start();
  }

}
