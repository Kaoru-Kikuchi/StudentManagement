package raisetech.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.CourseRegistrationStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.CourseRegistrationStatusRepository;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private final StudentRepository repository;
  private final StudentConverter converter;
  // 変更点①：CourseRegistrationStatusRepository を追加
  private final CourseRegistrationStatusRepository statusRepository;

  // 変更点②：コンストラクタに追加
  @Autowired
  public StudentService(StudentRepository repository,
      StudentConverter converter,
      CourseRegistrationStatusRepository statusRepository) {
    this.repository = repository;
    this.converter = converter;
    this.statusRepository = statusRepository;
  }

  // 変更点③：searchStudentListで各コースにステータスをセット
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
    List<StudentDetail> details = converter.convertStudentDetails(studentList, studentCourseList);

    // 各コースに登録ステータスをセット
    details.forEach(detail -> {
      detail.getStudentCourseList().forEach(course -> {
        List<CourseRegistrationStatus> statusList = statusRepository.findByStudentCourseId(
            course.getId());
        if (!statusList.isEmpty()) {
          course.setRegistrationStatus(statusList.get(0).getRegistrationStatus());
        }
      });
    });
    return details;
  }

  // 変更点④：searchStudentでもステータスをセット
  public StudentDetail searchStudent(int id) {
    Student student = repository.searchStudent(id);
    List<StudentCourse> studentCourse = repository.searchStudentCourse(student.getId());

    // ステータス追加
    studentCourse.forEach(course -> {
      List<CourseRegistrationStatus> statusList = statusRepository.findByStudentCourseId(
          course.getId());
      if (!statusList.isEmpty()) {
        course.setRegistrationStatus(statusList.get(0).getRegistrationStatus());
      }
    });
    return new StudentDetail(student, studentCourse);
  }

  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    repository.registerStudent(student);

    studentDetail.getStudentCourseList().forEach(studentCourse -> {
      initStudentsCourse(studentCourse, student.getId());
      repository.registerStudentCourse(studentCourse);

      // 変更点⑤：新規登録時、ステータスを仮申込に初期化
      CourseRegistrationStatus status = new CourseRegistrationStatus();
      status.setStudentCourseId(studentCourse.getId());
      status.setRegistrationStatus("仮申込");
      // ステータス登録用メソッドを追加して repository に保存
      // （別に insertCourseRegistrationStatus メソッドを作る必要があります）
      statusRepository.insert(status);
    });
    return studentDetail;
  }

  void initStudentsCourse(StudentCourse studentCourse, int id) {
    LocalDateTime now = LocalDateTime.now();
    studentCourse.setStudentId(id);
    studentCourse.setCourseStartAt(now);
    studentCourse.setCourseEndAt(now.plusYears(1));
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    for (StudentCourse studentCourse : studentDetail.getStudentCourseList()) {
      repository.updateStudentCourse(studentCourse);

      // 変更点⑥：ステータス更新（必要に応じて）
      if (studentCourse.getRegistrationStatus() != null) {
        List<CourseRegistrationStatus> statusList = statusRepository.findByStudentCourseId(
            studentCourse.getId());
        if (!statusList.isEmpty()) {
          CourseRegistrationStatus status = statusList.get(0);
          status.setRegistrationStatus(studentCourse.getRegistrationStatus());
          statusRepository.update(status);
        }
      }
    }
  }
}
