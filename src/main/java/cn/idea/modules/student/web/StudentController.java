package cn.idea.modules.student.web;

import cn.idea.modules.common.annotation.MethodMonitor;
import cn.idea.modules.common.bean.BaseVo;
import cn.idea.modules.common.bean.PageVo;
import cn.idea.modules.common.consts.OperationConst;
import cn.idea.modules.common.consts.Role;
import cn.idea.modules.common.consts.SessionConst;
import cn.idea.modules.common.exception.ServiceException;
import cn.idea.modules.common.group.SaveGroup;
import cn.idea.modules.common.group.UpdateGroup;
import cn.idea.modules.employee.bean.EmployeeVo;
import cn.idea.modules.employee.dao.EmployeeMapper;
import cn.idea.modules.student.bean.*;
import cn.idea.modules.student.service.StudentService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @apiDefine Student 学员相关
 */

@MethodMonitor
@RestController
@RequestMapping("/student")
public class StudentController {
    private static final String STUDENT = "student";
    @Autowired
    private StudentService studentService;
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * @api {post} /student 创建学员
     * @apiGroup StudentController
     * @apiDescription 该接口用于创建学员
     * @apiPermission 管理员|市场经理|员工
     * @apiParam {String} name 姓名
     * @apiParam {String} [nickname] 昵称
     * @apiParam {Integer} [age] 年龄
     * @apiParam {String} [birthday]生日
     * @apiParam {String} [parentName] 家长姓名
     * @apiParam {String} phone 手机号
     * @apiParam {String} [alternateNumber] 备用号码
     * @apiParam {String} [wechat] 微信号
     * @apiParam {String} [addr] 家庭住址
     * @apiParam {Integer} [sourceId] 来源id
     * @apiParam {Integer} [eid] 员工id，管理员、市场经理创建时必须指定
     * @apiParam {Byte} state 学员状态
     * @apiParam {Byte} degree 重要程度
     * @apiParam {Byte} salesStage 销售阶段
     * @apiParam {String} [expectedDate] 意向日期，只有销售阶段为试听、继续跟进需要
     * @apiSuccessExample {json}
     * {"addr":"江苏省连云港市赣榆区","alternateNumber":"12323232323","birthday":"19991020","degree":1,"expectedDate":"20200101","name":"学生[测试]","nickname":"小二","parentName":"张三","phone":"12323232323","salesStage":2,"sourceId":3,"state":1,"wechat":"hello,motherfucker"}
     */
    @PostMapping
    @RequiresPermissions(STUDENT + OperationConst.CREATE)
    public BaseVo create(@RequestBody @Validated(SaveGroup.class) StudentVo sv,
                         @SessionAttribute(SessionConst.AUTH) byte auth,
                         @SessionAttribute(SessionConst.UID) Integer uid) throws Exception {
        if (auth == Role.EMPLOYEE.getCode()) {
            sv.setEid(uid);
        } else {
            if (sv.getEid() == null) {
                throw new ServiceException("管理员、市场经理创建学员时应指定员工");
            }
        }
        return studentService.save(sv);
    }

    /**
     * @api {get} /student/{id} 查看学员
     * @apiGroup StudentController
     * @apiDescription 该接口用于学员的查看
     * @apiPermission 管理员|市场经理|员工
     * @apiParam {Integer} id 员工id
     * @apiParamSuccess {String} name 姓名
     * @apiParamSuccess {String} nickname 昵称
     * @apiParamSuccess {String} birthday 生日
     * @apiParamSuccess {String} parentName 家长姓名
     * @apiParamSuccess {String} phone 手机号
     * @apiParamSuccess {String} alternateNumber 备用号码
     * @apiParamSuccess {String} wechat 微信号
     * @apiParamSuccess {String} addr 家庭住址
     * @apiParamSuccess {Integer} sourceId 来源id
     * @apiParamSuccess {Integer} ctime 创建时间
     * @apiParamSuccess {Integer} eid 创建人id
     * @apiParamSuccess {Byte} state 学员状态
     * @apiParamSuccess {Byte} degree 重要程度
     * @apiParamSuccess {Byte} salesStage 销售阶段
     * @apiParamSuccess {String} expectedDate 意向日期，只有销售阶段为继续跟进和试听才存在
     * @apiParamSuccess {String} contractDate 签约日期，只有会员才存在
     * @apiSuccessExample {json}
     * {"addr":"江苏省连云港市赣榆区","alternateNumber":"12323232323","ctime":1538589861,"degree":1,"expectedDate":"20181004","id":6,"name":"学生丙","eid":1,"parentName":"张三","phone":"12313312312","salesStage":2,"sourceId":3,"state":1,"wechat":"hello,motherfucker"}
     */
    @GetMapping("/{id:\\d+}")
    @RequiresPermissions(STUDENT + OperationConst.VIEW)
    public StudentVo view(@PathVariable("id") Integer id) throws ServiceException {
        return studentService.view(id);
    }

    /**
     * @api {get} /student?... 查询学员
     * @apiGroup StudentController
     * @apiDescription 该接口用于查询学员
     * @apiPermission 员工
     * @apiParam {Integer} [_no] 页数,默认为1
     * @apiParam {Integer} [_pageSize] 页数大小，默认15
     * @apiParam {String} [name]姓名，模糊匹配
     * @apiParam {String} [nickname] 昵称，模糊匹配
     * @apiParam {Integer} [ageLow] 年龄下限
     * @apiParam {Integer} [ageHigh] 年龄上限
     * @apiParam {String} [birthdayLow] 生日下限
     * @apiParam {String} [birthdayHigh] 生日上限
     * @apiParam {String} [parentName] 家长姓名，模糊匹配
     * @apiParam {String} [phone] 手机号，模糊匹配
     * @apiParam {String} [alternateNumber] 备用号码，模糊匹配
     * @apiParam {String} [wechat] 微信号，模糊匹配
     * @apiParam {String} [addr] 家庭住址，模糊匹配
     * @apiParam {String} [sourceIdStr] 来源id，允许多个，用:隔开，如1:2:4
     * @apiParam {Integer} [ctimeLow] 创建时间下限
     * @apiParam {Integer} [ctimeHigh] 创建时间上限
     * @apiParam {String} [stateStr] 学员状态，允许多个，用:隔开，如1:2:4
     * @apiParam {String} [degreeSeyStr] 重要程度，允许多个，用:隔开，如1:2:4
     * @apiParam {String} [salesStageStr] 销售阶段，允许多个，用:隔开，如1:2:4
     * @apiParam {String} [expectedDateLow] 意向日期下限
     * @apiParam {String} [expectedDateHigh] 意向日期上限
     * @apiParam {String} [contractDateLow] 签约日期下限
     * @apiParam {String} [contractDateHigh] 签约日期上限
     * @apiParam {boolean} [ascFlag] 升序标志
     * @apiSuccess {PageVo[StudentVo]} studentVoList 具体参考查看方法
     */
    @GetMapping
    @RequiresPermissions(STUDENT + OperationConst.LIST)
    public PageVo<StudentVo> list(@RequestParam(name = "_no", defaultValue = "1") Integer no,
                                  @RequestParam(name = "_pageSize", required = false) Integer pageSize,
                                  @RequestParam(name = "_name", required = false) String name,
                                  @RequestParam(name = "nickname", required = false) String nickname,
                                  @RequestParam(name = "ageLow", required = false) Integer ageLow,
                                  @RequestParam(name = "ageHigh", required = false) Integer ageHigh,
                                  @RequestParam(name = "birthdayLow", required = false) String birthdayLow,
                                  @RequestParam(name = "birthdayHigh", required = false) String birthdayHigh,
                                  @RequestParam(name = "parentName", required = false) String parentName,
                                  @RequestParam(name = "phone", required = false) String phone,
                                  @RequestParam(name = "alternateNumber", required = false) String alternateNumber,
                                  @RequestParam(name = "wechat", required = false) String wechat,
                                  @RequestParam(name = "addr", required = false) String addr,
                                  @RequestParam(name = "sourceIdStr", required = false) String sourceIdStr,
                                  @RequestParam(name = "ctimeLow", required = false) Integer ctimeLow,
                                  @RequestParam(name = "ctimeHigh", required = false) Integer ctimeHigh,
                                  @RequestParam(name = "stateStr", required = false) String stateStr,
                                  @RequestParam(name = "degreeStr", required = false) String degreeStr,
                                  @RequestParam(name = "salesStageStr", required = false) String salesStageStr,
                                  @RequestParam(name = "expectedDateLow", required = false) String expectedDateLow,
                                  @RequestParam(name = "expectedDateHigh", required = false) String expectedDateHigh,
                                  @RequestParam(name = "contractDateLow", required = false) String contractDateLow,
                                  @RequestParam(name = "contractDateHigh", required = false) String contractDateHigh,
                                  @RequestParam(name = "ascFlag", defaultValue = "true") Boolean ascFlag,
                                  @SessionAttribute(SessionConst.UID) Integer uid,
                                  @SessionAttribute(SessionConst.AUTH) Byte auth) throws ServiceException {
        StudentQVo.StudentQVoBuilder sqv = StudentQVo.builder()
                .name(name)
                .nickname(nickname)
                .ageRange(ageLow, ageHigh)
                .birthdayRange(birthdayLow, birthdayHigh)
                .parentName(parentName)
                .phone(phone)
                .alternateNumber(alternateNumber)
                .wechat(wechat)
                .addr(addr)
                .sourceId(sourceIdStr)
                .ctimeRange(ctimeLow, ctimeHigh)
                .state(stateStr)
                .degree(degreeStr)
                .salesStage(salesStageStr)
                .expectedDateRange(expectedDateLow, expectedDateHigh)
                .contractDateRange(contractDateLow, contractDateHigh)
                .ascFlag(ascFlag);

        if (auth.equals(Role.EMPLOYEE.getCode())) {
            sqv.eidSet(new HashSet<>(uid));
        } else if (auth.equals(Role.MARKETING_MANAGER.getCode())) {
            List<EmployeeVo> evs = employeeMapper.findByMid(uid);
            Set<Integer> eids = evs.stream().map(EmployeeVo::getId).collect(Collectors.toSet());
            sqv.eidSet(eids);
        }
        return studentService.list(no, pageSize, sqv.build());
    }

    /**
     * @api {put} /student/{id} 更新学员
     * @apiGroup StudentController
     * @apiDescription 该接口用于更新学员
     * @apiPermission 管理员|市场经理|员工
     * @apiParam {String} [name] 姓名
     * @apiParam {String} [nickname] 昵称
     * @apiParam {String} [birthday]生日
     * @apiParam {String} [age] 年龄
     * @apiParam {String} [parentName] 家长姓名
     * @apiParam {String} [phone] 手机号
     * @apiParam {String} [alternateNumber] 备用号码
     * @apiParam {String} [wechat] 微信号
     * @apiParam {String} [addr] 家庭住址
     * @apiParam {Integer} [sourceId] 来源id
     * @apiParam {Byte} [state] 学员状态
     * @apiParam {Byte} [degree] 重要程度
     * @apiParam {Byte} [salesStage] 销售阶段
     * @apiParam {String} [expectedDate] 意向日期
     */
    @PutMapping("/{id:\\d+}")
    @RequiresPermissions(STUDENT + OperationConst.UPDATE)
    public BaseVo update(@PathVariable("id") Integer id,
                         @RequestBody @Validated(UpdateGroup.class) StudentVo sv) throws ServiceException {
        sv.setId(id);
        return studentService.update(sv);
    }

    /**
     * @api {put} /student/eid/change?idStr={}&eid={}
     * @apiGroup StudentController
     * @apiDescription 该接口用于更新学员的员工id
     * @apiPermission 管理员|市场经理
     * @apiParam {String} idStr 学员id，允许多个，用:隔开，如1:2:4
     * @apiParam {Integer} eid 员工id
     */
    @PutMapping("/eid/change")
    @RequiresPermissions(STUDENT + OperationConst.CHANGE_EID)
    public Integer updateEid(@RequestParam(name = "idStr") String idStr,
                             @RequestParam(name = "eid") Integer eid) throws ServiceException {
        return studentService.updateEid(idStr, eid);
    }

    /**
     * @api {delete} /student/{id} 删除学员
     * @apiGroup StudentController
     * @apiDescription 该接口用于删除学员
     * @apiPermission 市场经理|管理员
     * @apiParam {Integer} id 学员id
     */
    @DeleteMapping("/{id:\\d+}")
    @RequiresPermissions(STUDENT + OperationConst.DELETE)
    public BaseVo delete(@PathVariable("id") Integer id) throws ServiceException {
        return studentService.delete(id);
    }

    /**
     * @api {get} /student/importance > 获取重要程度的含义
     * @apiGroup StudentController
     * @apiPermission 登录状态
     * @apiDescription 该接口用于根据重要程度code获取含义
     * @apiSuccess {Map} kv 重要程度code及含义
     */
    @GetMapping("/importance")
    @RequiresAuthentication
    public Map<Byte, String> importance() throws ServiceException {
        return ImportanceEnum.kv;
    }

    /**
     * @api {get} /student/sales > 获取销售阶段的含义
     * @apiGroup StudentController
     * @apiPermission 登录状态
     * @apiDescription 该接口用于根据销售阶段code获取含义
     * @apiSuccess {Map} kv 销售阶段code及含义
     */
    @GetMapping("/sales")
    @RequiresAuthentication
    public Map<Byte, String> sales() throws ServiceException {
        return SalesStageEnum.kv;
    }

    /**
     * @api {get} /student/status > 获取学员状态的含义
     * @apiGroup StudentController
     * @apiPermission 登录状态
     * @apiDescription 该接口用于根据学员状态code获取含义
     * @apiSuccess {Map} kv 学员状态code及含义
     */
    @GetMapping("/status")
    @RequiresAuthentication
    public Map<Byte, String> status() throws ServiceException {
        return StatusEnum.kv;
    }

}