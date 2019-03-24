package top.songm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import top.songm.model.response.Msg;

/**
 * 全局异常处理器
 * @author songm
 * @datetime 2018/12/23 15:27
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Msg myException(Exception e) {
        e.printStackTrace();
        return new Msg(e.getMessage());
    }

    @ExceptionHandler(value = UploadException.class)
    @ResponseStatus(code = HttpStatus.NOT_IMPLEMENTED)
    @ResponseBody
    public String uploadException(Exception e) {
        e.printStackTrace();
        return "文件上传失败！";
    }

}
