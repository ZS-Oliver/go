package cn.idea.utils.assistant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * ResponseEntity工具
 */
public final class ResponseEntities {

    public static final ResponseEntity<Void> UNAUTHORIZED = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    public static final ResponseEntity<Void> CONFLICT = new ResponseEntity<>(HttpStatus.CONFLICT);
    public static final ResponseEntity<Void> INTERNAL_SERVER_ERROR = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    public static final ResponseEntity<Void> UNAUTHENTICATED = new ResponseEntity<Void>(HttpStatus.PAYMENT_REQUIRED);
    /**
     * Session会话过期
     */
    private static final ResponseEntity BAD_REQUEST = new ResponseEntity(HttpStatus.BAD_REQUEST);
    private static final ResponseEntity OK = new ResponseEntity(HttpStatus.OK);

    private ResponseEntities() {
    }

    /**
     * 本次操作成功
     */
    @SuppressWarnings("unchecked")
    public static <T> ResponseEntity<T> ok() {
        return OK;
    }

    /**
     * 本次操作成功
     *
     * @param o 需要返回的对象
     */
    public static <T> ResponseEntity<T> ok(T o) {
        return new ResponseEntity<>(o, HttpStatus.OK);
    }


    /**
     * 一般的业务操作失败
     * 返回带有 CONFLICT 的 Couple
     *
     * @param msg  需要带到前端的信息，支持%s，需要设置args
     * @param args 拼接如msg中的参数
     */
    public static ResponseEntity<String> fail(String msg, Object... args) {
        if (args != null && args.length > 0) {
            msg = String.format(msg, args);
        }
        return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
    }
}
