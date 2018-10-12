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
 * @apiDefine EmployeeController 用户相关
 */
@MethodMonitor
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private static final String EMPLOYEE = "employee";
    @Autowired
    private EmployeeService employeeService;

    /**
     * @api {post} /employee [用户]创建用户
     * @apiGroup EmployeeController
     * @apiPermission 管理员|市场经理
     * @apiDescription 该接口用于用户的注册
     * @apiParam {String} name 用户名
     * @apiParam {String} phone 手机号
     * @apiParam {Integer} [mid] 市场经理id，管理员创建时需要指定
     */
    @PostMapping
    @RequiresPermissions(EMPLOYEE + OperationConst.CREATE)
    public BaseVo create(@RequestBody @Validated(SaveGroup.class) EmployeeVo uv,
                         @SessionAttribute(SessionConst.AUTH) byte auth,
                         @SessionAttribute(SessionConst.UID) Integer uid) throws Exception {
        if (auth == Role.ADMIN.getCode()) {
            ServiceException.when(uv.getMid() == null, "管理员创建员工应指定市场经理");
        }
        if (auth == Role.MARKETING_MANAGER.getCode()) {
            uv.setMid(uid);
        }
        return employeeService.save(uv);
    }

    /**
     * @api {put} /employee 更新员工
     * @apiGroup EmployeeController
     * @apiPermissin 管理员|市场经理
     * @apiDescription 该接口用于用户修改个人信息
     * @apiParam {String} [phone] 手机号
     * @apiParam {String} [name] 姓名
     * @apiParamExample {json} 请求样例
     * {"birthday":869932800,"email":"1091227987@qq.com"}
     * @apiSuccessExample {json} 成功样例
     * {"id":1,"valid":1}
     */
    @PutMapping("{id:\\d+}")
    @RequiresPermissions(EMPLOYEE + OperationConst.UPDATE)
    public BaseVo update(@RequestBody @Validated(UpdateGroup.class) EmployeeVo uv,
                         @PathVariable("id") Integer id) throws ServiceException {
        uv.setId(id);
        return employeeService.update(uv);
    }

    /**
     * @api {get} /employee/{id} 查看员工
     * @apiGroup EmployeeController
     * @apiPermissin 管理员|市场经理
     * @apiParam {Integer} id 员工id
     * @apiSuccess {Integer} id id
     * @apiSuccess {String} name 姓名
     * @apiSuccess {String} phone 手机号
     * @apiSuccess {String} school 学校
     */
    @GetMapping("/{id:\\d+}")
    @RequiresPermissions(EMPLOYEE + OperationConst.VIEW)
    public EmployeeVo view(@PathVariable("id") Integer id) throws ServiceException {
        return employeeService.view(id);
    }

    /**
     * @api {get} /employee?... 查询员工
     * @apiGroup EmployeeController
     * @apiPermissin 管理员|市场经理
     * @apiParam {Integer} [_no] 页数,默认为1
     * @apiParam {Integer} [_pageSize] 页数大小，默认15
     * @apiParam {String} [name] 用户名，模糊匹配
     * @apiParam {String} [phone] 手机号，模糊匹配
     * @apiParam {String} [school] 学校，模糊匹配
     * @apiParam {boolean} [ascFlag] 升序标志
     * @apiSuccess {PageVo[EmployeeVo]} userVoList 具体参考查看方法
     */
    @GetMapping
    @RequiresPermissions(EMPLOYEE + OperationConst.LIST)
    public PageVo<EmployeeVo> list(@RequestParam(name = "_no", defaultValue = "1") Integer no,
                                   @RequestParam(name = "_pageSize", required = false) Integer pageSize,
                                   @RequestParam(name = "name", required = false) String name,
                                   @RequestParam(name = "phone", required = false) String phone,
                                   @RequestParam(name = "ascFlag", defaultValue = "true") Boolean ascFlag,
                                   @RequestParam(name = "school", required = false) String school) throws ServiceException {
        EmployeeQVo uqv = EmployeeQVo.builder()
                .name(name)
                .phone(phone)
                .ascFlag(ascFlag)
                .school(school)
                .build();
        return employeeService.list(no, pageSize, uqv);
    }

    /**
     * @api {delete} /employee/{id} 删除员工
     * @apiGroup EmployeeController
     * @apiPermission 管理员|市场经理
     * @apiDescription 该接口用于删除员工
     * @apiParam {Integer} id ID
     */
    @DeleteMapping("/{id:\\d+}")
    @RequiresPermissions(EMPLOYEE + OperationConst.DELETE)
    public BaseVo delete(@PathVariable("id") Integer id) throws ServiceException {
        return employeeService.delete(id);
    }
}