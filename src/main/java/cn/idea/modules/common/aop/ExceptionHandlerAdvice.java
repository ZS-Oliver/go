package cn.idea.modules.common.aop;

import cn.idea.modules.common.exception.ServiceException;
import cn.idea.utils.assistant.ResponseEntities;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一异常处理
 */
@Log4j2
@ControllerAdvice("cn.idea.modules") // 这个不支持通配符啊！
public class ExceptionHandlerAdvice {

    /**
     * 用户未登录即访问资源，返回402
     */
    @ExceptionHandler(UnauthenticatedException.class)
    ResponseEntity<Void> handleUnauthenticatedException(UnauthenticatedException e) {
        log.info(e.getMessage());
        return ResponseEntities.UNAUTHENTICATED;
    }

    /**
     * Shiro的权限认证异常处理，返回401
     */
    @ExceptionHandler(AuthorizationException.class)
    ResponseEntity<Void> handleAuthorizationException(AuthorizationException e) {
        log.info(e.getMessage());
        return ResponseEntities.UNAUTHORIZED;
    }

    /**
     * 参数校验的异常处理，返回400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map<String, String> errMap = new HashMap<>();
        for (FieldError fieldError : fieldErrors) {
            errMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(errMap, HttpStatus.BAD_REQUEST);
    }


    /**
     * 业务异常的统一处理
     */
    @ExceptionHandler(ServiceException.class)
    ResponseEntity<String> handleServiceException(ServiceException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }


    /**
     * 其余异常处理，返回500
     */
    @ExceptionHandler(Exception.class)
    ResponseEntity<Void> handle(Exception e) {
        log.error("未知异常", e);
        return ResponseEntities.INTERNAL_SERVER_ERROR;
    }
}
