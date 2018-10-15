package cn.idea.modules.source.dao;

import cn.idea.modules.common.dao.BaseMapper;
import cn.idea.modules.source.bean.SourceQVo;
import cn.idea.modules.source.bean.SourceVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceMapper extends BaseMapper<SourceVo, SourceQVo> {


    /**
     * 通过type获得其下的所有source
     *
     * @param type
     * @return
     */
    List<String> getSourceListByType(@Param("type") Byte type);

    /**
     * 通过类型和具体来源获得id
     *
     * @param type
     * @param source
     * @return
     */
    Integer getIdByTypeAndSource(@Param("type") Byte type, @Param("source") String source);
}
