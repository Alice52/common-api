package common.token.exception;

/**
 * @author T04856 <br>
 * @create 2023-05-22 2:43 PM <br>
 * @project project-cloud-custom <br>
 */
public class ValidTokenException extends RuntimeException {

  public ValidTokenException(String message, Exception e) {
    super(message, e);
  }

  public ValidTokenException(String message) {
    super(message);
  }
}
