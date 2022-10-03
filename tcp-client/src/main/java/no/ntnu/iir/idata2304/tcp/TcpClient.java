package no.ntnu.iir.idata2304.tcp;

import java.io.IOException;
import java.net.InetAddress;

import no.ntnu.iir.idata2304.shared.Client;
import no.ntnu.iir.idata2304.shared.ClientResponseHandler;
import no.ntnu.iir.idata2304.shared.ValidationUtils;

public class TcpClient implements Client {
  private InetAddress address;
  private int port;
  private Character terminator;

  public TcpClient(InetAddress address, int port, Character terminator) throws IOException {
    this.address = address;
    this.port = ValidationUtils.validatePort(port);
    this.terminator = terminator;
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
