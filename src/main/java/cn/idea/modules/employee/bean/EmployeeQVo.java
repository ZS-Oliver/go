package cn.idea.modules.employee.bean;

import cn.idea.modules.common.bean.ConflictingQVo;
import org.apache.ibatis.type.Alias;

import javax.annotation.Nullable;

@Alias("employeeQVo")
public class EmployeeQVo extends ConflictingQVo {
    private EmployeeVo ev = new EmployeeVo();
    private Boolean ascFlag;

    public String getName() {
        return ev.getName();
    }

    public String getPhone() {
        return ev.getPhone();
    }

    public String getSchool() {
        return ev.getSchool();
    }

    public Boolean getAscFlag() {
        return ascFlag;
    }

    private EmployeeQVo() {
        super(false);
    }

    public static EmployeeQVoBuilder builder() {
        return new EmployeeQVoBuilder();
    }

    public static class EmployeeQVoBuilder extends Builder<EmployeeQVo> {

        protected EmployeeQVoBuilder() {
            super(EmployeeQVo.class);
        }

        public EmployeeQVoBuilder name(@Nullable String name) {
            if (!q.isConflicting) {
                q.ev.setName(name);
            }
            return this;
        }

        public EmployeeQVoBuilder phone(@Nullable String phone) {
            if (!q.isConflicting) {
                q.ev.setPhone(phone);
            }
            return this;
        }

        public EmployeeQVoBuilder ascFlag(Boolean ascFlag) {
            if (!q.isConflicting) {
                q.ascFlag = ascFlag;
            }
            return this;
        }

        public EmployeeQVoBuilder school(String school) {
            if (!q.isConflicting) {
                q.ev.setSchool(school);
            }
            return this;
        }
    }
}
