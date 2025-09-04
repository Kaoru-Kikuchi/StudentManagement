package raisetech.StudentManagement.controller.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * 受講生が見つからない場合の例外処理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 受講生が見つからない場合の例外処理
   *
   * @param ex 　例外
   * @return 404を返し、「学生情報：その生徒は存在しません」と表示する。
   */
  @ExceptionHandler(StudentNotFoundException.class)
  public ResponseEntity<String> handle(StudentNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body("学生情報: " + ex.getMessage());
  }
}
