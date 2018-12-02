package cn.idea.utils.mock.test;

import cn.idea.modules.common.consts.UserConst;
import cn.idea.modules.security.vo.UserVo;
import cn.idea.utils.kits.HashKit;

public class Users {
    public static UserVo genAdmin() {
        UserVo admin = new UserVo();
        admin.setPhone(UserConst.ADMIN_CODE);
        admin.setPwd(HashKit.md5("password").toUpperCase());
        return admin;
    }

    // 普通员工
    public static UserVo genEmployee() {
        UserVo employee = new UserVo();
        employee.setPhone("12345678901");
        employee.setPwd(UserConst.DEFAULT_PWD);
        return employee;
    }

    public static UserVo genMarketingManager() {
        UserVo marketingManager = new UserVo();
        marketingManager.setPhone("13011962616");
        marketingManager.setPwd(UserConst.DEFAULT_PWD);
        return marketingManager;
    }
}
