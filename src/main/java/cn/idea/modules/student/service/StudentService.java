package cn.idea.modules.student.service;

import cn.idea.modules.common.exception.JudgeException;
import cn.idea.modules.common.exception.ServiceException;
import cn.idea.modules.common.service.BaseService;
import cn.idea.modules.employee.bean.EmployeeVo;
import cn.idea.modules.employee.dao.EmployeeMapper;
import cn.idea.modules.source.dao.SourceMapper;
import cn.idea.modules.student.bean.*;
import cn.idea.modules.student.dao.StudentMapper;
import cn.idea.utils.assistant.CommonUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Log4j2
@Service
public class StudentService extends BaseService<StudentVo, StudentQVo> {
    private static final String BEAN_NAME = "学员";
    private static final Predicate<String> Checker = Pattern.compile("([0-9]+:?)+").asPredicate();

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private SourceMapper sourceMapper;
    @Autowired
    private EmployeeMapper employeeMapper;

    protected StudentService() {
        super(BEAN_NAME);
    }

    private void judgeConflictWithFormer4Save(StudentVo sv) throws JudgeException {
        StudentVo _sv = studentMapper.findByPhone(sv.getPhone());
        if (_sv != null) {
            throw new JudgeException(String.format("手机号同学员%s重复", _sv.getName()));
        }
    }

    private void judgeConflict(StudentVo sv) throws JudgeException {
        JudgeException.when(sv.getSourceId() != null && sourceMapper.find(sv.getSourceId()) == null, "来源不存在");
        JudgeException.when(Arrays.stream(StatusEnum.values()).noneMatch(x -> x.getCode() == sv.getState()), "学员状态字段有误");
        JudgeException.when(Arrays.stream(ImportanceEnum.values()).noneMatch(x -> x.getCode() == sv.getDegree()), "重要程度字段有误");
        JudgeException.when(Arrays.stream(SalesStageEnum.values()).noneMatch(x -> x.getCode() == sv.getSalesStage()), "销售阶段字段有误");
        if (sv.getSalesStage() != SalesStageEnum.FOLLOW.getCode() && sv.getSalesStage() != SalesStageEnum.AUDITION.getCode()) {
            JudgeException.when(sv.getExpectedDate() != null, "只有继续跟进与试听阶段才需输入意向时间");
        } else if (sv.getSalesStage() == SalesStageEnum.FOLLOW.getCode() || sv.getSalesStage() == SalesStageEnum.AUDITION.getCode()) {
            JudgeException.when(sv.getExpectedDate() == null, "继续跟进与试听阶段必须填入意向时间");
        }
    }

    @Override
    protected void preSave(StudentVo sv) throws JudgeException {
        EmployeeVo ev = employeeMapper.find(sv.getEid());
        JudgeException.when(ev == null, "录入学员的员工不存在");
        judgeConflictWithFormer4Save(sv);
        judgeConflict(sv);
        sv.setCtime((int) (System.currentTimeMillis() / 1000));
        if (sv.getSalesStage() == SalesStageEnum.BECOME_VIP.getCode()) {
            sv.setContractDate(CommonUtil.curTimeStr());
            sv.setState(StatusEnum.VIP.getCode());
        }
        if (sv.getBirthday() != null) {
            sv.setAge(CommonUtil.getAgeByBirth(sv.getBirthday()));
        }
    }

    @Override
    protected void preUpdate(StudentVo newV, StudentVo oldV) throws JudgeException {
        StudentVo sv = studentMapper.findByPhone(newV.getPhone());
        if (sv != null && !newV.getId().equals(sv.getId())) {
            throw new JudgeException(String.format("手机号同%s重复", sv.getName()));
        }
        judgeConflict(newV);
        if (newV.getSalesStage() == SalesStageEnum.BECOME_VIP.getCode()) {
            newV.setContractDate(CommonUtil.curTimeStr());
            newV.setState(StatusEnum.VIP.getCode());
        }
        if (newV.getBirthday() != null) {
            newV.setAge(CommonUtil.getAgeByBirth(newV.getBirthday()));
        }
    }

    /**
     * 根据意向日期得到试听学员
     */
    public List<StudentVo> viewStudentToAudition(String date) {
        List<StudentVo> svList = studentMapper.viewStudentToAudition(date);
        if (svList != null) {
            svList = svList.stream()
                    .filter(x -> x.getSalesStage() == SalesStageEnum.AUDITION.getCode())
                    .collect(Collectors.toList());
        }
        return svList;
    }

    /**
     * 更新学员的eid
     */
    public Integer updateEid(String idStr, Integer eid) throws ServiceException {
        Set<Integer> idSet = CommonUtil.str2IntSet(Checker, idStr);
        EmployeeVo ev = employeeMapper.find(eid);
        ServiceException.when(ev == null, "该员工不存在");
        int res = studentMapper.batchUpdateEid(idSet, eid);
        return res;
    }
}
