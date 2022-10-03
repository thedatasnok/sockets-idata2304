package no.ntnu.iir.idata2304.udp;

import java.io.IOException;
import java.net.InetAddress;

import no.ntnu.iir.idata2304.shared.Client;
import no.ntnu.iir.idata2304.shared.ClientResponseHandler;
import no.ntnu.iir.idata2304.shared.ValidationUtils;

public class UdpClient implements Client {
  private InetAddress address;
  private int port;

  public UdpClient(InetAddress address, int port) {
    this.address = address;
    this.port = ValidationUtils.validatePort(port);
  }

  @Override
  public void send(byte[] requestData) throws IOException {
    // TODO: implement this method
  }

  @Override
  public void setResponseHandler(ClientResponseHandler handler) {
    // TODO: implement this method
  }

}
