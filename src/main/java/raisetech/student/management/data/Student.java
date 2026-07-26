package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生")
@Getter
@Setter
public class Student {

  @Schema(
      description = "受講生ID",
      example = "ada1f007-7942-11f1-b4d0-b81ea42bf144"
  )
  private String id;

  @Schema(
      description = "氏名",
      example = "山田 太郎"
  )
  @NotBlank(message = "名前は必須です。")
  @Size(max = 100, message = "名前は100文字以内で入力してください。")
  private String name;

  @Schema(
      description = "カナ名",
      example = "ヤマダ タロウ"
  )
  @NotBlank(message = "カナ名は必須です。")
  @Size(max = 100, message = "カナ名は100文字以内で入力してください。")
  private String kanaName;

  @Schema(
      description = "ニックネーム",
      example = "たろう"
  )
  @NotBlank(message = "ニックネームは必須です。")
  @Size(max = 50, message = "ニックネームは50文字以内で入力してください。")
  private String nickname;

  @Schema(
      description = "メールアドレス",
      example = "yamada@example.com"
  )
  @NotBlank(message = "メールアドレスは必須です。")
  @Email(message = "メールアドレスの形式が正しくありません。")
  @Size(max = 255, message = "メールアドレスは255文字以内で入力してください。")
  private String email;

  @Schema(
      description = "地域",
      example = "東京都"
  )
  @NotBlank(message = "地域は必須です。")
  @Size(max = 100, message = "地域は100文字以内で入力してください。")
  private String area;

  @Schema(
      description = "年齢",
      example = "25",
      minimum = "0",
      maximum = "120"
  )
  @Min(value = 0, message = "年齢は0歳以上で入力してください。")
  @Max(value = 120, message = "年齢は120歳以下で入力してください。")
  private int age;

  @Schema(
      description = "性別",
      example = "男性"
  )
  @NotBlank(message = "性別は必須です。")
  private String sex;

  @Schema(
      description = "備考",
      example = "Javaコース受講中"
  )
  @Size(max = 500, message = "備考は500文字以内で入力してください。")
  private String remark;

  @Schema(
      description = "論理削除フラグ",
      example = "false"
  )
  private boolean isDeleted;
}