package cn.idea.modules.employee.bean;

import cn.idea.modules.common.bean.BaseVo;
import cn.idea.modules.common.group.SaveGroup;
import cn.idea.modules.common.group.UpdateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@Alias("employeeVo")
@EqualsAndHashCode(callSuper = true)
public class EmployeeVo extends BaseVo {
    @NotNull(groups = SaveGroup.class, message = "用户名不能为空")
    @Length(min = 2, max = 20, groups = SaveGroup.class, message = "用户名长度为2-20")
    private String name;
    @NotNull(groups = SaveGroup.class, message = "手机号不能为空")
    @Length(min = 11, max = 11, groups = {SaveGroup.class, UpdateGroup.class}, message = "手机号应为11位")
    private String phone;
    private Integer mid;
    private String school;
    private Byte auth;
    private String passwd;
}
