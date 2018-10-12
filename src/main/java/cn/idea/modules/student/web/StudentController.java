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
import cn.idea.modules.student.bean.*;
import cn.idea.modules.student.service.StudentService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    /**
     * @api {post} /student 创建学员
     * @apiGroup StudentController
     * @apiDescription 该接口用于创建学员
     * @apiPermission 管理员|市场经理|员工
     * @apiParam {String} name 姓名
     * @apiParam {String} [nickname] 昵称
     * @apiParam {String} [birthday]生日
     * @apiParam {String} [parentName] 家长姓名
     * @apiParam {String} phone 手机号
     * @apiParam {String} [alternateNumber] 备用号码
     * @apiParam {String} [wechat] 微信号
     * @apiParam {String} [addr] 家庭住址
     * @apiParam {Integer} [sourceId] 来源id
     * @apiParam {Byte} state 学员状态
     * @apiParam {Byte} degree 重要程度
     * @apiParam {Byte} salesStage 销售阶段
     * @apiParam {String} [expectedDate] 意向日期
     * @apiSuccessExample {json}
     * {"addr":"江苏省连云港市赣榆区","alternateNumber":"12323232323","birthday":"19991020","degree":1,"expectedDate":"20200101","name":"学生[测试]","nickname":"小二","parentName":"张三","phone":"12323232323","salesStage":2,"sourceId":3,"state":1,"wechat":"hello,motherfucker"}
     */
    @PostMapping
    @RequiresPermissions(STUDENT + OperationConst.CREATE)
    public BaseVo create(@RequestBody @Validated(SaveGroup.class) StudentVo sv,
                         @SessionAttribute(SessionConst.AUTH) byte auth,
                         @SessionAttribute(SessionConst.UID) Integer uid) throws Exception {
        ServiceException.when(auth != Role.EMPLOYEE.getCode(), "只有员工可以创建学员");
        sv.setEid(uid);
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
     * @apiParamSuccess {String} expectedDate 意向日期
     * @apiParamSuccess {String} contrateDate 签约日期
     * @apiSuccessExample {json}
     * {"addr":"江苏省连云港市赣榆区","alternateNumber":"12323232323","ctime":1538589861,"degree":1,"expectedDate":"20181004","id":6,"name":"学生丙","eid":1,"parentName":"张三","phone":"12313312312","salesStage":2,"sourceId":3,"state":1,"wechat":"hello,motherfucker"}
     */
    @GetMapping("/{id:\\d+}")
    @RequiresPermissions(STUDENT + OperationConst.VIEW)
    public StudentVo view(@PathVariable("id") Integer id) throws ServiceException {
        return studentService.view(id);
    }

    /**
     * @api {get} /student/date/{date} 查看学员
     * @apiGroup StudentController
     * @apiPermission 管理员|市场经理|员工
     * @apiDescription 该接口用于查询参加试听的学员
     * @apiParam {String} expectedDate 意向时间
     * @apiParamSuccess {Integer} id 员工id
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
     * @apiParamSuccess {String} expectedDate 意向日期
     * @apiParamSuccess {String} contrateDate 签约日期
     * @apiSuccessExample {json}
     * {"addr":"江苏省连云港市赣榆区","alternateNumber":"12323232323","ctime":1538589861,"degree":1,"expectedDate":"20181004","id":6,"name":"学生丙","eid":1,"parentName":"张三","phone":"12313312312","salesStage":2,"sourceId":3,"state":1,"wechat":"hello,motherfucker"},{"addr":"江苏省连云港市赣榆区","alternateNumber":"12323232323","ctime":1538590139,"degree":1,"expectedDate":"20181004","id":8,"name":"大阿璇","eid":1,"parentName":"张三","phone":"12313312315","salesStage":2,"sourceId":3,"state":1,"wechat":"hello,world"}
     */
    @GetMapping("/date/{date}")
    @RequiresPermissions(STUDENT + OperationConst.VIEW)
    public List<StudentVo> viewStudentToAudition(@PathVariable("date") String date) throws ServiceException {
        return studentService.viewStudentToAudition(date);
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
     * @apiParam {String} [birthday] 生日，模糊匹配
     * @apiParam {String} [parentName] 家长姓名，模糊匹配
     * @apiParam {String} [phone] 手机号，模糊匹配
     * @apiParam {String} [alternateNumber] 备用号码，模糊匹配
     * @apiParam {String} [wechat] 微信号，模糊匹配
     * @apiParam {String} [addr] 家庭住址，模糊匹配
     * @apiParam {Integer} [sourceId] 来源id，模糊匹配
     * @apiParam {Integer} [ctime] 创建时间，模糊匹配
     * @apiParam {Integer} [eid] 创建人id，模糊匹配
     * @apiParam {Byte} [state] 学员状态，模糊匹配
     * @apiParam {Byte} [degree] 重要程度，模糊匹配
     * @apiParam {Byte} [salesStage] 销售阶段，模糊匹配
     * @apiParam {String} [expectedDate]意向日期，模糊匹配
     * @apiParam {String} [contrateDate] 签约日期，模糊匹配
     * @apiParam {boolean} [ascFlag] 升序标志
     * @apiSuccess {PageVo[StudentVo]} studentVoList 具体参考查看方法
     */
    @GetMapping
    @RequiresPermissions(STUDENT + OperationConst.LIST)
    public PageVo<StudentVo> list(@RequestParam(name = "_no", defaultValue = "1") Integer no,
                                  @RequestParam(name = "_pageSize", required = false) Integer pageSize,
                                  @RequestParam(name = "_name", required = false) String name,
                                  @RequestParam(name = "nickname", required = false) String nickname,
                                  @RequestParam(name = "birthday", required = false) String birthday,
                                  @RequestParam(name = "parentName", required = false) String parentName,
                                  @RequestParam(name = "phone", required = false) String phone,
                                  @RequestParam(name = "alternateNumber", required = false) String alternateNumber,
                                  @RequestParam(name = "wechat", required = false) String wechat,
                                  @RequestParam(name = "addr", required = false) String addr,
                                  @RequestParam(name = "sourceId", required = false) Integer sourceId,
                                  @RequestParam(name = "ctime", required = false) Integer ctime,
                                  @RequestParam(name = "eid", required = false) Integer eid,
                                  @RequestParam(name = "state", required = false) Byte state,
                                  @RequestParam(name = "degree", required = false) Byte degree,
                                  @RequestParam(name = "salesStage", required = false) Byte salesStage,
                                  @RequestParam(name = "expectedDate", required = false) String expectedDate,
                                  @RequestParam(name = "contrateDate", required = false) String contrateDate,
                                  @RequestParam(name = "ascFlag", defaultValue = "true") Boolean ascFlag) throws ServiceException {
        StudentQVo sqv = StudentQVo.builder()
                .name(name)
                .nickname(nickname)
                .birthday(birthday)
                .parentName(parentName)
                .phone(phone)
                .alternateNumber(alternateNumber)
                .wechat(wechat)
                .addr(addr)
                .sourceId(sourceId)
                .ctime(ctime)
                .eid(eid)
                .state(state)
                .degree(degree)
                .salesStage(salesStage)
                .expectedDate(expectedDate)
                .contrateDate(contrateDate)
                .ascFlag(ascFlag)
                .build();
        return studentService.list(no, pageSize, sqv);
    }

    /**
     * @api {put} /student/{id} 更新学员
     * @apiGroup StudentController
     * @apiDescription 该接口用于更新学员
     * @apiPermission 管理员|市场经理
     * @apiParam {String} [name] 姓名
     * @apiParam {String} [nickname] 昵称
     * @apiParam {String} [birthday]生日
     * @apiParam {String} [parentName] 家长姓名
     * @apiParam {String} [phone] 手机号
     * @apiParam {String} [alternateNumber] 备用号码
     * @apiParam {String} [wechat] 微信号
     * @apiParam {String} [addr] 家庭住址
     * @apiParam {Integer} [sourceId] 来源id
     * @apiParam {Integer} [eid] 创建人id
     * @apiParam {Byte} [state] 学员状态
     * @apiParam {Byte} [degree] 重要程度
     * @apiParam {Byte} [salesStage] 销售阶段
     * @apiParam {String} [expectedDate] 意向日期
     * @apiSuccess {json}
     * {"name":"阿璇","eid":2}
     */
    @PutMapping("/{id:\\d+}")
    @RequiresPermissions(STUDENT + OperationConst.UPDATE)
    public BaseVo update(@PathVariable("id") Integer id,
                         @RequestBody @Validated(UpdateGroup.class) StudentVo sv) throws ServiceException {
        sv.setId(id);
        return studentService.update(sv);
    }

    /**
     * @api {delete} /student/{id} 删除学员
     * @apiGroup StudentController
     * @apiDescription 该接口用于删除学员
     * @apiPermission 员工
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
     * @apiPermission 管理员|市场经理|员工
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
     * @apiPermission 管理员|市场经理|员工
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
     * @apiPermission 管理员|市场经理|员工
     * @apiDescription 该接口用于根据学员状态code获取含义
     * @apiSuccess {Map} kv 学员状态code及含义
     */
    @GetMapping("/status")
    @RequiresAuthentication
    public Map<Byte, String> status() throws ServiceException {
        return StatusEnum.kv;
    }

}
