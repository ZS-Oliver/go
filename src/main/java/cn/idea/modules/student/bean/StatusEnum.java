package cn.idea.modules.student.bean;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum StatusEnum {
    VALID((byte) 0, "有效"),
    INVALID((byte) 1, "无效"),
    VIP((byte) 2, "会员");

    public static final Map<Byte, String> kv = Arrays.stream(values()).collect(Collectors.toMap(StatusEnum::getCode, StatusEnum::getName));
    private byte code;
    private String name;


    StatusEnum(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
