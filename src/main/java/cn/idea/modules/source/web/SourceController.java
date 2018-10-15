package cn.idea.modules.source.web;

import cn.idea.modules.common.annotation.MethodMonitor;
import cn.idea.modules.common.bean.BaseVo;
import cn.idea.modules.common.bean.PageVo;
import cn.idea.modules.common.consts.OperationConst;
import cn.idea.modules.common.exception.ServiceException;
import cn.idea.modules.common.group.SaveGroup;
import cn.idea.modules.common.group.UpdateGroup;
import cn.idea.modules.source.bean.SourceEnum;
import cn.idea.modules.source.bean.SourceQVo;
import cn.idea.modules.source.bean.SourceVo;
import cn.idea.modules.source.service.SourceService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @apiDefine SourceController 学员来源相关
 */

@MethodMonitor
@RestController
@RequestMapping("/source")
public class SourceController {
    private static final String SOURCE = "source";
    @Autowired
    private SourceService sourceService;

    /**
     * @api {post} /source 创建学员来源
     * @apiGroup SourceController
     * @apiDescription 该接口用于创建学员来源
     * @apiPermission 管理员|市场经理
     * @apiParam {Byte} type 类别
     * @apiParam {String} source 具体来源
     */
    @PostMapping
    @RequiresPermissions(SOURCE + OperationConst.CREATE)
    public BaseVo create(@RequestBody @Validated({SaveGroup.class}) SourceVo sv) throws ServiceException {
        return sourceService.save(sv);
    }

    /**
     * @api {get} /source/{id} 查看来源
     * @apiGroup SourceController
     * @apiPermissin 该接口用于来源的查看
     * @apiParam {Integer} id 来源id
     * @apiSuccess {Integer} id id
     * @apiSuccess {String} type 类型
     * @apiSuccess {String} source 具体来源
     * @apiSuccessExample {json} 成功样例
     * {"id":2,"source":"金海岸花园","type":0}
     */
    @GetMapping("/{id:\\d+}")
    @RequiresPermissions(SOURCE + OperationConst.VIEW)
    public SourceVo view(@PathVariable("id") Integer id) throws ServiceException {
        return sourceService.view(id);
    }

    /**
     * @api {get} /source?... 查询来源
     * @apiGroup SourceController
     * @apiDescription 该接口用于来源种类的查询
     * @apiPermission 管理员|市场经理|员工
     * @apiParam {Integer} [_no] 页数,默认为1
     * @apiParam {Integer} [_pageSize]
     * @apiParam {Byte} [type] 类别
     * @apiParam {String} [source] 具体来源,允许多个来源,以:分隔
     * @apiParam {boolean} [ascFlag] 升序标志
     * @apiParamExample {json} 请求样例
     * {type=[0], source=[金海岸花园:盐场家园]}
     * @apiSuccessExample {json} 成功样例
     * {"list":[{"id":1,"source":"盐场家园","type":0},{"id":2,"source":"金海岸花园","type":0}],"no":1,"total":2,"totalPage":1}
     */
    @GetMapping
    @RequiresPermissions(SOURCE + OperationConst.LIST)
    public PageVo<SourceVo> list(@RequestParam(name = "_no", defaultValue = "1") Integer no,
                                 @RequestParam(name = "_pageSize", required = false) Integer pageSize,
                                 @RequestParam(name = "type", required = false) Byte type,
                                 @RequestParam(name = "source", required = false) String source,
                                 @RequestParam(name = "ascFlag", defaultValue = "true") Boolean ascFlag) throws ServiceException {
        SourceQVo sqv = SourceQVo.builder()
                .type(type)
                .source(source)
                .ascFlag(ascFlag)
                .build();
        return sourceService.list(no, pageSize, sqv);
    }

    /**
     * @api {get} /source/phone > 获取来源的含义
     * @apiGroup SourceController
     * @apiPermission 管理员|市场经理|员工
     * @apiDescription 该接口用于根据类型code获取含义
     * @apiSuccess {Map} kv 来源code及含义
     */
    @GetMapping("/code")
    @RequiresAuthentication
    public Map<Byte, String> statuses() throws ServiceException {
        return SourceEnum.kv;
    }

    /**
     * @api {put} /source/{id} 更新来源
     * @apiGroup SourceController
     * @apiPermission 管理员|市场经理
     * @apiDescription 该接口用于更新来源
     * @apiParam {Integer} id ID
     */
    @PutMapping("/{id:\\d+}")
    @RequiresPermissions(SOURCE + OperationConst.UPDATE)
    public BaseVo update(@PathVariable("id") Integer id,
                         @Validated(UpdateGroup.class) @RequestBody SourceVo sv) throws ServiceException {
        sv.setId(id);
        return sourceService.update(sv);
    }
}
