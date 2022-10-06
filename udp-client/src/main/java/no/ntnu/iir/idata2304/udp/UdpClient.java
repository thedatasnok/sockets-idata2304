package no.ntnu.iir.idata2304.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import no.ntnu.iir.idata2304.shared.Client;
import no.ntnu.iir.idata2304.shared.ClientResponseHandler;
import no.ntnu.iir.idata2304.shared.ValidationUtils;

public class UdpClient implements Client {
  private InetAddress address;
  private int port;
  private DatagramSocket socket;
  private byte[] responseBuffer;
  private ClientResponseHandler responseHandler;

  public UdpClient(InetAddress address, int port) throws SocketException {
    this.address = address;
    this.port = ValidationUtils.validatePort(port);
    this.socket = new DatagramSocket();
    this.responseBuffer = new byte[1024];
  }

  @Override
  public void send(byte[] requestData) throws IOException {
    DatagramPacket requestPacket = new DatagramPacket(
      requestData, 
      requestData.length, 
      this.address, 
      this.port
    );

    this.socket.send(requestPacket);

    DatagramPacket responsePacket = new DatagramPacket(
      this.responseBuffer,
      this.responseBuffer.length
    );

    this.socket.receive(responsePacket);
    if (this.responseHandler != null) {
      byte[] newRequest = this.responseHandler.onResponse(this.responseBuffer, responsePacket.getLength());
      if (newRequest.length > 0) this.send(newRequest);
    }
  }

  @Override
  public void setResponseHandler(ClientResponseHandler handler) {
    this.responseHandler = handler;
  }

}
