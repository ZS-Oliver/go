package cn.idea.modules.student.bean;

import cn.idea.modules.common.bean.BaseVo;
import cn.idea.modules.common.group.SaveGroup;
import cn.idea.modules.common.group.UpdateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@Alias("studentVo")
@EqualsAndHashCode(callSuper = true)
public class StudentVo extends BaseVo {
    @NotNull(groups = SaveGroup.class, message = "用户名不能为空")
    @Length(min = 2, max = 20, groups = SaveGroup.class, message = "用户名长度为2-20")
    private String name;
    private String nickname;
    private String birthday;
    private Integer age;
    private String parentName;
    @NotNull(groups = SaveGroup.class, message = "手机号不能为空")
    @Length(min = 11, max = 11, groups = {SaveGroup.class, UpdateGroup.class}, message = "手机号应为11位")
    private String phone;
    private String alternateNumber;
    private String wechat;
    private String addr;
    private Integer sourceId;
    private Integer ctime;
    private Integer eid;
    @NotNull(groups = SaveGroup.class, message = "学员状态不能为空")
    private Byte state;
    @NotNull(groups = SaveGroup.class, message = "重要程度不能为空")
    private Byte degree;
    @NotNull(groups = SaveGroup.class, message = "销售阶段不能为空")
    private Byte salesStage;
    private String expectedDate;
    private String contractDate;
}
