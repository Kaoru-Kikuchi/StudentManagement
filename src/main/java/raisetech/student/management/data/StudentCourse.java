package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生コース情報")
@Getter
@Setter
public class StudentCourse {

  @Schema(
      description = "受講生コースID",
      example = "ebed9c78-7942-11f1-b4d0-b81ea42bf144"
  )
  private String id;

  @Schema(
      description = "受講生ID",
      example = "ada1f007-7942-11f1-b4d0-b81ea42bf144"
  )
  private String studentId;

  @Schema(
      description = "コース名",
      example = "Java基礎"
  )
  @NotBlank(message = "コース名は必須です。")
  @Size(max = 100, message = "コース名は100文字以内で入力してください。")
  private String courseName;

  @Schema(
      description = "コース開始日時",
      example = "2026-07-01T09:00:00"
  )
  private LocalDateTime courseStartAt;

  @Schema(
      description = "コース終了日時",
      example = "2027-07-01T09:00:00"
  )
  private LocalDateTime courseEndAt;

  @Schema(
      description = "申込状況"
  )
  private StudentCourseStatus studentCourseStatus;
}