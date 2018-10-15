package cn.idea.utils.mock.test;

import cn.idea.modules.common.consts.UserConst;
import cn.idea.modules.security.vo.UserVo;
import cn.idea.utils.kits.HashKit;

public class Users {
    public static UserVo genAdmin() {
        UserVo admin = new UserVo();
<<<<<<< HEAD
        admin.setPhone(UserConst.ADMIN_CODE);
=======
        admin.setCode(UserConst.ADMIN_CODE);
>>>>>>> 7cb7a9d8ca24c2a7882d480f525ee7b7fd4e1a83
        admin.setPwd(HashKit.md5("password").toUpperCase());
        return admin;
    }

    // 普通员工
    public static UserVo genEmployee() {
        UserVo employee = new UserVo();
<<<<<<< HEAD
        employee.setPhone("12345678912");
        employee.setPwd(UserConst.DEFAULT_PWD);
        return employee;
    }

    public static UserVo genMarketingManager() {
        UserVo marketingManager = new UserVo();
        marketingManager.setPhone("15232786339");
        marketingManager.setPwd(UserConst.DEFAULT_PWD);
        return marketingManager;
    }
=======
        employee.setCode("12345678912");
        employee.setPwd(UserConst.DEFAULT_PWD);
        return employee;
    }
>>>>>>> 7cb7a9d8ca24c2a7882d480f525ee7b7fd4e1a83
}
