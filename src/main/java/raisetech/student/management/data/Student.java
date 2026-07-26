package raisetech.student.management.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

  private String id;

  @NotBlank(message = "名前は必須です。")
  @Size(max = 100, message = "名前は100文字以内で入力してください。")
  private String name;

  @NotBlank(message = "カナ名は必須です。")
  @Size(max = 100, message = "カナ名は100文字以内で入力してください。")
  private String kanaName;

  @NotBlank(message = "ニックネームは必須です。")
  @Size(max = 50, message = "ニックネームは50文字以内で入力してください。")
  private String nickname;

  @NotBlank(message = "メールアドレスは必須です。")
  @Email(message = "メールアドレスの形式が正しくありません。")
  @Size(max = 255, message = "メールアドレスは255文字以内で入力してください。")
  private String email;

  @NotBlank(message = "地域は必須です。")
  @Size(max = 100, message = "地域は100文字以内で入力してください。")
  private String area;

  @Min(value = 0, message = "年齢は0歳以上で入力してください。")
  @Max(value = 120, message = "年齢は120歳以下で入力してください。")
  private int age;

  @NotBlank(message = "性別は必須です。")
  private String sex;

  @Size(max = 500, message = "備考は500文字以内で入力してください。")
  private String remark;

  private boolean isDeleted;
}