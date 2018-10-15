package cn.idea.modules.marketingManager.bean;

import cn.idea.modules.common.bean.BaseQVo;
import org.apache.ibatis.type.Alias;

import javax.annotation.Nullable;

@Alias("marketingMangerQVo")
public class MarketingManagerQVo extends BaseQVo {
    private Boolean ascFlag;

    private MarketingManagerVo mv = new MarketingManagerVo();

    public String getName() {
        return mv.getName();
    }

    public String getPhone() {
        return mv.getPhone();
    }

    public String getSchool() {
        return mv.getSchool();
    }

    public Boolean getAscFlag() {
        return ascFlag;
    }

    public MarketingManagerQVo(@Nullable String name, @Nullable String phone, @Nullable String school, @Nullable Boolean ascFlag) {
        mv.setName(name);
        mv.setPhone(phone);
        mv.setSchool(school);
        this.ascFlag =ascFlag;
    }
}
