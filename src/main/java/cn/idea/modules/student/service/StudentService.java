package cn.idea.modules.student.service;

import cn.idea.modules.common.exception.JudgeException;
import cn.idea.modules.common.exception.ServiceException;
import cn.idea.modules.common.service.BaseService;
import cn.idea.modules.employee.bean.EmployeeVo;
import cn.idea.modules.employee.dao.EmployeeMapper;
import cn.idea.modules.source.bean.SourceVo;
import cn.idea.modules.source.dao.SourceMapper;
import cn.idea.modules.student.bean.*;
import cn.idea.modules.student.dao.StudentMapper;
import cn.idea.utils.assistant.CommonUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
            EmployeeVo ev = employeeMapper.find(_sv.getEid());
            if (ev != null) {
                throw new JudgeException(String.format("手机号同%s重复，录入人是%s，学校是%s", _sv.getName(), ev.getName(), ev.getSchool()));
            } else {
                throw new JudgeException(String.format("手机号同%s重复，录入人可能已删除", _sv.getName()));
            }
        }
    }

    private void judgeConflict(StudentVo sv) throws JudgeException {
        if (sv.getSourceId() != null) {
            JudgeException.when(sv.getSourceId() != null && sourceMapper.find(sv.getSourceId()) == null, "来源不存在");
        }
        if (sv.getState() != null) {
            JudgeException.when(Arrays.stream(StatusEnum.values()).noneMatch(x -> x.getCode() == sv.getState()), "学员状态字段有误");
        }
        if (sv.getSalesStage() != null) {
            JudgeException.when(Arrays.stream(SalesStageEnum.values()).noneMatch(x -> x.getCode() == sv.getSalesStage()), "销售阶段字段有误");
            if (sv.getSalesStage() != SalesStageEnum.FOLLOW.getCode() && sv.getSalesStage() != SalesStageEnum.AUDITION.getCode()) {
                JudgeException.when(sv.getExpectedDate() != null, "只有继续跟进与试听阶段才需输入意向时间");
            } else if (sv.getSalesStage() == SalesStageEnum.FOLLOW.getCode() || sv.getSalesStage() == SalesStageEnum.AUDITION.getCode()) {
                JudgeException.when(sv.getExpectedDate() == null, "继续跟进与试听阶段必须填入意向时间");
            }
        }
        if (sv.getDegree() != null) {
            JudgeException.when(Arrays.stream(ImportanceEnum.values()).noneMatch(x -> x.getCode() == sv.getDegree()), "重要程度字段有误");
        }
    }

    @Override
    protected void preSave(StudentVo sv) throws JudgeException {
        EmployeeVo ev = employeeMapper.find(sv.getEid());
        System.out.println(JSON.toJSON(ev));
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
            EmployeeVo ev = employeeMapper.find(sv.getEid());
            if (ev != null) {
                throw new JudgeException(String.format("手机号同%s重复，录入人是%s，学校是%s", sv.getName(), ev.getName(), ev.getSchool()));
            } else {
                throw new JudgeException(String.format("手机号同%s重复，录入人可能已删除", sv.getName()));
            }
        }
        perfectNewV(newV, oldV);
        judgeConflict(newV);
        if (newV.getSalesStage() != null && newV.getSalesStage() == SalesStageEnum.BECOME_VIP.getCode()) {
            newV.setContractDate(CommonUtil.curTimeStr());
            newV.setState(StatusEnum.VIP.getCode());
        }
        // 更新后为会员状态，更新前不是会员状态
        if (newV.getState().equals(StatusEnum.VIP.getCode()) && !oldV.getState().equals(StatusEnum.VIP.getCode())) {
            newV.setContractDate(CommonUtil.curTimeStr());
        }
        if (newV.getBirthday() != null) {
            newV.setAge(CommonUtil.getAgeByBirth(newV.getBirthday()));
        }
    }

    private void perfectNewV(StudentVo newV, StudentVo oldV) {
        if (newV.getState() == null) {
            newV.setState(oldV.getState() != null ? oldV.getState() : StatusEnum.VALID.getCode());
        }
        if (newV.getDegree() == null) {
            newV.setDegree(oldV.getDegree() != null ? oldV.getDegree() : ImportanceEnum.NORMAL.getCode());
        }
        if (newV.getSalesStage() == null) {
            newV.setSalesStage(oldV.getSalesStage() != null ? oldV.getSalesStage() : SalesStageEnum.NOINTENTION.getCode());
        }
    }

    @Override
    protected void postView(StudentVo sv) throws ServiceException {
        sv.setEname(employeeMapper.find(sv.getEid()).getName());
        if (sv.getSourceId() != null) {
            sv.setSource(sourceMapper.find(sv.getSourceId()).getSource());
        }
    }

    @Override
    protected void postList(List<StudentVo> vl) throws ServiceException {
        Set<Integer> eids = vl.stream().map(StudentVo::getEid).collect(Collectors.toSet());
        List<EmployeeVo> evList = employeeMapper.findByIds(eids);
        Map<Integer, String> evMap = evList.stream().collect(Collectors.toMap(EmployeeVo::getId, EmployeeVo::getName));

        Set<Integer> sids = vl.stream().map(StudentVo::getSourceId).collect(Collectors.toSet());
        List<SourceVo> svList = sourceMapper.findByIds(sids);
        Map<Integer, String> svMap = svList.stream().collect(Collectors.toMap(SourceVo::getId, SourceVo::getSource));

        vl.forEach(v -> {
            v.setEname(evMap.get(v.getEid()));
            v.setSource(svMap.get(v.getSourceId()));
        });

//        List<Integer> svEids = vl.stream().map(StudentVo::getEid).collect(Collectors.toList());
//        List<EmployeeVo> evList = employeeMapper.findByIds(svEids);
//        Map<Integer, List<EmployeeVo>> sv2evs = evList.stream().collect(Collectors.groupingBy(EmployeeVo::getId));
//        vl.forEach(x -> x.setEname(sv2evs.get(x.getEid()).get(0).getName()));
//        List<Integer> svSids = vl.stream().map(StudentVo::getSourceId).collect(Collectors.toList());

    }

    /**
     * 更新学员的eid
     */
    public Integer updateEid(String idStr, Integer eid) throws ServiceException {
        Set<Integer> idSet = CommonUtil.str2IntSet(Checker, idStr);
        List<StudentVo> svs = studentMapper.findByIds(idSet);
        Set<Integer> eids = svs.stream().map(StudentVo::getEid).collect(Collectors.toSet());
        List<EmployeeVo> evs = employeeMapper.findByIds(eids);
        EmployeeVo ev = employeeMapper.find(eid);
        ServiceException.when(ev == null, "该员工不存在");
        ServiceException.when(!evs.stream().allMatch(e -> e.getMid().equals(ev.getMid())), "存在某个员工和要改成的员工不是同一学校");
        return studentMapper.batchUpdateEid(idSet, eid);
    }

    public String checkPhoneUsed(String phone) throws ServiceException {
        if (Strings.isNullOrEmpty(phone)) {
            throw new ServiceException("手机号为空");
        } else {
            StudentVo sv = studentMapper.findByPhone(phone);
            if (sv != null) {
                EmployeeVo ev = employeeMapper.find(sv.getEid());
                if (ev != null) {
                    throw new ServiceException(String.format("手机号同%s重复，录入人是%s，学校是%s", sv.getName(), ev.getName(), ev.getSchool()));
                } else {
                    throw new ServiceException(String.format("手机号同%s重复，录入人可能已删除", sv.getName()));
                }
            } else {
                return "手机号可以正常使用";
            }
        }
    }
}
