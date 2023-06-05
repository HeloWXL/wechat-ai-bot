package wx.util;

/**
 * @author wangxl
 * @ClassName ResultEnum
 * @Description TODO
 * @date 2020/11/18 22:34
 */
public enum ResultEnum {

  NO_LOGIN(401, "用户未登录"),

  ERROR_USERNAME_PASSWORD(500, "用户名或密码错误"),

  NO_USER_NAME(500, "用户名不能为空"),

  ERROR(500, "服务器内部错误"),

  SUCCESS(200, "成功"),

  LOGIN_SUCCESS(200, "登录成功"),

  NO_PASS_WORD(500, "密码不能为空"),

  UPLOAD_USCCESS(200, "上传成功"),

  UPLOAD_FILE_TOO_MAX(500, "文件过大，请压缩处理"),

  UPLOAD_ERROR(500, "上传文件异常");


  private Integer code;

  private String msg;

  ResultEnum(Integer code, String desc) {
    this.code = code;
    this.msg = desc;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
