package cn.idea.modules.marketingManager.dao;

import cn.idea.modules.common.dao.BaseMapper;
import cn.idea.modules.marketingManager.bean.MarketingManagerQVo;
import cn.idea.modules.marketingManager.bean.MarketingManagerVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketingManagerMapper extends BaseMapper<MarketingManagerVo, MarketingManagerQVo> {
    /**
     * 通过手机号查找
     */
    MarketingManagerVo findByPhone(@Param("phone") String phone);

    /**
     * 无视valid查找
     */
    MarketingManagerVo findAll(Integer id);
}
