package cn.idea.modules.audiClasses.web;

import cn.idea.modules.audiClasses.bean.AudiClassesQVo;
import cn.idea.modules.audiClasses.bean.AudiClassesVo;
import cn.idea.modules.audiClasses.service.AudiClassesService;
import cn.idea.modules.common.annotation.MethodMonitor;
import cn.idea.modules.common.bean.BaseVo;
import cn.idea.modules.common.bean.PageVo;
import cn.idea.modules.common.consts.OperationConst;
import cn.idea.modules.common.consts.Role;
import cn.idea.modules.common.consts.SessionConst;
import cn.idea.modules.common.exception.ServiceException;
import cn.idea.modules.common.group.SaveGroup;
import cn.idea.modules.common.group.UpdateGroup;
import cn.idea.modules.employee.dao.EmployeeMapper;
import cn.idea.modules.marketingManager.dao.MarketingManagerMapper;
import cn.idea.modules.student.dao.StudentMapper;
import cn.idea.utils.assistant.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * 试听班级相关
 */


@MethodMonitor
@RestController
@RequestMapping("/audiClasses")
public class AudiClassesController {
    private static final Predicate<String> sidsChecker = Pattern.compile("([0-9]+:?)+").asPredicate();
    private static final String CLASSES = "AudiClassesQVo";
    @Autowired
    private AudiClassesService audiClassesService;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private MarketingManagerMapper marketingManagerMapper;
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * @api {post} /audiClasses
     * @apiGroup AudiClassesGroup
     * @apiDescription 该接口用于创建试听课程
     * @apiPermission 管理员|市场经理|员工
     * @apiParam {String} name 课程姓名
     * @apiParam {String} auditionTime 试听时间
     * @apiParam {Integer} total 总量
     * @apiParam {String} [sids] 学员id,以":"分隔
     * @apiParam {String} [site] 地点
     */
    @PostMapping
    @RequiresPermissions(CLASSES + OperationConst.CREATE)
    public BaseVo create(@RequestBody @Validated(SaveGroup.class) AudiClassesVo av,
                         @SessionAttribute(SessionConst.UID) Integer uid,
                         @SessionAttribute(SessionConst.AUTH) Byte auth) throws Exception {
        if (auth.equals(Role.ADMIN.getCode())) {
            ServiceException.when(av.getSite() == null, "总校长创建试听课程时需指定学校");
        } else if (auth.equals(Role.MARKETING_MANAGER.getCode())) {
            av.setSite(marketingManagerMapper.find(uid).getSchool());
        } else {
            av.setSite(employeeMapper.find(uid).getSchool());
        }
        av.setOpId(uid);
        return audiClassesService.save(av);
    }

    /**
     * @api {get}/audiClasses/{id} 查看试听课程
     * @apiGroup AudiClassesGroup
     * @apiPermission 管理员|市场经理|员工
     * @apiDescription 该接口用于查看试听课程的信息
     * @apiParam {Integer} id 课程id
     * @apiParamSuccess {String} name 班级名
     * @apiParamSuccess {String} auditionTime 试听时间
     * @apiParamSuccess {Integer} eid 创建人
     * @apiParamSuccess {Integer} total 总量
     * @apiParamSuccess {String} sids 学员id
     * @apiParamSuccess {String} site 地点
     * @apiSuccessExample {json}
     * {"auditionTime":"20181004","id":2,"name":"围棋[测试]","eid":1,"sids":"6:8","site":"小东门","total":30}
     */
    @GetMapping("/{id:\\d+}")
    @RequiresPermissions(CLASSES + OperationConst.VIEW)
    public AudiClassesVo view(@SessionAttribute(SessionConst.AUTH) Byte auth,
                              @SessionAttribute(SessionConst.UID) Integer uid,
                              @PathVariable("id") Integer id) throws ServiceException {
        if (auth == Role.EMPLOYEE.getCode() && !studentMapper.find(id).getEid().equals(uid)) {
            throw new ServiceException("该员工座下不存在该学员");
        }
        return audiClassesService.view(id);
    }

    /**
     * @api {get} /audiClasses?... 查看课程
     * @apiGroup AudiClassesGroup
     * @apiPermission 管理员|市场经理|员工
     * @apiDescription 该接口用于查询试听课程信息
     * @apiParam {String} [name] 课程名,模糊匹配
     * @apiParam {String} [audiTimeLow] 试听时间下限
     * @apiParam {String} [audiTimeHigh] 试听时间上限
     * @apiParam {String} [opIdStr] 操作人id,以:分隔
     * @apiParam {Integer} [total] 班级总人数
     * @apiParam {String} [sids] 学员id
     * @apiParam {String} [site] 地点
     * @apiParamExample {json}
     * {eid=[1]}
     * @apiSuccessExample {json} 成功样例
     * {"list":[{"auditionTime":"20181004","id":2,"name":"围棋[测试]","eid":1,"sids":"6:8","site":"小东门","total":30},{"auditionTime":"20181005","id":3,"name":"围棋[测试]","eid":1,"sids":"4:5:7","site":"东北大学","total":30}],"no":1,"total":2,"totalPage":1}
     */
    @GetMapping
    @RequiresPermissions(CLASSES + OperationConst.LIST)
    public PageVo<AudiClassesVo> list(@RequestParam(name = "_no", defaultValue = "1") Integer no,
                                      @RequestParam(name = "_pageSize", required = false) Integer pageSize,
                                      @RequestParam(name = "name", required = false) String name,
                                      @RequestParam(name = "eid", required = false) Integer opId,
                                      @RequestParam(name = "audiTimeLow", required = false) String audiTimeLow,
                                      @RequestParam(name = "audiTimeHigh", required = false) String audiTimeHigh,
                                      @RequestParam(name = "opIdStr", required = false) String opIdStr,
                                      @RequestParam(name = "total", required = false) Integer total,
                                      @RequestParam(name = "sids", required = false) String sids,
                                      @RequestParam(name = "site", required = false) String site) throws ServiceException {
        AudiClassesQVo aqv = AudiClassesQVo.builder()
                .name(name)
                .auditionTime(audiTimeLow, audiTimeHigh)
                .opId(opIdStr)
                .total(total)
                .sids(sids)
                .site(site)
                .build();
        return audiClassesService.list(no, pageSize, aqv);
    }

    /**
     * @api {put} /audiClasses/{id} 更新课程
     * @apiGroup AudiClassesGroup
     * @apiDescription 该接口用于更新试听课程信息
     * @apiPermission 管理员|市场经理|员工
     * @apiParam {String} [name] 班级名
     * @apiParam {String} [audiTime] 试听时间
     * @apiParam {Integer} [total] 总容量
     * @apiParam {String} [sids] 学员id,用:分开
     * @apiParam {String} [site] 地点
     */
    @PutMapping("/{id:\\d+}")
    @RequiresPermissions(CLASSES + OperationConst.UPDATE)
    public BaseVo update(@SessionAttribute(SessionConst.AUTH) Byte auth,
                         @SessionAttribute(SessionConst.UID) Integer uid,
                         @PathVariable("id") Integer id,
                         @Validated(UpdateGroup.class) @RequestBody AudiClassesVo av) throws ServiceException {
        if (auth == Role.EMPLOYEE.getCode() && av.getSids() != null) {

            if (CommonUtil.str2IntSet(sidsChecker, av.getSids())
                    .stream()
                    .anyMatch(x -> !studentMapper.find(x).getEid().equals(uid))) {
                throw new ServiceException("存在不属于该员工的学员");
            }
        }
        av.setId(id);
        return audiClassesService.update(av);
    }

    /**
     * @api {delete} /audiClasses/{id} 删除试听课程
     * @apiGroup AudiClassesGroup
     * @apiDescription 该接口用于删除试听课程
     * @apiPermission 管理员|市场经理|员工
     * @apiParam {Integer} id 试听课程id
     */
    @DeleteMapping("/{id:\\d+}")
    @RequiresPermissions(CLASSES + OperationConst.DELETE)
    public BaseVo delete(@PathVariable("id") Integer id) throws ServiceException {
        return audiClassesService.delete(id);
    }
}
