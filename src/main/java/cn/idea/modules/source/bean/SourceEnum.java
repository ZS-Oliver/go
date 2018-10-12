package cn.idea.modules.source.bean;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum SourceEnum {
    COMMUNITY((byte) 0, "社区"),// 社区
    PRIMARY_SCHOOL((byte) 1, "小学"),// 小学
    KINDERGARTEN((byte) 2, "幼儿园"),// 幼儿园
    INTERNET((byte) 3, "网络"),// 网络
    INTRODUCTION((byte) 4, "转介绍"),//转介绍
    MARKET_SURVEY((byte) 5, "市调"),//市场调查
    DIRECT_VISIT((byte) 6, "直访"); // 直接访问

    public static final Map<Byte, String> kv = Arrays.stream(values()).collect(Collectors.toMap(SourceEnum::getCode, SourceEnum::getName));
    private byte code;
    private String name;


    SourceEnum(byte code, String name) {
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
