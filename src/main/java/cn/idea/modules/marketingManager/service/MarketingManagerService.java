package cn.idea.modules.marketingManager.service;

import cn.idea.modules.common.exception.JudgeException;
import cn.idea.modules.common.service.BaseService;
import cn.idea.modules.employee.bean.EmployeeVo;
import cn.idea.modules.employee.dao.EmployeeMapper;
import cn.idea.modules.marketingManager.bean.MarketingManagerQVo;
import cn.idea.modules.marketingManager.bean.MarketingManagerVo;
import cn.idea.modules.marketingManager.dao.MarketingManagerMapper;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketingManagerService extends BaseService<MarketingManagerVo, MarketingManagerQVo> {
    private final static String BEAN_NAME = "市场经理";

    @Autowired
    private MarketingManagerMapper marketingManagerMapper;
    @Autowired
    private EmployeeMapper employeeMapper;

    protected MarketingManagerService() {
        super(BEAN_NAME);
    }

    @Override
    protected void preSave(MarketingManagerVo marketingManagerVo) throws JudgeException {
        judgeConflictWithFormer(marketingManagerVo);
    }

    private void judgeConflictWithFormer(MarketingManagerVo managerVo) throws JudgeException {
        MarketingManagerVo mv = marketingManagerMapper.findByPhone(managerVo.getPhone());
        EmployeeVo ev = employeeMapper.findByPhone(managerVo.getPhone());
        if (mv != null) {
            throw new JudgeException(String.format("手机号同分校校长%s重复", mv.getName()));
        } else if (ev != null) {
            throw new JudgeException(String.format("手机号同员工%s重复", ev.getName()));
        }
    }

    @Override
    protected void preUpdate(MarketingManagerVo newV, MarketingManagerVo oldV) throws JudgeException {
        EmployeeVo ev = employeeMapper.findByPhone(newV.getPhone());
        if (ev != null) {
            throw new JudgeException(String.format("手机号同员工%s重复", ev.getName()));
        }
        MarketingManagerVo mv = marketingManagerMapper.findByPhone(newV.getPhone());
        if (mv != null && !mv.getId().equals(newV.getId())) {
            throw new JudgeException(String.format("手机号同分校校长%s重复", mv.getName()));
        }
    }

    @Override
    protected void judgeAssociative(MarketingManagerVo mv) throws JudgeException {
        List<EmployeeVo> evs = employeeMapper.findByMid(mv.getId());
        if (!CollectionUtils.isEmpty(evs)) {
            throw new JudgeException("该市场经理下有员工，请先删除员工再重试");
        }
    }
}