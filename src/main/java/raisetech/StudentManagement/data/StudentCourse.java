package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生コース情報")
@Getter
@Setter
public class StudentCourse {

  @Schema(description = "コースID", example = "101")
  private int id;

  @Schema(description = "受講生ID", example = "1")
  private int studentId;

  @Schema(description = "コース名", example = "Java基礎")
  private String courseName;

  @Schema(description = "コース開始日", example = "2025-04-01T10:00:00")
  private LocalDateTime courseStartAt;

  @Schema(description = "コース終了日", example = "2025-09-30T18:00:00")
  private LocalDateTime courseEndAt;

  @Schema(description = "コース申込状況", example = "仮申込")
  private String registrationStatus; // 追加
}
