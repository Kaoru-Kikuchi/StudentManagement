package raisetech.StudentManagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.http.MediaType;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @PostConstruct
  void setUp() {
    objectMapper.registerModule(new JavaTimeModule());
  }


  @MockBean
  private StudentService service;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void 受講生詳細の一覧検索が実行できること() throws Exception {
    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 受講生詳細の検索_IDに紐づく任意の受講生情報を取得できること() throws Exception {
    String id = "123";
    Student student = new Student();
    student.setId(id);
    StudentDetail studentDetail = new StudentDetail(student, List.of());
    when(service.searchStudent(id)).thenReturn(studentDetail);
    mockMvc.perform(get("/student/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.student.id").value(id));

    verify(service, times(1)).searchStudent(id);
  }

  @Test
  void 受講生詳細の登録ができること() throws Exception {
    Student student = new Student();
    student.setId("1");
    student.setName("田中太郎");
    student.setKanaName("タナカタロウ");
    student.setNickname("タナカ");
    student.setEmail("test@example.com");
    student.setArea("東京");
    student.setSex("男性");

    StudentCourse course = new StudentCourse();
    course.setId("101");
    course.setCourseName("Java基礎");
    course.setStudentId("1");
    course.setCourseStartAt(LocalDateTime.now());
    course.setCourseEndAt(LocalDateTime.now().plusYears(1));

    StudentDetail request = new StudentDetail(student, List.of(course));

    when(service.registerStudent(org.mockito.ArgumentMatchers.any(StudentDetail.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    mockMvc.perform(MockMvcRequestBuilders.post("/registerStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.student.id").value("1"))
        .andExpect(jsonPath("$.student.name").value("田中太郎"));

    verify(service, times(1)).registerStudent(
        org.mockito.ArgumentMatchers.any(StudentDetail.class));

  }

  @Test
  void 受講生詳細の更新が正常に実行できること() throws Exception {

    Student student = new Student();
    student.setId("1");
    student.setName("田中太郎");
    student.setKanaName("タナカタロウ");
    student.setNickname("タナカ");
    student.setEmail("test@example.com");
    student.setArea("東京");
    student.setSex("男性");

    StudentCourse course = new StudentCourse();
    course.setId("101");
    course.setCourseName("Java基礎");
    course.setStudentId("1");
    course.setCourseStartAt(LocalDateTime.now());
    course.setCourseEndAt(LocalDateTime.now().plusYears(1));

    StudentDetail request = new StudentDetail(student, List.of(course));

    mockMvc.perform(MockMvcRequestBuilders.put("/updateStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value("更新処理が成功しました"));

    verify(service, times(1)).updateStudent(org.mockito.ArgumentMatchers.any(StudentDetail.class));
  }


  @Test
  void 受講生詳細の受講生で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    Student student = new Student();
    student.setId("1");
    student.setName("江並公史");
    student.setKanaName("エナミコウジ");
    student.setNickname("エナミ");
    student.setEmail("test@example.com");
    student.setArea("奈良県");
    student.setSex("男性");
    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);

  }

  @Test
  void 受講生詳細の受講生でIDに数字以外を用いた時に入力チェックに掛かること() {
    Student student = new Student();
    student.setId("テストです。");
    student.setName("江並公史");
    student.setKanaName("エナミコウジ");
    student.setNickname("エナミ");
    student.setEmail("test@example.com");
    student.setArea("奈良県");
    student.setSex("男性");
    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("数字のみ入力するようにしてください。");

  }

  @Test
  void 受講生コースで適切な値を入力した時に入力チェックに異常が発生しないこと() {
    StudentCourse course = new StudentCourse();
    course.setId("1");
    course.setStudentId("1");
    course.setCourseName("Java基礎コース");
    course.setCourseStartAt(LocalDateTime.now());
    course.setCourseEndAt(LocalDateTime.now().plusMonths(6));

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(course);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生コースでIDに数字以外を用いた時に入力チェックに掛かること() {
    StudentCourse course = new StudentCourse();
    course.setId("abc");
    course.setStudentId("1");
    course.setCourseName("Java基礎コース");
    course.setCourseStartAt(LocalDateTime.now());
    course.setCourseEndAt(LocalDateTime.now().plusMonths(6));

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(course);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("数字のみ入力するようにしてください。");
  }


}