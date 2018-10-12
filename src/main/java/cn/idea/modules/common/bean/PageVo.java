package cn.idea.modules.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageVo<Vo> {
    private Integer no; // 当前页码
    private List<Vo> list;
    private Integer totalPage; // 总页数
    private Integer total; // 总条目数
}
