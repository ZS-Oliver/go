package cn.idea.modules.security.util;

import cn.idea.modules.common.bean.BaseVo;
import cn.idea.modules.common.consts.UserConst;

/**
 * 处理用户的工具
 */
public class UserUtil {

    // 管理员身份
    public static final BaseVo ADMIN = BaseVo.ofValid(UserConst.ADMIN_ID);

    /**
     * 用于判断是否为管理员账号
     *
     * @param code 账号
     * @return 是否为管理员账号
     */
    public static boolean isAdminCode(String code) {
        return UserConst.ADMIN_CODE.equals(code);
    }

}
