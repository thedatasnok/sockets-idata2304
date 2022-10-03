package no.ntnu.iir.idata2304.shared;

public class ValidationUtils {
  
  private ValidationUtils() {
    // ...
  }

  /**
   * Validates a port number, returning the number if it was valid or throwing if it was invalid.
   * 
   * @param port the port number to validate
   * 
   * @return the validated port number
   * 
   * @throws IllegalArgumentException if the port was outside the range of 0-65535 
   */
  public static int validatePort(int port) {
    if (port < 0 || port > 65535) throw new IllegalArgumentException("A port may not be outside the range of 0-65535!");

    return port;
  }

}
