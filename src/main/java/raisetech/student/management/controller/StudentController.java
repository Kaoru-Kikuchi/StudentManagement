package raisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exception.TestException;
import raisetech.student.management.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして受け付けるControllerです。
 */
@Tag(
    name = "受講生管理",
    description = "受講生情報、受講生コース情報、申込状況の検索、登録、更新を行うAPIです。"
)
@Validated
@RestController
public class StudentController {

  /** 受講生サービス */
  private final StudentService service;

  /**
   * コンストラクタ。
   *
   * @param service 受講生サービス
   */
  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細の一覧検索です。
   * 条件を指定せず、登録されている受講生詳細を全件取得します。
   *
   * @return 受講生詳細一覧
   */
  @Operation(
      summary = "受講生一覧検索",
      description = "登録されている受講生情報、受講生コース情報、申込状況を一覧で取得します。"
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "受講生一覧の取得に成功しました。"
      ),
      @ApiResponse(
          responseCode = "500",
          description = "サーバー内部でエラーが発生しました。"
      )
  })
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  /**
   * 受講生詳細の検索です。
   * IDに紐づく任意の受講生を取得します。
   *
   * @param id 受講生ID
   * @return 受講生詳細
   */
  @Operation(
      summary = "受講生詳細検索",
      description = "指定した受講生IDに紐づく受講生情報、受講生コース情報、申込状況を取得します。"
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "受講生詳細の取得に成功しました。"
      ),
      @ApiResponse(
          responseCode = "400",
          description = "受講生IDの形式が不正です。"
      ),
      @ApiResponse(
          responseCode = "500",
          description = "サーバー内部でエラーが発生しました。"
      )
  })
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable String id) {
    return service.searchStudent(id);
  }

  /**
   * 受講生詳細の登録を行います。
   *
   * @param studentDetail 受講生詳細
   * @return 登録した受講生詳細
   */
  @Operation(
      summary = "受講生登録",
      description = "受講生情報、受講生コース情報、申込状況を新規登録します。"
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "受講生の登録に成功しました。"
      ),
      @ApiResponse(
          responseCode = "400",
          description = "入力内容が不正です。"
      ),
      @ApiResponse(
          responseCode = "500",
          description = "サーバー内部でエラーが発生しました。"
      )
  })
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {

    StudentDetail responseStudentDetail =
        service.registerStudent(studentDetail);

    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細の更新を行います。
   * キャンセルフラグの更新もここで行います（論理削除）。
   *
   * @param studentDetail 受講生詳細
   * @return 更新結果
   */
  @Operation(
      summary = "受講生更新",
      description = "受講生情報、受講生コース情報、申込状況およびキャンセルフラグを更新します。"
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "受講生情報の更新に成功しました。"
      ),
      @ApiResponse(
          responseCode = "400",
          description = "入力内容が不正です。"
      ),
      @ApiResponse(
          responseCode = "500",
          description = "サーバー内部でエラーが発生しました。"
      )
  })
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(
      @RequestBody @Valid StudentDetail studentDetail) {

    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました");
  }

  /**
   * 例外処理のテスト用です。
   *
   * @return なし
   * @throws TestException テスト例外
   */
  @Operation(
      summary = "例外処理テスト",
      description = "共通例外処理の動作を確認するため、テスト例外を発生させます。"
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "400",
          description = "テスト例外が発生しました。"
      )
  })
  @GetMapping("/testException")
  public String testException() throws TestException {
    throw new TestException("失敗しました");
  }
}