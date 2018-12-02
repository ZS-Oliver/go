package cn.idea.modules.security.web;

import cn.idea.modules.common.annotation.MethodMonitor;
import cn.idea.modules.common.bean.BaseVo;
import cn.idea.modules.common.consts.OperationConst;
import cn.idea.modules.common.consts.Role;
import cn.idea.modules.common.consts.SessionConst;
import cn.idea.modules.common.exception.ServiceException;
import cn.idea.modules.common.group.UpdateGroup;
import cn.idea.modules.employee.bean.EmployeeVo;
import cn.idea.modules.marketingManager.bean.MarketingManagerVo;
import cn.idea.modules.security.service.UserService;
import cn.idea.modules.security.vo.PwdPair;
import cn.idea.modules.security.vo.UserVo;
import cn.idea.utils.assistant.ResponseEntities;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @apiDefine UserController 用户相关
 */
@MethodMonitor
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * @api {post} /user > 登录
     * @apiGroup UserController
     * @apiPermission 任何人
     * @apiDescription 该接口可用于员工和市场经理两者的登录。
     * @apiParam {String} phone 账号
     * @apiParam {String} pwd 密码（需要将用户填写的内容用md5加密，并统一转化为大写）
     * @apiParamExample {json} 参数样例(市场经理)
     * {"phone":"15232786339","pwd":"25D55AD283AA400AF464C76D713C07AD"}
     * @apiErrorExample {json} 登录失败2
     * "用户名或密码错误"
     */
    @PostMapping
    public void login(HttpSession session,
                      @RequestBody @Valid UserVo uv
    ) throws ServiceException {
        userService.login(uv, session);
    }

    /**
     * @api {get} /user/auth > 获取登陆人权限
     * @apiGroup UserController
     * @apiPermission 登录状态
     * @apiDescription 该接口用于获取登陆人权限
     * @apiSuccess {byte} auth 登陆人权限
     * @apiSuccessExample 成功样例
     * 2
     */
    @GetMapping("/auth")
    @RequiresAuthentication
    public byte acquireAuth(@SessionAttribute(SessionConst.AUTH) Byte auth) {
        return auth;
    }

    /**
     * @api {post} /user/temp > 登录（测试用）
     * @apiGroup UserController
     * @apiPermission 任何人
     * @apiDescription 该接口可用于员工和市场经理两者的登录。
     * @apiParam {String} phone 账号
     * @apiParam {String} pwd 密码（需要将用户填写的内容用md5加密，并统一转化为大写）
     * @apiParamExample {json} 管理员
     * {"phone":"00000000","pwd":"5F4DCC3B5AA765D61D8327DEB882CF99"}
     * @apiErrorExample {json} 登录失败
     * "用户名或密码错误"
     */
    @PostMapping("/temp")
    public void loginForTest(HttpSession session,
                             @RequestBody @Valid UserVo uv
    ) throws ServiceException {
        userService.login(uv, session);
    }

    /**
     * @api {delete} /user > 退出登录
     * @apiGroup UserController
     * @apiPermission 登录状态
     * @apiDescription 该接口用于让用户退出登录状态，以保护用户的安全。
     */
    @DeleteMapping
    @RequiresAuthentication
    public void logout(HttpSession session) {
        userService.logout(session);
    }

    /**
     * @api {get} /user > 个人信息展示
     * @apiGroup UserController
     * @apiPermission 已登录状态
     * @apiDescription 该接口用于获取当前登录用户的基本信息，注意管理员、员工和市场经理的信息是不同的。
     * @apiSuccess {Integer} id id
     * @apiSuccess {Integer} auth 具体权限：1-市场经理，2-员工
     * @apiSuccess {String} name 姓名
     * @apiSuccess {String} phone 手机号
     * @apiSuccess {String} school 学校名称
     * @apiSuccessExample {json} 管理员
     * {"id":-1,"valid":1}
     * @apiSuccessExample {json} 厂内员工
     * {"auth":2,"id":1,"name":"员工[测试]","phone":"12345678912","school":"东北大学"}
     * @apiSuccessExample {json} 市场经理
     * {"auth":1,"id":7,"name":"王艳涛","phone":"15232786339","school":"东北大学"}
     */
    @GetMapping
    @RequiresAuthentication
    public BaseVo info(HttpSession session) throws ServiceException {
        return userService.info(session);
    }

    /**
     * @api {put} /user/marketingManager > 更新个人信息（市场经理）
     * @apiGroup UserController
     * @apiPermission 已登录状态
     * @apiDescription 该接口用于更新市场经理的个人信息
     * @apiParam {String} [name] 姓名
     * @apiParam {String} [phone] 手机号
     * @apiParam {String} [school] 学校
     * @apiParamExample {json} 请求样例
     * {"phone":"15232786339","school":"重庆大学"}
     * @apiErrorExample {json} 失败样例
     * "手机号同已有账号重复"
     */
    @PutMapping("/marketingManager")
    @RequiresAuthentication
    public void updateInfo(@RequestBody @Validated(UpdateGroup.class) MarketingManagerVo marketingManagerVo,
                           @SessionAttribute(SessionConst.UID) Integer uid,
                           @SessionAttribute(SessionConst.AUTH) Byte auth) throws ServiceException {
        ServiceException.when(Role.MARKETING_MANAGER.getCode() != auth, "身份不匹配，无法更新信息");
        userService.updateInfo(marketingManagerVo, uid);
    }

    /**
     * @api {put} /user/employee > 更新个人信息（员工）
     * @apiGroup UserController
     * @apiPermission 已登录状态
     * @apiDescription 该接口用于更新员工的个人信息
     * @apiParam {String} [name] 姓名
     * @apiParam {String} [phone] 手机号
     * @apiErrorExample {json} 失败样例
     * "手机号同已有账号重复"
     */
    @PutMapping("/employee")
    @RequiresAuthentication
    public void updateInfo(@RequestBody @Validated(UpdateGroup.class) EmployeeVo employeeVo,
                           @SessionAttribute(SessionConst.UID) Integer uid,
                           @SessionAttribute(SessionConst.AUTH) Byte auth) throws ServiceException {
        ServiceException.when(Role.EMPLOYEE.getCode() != auth, "身份不匹配，无法更新信息");
        userService.updateInfo(employeeVo, uid);
    }

    /**
     * @api {put} /user/pwd > 更新密码
     * @apiGroup UserController
     * @apiPermission 已登录状态
     * @apiDescription 该接口用于用户的更新密码，当用户更新密码成功后，需要重新登录。注意：管理员无法修改密码。
     * @apiParam {PwdPair} pwdPair 密码对
     * @apiParam {String} pwdPair.oldPwd 原有密码
     * @apiParam {String} pwdPair.newPwd 新密码
     * @apiParamExample {json} 请求样例
     * {"newPwd":"5e8667a439c68f5145dd2fcbecf02209","oldPwd":"25D55AD283AA400AF464C76D713C07AD"}
     * @apiErrorExample {json} 管理员无法修改密码
     * "抱歉，管理员账号无法修改密码"
     * @apiErrorExample {json} 一般错误提示
     * "原密码错误"
     */
    @PutMapping("pwd")
    @RequiresAuthentication
    public void updatePwd(@RequestBody PwdPair pair, HttpSession session) throws ServiceException {
        userService.updatePwd(pair, session);
    }

    /**
     * @api {put} /user/marketingManager/pwd/reset/{id} > 重置市场经理密码
     * @apiGroup UserController
     * @apiPermission 管理员
     * @apiDescription 重置市场经理的密码，当市场经理忘记密码时使用
     * @apiParam {integer} id 市场经理ID
     * @apiErrorExample {json} 一般错误提示
     * "当前市场经理不存在"
     */
    @PutMapping("/marketingManager/pwd/reset/{id:\\d+}")
    @RequiresPermissions("marketingManager" + OperationConst.RESET_PWD)
    public void resetMarketingManagerPwd(@PathVariable("id") Integer id) throws ServiceException {
        userService.resetMarketingManagerPwd(id);
    }

    /**
     * @api {put} /user/employee/pwd/reset/{id} > 重置厂内员工密码
     * @apiGroup UserController
     * @apiPermission 管理员|市场经理
     * @apiDescription 重置场内员工的密码，当场内员工忘记密码时使用，市场经理只能重置自己学校的员工
     * @apiParam {integer} id 员工ID
     * @apiErrorExample {json} 一般错误提示
     * "当前员工不存在"
     */
    @PutMapping("/employee/pwd/reset/{id:\\d+}")
    @RequiresPermissions("employee" + OperationConst.RESET_PWD)
    public void resetEmployeePwd(@PathVariable("id") Integer id,
                                 @SessionAttribute(SessionConst.AUTH) Byte auth,
                                 @SessionAttribute(SessionConst.UID) Integer uid) throws ServiceException {
        userService.resetEmployeePwd(id, auth, uid);
    }

    /**
     * 功能接口，无需添加注释
     */
    @GetMapping("/unauthc")
    public ResponseEntity unauthc() {
        return ResponseEntities.UNAUTHENTICATED;
    }
}
