package cn.idea.modules.common.web;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @apiDefine TestController 供测试用接口
 */
@Log4j2
@RestController
@RequestMapping("/test")
public class TestController {
    /**
     * @api {get} /test/content?str={} > 返回发送来的内容
     * @apiGroup TestController
     * @apiPermission 所有人
     * @apiDescription 返回发送来的内容
     * @apiParam {String} str 内容
     * @apiParamExample {json} 测试样例
     * {"str" : "hello testController"}
     * @apiSuccess {String} content 传来的内容，直接在返回的body中
     * @apiSuccessExample {json} 成功返回
     * "hello testController"
     */
    @GetMapping("/content")
    public String showContent(@RequestParam(name = "str", required = false) String str) {
        log.info("str = {}", str);
        return str;
    }
}
