package cn.idea.modules.common.bean;

import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

@Log4j2
public class ConflictingQVo extends BaseQVo {
    public boolean isConflicting; // 是否冲突

    protected ConflictingQVo(boolean isConflicting) {
        this.isConflicting = isConflicting;
    }

    public static class Builder<Q extends ConflictingQVo> {
        protected Q q;
        private Class<Q> clazz;

        // 通过构造函数将Q初始化
        protected Builder(Class<Q> clazz) {
            try {
                this.clazz = clazz;

                Constructor<Q> constructor = clazz.getDeclaredConstructor();// 得到构造器
                constructor.setAccessible(true); // 获取私有成员变量
                q = constructor.newInstance(); // 创建对象(ConflictingQVo)
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 构建查询对象
         */
        public Q build() {
            Method[] methods = clazz.getMethods();
            long numOfContainsValue = Arrays.stream(methods).filter(method -> method.getName().startsWith("get"))
                    .map(method -> {
                        try {
                            return method.invoke(q);
                        } catch (ReflectiveOperationException e) {
                            log.error("通过反射执行get方法有误，q = {}", q, e);
                        }
                        return null;
                    }).distinct().filter(Objects::nonNull).count();
            if (numOfContainsValue == 0) return null;

            return q;
        }
    }
}
