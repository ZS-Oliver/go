package cn.idea.modules.student.bean;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum SalesStageEnum {
    NOINTENTION((byte) 0, "没有意向"),
    FOLLOW((byte) 1, "继续跟进"),
    AUDITION((byte) 2, "试听课程"),
    BECOMEVIP((byte) 3, "成为会员");

    public static final Map<Byte, String> kv = Arrays.stream(values()).collect(Collectors.toMap(SalesStageEnum::getCode, SalesStageEnum::getName));
    private byte code;
    private String name;


    SalesStageEnum(byte code, String name) {
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
