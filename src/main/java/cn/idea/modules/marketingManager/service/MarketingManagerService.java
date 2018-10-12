package cn.idea.modules.marketingManager.service;

import cn.idea.modules.common.exception.JudgeException;
import cn.idea.modules.common.service.BaseService;
import cn.idea.modules.marketingManager.bean.MarketingManagerQVo;
import cn.idea.modules.marketingManager.bean.MarketingManagerVo;
import cn.idea.modules.marketingManager.dao.MarketingManagerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarketingManagerService extends BaseService<MarketingManagerVo, MarketingManagerQVo> {
    private final static String BEAN_NAME = "市场经理";

    @Autowired
    private MarketingManagerMapper marketingManagerMapper;

    protected MarketingManagerService() {
        super(BEAN_NAME);
    }

    @Override
    protected void preSave(MarketingManagerVo marketingManagerVo) throws JudgeException {
        judgeConflictWithFormer(marketingManagerVo);
    }

    private void judgeConflictWithFormer(MarketingManagerVo managerVo) throws JudgeException {
        MarketingManagerVo mv = marketingManagerMapper.findByPhone(managerVo.getPhone());
        if (mv != null) {
            throw new JudgeException(String.format("手机号同%s重复", mv.getName()));
        }
    }

    @Override
    protected void preUpdate(MarketingManagerVo newV, MarketingManagerVo oldV) throws JudgeException {
        MarketingManagerVo mv = marketingManagerMapper.findByPhone(newV.getPhone());
        if (mv != null && !mv.getId().equals(newV.getId())) {
            throw new JudgeException(String.format("手机号同%s重复", mv.getName()));
        }
    }
}