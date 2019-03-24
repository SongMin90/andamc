package top.songm.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 返回消息统一处理
 * @author songm
 * @datetime 2018/12/23 15:30
 */
@Getter
@Setter
public class Msg {

    private boolean success;
    private String msg;
    private Object data;

    public Msg() {
        this.success = true;
        this.msg = "ok";
    }

    public Msg(String msg) {
        this.msg = msg;
    }

    @Getter
    @AllArgsConstructor
    public enum MessageEnum {
        INVALID_REQUEST(-1, "无效请求"),
        FIND_ERROR(0, "找到错误：{}"),
        VERIFY_USER_FAIL(1, "验证用户失败")
        ;
        private int code;
        private String name;
    }
}
