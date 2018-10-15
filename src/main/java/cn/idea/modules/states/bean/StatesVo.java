package cn.idea.modules.states.bean;

import cn.idea.modules.common.bean.BaseVo;
import cn.idea.modules.common.group.SaveGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.NotNull;

@Data
@Alias("statesVo")
@EqualsAndHashCode(callSuper = true)
public class StatesVo extends BaseVo {
    //    @NotNull(groups = SaveGroup.class, message = "跟进人id不能为空")
    private Integer opId;
    @NotNull(groups = SaveGroup.class, message = "学员id不能为空")
    private Integer sid;
    //    @NotNull(groups = SaveGroup.class, message = "更改状态的时间不能为空")
    private Integer ctime;
    @NotNull(groups = SaveGroup.class, message = "当前阶段的状态不能为空")
    private Byte curStage;
    private String content;
    private Byte nextState;
    private Byte nextDegree;
    private Byte nextStage;
    private String exceptedDate;
}
