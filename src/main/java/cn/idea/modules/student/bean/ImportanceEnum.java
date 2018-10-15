package cn.idea.modules.student.bean;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum ImportanceEnum {
    IMPORTANT((byte) 0, "重要"), // 重要
    NORMAL((byte) 1, "一般"), // 一般
    UNIMPORTANT((byte) 2, "不重要"); // 不重要

    public static final Map<Byte, String> kv = Arrays.stream(values()).collect(Collectors.toMap(ImportanceEnum::getCode, ImportanceEnum::getName));
    private byte code;
    private String name;


    ImportanceEnum(byte code, String name) {
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
