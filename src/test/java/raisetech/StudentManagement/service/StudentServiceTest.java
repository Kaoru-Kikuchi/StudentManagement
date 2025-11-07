package raisetech.StudentManagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.CourseRegistrationStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.CourseRegistrationStatusRepository;
import raisetech.StudentManagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  @Mock
  private CourseRegistrationStatusRepository statusRepository;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter, statusRepository);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    StudentCourse course = new StudentCourse();
    course.setId(1);
    studentCourseList.add(course);

    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
    when(converter.convertStudentDetails(studentList, studentCourseList))
        .thenReturn(List.of(new StudentDetail(new Student(), studentCourseList)));

    when(statusRepository.findByStudentCourseId(1))
        .thenReturn(List.of(new CourseRegistrationStatus(1, 1, "仮申込")));

    sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
    verify(statusRepository, times(1)).findByStudentCourseId(1);
  }

  @Test
  void 受講生詳細検索_IDに紐づく受講生情報と受講生コース情報を取得できること() {
    int id = 1;
    Student student = new Student();
    student.setId(id);
    StudentCourse course = new StudentCourse();
    course.setId(1);
    List<StudentCourse> courseList = List.of(course);

    when(repository.searchStudent(id)).thenReturn(student);
    when(repository.searchStudentCourse(id)).thenReturn(courseList);
    when(statusRepository.findByStudentCourseId(1))
        .thenReturn(List.of(new CourseRegistrationStatus(1, 1, "仮申込")));

    StudentDetail actual = sut.searchStudent(id);

    verify(repository, times(1)).searchStudent(id);
    verify(repository, times(1)).searchStudentCourse(id);
    verify(statusRepository, times(1)).findByStudentCourseId(1);

    assertEquals(id, actual.getStudent().getId());
    assertEquals("仮申込", actual.getStudentCourseList().get(0).getRegistrationStatus());
  }

  @Test
  void 受講生詳細の登録_受講生と受講生コース情報とステータスを登録できること() {
    Student student = new Student();
    student.setId(1);
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId(1);
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    StudentDetail studentDetails = new StudentDetail(student, studentCourseList);

    sut.registerStudent(studentDetails);

    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentCourse(studentCourse);
    verify(statusRepository, times(1))
        .insert(org.mockito.Mockito.any(CourseRegistrationStatus.class));
  }

  @Test
  void 受講生詳細の更新_受講生と受講生コース情報とステータスを更新できること() {
    Student student = new Student();
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId(1);
    studentCourse.setRegistrationStatus("受講中");
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    StudentDetail studentDetails = new StudentDetail(student, studentCourseList);

    when(statusRepository.findByStudentCourseId(1))
        .thenReturn(List.of(new CourseRegistrationStatus(1, 1, "仮申込")));

    sut.updateStudent(studentDetails);

    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(1)).updateStudentCourse(studentCourse);
    verify(statusRepository, times(1))
        .update(org.mockito.Mockito.any(CourseRegistrationStatus.class));
  }

  @Test
  void 受講生詳細の登録_初期化処理が行われること() {
    int id = 1;
    Student student = new Student();
    student.setId(id);
    StudentCourse studentCourse = new StudentCourse();

    sut.initStudentsCourse(studentCourse, student.getId());

    assertEquals(id, studentCourse.getStudentId());
    assertEquals(LocalDateTime.now().getHour(), studentCourse.getCourseStartAt().getHour());
    assertEquals(LocalDateTime.now().plusYears(1).getYear(),
        studentCourse.getCourseEndAt().getYear());
  }
}
