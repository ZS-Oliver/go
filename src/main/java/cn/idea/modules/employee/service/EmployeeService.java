package cn.idea.modules.employee.service;

import cn.idea.modules.common.exception.JudgeException;
import cn.idea.modules.common.service.BaseService;
import cn.idea.modules.employee.bean.EmployeeQVo;
import cn.idea.modules.employee.bean.EmployeeVo;
import cn.idea.modules.employee.dao.EmployeeMapper;
import cn.idea.modules.marketingManager.bean.MarketingManagerVo;
import cn.idea.modules.marketingManager.dao.MarketingManagerMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class EmployeeService extends BaseService<EmployeeVo, EmployeeQVo> {
    private static final String BEAN_NAME = "员工";

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private MarketingManagerMapper marketingManagerMapper;

    protected EmployeeService() {
        super(BEAN_NAME);
    }

    @Override
    protected void preSave(EmployeeVo employeeVo) throws JudgeException {
        MarketingManagerVo mv = marketingManagerMapper.find(employeeVo.getMid());
        JudgeException.when(mv == null, "该员工所属的市场经理不存在");
        employeeVo.setSchool(mv.getSchool());
        judgeConflictWithFormer4Save(employeeVo);
    }

    private void judgeConflictWithFormer4Save(EmployeeVo ev) throws JudgeException {
        EmployeeVo _ev = employeeMapper.findByPhone(ev.getPhone());
        JudgeException.when(_ev != null, String.format("手机号同%s重复", ev.getName()));
    }

    @Override
    protected void preUpdate(EmployeeVo newV, EmployeeVo oldV) throws JudgeException {
        EmployeeVo _ev = employeeMapper.findByPhone(newV.getPhone());
        JudgeException.when(_ev != null && !_ev.getId().equals(newV.getId()), String.format("手机号同%s重复", _ev.getName()));
    }
}