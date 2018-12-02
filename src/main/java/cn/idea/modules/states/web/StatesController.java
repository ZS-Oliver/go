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

import java.util.List;

/**
 * @apiDefine StatesController 学员状态相关
 */

@MethodMonitor
@RestController
@RequestMapping("/states")
public class StatesController {
    private static final String STATES = "states";
    @Autowired
    private StatesService statesService;

    /**
     * @api {post} /states > 创建状态
     * @apiGroup StatesController
     * @apiDescription 该接口用于学员状态更改记录
     * @apiPermission 管理员|市场经理|员工
     * @apiParam {Integer} sid 学员id
     * @apiParam {String} content 内容
     * @apiParam {Byte} nextState 下一状态
     * @apiParam {Byte} nextDegree 下一重要程度
     * @apiParam {Byte} nextStage 下一阶段
     * @apiParam {String} expectedDate 意向时间
     * @apiSuccessExample {json}
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
     * @api {get} /states/{id} > 查看状态
     * @apiGroup StatesController
     * @apiDescription 该接口用于查看学员状态更改记录
     * @apiPermission 管理员|市场经理|员工
     * @apiParam {Integer} id 状态记录id
     * @apiSuccess {Integer} eid 跟进人id
     * @apiSuccess {Integer} sid 学员id
     * @apiSuccess {Integer} ctime 创建时间
     * @apiSuccess {Byte} curStage 当前阶段
     * @apiSuccess {String} content 内容
     * @apiSuccess {Byte} nextState 学员下一状态
     * @apiSuccess {Byte} nextDegree 学员下一重要程度
     * @apiSuccess {Byte} nextStage 学员下一阶段
     * @apiSuccess {String} expectedDate 意向时间
     * @apiSuccessExample {json}
     * {"content":"不同意成为会员","curStage":2,"expectedDate":"20181005","nextDegree":2,"nextStage":2,"nextState":2,"sid":7}
     */
    @GetMapping("/{id:\\d+}")
    @RequiresPermissions(STATES + OperationConst.VIEW)
    public StatesVo view(@PathVariable("id") Integer id) throws ServiceException {
        return statesService.view(id);
    }

    /**
     * @api {get} /states?... > 查询状态
     * @apiGroup StatesController
     * @apiPermission 管理员|市场经理|员工
     * @apiDescription 该接口用于分页查看学员状态变化记录
     * @apiParam {Integer} [_no] 页数,默认为1
     * @apiParam {Integer} [_pageSize] 页数大小，默认15
     * @apiParam {Integer} [eid] 跟进人id
     * @apiParam {Integer} [sid] 学员id
     * @apiParam {Integer} [ctimeLow] 创建时间下限
     * @apiParam {Integer} [ctimeHigh] 创建时间上限
     * @apiParam {Byte} [curStage] 当前阶段
     * @apiParam {String} [content] 内容
     * @apiParam {Byte} [nextState] 学员下一状态
     * @apiParam {Byte} [nextDegree] 学员下一重要程度
     * @apiParam {Byte} [nextStage] 学员下一阶段
     * @apiParam {String} [expectedDateLow] 意向时间下限
     * @apiParam {String} [expectedDateHigh] 意向时间上限
     * @apiParam {boolean} [ascFlag] 升序标志
     * @apiSuccess {PageVo[StatesVo]} statesVoList 具体参考查看方法
     * @apiSuccessExample {json}
     * {"list":[{"content":"不同意成为会员","ctime":1538728041,"curStage":1,"expectedDate":"20181005","id":4,"nextDegree":2,"nextStage":2,"nextState":2,"eid":1,"sid":7},{"content":"不同意成为会员","ctime":1538728113,"curStage":2,"expectedDate":"20181005","id":5,"nextDegree":2,"nextStage":2,"nextState":2,"eid":1,"sid":7}],"no":1,"total":2,"totalPage":1}
     */
    @GetMapping
    @RequiresPermissions(STATES + OperationConst.LIST)
    public PageVo<StatesVo> list(@RequestParam(name = "_no", defaultValue = "1") Integer no,
                                 @RequestParam(name = "_pageSize", required = false) Integer pageSize,
                                 @RequestParam(name = "eid", required = false) Integer opId,
                                 @RequestParam(name = "sid", required = false) Integer sid,
                                 @RequestParam(name = "ctimeLow", required = false) Integer ctimeLow,
                                 @RequestParam(name = "ctimeHigh", required = false) Integer ctimeHigh,
                                 @RequestParam(name = "curStage", required = false) Byte curStage,
                                 @RequestParam(name = "content", required = false) String content,
                                 @RequestParam(name = "nextState", required = false) Byte nextState,
                                 @RequestParam(name = "nextDegree", required = false) Byte nextDegree,
                                 @RequestParam(name = "nextStage", required = false) Byte nextStage,
                                 @RequestParam(name = "expectedDateLow", required = false) String expectedDateLow,
                                 @RequestParam(name = "expectedDateHigh", required = false) String expectedDateHigh,
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
                .expectedDate(expectedDateLow, expectedDateHigh)
                .build();
        return statesService.list(no, pageSize, sqv);
    }

    /**
     * @api {delete} /states/{id} > 删除状态
     * @apiGroup StatesController
     * @apiDescription 该接口用于删除学员状态变更记录
     * @apiPermission 管理员|市场经理|员工
     * @apiParam {Integer} id 记录id
     */
    @DeleteMapping("/{id:\\d+}")
    @RequiresPermissions(STATES + OperationConst.DELETE)
    public BaseVo delete(@PathVariable("id") Integer id) throws ServiceException {
        return statesService.delete(id);
    }

    /**
     * @api {get} /states/student/{sid} > 查看学员状态
     * @apiGroup StatesController
     * @apiDescription 该接口用于查询学员的所有状态，按创建时间先后排序，新的在前
     * @apiPermission 管理员|市场经理|员工
     * @apiParam {Integer} sid 学员id
     * @apiSuccessExample {json} 成功样例
     * [{"content":"不同意成为会员","ctime":1538728113,"curStage":2,"expectedDate":"20181005","id":5,"nextDegree":2,"nextStage":2,"nextState":2,"opId":1,"sid":7},{"content":"不同意成为会员","ctime":1538728041,"curStage":1,"expectedDate":"20181005","id":4,"nextDegree":2,"nextStage":2,"nextState":2,"opId":1,"sid":7}]
     */
    @GetMapping("/student/{sid:\\d+}")
    @RequiresPermissions(STATES + OperationConst.VIEW)
    public List<StatesVo> studentStates(@PathVariable("sid") Integer sid) throws ServiceException {
        return statesService.acquireStates(sid);
    }
}
