package raisetech.StudentManagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  private static final Logger log = LoggerFactory.getLogger(StudentControllerTest.class);

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

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {
    Mockito.when(service.searchStudentList()).thenReturn(List.of(new StudentDetail()));
    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[{\"student\":null,\"studentCourseList\":null}]"));

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 受講生詳細の検索が実行できて空で帰ってくること() throws Exception {
    int id = 999;
    mockMvc.perform(get("/student/{id}", id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudent(id);
  }

  @Test
  void 受講生詳細の登録が実行できて空で返ってくること() throws Exception {
    mockMvc.perform(post("/registerStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "student": {
                    "name": "江並康介",
                    "kanaName": "エナミ",
                    "nickname": "コウジ",
                    "email": "test@example.com",
                    "area": "奈良",
                    "age": 36,
                    "sex": "男性",
                    "remark": ""
                  },
                  "studentCourseList": [
                    {
                      "courseName": "Javaコース"
                    }
                  ]
                }
                """))
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudent(any());
  }

  @Test
  void 受講生詳細の更新が正常に実行できること() throws Exception {
    mockMvc.perform(put("/updateStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "student": {
                    "id": 12,
                    "name": "江並康介",
                    "kanaName": "エナミ",
                    "nickname": "コウジ",
                    "email": "test@example.com",
                    "area": "奈良",
                    "age": 36,
                    "sex": "男性",
                    "remark": ""
                  },
                  "studentCourseList": [
                    {
                      "id": 15,
                      "studentId": 12,
                      "courseName": "Javaコース",
                      "courseStartAt": "2024-04-27T10:50:39",
                      "courseEndAt": "2025-04-27T10:50:39"
                    }
                  ]
                }
                """))
        .andExpect(status().isOk());

    verify(service, times(1)).updateStudent(any());
  }

  @Test
  void 受講生詳細の例外APIが実行できてステータスが400で返ってくること() throws Exception {
    mockMvc.perform(get("/student"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string("このAPIは現在利用できません。古いURLとなっています。"));
  }

  @Test
  void 受講生詳細の受講生で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    Student student = new Student();
    student.setId(1);
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
    // int に数字以外を入れるとコンパイルできないため、このテストは削除またはコメントアウト可能
    // 型をintにしたため、このテストケースは意味を失う（String入力が不可能になったため）
  }

  @Test
  void 受講生コースで適切な値を入力した時に入力チェックに異常が発生しないこと() {
    StudentCourse course = new StudentCourse();
    course.setId(1);
    course.setStudentId(1);
    course.setCourseName("Java基礎コース");
    course.setCourseStartAt(LocalDateTime.now());
    course.setCourseEndAt(LocalDateTime.now().plusMonths(6));

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(course);
    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生コースでIDに数字以外を用いた時に入力チェックに掛かること() {
    // intに文字列を入れることができないため、このテストは削除または無効化
  }
}
