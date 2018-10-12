package cn.idea.modules.security.vo;

import cn.idea.modules.common.group.SaveGroup;
import cn.idea.modules.common.group.UpdateGroup;
import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * 网站端用户实体
 */
@Data
@Alias("userVo")
public class UserVo {
    @NotBlank(groups = SaveGroup.class, message = "手机号或工号不能为空")
    @Length(min = 8, max = 20, message = "手机号或工号长度应为8-20")
    private String code; // 工号或手机号

    @NotBlank(groups = SaveGroup.class, message = "密码不能为空")
    @Pattern(regexp = "^[0-9A-F]{32}$", groups = {SaveGroup.class, UpdateGroup.class}, message = "密码应为32位16进制")
    private String pwd; // 密码

    private Integer auth;// 角色
}
