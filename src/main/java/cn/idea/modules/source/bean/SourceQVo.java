package cn.idea.modules.source.bean;


import cn.idea.modules.common.bean.ConflictingQVo;
import cn.idea.utils.assistant.CommonUtil;
import org.apache.ibatis.type.Alias;

import javax.annotation.Nullable;
import java.util.Set;

@Alias("sourceQVo")
public class SourceQVo extends ConflictingQVo {
    private SourceVo sv = new SourceVo();
    private Boolean ascFlag;
    private Set<String> sourceSet;

    public Byte getType() {
        return sv.getType();
    }

    public Set<String> getSourceSet() {
        return sourceSet;
    }

    public Boolean getAscFlag() {
        return ascFlag;
    }

    private SourceQVo() {
        super(false);
    }

    public static SourceQVoBuilder builder() {
        return new SourceQVoBuilder();
    }


    public static class SourceQVoBuilder extends Builder<SourceQVo> {
        protected SourceQVoBuilder() {
            super(SourceQVo.class);
        }

        public SourceQVoBuilder type(@Nullable Byte type) {
            if (!q.isConflicting) {
                q.sv.setType(type);
            }
            return this;
        }

        public SourceQVoBuilder source(@Nullable String source) {
            if (q.isConflicting) return this;
            Set<String> set = CommonUtil.str2StringSet(source);
            if (set != null && set.isEmpty()) {
                q.isConflicting = true;
            } else {
                q.sourceSet = set;
            }
            return this;
        }

        public SourceQVoBuilder ascFlag(Boolean ascFlag) {
            if (!q.isConflicting) {
                q.ascFlag = ascFlag;
            }
            return this;
        }
    }

}
