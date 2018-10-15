package cn.idea.modules.common.aop;

import com.google.common.base.Joiner;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 配合{@link cn.idea.modules.common.annotation.MethodMonitor} 使用，用于监控方法的入参，返回值及异常
 */
@Log4j2
@Aspect
@Component
public class MethodMonitorAspect implements Ordered {
    private Joiner joiner = Joiner.on(",").useForNull("<null>");

    // 因为还有其他织入增强，所有采用@within而不是@target来实现，这样就防止启动报错了
    @Around("@within(cn.idea.modules.common.annotation.MethodMonitor)")
    public Object printParamsAndRet(ProceedingJoinPoint joinPoint) throws Throwable {

        String clazzName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        String fullName = (clazzName + "#" + methodName).intern(); // 运行时会频繁调用，所以将共有的放入常量池中

        // 不对参数使用JSON#toJSONString，防止出错，遇到过不支持MockHttpSession出错的情况
        String params = joiner.join(joinPoint.getArgs());
        log.info(fullName + " → (" + params + ")");

        Object ret;

        // 处理异常的情况
        try {
            ret = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.info(fullName + " ! " + throwable.getMessage());
            throw throwable;
        }

        log.info(fullName + " ← " + ret);

        return ret;
    }

    // 用于多个注入间的排序
    @Override
    public int getOrder() {
        return 1;
    }
}
