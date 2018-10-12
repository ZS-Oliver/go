package cn.idea.modules.states.service;

import cn.idea.modules.common.exception.JudgeException;
import cn.idea.modules.common.service.BaseService;
import cn.idea.modules.states.bean.StatesQVo;
import cn.idea.modules.states.bean.StatesVo;
import cn.idea.modules.student.bean.SalesStageEnum;
import cn.idea.modules.student.bean.StudentVo;
import cn.idea.modules.student.dao.StudentMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class StatesService extends BaseService<StatesVo, StatesQVo> {
    private static final String BEAN_NAME = "状态轨迹";

    @Autowired
    private StudentMapper studentMapper;

    protected StatesService() {
        super(BEAN_NAME);
    }

    public void judgeConflictWithFormer4Save(StatesVo sv) throws JudgeException {
        JudgeException.when(studentMapper.find(sv.getSid()) == null, "不存在该学员");
    }

    @Override
    protected void preSave(StatesVo sv) throws JudgeException {
        judgeConflictWithFormer4Save(sv);
        StudentVo _sv = new StudentVo(); // 更新学员表
        _sv.setId(sv.getSid());
        sv.setCurStage(studentMapper.find(sv.getSid()).getSalesStage());
        sv.setCtime((int) (System.currentTimeMillis() / 1000));
        if (sv.getNextStage() != null) {
            boolean isNeedExceptedDate = sv.getNextStage() == SalesStageEnum.FOLLOW.getCode() || sv.getNextStage() == SalesStageEnum.AUDITION.getCode();
            JudgeException.when(isNeedExceptedDate && sv.getExceptedDate() == null, "销售阶段为试听和继续跟进的必须填入意向时间");
            _sv.setSalesStage(sv.getNextStage());
            if (isNeedExceptedDate) {
                _sv.setExpectedDate(sv.getExceptedDate());
            }
        } else {
            JudgeException.when(sv.getExceptedDate() != null, "只有为销售阶段为试听和继续跟进的需要填写时间");
        }
        if (sv.getNextState() != null) {
            _sv.setState(sv.getNextState());
        }
        if (sv.getNextDegree() != null) {
            _sv.setDegree(sv.getNextDegree());
        }
        studentMapper.update(_sv);
    }

    @Override
    protected void preUpdate(StatesVo newV, StatesVo oldV) throws JudgeException {

    }
}
