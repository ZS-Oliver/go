package cn.idea.modules.security.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 新旧密码对
 */
@Data
public class PwdPair {
    @NotNull
    @Length(min = 32, max = 32)
    private String oldPwd;
    @NotNull
    @Length(min = 32, max = 32)
    private String newPwd;
}
