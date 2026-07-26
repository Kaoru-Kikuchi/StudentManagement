package raisetech.student.management.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@Schema(description = "受講生詳細")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {

  @Schema(description = "受講生情報")
  @Valid
  @NotNull(message = "受講生情報は必須です。")
  private Student student;

  @Schema(
      description = "受講生コース情報一覧"
  )
  @Valid
  @NotNull(message = "受講生コース情報は必須です。")
  private List<StudentCourse> studentCourseList;

}