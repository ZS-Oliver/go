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
import cn.idea.utils.assistant.CommonUtil;
import com.google.common.base.Strings;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @apiDefine StudentController 学员相关
 */

@MethodMonitor
@RestController
@RequestMapping("/student")
public class StudentController {
    private static final String STUDENT = "student";
    private static final Predicate<String> Checker = Pattern.compile("([0-9]+:?)+").asPredicate();

    @Autowired
    private StudentService studentService;
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * @api {post} /student > 创建学员
     * @apiGroup StudentController
     * @apiDescription 该接口用于创建学员，市场经理、管理员创建学员必须指定员工
     * @apiPermission 管理员|市场经理|员工
     * @apiParam {String} name 姓名
     * @apiParam {String} [nickname] 昵称
     * @apiParam {Float} [age] 年龄
     * @apiParam {String} [sex] 性别，男或女
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
     * @apiParamExample {json} 请求样例
     * {"addr":"江苏省连云港市赣榆区","alternateNumber":"12323232323","birthday":"19991020","degree":1,"expectedDate":"20200101","name":"学生[测试]","nickname":"小二","parentName":"张三","phone":"12323232323","salesStage":2,"sourceId":3,"state":1,"wechat":"hello,motherfucker"}
     * @apiErrorExample {json} 失败样例
     * "管理员、市场经理创建学员时应指定员工"
     * @apiErrorExample {json} 失败样例
     * "学员保存失败，原因为:继续跟进与试听阶段必须填入意向时间"
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
     * @api {get} /student/{id} > 查看学员
     * @apiGroup StudentController
     * @apiDescription 该接口用于学员的查看
     * @apiPermission 管理员|市场经理|员工
     * @apiParam {Integer} id 学员id
     * @apiSuccess {String} name 姓名
     * @apiSuccess {String} nickname 昵称
     * @apiSuccess {String} birthday 生日
     * @apiSuccess {Float} age 年龄
     * @apiSuccess {String} sex 性别
     * @apiSuccess {String} parentName 家长姓名
     * @apiSuccess {String} phone 手机号
     * @apiSuccess {String} alternateNumber 备用号码
     * @apiSuccess {String} wechat 微信号
     * @apiSuccess {String} addr 家庭住址
     * @apiSuccess {Integer} sourceId 来源id
     * @apiSuccess {String} source 来源
     * @apiSuccess {Integer} ctime 创建时间
     * @apiSuccess {Integer} eid 员工id
     * @apiSuccess {String} ename 员工姓名
     * @apiSuccess {Byte} state 学员状态
     * @apiSuccess {Byte} degree 重要程度
     * @apiSuccess {Byte} salesStage 销售阶段
     * @apiSuccess {String} expectedDate 意向日期，只有销售阶段为继续跟进和试听才存在
     * @apiSuccess {String} contractDate 签约日期，只有会员才存在
     * @apiSuccessExample {json} 成功样例
     * {"addr":"江苏省连云港市赣榆区","alternateNumber":"12323232323","ctime":1538589861,"degree":2,"eid":1,"ename":"员工[测试]","expectedDate":"20181005","id":6,"name":"学生丙","parentName":"张三","phone":"12313312312","salesStage":2,"source":"赣榆高级中学","sourceId":3,"state":2,"wechat":"hello,motherfucker"}
     */
    @GetMapping("/{id:\\d+}")
    @RequiresPermissions(STUDENT + OperationConst.VIEW)
    public StudentVo view(@PathVariable("id") Integer id) throws ServiceException {
        return studentService.view(id);
    }

    /**
     * @api {get} /student?... > 查询学员
     * @apiGroup StudentController
     * @apiDescription 该接口用于查询学员
     * @apiPermission 员工|市场经理|管理员
     * @apiParam {Integer} [_no] 页数,默认为1
     * @apiParam {Integer} [_pageSize] 页数大小，默认15
     * @apiParam {String} [name]姓名，模糊匹配
     * @apiParam {String} [nickname] 昵称，模糊匹配
     * @apiParam {Float} [ageLow] 年龄下限
     * @apiParam {Float} [ageHigh] 年龄上限
     * @apiParam {String} [sex] 性别
     * @apiParam {String} [birthdayLow] 生日下限
     * @apiParam {String} [birthdayHigh] 生日上限
     * @apiParam {String} [parentName] 家长姓名，模糊匹配
     * @apiParam {String} [phone] 手机号，模糊匹配
     * @apiParam {String} [alternateNumber] 备用号码，模糊匹配
     * @apiParam {String} [wechat] 微信号，模糊匹配
     * @apiParam {String} [addr] 家庭住址，模糊匹配
     * @apiParam {String} [eidStr] 员工id，可以批量查询，用:隔开，如1:2:4
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
     * @apiParamExample 请求样例
     * {parentName=[张三]}
     * @apiSuccessExample {json} 成功样例
     * {"list":[{"addr":"江苏省连云港市赣榆区","alternateNumber":"12323232323","birthday":"19991020","ctime":1538504009,"degree":1,"eid":1,"ename":"员工[测试]","expectedDate":"20200101","id":4,"name":"学生[测试]","nickname":"小二","parentName":"张三","phone":"12323232323","salesStage":2,"source":"赣榆高级中学","sourceId":3,"state":1,"wechat":"hello,motherfucker"},{"addr":"江苏省连云港市赣榆区","alternateNumber":"12323232323","ctime":1538589861,"degree":2,"eid":1,"ename":"员工[测试]","expectedDate":"20181005","id":6,"name":"学生丙","parentName":"张三","phone":"12313312312","salesStage":2,"source":"赣榆高级中学","sourceId":3,"state":2,"wechat":"hello,motherfucker"},{"addr":"江苏省连云港市赣榆区","alternateNumber":"12323232323","ctime":1538590139,"degree":2,"eid":1,"ename":"员工[测试]","expectedDate":"20181004","id":8,"name":"大阿璇","parentName":"张三","phone":"12313312315","salesStage":3,"source":"赣榆高级中学","sourceId":3,"state":2,"wechat":"hello,world"}],"no":1,"total":3,"totalPage":1}
     * @apiErrorExample 失败样例
     * "没有找到学员信息"
     */
    @GetMapping
    @RequiresPermissions(STUDENT + OperationConst.LIST)
    public PageVo<StudentVo> list(@RequestParam(name = "_no", defaultValue = "1") Integer no,
                                  @RequestParam(name = "_pageSize", required = false) Integer pageSize,
                                  @RequestParam(name = "name", required = false) String name,
                                  @RequestParam(name = "nickname", required = false) String nickname,
                                  @RequestParam(name = "ageLow", required = false) Float ageLow,
                                  @RequestParam(name = "ageHigh", required = false) Float ageHigh,
                                  @RequestParam(name = "sex", required = false) String sex,
                                  @RequestParam(name = "birthdayLow", required = false) String birthdayLow,
                                  @RequestParam(name = "birthdayHigh", required = false) String birthdayHigh,
                                  @RequestParam(name = "parentName", required = false) String parentName,
                                  @RequestParam(name = "phone", required = false) String phone,
                                  @RequestParam(name = "alternateNumber", required = false) String alternateNumber,
                                  @RequestParam(name = "wechat", required = false) String wechat,
                                  @RequestParam(name = "addr", required = false) String addr,
                                  @RequestParam(name = "eidStr", required = false) String eidStr,
                                  @RequestParam(name = "sourceIdStr", required = false) String sourceIdStr,
                                  @RequestParam(name = "ctimeLow", required = false) Integer ctimeLow,
                                  @RequestParam(name = "ctimeHigh", required = false) Integer ctimeHigh,
                                  @RequestParam(name = "stateStr", required = false) String stateStr,
                                  @RequestParam(name = "degreeSeyStr", required = false) String degreeStr,
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
                .sex(sex)
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
            Set<Integer> eidSet = new HashSet<>();
            eidSet.add(uid);
            sqv.eidSet(eidSet);
        } else if (auth.equals(Role.MARKETING_MANAGER.getCode())) {
            Set<Integer> eids;
            if (Strings.isNullOrEmpty(eidStr)) {
                List<EmployeeVo> evs = employeeMapper.findByMid(uid);
                eids = evs.stream().map(EmployeeVo::getId).collect(Collectors.toSet());
                if (CollectionUtils.isEmpty(eids)) {
                    eids.add(-1);
                }
            } else {
                eids = CommonUtil.str2IntSet(Checker, eidStr);
            }
            sqv.eidSet(eids);
        } else if (auth.equals(Role.ADMIN.getCode())) {
            Set<Integer> eids = null;
            if (!Strings.isNullOrEmpty(eidStr)) {
                eids = CommonUtil.str2IntSet(Checker, eidStr);
            }
            sqv.eidSet(eids);
        }
        return studentService.list(no, pageSize, sqv.build());
    }

    /**
     * @api {put} /student/{id} > 更新学员
     * @apiGroup StudentController
     * @apiDescription 该接口用于更新学员
     * @apiPermission 管理员|市场经理|员工
     * @apiParam {String} [name] 姓名
     * @apiParam {String} [nickname] 昵称
     * @apiParam {String} [birthday]生日
     * @apiParam {Float} [age] 年龄
     * @apiParam {String} [sex] 性别
     * @apiParam {String} [parentName] 家长姓名
     * @apiParam {String} [phone] 手机号
     * @apiParam {String} [alternateNumber] 备用号码
     * @apiParam {String} [wechat] 微信号
     * @apiParam {String} [addr] 家庭住址
     * @apiParam {Integer} [sourceId] 来源id
     * @apiParam {Byte} [state] 学员状态
     * @apiParam {Byte} [degree] 重要程度
     * @apiParam {Byte} [salesStage] 销售阶段
     * @apiParam {String} [expectedDate] 意向日期，如果将销售阶段更改为试听、继续跟进，该字段必须填写，否则不能填写
     * @apiParamExample {json} 请求样例
     * {"nickname":"阿璇","phone":"12323232323"}
     * @apiErrorExample 失败样例
     * "学员信息更新失败，原因为:手机号同学生[测试]重复"
     */
    @PutMapping("/{id:\\d+}")
    @RequiresPermissions(STUDENT + OperationConst.UPDATE)
    public BaseVo update(@PathVariable("id") Integer id,
                         @RequestBody @Validated(UpdateGroup.class) StudentVo sv) throws ServiceException {
        sv.setId(id);
        return studentService.update(sv);
    }

    /**
     * @api {put} /student/eid/change?idStr={}&eid={} > 修改学员所属员工
     * @apiGroup StudentController
     * @apiDescription 该接口用于更新学员的员工id
     * @apiPermission 管理员|市场经理
     * @apiParam {String} idStr 学员id，允许多个，用:隔开，如1:2:4
     * @apiParam {Integer} eid 员工id
     * @apiSuccess {Integer} num 成功改动的个数
     * @apiParamExample 请求样例
     * {idStr=[7], eid=[5]}
     */
    @PutMapping("/eid/change")
    @RequiresPermissions(STUDENT + OperationConst.CHANGE_EID)
    public Integer updateEid(@RequestParam(name = "idStr") String idStr,
                             @RequestParam(name = "eid") Integer eid) throws ServiceException {
        return studentService.updateEid(idStr, eid);
    }

    /**
     * @api {delete} /student/{id} > 删除学员
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
     * @apiSuccessExample {json} 成功样例
     * {0:"重要",1:"一般",2:"不重要"}
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
     * @apiSuccessExample {json} 成功样例
     * {0:"没有意向",1:"继续跟进",2:"试听课程",3:"成为会员"}
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
     * @apiSuccessExample {json} 成功样例
     * {0:"有效",1:"无效",2:"会员"}
     */
    @GetMapping("/status")
    @RequiresAuthentication
    public Map<Byte, String> status() throws ServiceException {
        return StatusEnum.kv;
    }

    /**
     * @api {get} /student/phone?phone={} > 获取该电话是否被占用
     * @apiGroup StudentController
     * @apiPermission 登录状态
     * @apiDescription 该接口用于返回手机号是否被使用的信息
     * @apiParam {String} phone 检测的手机号
     * @apiSuccess {String} string 手机号检测结果
     * @apiParamExample 请求样例1
     * {phone=[12345678910]}
     * @apiSuccessExample 成功样例1
     * "手机号同王哈哈重复，录入人是员工[测试用]，学校是东北大学"
     * @apiParamExample 请求样例2
     * {phone=[13456780000]}
     * @apiSuccessExample 成功样例2
     * "手机号可以正常使用"
     * @apiParamExample 请求样例3
     * {phone=[]}
     * @apiSuccessExample 成功样例3
     * "手机号为空"
     */
    @GetMapping("/phone")
    @RequiresAuthentication
    public String checkPhoneUsed(@RequestParam(name = "phone") String phone) throws ServiceException {
        return studentService.checkPhoneUsed(phone);
    }
}