package cn.idea.modules.student.bean;

import cn.idea.modules.common.bean.ConflictingQVo;
import org.apache.ibatis.type.Alias;

import javax.annotation.Nullable;

@Alias("studentQVo")
public class StudentQVo extends ConflictingQVo {
    private StudentVo sv = new StudentVo();
    private Boolean ascFlag;

    public String getName() {
        return sv.getName();
    }

    public String getNickname() {
        return sv.getNickname();
    }

    public String getBirthday() {
        return sv.getBirthday();
    }

    public String getParentName() {
        return sv.getParentName();
    }

    public String getPhone() {
        return sv.getPhone();
    }

    public String getAlternateNumber() {
        return sv.getAlternateNumber();
    }

    public String getWechat() {
        return sv.getWechat();
    }

    public String getAddr() {
        return sv.getAddr();
    }

    public Integer getSourceId() {
        return sv.getSourceId();
    }

    public Integer getCtime() {
        return sv.getCtime();
    }

    public Integer getEid() {
        return sv.getEid();
    }

    public Byte getState() {
        return sv.getState();
    }

    public Byte getDegree() {
        return sv.getDegree();
    }

    public Byte getSalesStage() {
        return sv.getSalesStage();
    }

    public String getExpectedDate() {
        return sv.getExpectedDate();
    }

    public String getContrateDate() {
        return sv.getContrateDate();
    }

    public Boolean getAscFlag() {
        return ascFlag;
    }

    private StudentQVo() {
        super(false);
    }

    public static StudentQVoBuilder builder() {
        return new StudentQVoBuilder();
    }

    public static class StudentQVoBuilder extends Builder<StudentQVo> {

        protected StudentQVoBuilder() {
            super(StudentQVo.class);
        }

        public StudentQVoBuilder name(@Nullable String name) {
            if (!q.isConflicting) {
                q.sv.setName(name);
            }
            return this;
        }

        public StudentQVoBuilder nickname(@Nullable String nickname) {
            if (!q.isConflicting) {
                q.sv.setNickname(nickname);
            }
            return this;
        }

        public StudentQVoBuilder birthday(@Nullable String birthday) {
            if (!q.isConflicting) {
                q.sv.setBirthday(birthday);
            }
            return this;
        }

        public StudentQVoBuilder parentName(@Nullable String parentName) {
            if (!q.isConflicting) {
                q.sv.setParentName(parentName);
            }
            return this;
        }

        public StudentQVoBuilder phone(@Nullable String phone) {
            if (!q.isConflicting) {
                q.sv.setPhone(phone);
            }
            return this;
        }

        public StudentQVoBuilder alternateNumber(@Nullable String alternateNumber) {
            if (!q.isConflicting) {
                q.sv.setAlternateNumber(alternateNumber);
            }
            return this;
        }

        public StudentQVoBuilder wechat(@Nullable String wechat) {
            if (!q.isConflicting) {
                q.sv.setWechat(wechat);
            }
            return this;
        }

        public StudentQVoBuilder addr(@Nullable String addr) {
            if (!q.isConflicting) {
                q.sv.setAddr(addr);
            }
            return this;
        }

        public StudentQVoBuilder sourceId(@Nullable Integer sourceId) {
            if (!q.isConflicting) {
                q.sv.setSourceId(sourceId);
            }
            return this;
        }

        public StudentQVoBuilder ctime(@Nullable Integer ctime) {
            if (!q.isConflicting) {
                q.sv.setCtime(ctime);
            }
            return this;
        }

        public StudentQVoBuilder eid(@Nullable Integer eid) {
            if (!q.isConflicting) {
                q.sv.setEid(eid);
            }
            return this;
        }

        public StudentQVoBuilder state(@Nullable Byte state) {
            if (!q.isConflicting) {
                q.sv.setState(state);
            }
            return this;
        }

        public StudentQVoBuilder degree(@Nullable Byte degree) {
            if (!q.isConflicting) {
                q.sv.setDegree(degree);
            }
            return this;
        }

        public StudentQVoBuilder salesStage(@Nullable Byte salesStage) {
            if (!q.isConflicting) {
                q.sv.setSalesStage(salesStage);
            }
            return this;
        }

        public StudentQVoBuilder expectedDate(@Nullable String expectedDate) {
            if (!q.isConflicting) {
                q.sv.setExpectedDate(expectedDate);
            }
            return this;
        }

        public StudentQVoBuilder contrateDate(@Nullable String contrateDate) {
            if (!q.isConflicting) {
                q.sv.setContrateDate(contrateDate);
            }
            return this;
        }

        public StudentQVoBuilder ascFlag(Boolean ascFlag) {
            if (!q.isConflicting) {
                q.ascFlag = ascFlag;
            }
            return this;
        }
    }
}
