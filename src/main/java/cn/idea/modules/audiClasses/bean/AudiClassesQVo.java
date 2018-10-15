package cn.idea.modules.audiClasses.bean;

import cn.idea.modules.common.bean.ConflictingQVo;
import cn.idea.utils.assistant.CommonUtil;
import org.apache.ibatis.type.Alias;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Alias("audiClassesQVo")
public class AudiClassesQVo extends ConflictingQVo {
    private static final Predicate<String> opIdChecker = Pattern.compile("([0-9]+:?)+").asPredicate();

    private AudiClassesVo cv = new AudiClassesVo();
    private Set<Integer> opIdSet = new HashSet<>();
    private String audiTimeLow;
    private String audiTimeHigh;
    private Boolean ascFlag;

    public String getAudiTimeLow() {
        return audiTimeLow;
    }

    public String getAudiTimeHigh() {
        return audiTimeHigh;
    }

    public Set<Integer> getOpIdSet() {
        return opIdSet;
    }

    public String getName() {
        return cv.getName();
    }

    public Integer getTotal() {
        return cv.getTotal();
    }

    public String getSids() {
        return cv.getSids();
    }

    public String getSite() {
        return cv.getSite();
    }

    public Boolean getAscFlag() {
        return ascFlag;
    }

    private AudiClassesQVo() {
        super(false);
    }

    public static AudiClassesQVoBuilder builder() {
        return new AudiClassesQVoBuilder();
    }

    public static class AudiClassesQVoBuilder extends Builder<AudiClassesQVo> {
        protected AudiClassesQVoBuilder() {
            super(AudiClassesQVo.class);
        }

        public AudiClassesQVoBuilder name(@Nullable String name) {
            if (!q.isConflicting) {
                q.cv.setName(name);
            }
            return this;
        }

        public AudiClassesQVoBuilder auditionTime(@Nullable String audiTimeLow, @Nullable String audiTimeHigh) {
            try {
                Integer _audiTimeLow = audiTimeLow == null ? null : Integer.valueOf(audiTimeLow);
                Integer _audiTimeHigh = audiTimeHigh == null ? null : Integer.valueOf(audiTimeHigh);

                if (CommonUtil.firstBigger(_audiTimeLow, _audiTimeHigh)) {
                    q.isConflicting = true;
                } else {
                    q.audiTimeLow = audiTimeLow;
                    q.audiTimeHigh = audiTimeHigh;
                }
            } catch (Exception e) {
                q.isConflicting = true;
            }
            return this;
        }

        public AudiClassesQVoBuilder opId(@Nullable String opId) {
            if (q.isConflicting) return this;
            Set<Integer> set = CommonUtil.str2IntSet(opIdChecker, opId);
            if (set != null && set.isEmpty()) {
                q.isConflicting = true;
            } else {
                q.opIdSet = set;
            }
            return this;
        }

        public AudiClassesQVoBuilder total(@Nullable Integer total) {
            if (!q.isConflicting) {
                q.cv.setTotal(total);
            }
            return this;
        }

        public AudiClassesQVoBuilder sids(@Nullable String sids) {
            if (!q.isConflicting) {
                q.cv.setSids(sids);
            }
            return this;
        }

        public AudiClassesQVoBuilder site(@Nullable String site) {
            if (!q.isConflicting) {
                q.cv.setSite(site);
            }
            return this;
        }

        public AudiClassesQVoBuilder ascFlag(Boolean ascFlag) {
            if (!q.isConflicting) {
                q.ascFlag = ascFlag;
            }
            return this;
        }
    }
}
