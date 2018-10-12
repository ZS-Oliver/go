package cn.idea.modules.security.web;

import cn.idea.modules.common.bean.BaseVo;
import cn.idea.modules.common.consts.OperationConst;
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
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * @api {post} /user?checkCode={}&[clientId={}] > 登录
     * @apiGroup UserController
     * @apiPermission 任何人
     * @apiDescription 该接口可用于内部员工和承运商两者的登录。
     * 通过uv中的code部分来区分两者，内部员工的code为工号，而承运商则为手机号（需要以"+86"开头）。
     * 需要注意的是：在内存中的承运商code会带有"+86"，而数据库中存储的手机号是不带的。
     * 客户端ID是个推推送消息使用，pc端登陆不需要传这个值
     * @apiParam {String} checkCode 校验码
     * @apiParam {String} [clientId] 客户端ID
     * @apiParam {String} code 账号
     * @apiParam {String} pwd 密码（需要将用户填写的内容用md5加密，并统一转化为大写）
     * @apiErrorExample {json} 登录失败1
     * "验证码错误"
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
     * @api {post} /user/temp?[clientId={}] > 登录（测试用）
     * @apiGroup UserController
     * @apiPermission 任何人
     * @apiDescription 该接口可用于内部员工和承运商两者的登录。
     * 通过uv中的code部分来区分两者，内部员工的code为工号，而承运商则为手机号（需要以"+86"开头）。
     * 需要注意的是：在内存中的承运商code会带有"+86"，而数据库中存储的手机号是不带的。
     * 客户端ID是个推推送消息使用，pc端登陆不需要传这个值
     * @apiParam {String} [clientId] 客户端ID
     * @apiParam {String} code 账号
     * @apiParam {String} pwd 密码（需要将用户填写的内容用md5加密，并统一转化为大写）
     * @apiParamExample {json} 管理员
     * {"code":"00000000","pwd":"5F4DCC3B5AA765D61D8327DEB882CF99"}
     * @apiParamExample {json} 厂内员工
     * {"code":"12345675","pwd":"25D55AD283AA400AF464C76D713C07AD"}
     * @apiParamExample {json} 承运商
     * {"code":"+8618765658908","pwd":"25D55AD283AA400AF464C76D713C07AD"}
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
     * @apiDescription 该接口用于获取当前登录用户的基本信息，注意管理员、内部员工和承运商的信息是不同的。
     * @apiSuccess {Integer} [id] [管理员] 仅管理员有，且为-1
     * @apiSuccess {String} [code] [厂内员工] 工号
     * @apiSuccess {Integer} [auth] [厂内员工] 具体权限：1-贸易员，2-物流员，3-门卫，4-理货员
     * @apiSuccess {String} [name] [厂内员工，承运商] 姓名
     * @apiSuccess {String} [phone] [厂内员工，承运商] 手机号，厂内员工可选
     * @apiSuccess {Integer} [initPwdFlag] [厂内员工，承运商] 是否使用初始密码：1-是；0-否
     * @apiSuccess {String} [companyName] [承运商] 公司简称
     * @apiSuccessExample {json} 管理员
     * {"id":-1}
     * @apiSuccessExample {json} 厂内员工
     * {"auth":1,"code":"12345678","initPwdFlag":1,"name":"测试账号，勿删1","phone":"12345678"}
     * @apiSuccessExample {json} 承运商
     * {"companyName":"高大上公司","initPwdFlag":1,"name":"测试账号，勿删","phone":"18765658908"}
     */
    @GetMapping
    @RequiresAuthentication
    public BaseVo info(HttpSession session) throws ServiceException {
        return userService.info(session);
    }

    /**
     * @api {put} /user/carrier > 更新个人信息（承运商）
     * @apiGroup UserController
     * @apiPermission 已登录状态
     * @apiDescription 该接口用于更新承运商的个人信息
     * @apiParam {String} [name] 姓名
     * @apiParam {String} [phone] 手机号
     * @apiParam {String} [companyName] 公司简称
     * @apiParamExample {json} 请求样例
     * {"companyName":"东软","phone":"18716035721"}
     * @apiErrorExample {json} 失败样例
     * "手机号同已有账号重复"
     */
    @PutMapping("/carrier")
    @RequiresAuthentication
    public void updateInfo(@RequestBody @Validated(UpdateGroup.class) MarketingManagerVo marketingManagerVo,
                           @SessionAttribute(SessionConst.UID) Integer uid) throws ServiceException {
        userService.updateInfo(marketingManagerVo, uid);
    }

    /**
     * @api {put} /user/employee > 更新个人信息（厂内员工）
     * @apiGroup UserController
     * @apiPermission 已登录状态
     * @apiDescription 该接口用于更新厂内员工的个人信息
     * @apiParam {String} [name] 姓名
     * @apiParam {String} [code] 工号
     * @apiParam {String} [phone] 手机号
     * @apiParamExample {json} 请求样例
     * {"code":"11112222","name":"蛋蛋"}
     * @apiErrorExample {json} 失败样例
     * "工号同已有账号重复"
     */
    @PutMapping("/employee")
    @RequiresAuthentication
    public void updateInfo(@RequestBody @Validated(UpdateGroup.class) EmployeeVo employeeVo,
                           @SessionAttribute(SessionConst.UID) Integer uid) throws ServiceException {
        userService.updateInfo(employeeVo, uid);
    }

    /**
     * @api {put} /user/pwd > 更新密码
     * @apiGroup UserController
     * @apiPermission 已登录状态
     * @apiDescription 该接口用于用户的更新密码，当用户更新密码成功后，需要重新登录。注意：管理员无法修改密码。
     * @apiParam {String} oldPwd 原有密码
     * @apiParam {String} newPwd 新密码
     * @apiParamExample {json} 请求样例
     * {"oldPwd":"25D55AD283AA400AF464C76D713C07AD", "newPwd":"5E8667A439C68F5145DD2FCBECF02209"}
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
     * @api {put} /user/carrier/pwd/reset/{id} > 重置承运商密码
     * @apiGroup UserController
     * @apiPermission 管理员|物流员
     * @apiDescription 重置承运商的密码，当承运商忘记密码时使用
     * @apiParam {integer} id 承运商ID
     * @apiErrorExample {json} 一般错误提示
     * "当前承运商不存在"
     */
    @PutMapping("/carrier/pwd/reset/{id:\\d+}")
    @RequiresPermissions("marketingManager" + OperationConst.RESET_PWD)
    public void resetMarketingManagerPwd(@PathVariable("id") Integer id) throws ServiceException {
        userService.resetCarrierPwd(id);
    }

    /**
     * @api {put} /user/employee/pwd/reset/{id} > 重置厂内员工密码
     * @apiGroup UserController
     * @apiPermission 管理员
     * @apiDescription 重置场内员工的密码，当场内员工忘记密码时使用
     * @apiParam {integer} id 员工ID
     * @apiErrorExample {json} 一般错误提示
     * "当前员工不存在"
     */
    @PutMapping("/employee/pwd/reset/{id:\\d+}")
    @RequiresPermissions("employee" + OperationConst.RESET_PWD)
    public void resetEmployeePwd(@PathVariable("id") Integer id) throws ServiceException {
        userService.resetEmployeePwd(id);
    }

    /**
     * 功能接口，无需添加注释
     */
    @GetMapping("/unauthc")
    public ResponseEntity unauthc() {
        return ResponseEntities.UNAUTHENTICATED;
    }
}
