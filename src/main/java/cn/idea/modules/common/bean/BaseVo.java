package cn.idea.modules.common.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用实体
 */
@Data
public class BaseVo implements Serializable {
    private static byte VALID = 1;
    private static byte INVALID = 0;

    private Integer id;
    private Byte valid;

    private BaseVo(Integer id, Byte valid) {
        this.id = id;
        this.valid = valid;
    }

    protected BaseVo() {
    }

    /**
     * 用于创建和删除方法
     */
    public static BaseVo ofValid(Integer id) {
        return new BaseVo(id, VALID);
    }

    public static BaseVo ofInvalid(Integer id) {
        return new BaseVo(id,INVALID);
    }
}
