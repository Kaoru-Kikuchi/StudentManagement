package raisetech.student.management.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourse {

  private String id;

  private String studentId;

  @NotBlank(message = "コース名は必須です。")
  @Size(max = 100, message = "コース名は100文字以内で入力してください。")
  private String courseName;

  private LocalDateTime courseStartAt;

  private LocalDateTime courseEndAt;
}