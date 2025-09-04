package raisetech.StudentManagement.controller.exceptionHandler;

/**
 * 受講生が見つからない場合の例外クラス
 */
public class StudentNotFoundException extends Exception {

  public StudentNotFoundException() {
    super();
  }

  public StudentNotFoundException(String message) {
    super(message);
  }

  public StudentNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public StudentNotFoundException(Throwable cause) {
    super(cause);
  }
}
