package raisetech.StudentManagement.controller;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;
import org.springframework.ui.Model;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentCourse> studentsCourses = service.searchStudentsCourseList();

    model.addAttribute("studentList", converter.convertStudentDetails(students, studentsCourses));
    System.out.println(students);
    return "studentList";

  }


  @GetMapping("/studentCourseList")
  public List<StudentCourse> getStudentsCourseList() {
    return service.searchStudentsCourseList().stream()
        .filter(c -> "Java基礎".equals(c.getCourseName()))
        .toList();
  }


  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    model.addAttribute("studentDetail", new StudentDetail());
    return "registerStudent";
  }


  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }
    Student student = new Student();

    int maxId = service.getMaxStudentId();  // 上記リポジトリメソッドを呼ぶ
    student.setId(String.valueOf(maxId + 1));

    student.setName(studentDetail.getStudent().getName());
    student.setKanaName(studentDetail.getStudent().getKanaName());
    student.setNickname(studentDetail.getStudent().getNickname());
    student.setEmail(studentDetail.getStudent().getEmail());
    student.setArea(studentDetail.getStudent().getArea());
    student.setAge(studentDetail.getStudent().getAge());
    student.setSex(studentDetail.getStudent().getSex());
    service.saveStudent(student);

    if (studentDetail.getStudentCourse() != null) {
      for (StudentCourse course : studentDetail.getStudentCourse()) {
        if (course.getCourseName() != null && !course.getCourseName().isBlank()) {
          int maxCourseId = service.getMaxStudentCourseId();
          course.setId(String.valueOf(maxCourseId + 1));
          course.setStudentId(student.getId());

          LocalDateTime startAt = LocalDateTime.now();
          course.setCourseStartAt(startAt);

          LocalDateTime endAt = startAt.plusMonths(1)
              .withHour(17)
              .withMinute(0)
              .withSecond(0)
              .withNano(0);
          course.setCourseEndAt(endAt);

          service.saveStudentCourse(course);
        }
      }
    }

    return "redirect:/studentList";


  }
}


