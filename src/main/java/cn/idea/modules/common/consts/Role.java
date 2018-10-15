package cn.idea.modules.common.consts;

import com.google.common.collect.ImmutableMap;

import java.util.Optional;

public enum Role {
    ADMIN((byte) 0, "admin"),//管理员
    MARKETING_MANAGER((byte) 1, "marketing_manager"),//市场经理
    EMPLOYEE((byte) 2, "employee"); // 员工

    private byte code;
    private String name;

    Role(byte auth, String name) {
        this.code = auth;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }

    public volatile static ImmutableMap<Byte, Role> map = null;

    public static Role acquire(byte code) {
        if (map == null) { // 检查对象是否存在，防止不必要的加锁
            synchronized (Role.class) {
                if (map == null) { // 正常情况下的单例模式实现
                    ImmutableMap.Builder<Byte, Role> builder = ImmutableMap.builder();
                    for (Role role : values()) {
                        builder.put(role.code, role);
                    }
                    map = builder.build();
                }
            }
        }
        return map.get(code);
    }

    public static Optional<String> acquireName(byte code) {
        Role role = acquire(code);

        if (role != null) {
            return Optional.of(role.name);
        }
        return Optional.empty();
    }
}
