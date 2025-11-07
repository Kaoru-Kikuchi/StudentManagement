package raisetech.StudentManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.controller.exceptionHandler.StudentNotFoundException;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.exception.TestException;
import raisetech.StudentManagement.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして受け付けるControllerです。
 */
@Validated
@RestController
@Tag(name = "Student", description = "受講生関連のAPI")
public class StudentController {

  private final StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細の一覧検索です。
   */
  @Operation(summary = "一覧検索", description = "受講生の一覧を検索します")
  @ApiResponses({
      @ApiResponse(responseCode = "400", description = "現在利用できません")
  })
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  /**
   * 受講生詳細の検索です。IDに紐づく任意の受講生情報を取得します。
   */
  @Operation(summary = "受講生取得", description = "IDに紐づく受講生情報を取得します")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "成功"),
      @ApiResponse(responseCode = "404", description = "該当する受講生が存在しません")
  })
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable int id) {  // ← intに変更
    return service.searchStudent(id);
  }

  /**
   * 受講生詳細の登録を行います。
   */
  @Operation(summary = "受講生登録", description = "受講生を登録します")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "登録成功"),
      @ApiResponse(responseCode = "400", description = "バリデーションエラー")
  })
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細の更新を行います。キャンセルフラグの更新もここで行います（論理削除）。
   */
  @Operation(summary = "受講生更新", description = "受講生情報の更新を行います")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "更新成功"),
      @ApiResponse(responseCode = "404", description = "該当する受講生が存在しません")
  })
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました");
  }

  /**
   * TestException ハンドリング
   */
  @ExceptionHandler(TestException.class)
  public ResponseEntity<String> handleTestException(TestException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  /**
   * 第23回演習課題用の例外実行用メソッド
   */
  @Operation(summary = "例外確認用", description = "存在しない生徒情報を取得し例外を確認します")
  @ApiResponses({
      @ApiResponse(responseCode = "404", description = "このAPIは現在利用できません。古いURLとなっています。")
  })
  @GetMapping("/student")
  public List<StudentDetail> getStudent() throws StudentNotFoundException {
    throw new StudentNotFoundException("このAPIは現在利用できません。古いURLとなっています。");
  }

  @ExceptionHandler(StudentNotFoundException.class)
  public ResponseEntity<String> handleStudentNotFound(StudentNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ex.getMessage());
  }
}
