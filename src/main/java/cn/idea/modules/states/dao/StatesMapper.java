package cn.idea.modules.states.dao;

import cn.idea.modules.common.dao.BaseMapper;
import cn.idea.modules.states.bean.StatesQVo;
import cn.idea.modules.states.bean.StatesVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatesMapper extends BaseMapper<StatesVo, StatesQVo> {

    /**
     * 获得某个学生的所有状态
     */
    List<StatesVo> findBySid(@Param("sid") Integer sid);
}
