package cn.idea.modules.audiClasses.bean;

import cn.idea.modules.common.bean.BaseVo;
import cn.idea.modules.common.group.SaveGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.NotNull;


@Data
@Alias("audiClassesVo")
@EqualsAndHashCode(callSuper = true)
public class AudiClassesVo extends BaseVo {

    @NotNull(groups = SaveGroup.class, message = "班级名不能为空")
    private String name;
    @NotNull(groups = SaveGroup.class, message = "班级开课时间不能为空")
    private String auditionTime;
    private Integer opId;
    @NotNull(groups = SaveGroup.class, message = "总容量不能为空")
    private Integer total;
    private String sids;
    private String site;
}

