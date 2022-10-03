package no.ntnu.iir.idata2304.app;

import java.net.InetAddress;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import no.ntnu.iir.idata2304.shared.ServerRequestHandler;

@Slf4j
public class TaskDistributor implements ServerRequestHandler {
  private int currentTaskIndex;

  private static final String OK_RESPONSE = "ok";
  private static final String ERROR_RESPONSE = "error";

  private static final Map<String, String> TASK_ANSWER_MAP = Map.of(
    "NTNU.", "statement 1",
    "NTNU?", "question 1",
    "Will Will Smith smith?", "question 4",
    "Lorem ipsum.", "statement 2",
    "?", "question 0",
    ".", "statement 0"
  );

  private static final String[] TASKS = TASK_ANSWER_MAP.keySet().toArray(String[]::new);

  public TaskDistributor() {
    this.currentTaskIndex = 0;
  }

  /**
   * Returns the currently assigned task.
   * 
   * @return the currently assigned task
   */
  private String getCurrentTask() {
    return TASKS[currentTaskIndex % TASKS.length];
  }

  @Override
  public byte[] onRequest(byte[] requestData, int requestLength, InetAddress clientAddress) {
    log.info("Handling request from client @ {}", clientAddress.getHostAddress());

    String requestString = new String(requestData, 0, requestLength);
    String responseString;

    if (requestString.equals("task")) {
      responseString = this.getCurrentTask();
      log.info("Sending new task to client: {}", responseString);
    } else {
      responseString = TASK_ANSWER_MAP.get(this.getCurrentTask()).equals(requestString) ? OK_RESPONSE : ERROR_RESPONSE;
    }

    if (responseString.equals(OK_RESPONSE)) {
      this.currentTaskIndex++;
      log.info("Client answered correctly, incrementing task index: {}", this.currentTaskIndex);
    }
    
    return responseString.getBytes();
  }

}
