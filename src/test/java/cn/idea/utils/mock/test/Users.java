package cn.idea.utils.mock.test;

import cn.idea.modules.common.consts.UserConst;
import cn.idea.modules.security.vo.UserVo;
import cn.idea.utils.kits.HashKit;

public class Users {
    public static UserVo genAdmin() {
        UserVo admin = new UserVo();
        admin.setCode(UserConst.ADMIN_CODE);
        admin.setPwd(HashKit.md5("password").toUpperCase());
        return admin;
    }

    // 普通员工
    public static UserVo genEmployee() {
        UserVo employee = new UserVo();
        employee.setCode("12345678912");
        employee.setPwd(UserConst.DEFAULT_PWD);
        return employee;
    }
}
