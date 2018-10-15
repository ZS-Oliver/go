package cn.idea.modules.marketingManager.web;

import cn.idea.modules.common.annotation.MethodMonitor;
import cn.idea.modules.common.bean.BaseVo;
import cn.idea.modules.common.bean.PageVo;
import cn.idea.modules.common.consts.OperationConst;
import cn.idea.modules.common.exception.ServiceException;
import cn.idea.modules.common.group.SaveGroup;
import cn.idea.modules.common.group.UpdateGroup;
import cn.idea.modules.marketingManager.bean.MarketingManagerQVo;
import cn.idea.modules.marketingManager.bean.MarketingManagerVo;
import cn.idea.modules.marketingManager.service.MarketingManagerService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @apiDefine MarketingManager 市场经理相关
 */

@MethodMonitor
@RestController
@RequestMapping("/marketingManager")
public class MarketingManagerController {
    private static final String MARKETING_MANAGER = "marketing_manager";
    @Autowired
    private MarketingManagerService marketingManagerService;

    /**
     * @api {post} /marketingManager 创建市场经理
     * @apiGroup MarketingManagerController
     * @apiDescription 该接口用于创建市场经理，密码自动初始化为初始密码
     * @apiPermission 管理员
     * @apiParam {String} name 名字
     * @apiParam {String} phone 手机号
     * @apiParam {String} school 学校
     * @apiParamExample {json} 请求样例
     * {"name":"王艳涛","phone":"15232786339","school":"东北大学"}
     */
    @PostMapping
    @RequiresPermissions(MARKETING_MANAGER + OperationConst.CREATE)
    public BaseVo create(@RequestBody @Validated({SaveGroup.class}) MarketingManagerVo mv) throws ServiceException {
        return marketingManagerService.save(mv);
    }

    /**
     * @api {get} /marketingManager/_no=?... 查询市场经理
     * @apiGroup MarketingManagerController
     * @apiDescription 该接口用于查询市场经理
     * @apiPermission 管理员|市场经理
     * @apiParam {Integer} [_no] 页码，默认为1
     * @apiParam {Integer} [_pageSize] 页面大小，默认15
     * @apiParam {String} [name] 姓名，模糊匹配
     * @apiParam {String} [phone] 手机号，模糊匹配
     * @apiParam {String} [school] 学校名，模糊匹配
     * @apiParam {Boolean} [ascFlag] 是否升序，默认是
     * @apiSuccessExample {json} 成功样例
     * {"list":[{"auth":0,"id":7,"name":"王艳涛","phone":"15232786339","school":"东北大学"}],"no":1,"total":1,"totalPage":1}
     */
    @GetMapping
    @RequiresPermissions(MARKETING_MANAGER + OperationConst.LIST)
    public PageVo<MarketingManagerVo> list(@RequestParam(name = "_no", defaultValue = "1") Integer _no,
                                           @RequestParam(name = "_pageSize", required = false) Integer _pageSize,
                                           @RequestParam(name = "name", required = false) String name,
                                           @RequestParam(name = "phone", required = false) String phone,
                                           @RequestParam(name = "school", required = false) String school,
                                           @RequestParam(name = "ascFlag", required = false) Boolean ascFlag) throws ServiceException {

        return marketingManagerService.list(_no, _pageSize, new MarketingManagerQVo(name, phone, school, ascFlag));
    }

    /**
     * @api {get} /marketingManager/{id} 查看市场经理
     * @apiGroup MarketingManagerController
     * @apiDescription 该接口用于查看市场经理
     * @apiPermission 管理员|市场经理
     * @apiParam {Integer} id 市场经理id
     * @apiSuccessExample {json} 成功样例
     * {"id":7,"name":"王艳涛","phone":"15232786339","school":"东北大学"}
     */
    @GetMapping("/{id:\\d+}")
    @RequiresPermissions(MARKETING_MANAGER + OperationConst.VIEW)
    public MarketingManagerVo view(@PathVariable("id") Integer id) throws ServiceException {
        return marketingManagerService.view(id);
    }

    /**
     * @api {put} /marketingManager/{id} 更新市场经理
     * @apiGroup MarketingManagerController
     * @apiDescription 该接口用于更新市场经理
     * @apiPermission 管理员
     * @apiParam {String} [name] 姓名
     * @apiParam {String} [school] 学校
     * @apiParamExample {json} 请求样例
     * {"name":"张硕","phone":"11111111111"}
     * @apiSuccessExample {json} 成功样例
     * {"id":7,"valid":1}
     */
    @PutMapping("/{id:\\d+}")
    @RequiresPermissions(MARKETING_MANAGER + OperationConst.UPDATE)
    public BaseVo update(@PathVariable("id") Integer id,
                         @RequestBody @Validated(UpdateGroup.class) MarketingManagerVo mv) throws ServiceException {
        mv.setId(id);
        return marketingManagerService.update(mv);
    }

    /**
     * @api {put} /marketingManager/{id} 删除市场经理
     * @apiGroup MarketingManagerController
     * @apiDescription 该接口用于删除市场经理
     * @apiPermission 管理员
     * @apiParam {Integer} id 市场经理id
     * @apiSuccessExample {json} 成功样例
     * {"id":7,"valid":0}
     * @apiErrorExample 失败样例
     * "无法删除市场经理，原因为:该市场经理下有员工，请先删除员工再重试"
     */
    @DeleteMapping("/{id:\\d+}")
    @RequiresPermissions(MARKETING_MANAGER + OperationConst.DELETE)
    public BaseVo delete(@PathVariable("id") Integer id) throws ServiceException {
        return marketingManagerService.delete(id);
    }
}
