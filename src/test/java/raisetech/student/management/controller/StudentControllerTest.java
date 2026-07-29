package raisetech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.springframework.http.MediaType;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private StudentService service;

  private final Validator validator =
      Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること()
      throws Exception {

    // ① 準備
    when(service.searchStudentList())
        .thenReturn(Collections.emptyList());

    // ② 実行・③レスポンス確認
    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    // ④ Serviceが呼ばれたか確認
    verify(service, times(1)).searchStudentList();
  }

  @Test
  void メールアドレスが不正な形式の時に入力チェックに掛かること() {

    Student student = new Student();

    // 他の必須項目は正常値
    student.setName("山田太郎");
    student.setKanaName("ヤマダタロウ");
    student.setNickname("タロウ");

    // メールだけ不正
    student.setEmail("メールアドレスではありません");

    student.setArea("東京都");
    student.setAge(20);
    student.setSex("男性");
    student.setRemark("");

    Set<ConstraintViolation<Student>> violations =
        validator.validate(student);

    assertThat(violations).hasSize(1);

    ConstraintViolation<Student> violation =
        violations.iterator().next();

    assertThat(violation.getPropertyPath().toString())
        .isEqualTo("email");

    assertThat(violation.getMessage())
        .isEqualTo("メールアドレスの形式が正しくありません。");
  }

  @Test
  void メールアドレスが正しい形式の時に入力チェックに異常がないこと() {

    Student student = new Student();

    // 他の必須項目は正常値
    student.setName("山田太郎");
    student.setKanaName("ヤマダタロウ");
    student.setNickname("タロウ");

    // 正しいメールアドレス
    student.setEmail("yamada@example.com");

    student.setArea("東京都");
    student.setAge(20);
    student.setSex("男性");
    student.setRemark("");

    Set<ConstraintViolation<Student>> violations =
        validator.validate(student);

    assertThat(violations).isEmpty();
  }

  @Test
  void 受講生詳細検索が実行できて受講生詳細が取得できること()
      throws Exception {

    String id = "ada1f007-7942-11f1-b4d0-b81ea42bf144";

    Student student = new Student();
    student.setId(id);
    student.setName("山田太郎");
    student.setKanaName("ヤマダタロウ");
    student.setNickname("タロウ");
    student.setEmail("yamada@example.com");
    student.setArea("東京都");
    student.setAge(20);
    student.setSex("男性");
    student.setRemark("");

    List<StudentCourse> courseList = new ArrayList<>();

    StudentDetail detail =
        new StudentDetail(student, courseList);

    when(service.searchStudent(id))
        .thenReturn(detail);

    mockMvc.perform(get("/student/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.student.id").value(id))
        .andExpect(jsonPath("$.student.name").value("山田太郎"))
        .andExpect(jsonPath("$.student.email").value("yamada@example.com"))
        .andExpect(jsonPath("$.studentCourseList").isArray())
        .andExpect(jsonPath("$.studentCourseList").isEmpty());

    verify(service, times(1))
        .searchStudent(id);
  }

  @Test
  void 受講生詳細の登録が実行できて正常終了すること()
      throws Exception {

    Student student = new Student();
    student.setName("山田太郎");
    student.setKanaName("ヤマダタロウ");
    student.setNickname("タロウ");
    student.setEmail("yamada@example.com");
    student.setArea("東京都");
    student.setAge(20);
    student.setSex("男性");
    student.setRemark("");

    StudentDetail detail = new StudentDetail();
    detail.setStudent(student);
    detail.setStudentCourseList(new ArrayList<>());

    when(service.registerStudent(any(StudentDetail.class)))
        .thenReturn(detail);

    mockMvc.perform(post("/registerStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
              {
                "student": {
                  "name":"山田太郎",
                  "kanaName":"ヤマダタロウ",
                  "nickname":"タロウ",
                  "email":"yamada@example.com",
                  "area":"東京都",
                  "age":20,
                  "sex":"男性",
                  "remark":""
                },
                "studentCourseList":[]
              }
              """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.student.name").value("山田太郎"))
        .andExpect(jsonPath("$.student.email").value("yamada@example.com"));

    verify(service, times(1))
        .registerStudent(any(StudentDetail.class));
  }

  @Test
  void 受講生詳細の更新が実行できて正常終了すること()
      throws Exception {

    mockMvc.perform(put("/updateStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
              {
                "student": {
                  "id":"ada1f007-7942-11f1-b4d0-b81ea42bf144",
                  "name":"山田太郎",
                  "kanaName":"ヤマダタロウ",
                  "nickname":"タロウ",
                  "email":"yamada@example.com",
                  "area":"東京都",
                  "age":20,
                  "sex":"男性",
                  "remark":""
                },
                "studentCourseList":[]
              }
              """))
        .andExpect(status().isOk())
        .andExpect(content().string("更新処理が成功しました"));

    verify(service, times(1))
        .updateStudent(any(StudentDetail.class));
  }

  @Test
  void 例外APIが実行されてステータス400とエラーメッセージが返ること()
      throws Exception {

    mockMvc.perform(get("/testException"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("失敗しました"));
  }
}