package cn.idea.modules.states.web;

import cn.idea.modules.common.annotation.MethodMonitor;
import cn.idea.modules.common.bean.BaseVo;
import cn.idea.modules.common.bean.PageVo;
import cn.idea.modules.common.consts.OperationConst;
import cn.idea.modules.common.consts.SessionConst;
import cn.idea.modules.common.exception.ServiceException;
import cn.idea.modules.common.group.SaveGroup;
import cn.idea.modules.states.bean.StatesQVo;
import cn.idea.modules.states.bean.StatesVo;
import cn.idea.modules.states.service.StatesService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @apiDefine States 学员状态相关
 */

@MethodMonitor
@RestController
@RequestMapping("/states")
public class StatesController {
    private static final String STATES = "states";
    @Autowired
    private StatesService statesService;

    /**
     * @api {post} /states 创建学员状态
     * @apiGroup StatesController
     * @apiDescription 该接口用于学员状态更改记录
     * @apiPermission 管理员|市场经理|员工
     * @apiParam {Integer} sid 学员id
     * @apiParam {String} content 内容
     * @apiParam {Byte} nextState 下一状态
     * @apiParam {Byte} nextDegree 下一重要程度
     * @apiParam {Byte} nextStage 下一阶段
     * @apiParam {String} exceptedDate 意向时间
     * @apiSeccessExample {json}
     * {"content":"同意成为会员","curStage":2,"nextDegree":2,"nextStage":3,"nextState":2,"sid":8}
     */
    @PostMapping
    @RequiresPermissions(STATES + OperationConst.CREATE)
    public BaseVo create(@RequestBody @Validated(SaveGroup.class) StatesVo sv,
                         @SessionAttribute(SessionConst.UID) Integer uid) throws Exception {
        sv.setOpId(uid);
        return statesService.save(sv);
    }

    /**
     * @api {get} /states/{id} 查看状态记录
     * @apiGroup StatesController
     * @apiDescription 该接口用于查看学员状态更改记录
     * @apiPermission 管理员|市场经理|员工
     * @apiParam {Integer} id 状态记录id
     * @apiParamSuccess {Integer} opId 跟进人id
     * @apiParamSuccess {Integer} sid 学员id
     * @apiParamSuccess {Integer} ctime 创建时间
     * @apiParamSuccess {Byte} curStage 当前阶段
     * @apiParamSuccess {String} content 内容
     * @apiParamSuccess {Byte} nextState 学员下一状态
     * @apiParamSuccess {Byte} nextDegree 学员下一重要程度
     * @apiParamSuccess {Byte} nextStage 学员下一阶段
     * @apiParamSuccess {String} exceptedDate 意向时间
     * @apiSuccessExample {json}
     * {"content":"不同意成为会员","curStage":2,"exceptedDate":"20181005","nextDegree":2,"nextStage":2,"nextState":2,"sid":7}
     */
    @GetMapping("/{id:\\d+}")
    @RequiresPermissions(STATES + OperationConst.VIEW)
    public StatesVo view(@PathVariable("id") Integer id) throws ServiceException {
        return statesService.view(id);
    }

    /**
     * @api {get} /states?... 查看状态记录
     * @apiGroup StatesController
     * @apiPermission 管理员|市场经理|员工
     * @apiDescription 该接口用于分页查看学员状态变化记录
     * @apiParam {Integer} [_no] 页数,默认为1
     * @apiParam {Integer} [_pageSize] 页数大小，默认15
     * @apiParam {Integer} [opId] 跟进人id
     * @apiParam {Integer} [sid] 学员id
     * @apiParam {Integer} [ctimeLow] 创建时间下限
     * @apiParam {Integer} [ctimeHigh] 创建时间上限
     * @apiParam {Byte} [curStage] 当前阶段
     * @apiParam {String} [content] 内容
     * @apiParam {Byte} [nextState] 学员下一状态
     * @apiParam {Byte} [nextDegree] 学员下一重要程度
     * @apiParam {Byte} [nextStage] 学员下一阶段
     * @apiParam {String} [exceptedDateLow] 意向时间下限
     * @apiParam {String} [exceptedDateHigh] 意向时间上限
     * @apiParam {boolean} [ascFlag] 升序标志
     * @apiSuccess {PageVo[StatesVo]} statesVoList 具体参考查看方法
     * @apiSuccessExample {json}
     * {"list":[{"content":"不同意成为会员","ctime":1538728041,"curStage":1,"exceptedDate":"20181005","id":4,"nextDegree":2,"nextStage":2,"nextState":2,"eid":1,"sid":7},{"content":"不同意成为会员","ctime":1538728113,"curStage":2,"exceptedDate":"20181005","id":5,"nextDegree":2,"nextStage":2,"nextState":2,"eid":1,"sid":7}],"no":1,"total":2,"totalPage":1}
     */
    @GetMapping
    @RequiresPermissions(STATES + OperationConst.LIST)
    public PageVo<StatesVo> list(@RequestParam(name = "_no", defaultValue = "1") Integer no,
                                 @RequestParam(name = "_pageSize", required = false) Integer pageSize,
                                 @RequestParam(name = "opId", required = false) Integer opId,
                                 @RequestParam(name = "sid", required = false) Integer sid,
                                 @RequestParam(name = "ctimeLow", required = false) Integer ctimeLow,
                                 @RequestParam(name = "ctimeHigh", required = false) Integer ctimeHigh,
                                 @RequestParam(name = "curStage", required = false) Byte curStage,
                                 @RequestParam(name = "content", required = false) String content,
                                 @RequestParam(name = "nextState", required = false) Byte nextState,
                                 @RequestParam(name = "nextDegree", required = false) Byte nextDegree,
                                 @RequestParam(name = "nextStage", required = false) Byte nextStage,
                                 @RequestParam(name = "exceptedDateLow", required = false) String exceptedDateLow,
                                 @RequestParam(name = "exceptedDateHigh", required = false) String exceptedDateHigh,
                                 @RequestParam(name = "ascFlag", defaultValue = "true") Boolean ascFlag) throws ServiceException {
        StatesQVo sqv = StatesQVo.builder()
                .opId(opId)
                .sid(sid)
                .ctime(ctimeLow, ctimeHigh)
                .curStage(curStage)
                .content(content)
                .nextState(nextState)
                .nextDegree(nextDegree)
                .nextStage(nextStage)
                .exceptedDate(exceptedDateLow, exceptedDateHigh)
                .build();
        return statesService.list(no, pageSize, sqv);
    }

    /**
     * @api {delete} /states/{id} 删除学员状态记录
     * @apiGroup StateseController
     * @apiDescription 该接口用于删除学员状态变更记录
     * @apiPermission 管理员|市场经理
     * @apiParam {Integer} id 记录id
     */
    @DeleteMapping("/{id:\\d+}")
    @RequiresPermissions(STATES + OperationConst.DELETE)
    public BaseVo delete(@PathVariable("id") Integer id) throws ServiceException {
        return statesService.delete(id);
    }
}
