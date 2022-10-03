package no.ntnu.iir.idata2304.shared;

public interface Server {
  
  /**
   * Starts the server to make it run indefinetely.
   */
  void run();

  /**
   * Sets the request handler of the server.
   * 
   * @param handler the request handler for the server to use
   */
  void setRequestHandler(Class<? extends ServerRequestHandler> handlerClass);

}
