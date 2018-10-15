package cn.idea.modules.student.bean;

import cn.idea.modules.common.bean.ConflictingQVo;
import cn.idea.utils.assistant.CommonUtil;
import com.google.common.base.Strings;
import org.apache.ibatis.type.Alias;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Alias("studentQVo")
public class StudentQVo extends ConflictingQVo {
    private static final Predicate<String> Checker = Pattern.compile("([0-9]+:?)+").asPredicate();
    private StudentVo sv = new StudentVo();
    private Boolean ascFlag;
    private Set<Integer> eids;
    private Set<Byte> degreeSet;
    private Set<Byte> salesStageSet;
    private Set<Byte> stateSet;
    private Set<Integer> sourceIdSet;

    private String birthdayLow;
    private String birthdayHigh;
    private String expectedDateLow;
    private String expectedDateHigh;
    private String contractDateLow;
    private String contractDateHigh;
    private Integer ctimeLow;
    private Integer ctimeHigh;
    private Integer ageLow;
    private Integer ageHigh;

    public Integer getCtimeLow() {
        return ctimeLow;
    }

    public Integer getCtimeHigh() {
        return ctimeHigh;
    }

    public Set<Integer> getEids() {
        return eids;
    }

    public Set<Integer> getSourceIdSet() {
        return sourceIdSet;
    }

    public Set<Byte> getDegreeSet() {
        return degreeSet;
    }

    public Set<Byte> getSalesStageSet() {
        return salesStageSet;
    }

    public Set<Byte> getStateSet() {
        return stateSet;
    }

    public String getBirthdayLow() {
        return birthdayLow;
    }

    public String getBirthdayHigh() {
        return birthdayHigh;
    }

    public String getExpectedDateLow() {
        return expectedDateLow;
    }

    public String getExpectedDateHigh() {
        return expectedDateHigh;
    }

    public String getContractDateLow() {
        return contractDateLow;
    }

    public String getContractDateHigh() {
        return contractDateHigh;
    }

    public Integer getAgeLow() {
        return ageLow;
    }

    public Integer getAgeHigh() {
        return ageHigh;
    }

    public String getName() {
        return sv.getName();
    }

    public String getNickname() {
        return sv.getNickname();
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

        public StudentQVoBuilder sourceId(@Nullable String sourceIdStr) {
            if (!q.isConflicting) {
                if (!Strings.isNullOrEmpty(sourceIdStr)) {
                    q.sourceIdSet = CommonUtil.str2IntSet(Checker, sourceIdStr);
                }
            }
            return this;
        }

        public StudentQVoBuilder ctimeRange(@Nullable Integer ctimeLow, @Nullable Integer ctimeHigh) {
            if (q.isConflicting) {
                return this;
            }
            if (ctimeLow != null && ctimeHigh != null && ctimeLow > ctimeHigh) {
                q.isConflicting = true;
            } else {
                q.ctimeLow = ctimeLow;
                q.ctimeHigh = ctimeHigh;
            }
            return this;
        }

        public StudentQVoBuilder state(@Nullable String stateStr) {
            if (!q.isConflicting) {
                if (!Strings.isNullOrEmpty(stateStr)) {
                    q.stateSet = CommonUtil.str2ByteSet(Checker, stateStr);
                }
            }
            return this;
        }

        public StudentQVoBuilder degree(@Nullable String degreeStr) {
            if (!q.isConflicting) {
                if (!Strings.isNullOrEmpty(degreeStr)) {
                    q.degreeSet = CommonUtil.str2ByteSet(Checker, degreeStr);
                }
            }
            return this;
        }

        public StudentQVoBuilder salesStage(@Nullable String salesStageStr) {
            if (!q.isConflicting) {
                if (!Strings.isNullOrEmpty(salesStageStr)) {
                    q.salesStageSet = CommonUtil.str2ByteSet(Checker, salesStageStr);
                }
            }
            return this;
        }

        public StudentQVoBuilder expectedDateRange(@Nullable String expectedDateLow, @Nullable String expectedDateHigh) {
            if (q.isConflicting) {
                return this;
            }
            if (CommonUtil.dt2UnixTime(expectedDateLow) > CommonUtil.dt2UnixTime(expectedDateHigh)) {
                q.isConflicting = true;
            } else {
                q.expectedDateLow = expectedDateLow;
                q.expectedDateHigh = expectedDateHigh;
            }
            return this;
        }

        public StudentQVoBuilder contractDateRange(@Nullable String contractDateLow, @Nullable String contractDateHigh) {
            if (q.isConflicting) {
                return this;
            }
            if (CommonUtil.dt2UnixTime(contractDateLow) > CommonUtil.dt2UnixTime(contractDateHigh))
                q.isConflicting = true;
            else {
                q.contractDateLow = contractDateLow;
                q.contractDateHigh = contractDateHigh;
            }
            return this;
        }

        public StudentQVoBuilder ascFlag(Boolean ascFlag) {
            if (!q.isConflicting) {
                q.ascFlag = ascFlag;
            }
            return this;
        }

        public StudentQVoBuilder eidSet(@Nullable Set<Integer> eids) {
            if (!q.isConflicting) {
                q.eids = eids;
            }
            return this;
        }

        public StudentQVoBuilder birthdayRange(@Nullable String birthdayLow, @Nullable String birthdayHigh) {
            if (q.isConflicting) {
                return this;
            }
            if (CommonUtil.dt2UnixTime(birthdayLow) > CommonUtil.dt2UnixTime(birthdayHigh)) {
                q.isConflicting = true;
            } else {
                q.birthdayLow = birthdayLow;
                q.birthdayHigh = birthdayHigh;
            }
            return this;

        }

        public StudentQVoBuilder ageRange(@Nullable Integer ageLow, @Nullable Integer ageHigh) {
            if (q.isConflicting) {
                return this;
            }
            if (ageLow != null && ageHigh != null && ageLow > ageHigh) {
                q.isConflicting = true;
            } else {
                q.ageLow = ageLow;
                q.ageHigh = ageHigh;
            }
            return this;

        }
    }
}
