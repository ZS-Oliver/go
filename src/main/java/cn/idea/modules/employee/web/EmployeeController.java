package cn.idea.modules.employee.web;

import cn.idea.modules.common.annotation.MethodMonitor;
import cn.idea.modules.common.bean.BaseVo;
import cn.idea.modules.common.bean.PageVo;
import cn.idea.modules.common.consts.OperationConst;
import cn.idea.modules.common.consts.Role;
import cn.idea.modules.common.consts.SessionConst;
import cn.idea.modules.common.exception.ServiceException;
import cn.idea.modules.common.group.SaveGroup;
import cn.idea.modules.common.group.UpdateGroup;
import cn.idea.modules.employee.bean.EmployeeQVo;
import cn.idea.modules.employee.bean.EmployeeVo;
import cn.idea.modules.employee.service.EmployeeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @apiDefine EmployeeController 员工相关
 */
@MethodMonitor
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private static final String EMPLOYEE = "employee";
    @Autowired
    private EmployeeService employeeService;

    /**
     * @api {post} /employee > 创建用户
     * @apiGroup EmployeeController
     * @apiPermission 管理员|市场经理
     * @apiDescription 该接口用于用户的注册
     * @apiParam {String} name 用户名
     * @apiParam {String} phone 手机号
     * @apiParam {Integer} [mid] 市场经理id，管理员创建时需要指定
     * @apiParamExample {json} 请求样例，管理员
     * {"mid":7,"name":"员工","phone":"12345671214"}
     * @apiErrorExample 失败样例
     * "员工保存失败，原因为:手机号同员工员工重复"
     * @apiErrorExample 失败样例
     * "员工保存失败，原因为:手机号同市场经理王艳涛重复"
     * @apiErrorExample 失败样例，管理员
     * "管理员创建员工应指定市场经理"
     * @apiParamExample {json} 请求样例，市场经理
     * {"name":"whwu","phone":"12345678900"}
     */
    @PostMapping
    @RequiresPermissions(EMPLOYEE + OperationConst.CREATE)
    public BaseVo create(@RequestBody @Validated(SaveGroup.class) EmployeeVo uv,
                         @SessionAttribute(SessionConst.AUTH) byte auth,
                         @SessionAttribute(SessionConst.UID) Integer uid) throws Exception {
        if (auth == Role.ADMIN.getCode()) {
            ServiceException.when(uv.getMid() == null, "管理员创建员工应指定市场经理");
        } else if (auth == Role.MARKETING_MANAGER.getCode()) {
            uv.setMid(uid);
        }
        return employeeService.save(uv);
    }

    /**
     * @api {put} /employee > 更新员工
     * @apiGroup EmployeeController
     * @apiPermission 管理员|市场经理
     * @apiDescription 该接口用于修改员工信息，市场经理只能更新自己的员工
     * @apiParam {String} [phone] 手机号
     * @apiParam {String} [name] 姓名
     * @apiParamExample {json} 请求样例
     * {"name":"123","phone":"12345678905"}
     * @apiSuccessExample {json} 成功样例
     * {"id":1,"valid":1}
     * @apiErrorExample 失败样例
     * "员工信息更新失败，原因为:手机号同员工员工重复"
     * @apiErrorExample 失败样例
     * "员工信息更新失败，原因为:手机号同市场经理王艳涛重复"
     * @apiErrorExample 失败样例
     * "员工信息更新失败，原因为:该员工不存在"
     */
    @PutMapping("{id:\\d+}")
    @RequiresPermissions(EMPLOYEE + OperationConst.UPDATE)
    public BaseVo update(@RequestBody @Validated(UpdateGroup.class) EmployeeVo uv,
                         @PathVariable("id") Integer id,
                         @SessionAttribute(SessionConst.AUTH) Byte auth,
                         @SessionAttribute(SessionConst.UID) Integer uid) throws ServiceException {
        Integer mid = null;
        if (auth.equals(Role.MARKETING_MANAGER.getCode())) {
            mid = uid;
        }
        uv.setMid(mid);
        uv.setId(id);
        return employeeService.update(uv);
    }

    /**
     * @api {get} /employee/{id} > 查看员工
     * @apiGroup EmployeeController
     * @apiDescription 该接口用于查看员工信息
     * @apiPermission 管理员|市场经理
     * @apiParam {Integer} id 员工id
     * @apiSuccess {Integer} id id
     * @apiSuccess {String} name 姓名
     * @apiSuccess {String} phone 手机号
     * @apiSuccess {String} school 学校
     * @apiSuccessExample {json} 成功样例
     * {"auth":2,"id":1,"name":"员工[测试]","phone":"12345678912","school":"东北大学"}
     */
    @GetMapping("/{id:\\d+}")
    @RequiresPermissions(EMPLOYEE + OperationConst.VIEW)
    public EmployeeVo view(@PathVariable("id") Integer id) throws ServiceException {
        return employeeService.view(id);
    }

    /**
     * @api {get} /employee?... > 查询员工
     * @apiGroup EmployeeController
     * @apiPermission 管理员|市场经理
     * @apiDescription 该接口用于查询员工，市场经理只能查到自己的员工
     * @apiParam {Integer} [_no] 页数,默认为1
     * @apiParam {Integer} [_pageSize] 页数大小，默认15
     * @apiParam {String} [name] 用户名，模糊匹配
     * @apiParam {String} [phone] 手机号，模糊匹配
     * @apiParam {String} [school] 学校，模糊匹配
     * @apiParam {boolean} [ascFlag] 升序标志
     * @apiSuccess {PageVo[EmployeeVo]} userVoList 具体参考查看方法
     * @apiSuccessExample {json} 成功样例
     * {"list":[{"auth":2,"id":4,"name":"员工[测试用]","phone":"12345671212","school":"东北大学"},{"auth":2,"id":5,"name":"员工","phone":"12345671213","school":"东北大学"}],"no":1,"total":2,"totalPage":1}
     */
    @GetMapping
    @RequiresPermissions(EMPLOYEE + OperationConst.LIST)
    public PageVo<EmployeeVo> list(@RequestParam(name = "_no", defaultValue = "1") Integer no,
                                   @RequestParam(name = "_pageSize", required = false) Integer pageSize,
                                   @RequestParam(name = "name", required = false) String name,
                                   @RequestParam(name = "phone", required = false) String phone,
                                   @RequestParam(name = "ascFlag", defaultValue = "true") Boolean ascFlag,
                                   @RequestParam(name = "school", required = false) String school,
                                   @SessionAttribute(SessionConst.AUTH) Byte auth,
                                   @SessionAttribute(SessionConst.UID) Integer uid) throws ServiceException {
        EmployeeQVo.EmployeeQVoBuilder uqv = EmployeeQVo.builder()
                .name(name)
                .phone(phone)
                .ascFlag(ascFlag)
                .school(school);
        if (auth.equals(Role.MARKETING_MANAGER.getCode())) {
            uqv.mid(uid);
        }
        return employeeService.list(no, pageSize, uqv.build());
    }

    /**
     * @api {delete} /employee/{id} > 删除员工
     * @apiGroup EmployeeController
     * @apiDescription 删除员工，如果员工下有学员，则不能删除.市场经理只能删除自己的员工
     * @apiPermission 管理员|市场经理
     * @apiDescription 该接口用于删除员工
     * @apiParam {Integer} id ID
     * @apiErrorExample 失败样例
     * "无法删除员工，原因为:该员工下学员不为空，请转移学员后再尝试"
     * @apiSuccessExample {json} 成功样例
     * {"id":5,"valid":0}
     */
    @DeleteMapping("/{id:\\d+}")
    @RequiresPermissions(EMPLOYEE + OperationConst.DELETE)
    public BaseVo delete(@PathVariable("id") Integer id,
                         @SessionAttribute(SessionConst.UID) Integer uid,
                         @SessionAttribute(SessionConst.AUTH) Byte auth) throws ServiceException {
        Integer mid = null;
        if (auth.equals(Role.MARKETING_MANAGER.getCode())) {
            mid = uid;
        }
        return employeeService.delete(id, mid);
    }
}