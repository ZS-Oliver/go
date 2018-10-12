package cn.idea.modules.common.bean;

import lombok.Data;

@Data
public class BaseQVo {
    protected int offset;
    protected int size;

    public void setPageRange(int offset, int size) {
        this.offset = offset;
        this.size = size;
    }
}
