package cn.idea.modules.student.service;

import cn.idea.modules.common.exception.JudgeException;
import cn.idea.modules.common.service.BaseService;
import cn.idea.modules.source.dao.SourceMapper;
import cn.idea.modules.student.bean.*;
import cn.idea.modules.student.dao.StudentMapper;
import cn.idea.utils.assistant.CommonUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class StudentService extends BaseService<StudentVo, StudentQVo> {
    private static final String BEAN_NAME = "学员";

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private SourceMapper sourceMapper;

    protected StudentService() {
        super(BEAN_NAME);
    }

    private void judgeConflictWithFormer4Save(StudentVo sv) throws JudgeException {
        StudentVo _sv = studentMapper.findByPhone(sv.getPhone());
        if (_sv != null) {
            JudgeException.when(true, String.format("手机号同%s重复", _sv.getName()));
        }

        JudgeException.when(sv.getSourceId() != null && sourceMapper.find(sv.getSourceId()) == null, "该条来源不存在");
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
        judgeConflictWithFormer4Save(sv);
        sv.setCtime((int) (System.currentTimeMillis() / 1000));
        if (sv.getSalesStage() == SalesStageEnum.BECOMEVIP.getCode()) {
            sv.setContrateDate(CommonUtil.curTimeStr());
        }
    }

    @Override
    protected void preUpdate(StudentVo newV, StudentVo oldV) throws JudgeException {
        StudentVo sv = studentMapper.findByPhone(newV.getPhone());
        if (sv != null && !newV.getId().equals(sv.getId())) {
            throw new JudgeException(String.format("手机号同%s重复", sv.getName()));
        }
        JudgeException.when(newV.getSourceId() != null && sourceMapper.find(newV.getSourceId()) == null, "该条来源不存在");
        JudgeException.when(Arrays.stream(StatusEnum.values()).noneMatch(x -> x.getCode() == newV.getState()), "学员状态字段有误");
        JudgeException.when(Arrays.stream(ImportanceEnum.values()).noneMatch(x -> x.getCode() == newV.getDegree()), "重要程度字段有误");
        JudgeException.when(Arrays.stream(SalesStageEnum.values()).noneMatch(x -> x.getCode() == newV.getSalesStage()), "销售阶段字段有误");
        if (newV.getSalesStage() != SalesStageEnum.FOLLOW.getCode() && newV.getSalesStage() != SalesStageEnum.AUDITION.getCode()) {
            JudgeException.when(newV.getExpectedDate() != null, "只有继续跟进与试听阶段才需输入意向时间");
        } else if (newV.getSalesStage() == SalesStageEnum.FOLLOW.getCode() || newV.getSalesStage() == SalesStageEnum.AUDITION.getCode()) {
            JudgeException.when(newV.getExpectedDate() == null, "继续跟进与试听阶段必须填入意向时间");
        }
    }

    /**
     * 根据意向日期得到试听学员
     *
     * @param date
     * @return
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
}
