package raisetech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentCourseStatus;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

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

    when(service.searchStudentList())
        .thenReturn(Collections.emptyList());

    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1))
        .searchStudentList();
  }

  @Test
  void メールアドレスが不正な形式の時に入力チェックに掛かること() {

    Student student = new Student();
    student.setName("山田太郎");
    student.setKanaName("ヤマダタロウ");
    student.setNickname("タロウ");
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
    student.setName("山田太郎");
    student.setKanaName("ヤマダタロウ");
    student.setNickname("タロウ");
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
  void 受講生詳細検索が実行できて申込状況を含む受講生詳細が取得できること()
      throws Exception {

    String studentId =
        "ada1f007-7942-11f1-b4d0-b81ea42bf144";

    String studentCourseId =
        "ebed9c78-7942-11f1-b4d0-b81ea42bf144";

    Student student = new Student();
    student.setId(studentId);
    student.setName("山田太郎");
    student.setKanaName("ヤマダタロウ");
    student.setNickname("タロウ");
    student.setEmail("yamada@example.com");
    student.setArea("東京都");
    student.setAge(20);
    student.setSex("男性");
    student.setRemark("");

    StudentCourseStatus studentCourseStatus =
        new StudentCourseStatus();

    studentCourseStatus.setId(
        "11111111-1111-1111-1111-111111111111");
    studentCourseStatus.setStudentCourseId(studentCourseId);
    studentCourseStatus.setStatus("受講中");

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId(studentCourseId);
    studentCourse.setStudentId(studentId);
    studentCourse.setCourseName("Java基礎");
    studentCourse.setStudentCourseStatus(studentCourseStatus);

    List<StudentCourse> studentCourseList =
        List.of(studentCourse);

    StudentDetail detail =
        new StudentDetail(student, studentCourseList);

    when(service.searchStudent(studentId))
        .thenReturn(detail);

    mockMvc.perform(get("/student/" + studentId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.student.id")
            .value(studentId))
        .andExpect(jsonPath("$.student.name")
            .value("山田太郎"))
        .andExpect(jsonPath("$.student.email")
            .value("yamada@example.com"))
        .andExpect(jsonPath("$.studentCourseList")
            .isArray())
        .andExpect(jsonPath("$.studentCourseList[0].id")
            .value(studentCourseId))
        .andExpect(jsonPath("$.studentCourseList[0].courseName")
            .value("Java基礎"))
        .andExpect(
            jsonPath(
                "$.studentCourseList[0].studentCourseStatus.status")
                .value("受講中"));

    verify(service, times(1))
        .searchStudent(studentId);
  }

  @Test
  void 受講生詳細の登録が実行できて申込状況を含む結果が返ること()
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

    StudentCourseStatus studentCourseStatus =
        new StudentCourseStatus();

    studentCourseStatus.setStatus("仮申込");

    StudentCourse studentCourse =
        new StudentCourse();

    studentCourse.setCourseName("Java基礎");
    studentCourse.setStudentCourseStatus(
        studentCourseStatus);

    StudentDetail detail =
        new StudentDetail(
            student,
            List.of(studentCourse));

    when(service.registerStudent(
        any(StudentDetail.class)))
        .thenReturn(detail);

    mockMvc.perform(post("/registerStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
              {
                "student": {
                  "name": "山田太郎",
                  "kanaName": "ヤマダタロウ",
                  "nickname": "タロウ",
                  "email": "yamada@example.com",
                  "area": "東京都",
                  "age": 20,
                  "sex": "男性",
                  "remark": ""
                },
                "studentCourseList": [
                  {
                    "courseName": "Java基礎",
                    "studentCourseStatus": {
                      "status": "仮申込"
                    }
                  }
                ]
              }
              """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.student.name")
            .value("山田太郎"))
        .andExpect(jsonPath("$.student.email")
            .value("yamada@example.com"))
        .andExpect(jsonPath("$.studentCourseList[0].courseName")
            .value("Java基礎"))
        .andExpect(
            jsonPath(
                "$.studentCourseList[0].studentCourseStatus.status")
                .value("仮申込"));

    verify(service, times(1))
        .registerStudent(
            any(StudentDetail.class));
  }

  @Test
  void 受講生詳細の更新が実行できて申込状況を含む内容で正常終了すること()
      throws Exception {

    mockMvc.perform(put("/updateStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
              {
                "student": {
                  "id": "ada1f007-7942-11f1-b4d0-b81ea42bf144",
                  "name": "山田太郎",
                  "kanaName": "ヤマダタロウ",
                  "nickname": "タロウ",
                  "email": "yamada@example.com",
                  "area": "東京都",
                  "age": 20,
                  "sex": "男性",
                  "remark": ""
                },
                "studentCourseList": [
                  {
                    "id": "ebed9c78-7942-11f1-b4d0-b81ea42bf144",
                    "studentId": "ada1f007-7942-11f1-b4d0-b81ea42bf144",
                    "courseName": "Java基礎",
                    "studentCourseStatus": {
                      "id": "11111111-1111-1111-1111-111111111111",
                      "studentCourseId": "ebed9c78-7942-11f1-b4d0-b81ea42bf144",
                      "status": "受講終了"
                    }
                  }
                ]
              }
              """))
        .andExpect(status().isOk())
        .andExpect(
            content().string(
                "更新処理が成功しました"));

    verify(service, times(1))
        .updateStudent(
            any(StudentDetail.class));
  }

  @Test
  void 例外APIが実行されてステータス400とエラーメッセージが返ること()
      throws Exception {

    mockMvc.perform(get("/testException"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("失敗しました"));
  }

  @Test
  void 受講生詳細の一覧検索が実行できて申込状況を含む一覧が返ること()
      throws Exception {

    String studentId =
        "ada1f007-7942-11f1-b4d0-b81ea42bf144";

    String studentCourseId =
        "ebed9c78-7942-11f1-b4d0-b81ea42bf144";

    Student student = new Student();
    student.setId(studentId);
    student.setName("山田太郎");
    student.setKanaName("ヤマダタロウ");
    student.setNickname("タロウ");
    student.setEmail("yamada@example.com");
    student.setArea("東京都");
    student.setAge(20);
    student.setSex("男性");
    student.setRemark("");

    StudentCourseStatus status =
        new StudentCourseStatus();

    status.setId(
        "11111111-1111-1111-1111-111111111111");
    status.setStudentCourseId(studentCourseId);
    status.setStatus("受講中");

    StudentCourse course =
        new StudentCourse();

    course.setId(studentCourseId);
    course.setStudentId(studentId);
    course.setCourseName("Java基礎");
    course.setStudentCourseStatus(status);

    StudentDetail detail =
        new StudentDetail(
            student,
            List.of(course));

    when(service.searchStudentList())
        .thenReturn(List.of(detail));

    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].student.id")
            .value(studentId))
        .andExpect(jsonPath("$[0].studentCourseList[0].courseName")
            .value("Java基礎"))
        .andExpect(
            jsonPath(
                "$[0].studentCourseList[0].studentCourseStatus.status")
                .value("受講中"));

    verify(service, times(1))
        .searchStudentList();
  }

  @Test
  void コース名が未入力の時に受講生登録で400エラーになること()
      throws Exception {

    mockMvc.perform(post("/registerStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
            {
              "student": {
                "name": "山田太郎",
                "kanaName": "ヤマダタロウ",
                "nickname": "タロウ",
                "email": "yamada@example.com",
                "area": "東京都",
                "age": 20,
                "sex": "男性",
                "remark": ""
              },
              "studentCourseList": [
                {
                  "courseName": "",
                  "studentCourseStatus": {
                    "status": "仮申込"
                  }
                }
              ]
            }
            """))
        .andExpect(status().isBadRequest());

    verify(service, times(0))
        .registerStudent(any(StudentDetail.class));
  }
}