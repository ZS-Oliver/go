package cn.idea.modules.employee.service;

import cn.idea.modules.common.bean.BaseVo;
import cn.idea.modules.common.exception.JudgeException;
import cn.idea.modules.common.exception.ServiceException;
import cn.idea.modules.common.service.BaseService;
import cn.idea.modules.employee.bean.EmployeeQVo;
import cn.idea.modules.employee.bean.EmployeeVo;
import cn.idea.modules.employee.dao.EmployeeMapper;
import cn.idea.modules.marketingManager.bean.MarketingManagerVo;
import cn.idea.modules.marketingManager.dao.MarketingManagerMapper;
import cn.idea.modules.student.bean.StudentVo;
import cn.idea.modules.student.dao.StudentMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class EmployeeService extends BaseService<EmployeeVo, EmployeeQVo> {
    private static final String BEAN_NAME = "员工";

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private MarketingManagerMapper marketingManagerMapper;
    @Autowired
    private StudentMapper studentMapper;

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
        MarketingManagerVo mv = marketingManagerMapper.findByPhone(ev.getPhone());
        if (mv != null) {
            throw new JudgeException(String.format("手机号同市场经理%s重复", mv.getName()));
        }
        EmployeeVo _ev = employeeMapper.findByPhone(ev.getPhone());
        if (_ev != null) {
            throw new JudgeException(String.format("手机号同员工%s重复", _ev.getName()));
        }
    }

    @Override
    protected void preUpdate(EmployeeVo newV, EmployeeVo oldV) throws JudgeException {
        if (newV.getMid() != null && !newV.getMid().equals(oldV.getMid())) {
            throw new JudgeException("该员工不存在");
        }
        MarketingManagerVo mv = marketingManagerMapper.findByPhone(newV.getPhone());
        if (mv != null) {
            throw new JudgeException(String.format("手机号同市场经理%s重复", mv.getName()));
        }
        EmployeeVo _ev = employeeMapper.findByPhone(newV.getPhone());
        if (_ev != null && !_ev.getId().equals(newV.getId())) {
            throw new JudgeException(String.format("手机号同员工%s重复", _ev.getName()));
        }
    }

    @Override
    protected void judgeAssociative(EmployeeVo ev) throws JudgeException {
        List<StudentVo> svs = studentMapper.findByEid(ev.getId());
        if (!CollectionUtils.isEmpty(svs)) {
            throw new JudgeException("该员工下学员不为空，请转移学员后再尝试");
        }
    }

    public BaseVo delete(Integer id, Integer mid) throws ServiceException {
        EmployeeVo ev = employeeMapper.find(id);
        if (ev == null || (mid != null && !ev.getMid().equals(mid))) {
            throw new ServiceException("%s信息不存在", BEAN_NAME);
        }

        try {
            judgeAssociative(ev);
        } catch (JudgeException e) {
            throw new ServiceException("%s无法删除,原因为:%s", BEAN_NAME, e.getMessage());
        }

        employeeMapper.delete(id);
        return BaseVo.ofInvalid(id);
    }
}