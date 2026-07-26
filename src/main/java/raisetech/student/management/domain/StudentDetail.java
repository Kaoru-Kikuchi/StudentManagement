package raisetech.student.management.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {

  @Valid
  @NotNull(message = "受講生情報は必須です。")
  private Student student;

  @Valid
  @NotNull(message = "受講生コース情報は必須です。")
  private List<StudentCourse> studentCourseList;

}