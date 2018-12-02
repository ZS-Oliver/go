package cn.idea.modules.states.bean;

import cn.idea.modules.common.bean.ConflictingQVo;
import cn.idea.utils.assistant.CommonUtil;
import org.apache.ibatis.type.Alias;

import javax.annotation.Nullable;

@Alias("statesQVo")
public class StatesQVo extends ConflictingQVo {
    private StatesVo sv = new StatesVo();
    private Boolean ascFlag;
    private Integer ctimeLow;
    private Integer ctimeHigh;
    private String expectedDateLow;
    private String expectedDateHigh;

    public Integer getOpId() {
        return sv.getOpId();
    }

    public Integer getSid() {
        return sv.getSid();
    }

    public Integer getCtimeLow(){
        return ctimeLow;
    }

    public Integer getCtimeHigh(){
        return ctimeHigh;
    }

    public Byte getCurStage() {
        return sv.getCurStage();
    }

    public String getContent() {
        return sv.getContent();
    }

    public Byte getNextState() {
        return sv.getNextState();
    }

    public Byte getNextDegree() {
        return sv.getNextDegree();
    }

    public Byte getNextStage() {
        return sv.getNextStage();
    }

    public String getExceptedDateLow(){
        return expectedDateLow;
    }

    public String getExceptedDateHigh(){
        return expectedDateHigh;
    }

    public Boolean getAscFlag() {
        return ascFlag;
    }

    private StatesQVo() {
        super(false);
    }

    public static StatesQVoBuilder builder() {
        return new StatesQVoBuilder();
    }

    public static class StatesQVoBuilder extends Builder<StatesQVo> {

        protected StatesQVoBuilder() {
            super(StatesQVo.class);
        }

        public StatesQVoBuilder opId(@Nullable Integer opId) {
            if (!q.isConflicting) {
                q.sv.setOpId(opId);
            }
            return this;
        }

        public StatesQVoBuilder sid(@Nullable Integer sid) {
            if (!q.isConflicting) {
                q.sv.setSid(sid);
            }
            return this;
        }

        public StatesQVoBuilder ctime(@Nullable Integer ctimeLow,@Nullable Integer ctimeHigh) {
            if(q.isConflicting) return this;
            if(CommonUtil.firstBigger(ctimeLow, ctimeHigh)){
                q.isConflicting = true;
            }else {
                q.ctimeLow = ctimeLow;
                q.ctimeHigh = ctimeHigh;
            }
            return this;
        }

        public StatesQVoBuilder curStage(@Nullable Byte curStege) {
            if (!q.isConflicting) {
                q.sv.setCurStage(curStege);
            }
            return this;
        }

        public StatesQVoBuilder content(@Nullable String content) {
            if (!q.isConflicting) {
                q.sv.setContent(content);
            }
            return this;
        }

        public StatesQVoBuilder nextStage(@Nullable Byte nextStage) {
            if (!q.isConflicting) {
                q.sv.setNextStage(nextStage);
            }
            return this;
        }

        public StatesQVoBuilder nextDegree(@Nullable Byte nextDegree) {
            if (!q.isConflicting) {
                q.sv.setNextDegree(nextDegree);
            }
            return this;
        }

        public StatesQVoBuilder nextState(@Nullable Byte nextState) {
            if (!q.isConflicting) {
                q.sv.setNextState(nextState);
            }
            return this;
        }

        public StatesQVoBuilder expectedDate(@Nullable String expectedDateLow,@Nullable String expectedDateHigh) {
            try {
                Integer _expectedDateLow = expectedDateLow == null ? null : Integer.valueOf(expectedDateLow);
                Integer _expectedDateHigh = expectedDateHigh == null ? null : Integer.valueOf(expectedDateHigh);

                if (CommonUtil.firstBigger(_expectedDateLow, _expectedDateHigh)) {
                    q.isConflicting = true;
                } else {
                    q.expectedDateLow = expectedDateLow;
                    q.expectedDateHigh = expectedDateHigh;
                }
            } catch (Exception e) {
                q.isConflicting = true;
            }
            return this;

        }

        public StatesQVoBuilder ascFlag(Boolean ascFlag) {
            if (!q.isConflicting) {
                q.ascFlag = ascFlag;
            }
            return this;
        }
    }
}
