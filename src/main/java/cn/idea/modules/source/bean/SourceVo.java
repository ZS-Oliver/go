package cn.idea.modules.source.bean;

import cn.idea.modules.common.bean.BaseVo;
import cn.idea.modules.common.group.SaveGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@Alias("sourceVo")
@EqualsAndHashCode(callSuper = true)
public class SourceVo extends BaseVo {
    @NotNull(groups = SaveGroup.class, message = "学生来源类别不能为空")
    private Byte type;
    @NotBlank(groups = SaveGroup.class,message = "具体来源不能为空")
    @NotNull(groups = SaveGroup.class,message = "具体来源不能为空")
    private String source;
}
