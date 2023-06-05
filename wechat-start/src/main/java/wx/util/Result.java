package wx.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {
    /**
     * 响应前端是否成功标识
     */
    private Integer code;
    /**
     * 响应信息
     */
    private String msg;
    /**
     * 响应数据
     */
    private Object data;

    public Result() {
    }

    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 响应成功
     */
    public static Result success(String msg, Object data) {
        return new Result(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),data);
    }

    /**
     * 响应成功
     */
    public static Result success(String msg) {
        return new Result(ResultEnum.SUCCESS.getCode(),msg);
    }

    /**
     * 响应失败
     */
    public static Result fail(String msg) {
        return new Result(ResultEnum.ERROR.getCode(), msg,null);
    }

    /**
     * 响应失败
     * @return
     */
    public static Result fail() {
        return new Result(ResultEnum.ERROR.getCode(), ResultEnum.ERROR.getMsg(),null);
    }

}
