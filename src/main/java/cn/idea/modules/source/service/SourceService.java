package cn.idea.modules.source.service;


import cn.idea.modules.common.exception.JudgeException;
import cn.idea.modules.common.service.BaseService;
import cn.idea.modules.source.bean.SourceEnum;
import cn.idea.modules.source.bean.SourceQVo;
import cn.idea.modules.source.bean.SourceVo;
import cn.idea.modules.source.dao.SourceMapper;
import cn.idea.modules.student.dao.StudentMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Log4j2
@Service
public class SourceService extends BaseService<SourceVo, SourceQVo> {
    private static final String BEAN_NAME = "来源";

    @Autowired
    private SourceMapper sourceMapper;
    @Autowired
    private StudentMapper studentMapper;

    protected SourceService() {
        super(BEAN_NAME);
    }

    private void judgeConflictWithFormer4Save(SourceVo sv) throws JudgeException {
        JudgeException.when(Arrays.stream(SourceEnum.values()).noneMatch(x -> x.getCode() == sv.getType()), "该来源类型不存在");
        List<String> svList = sourceMapper.getSourceListByType(sv.getType());
        JudgeException.when(svList.stream().anyMatch(x -> x.equals(sv.getSource())), "已存在该来源");
    }


    @Override
    protected void preSave(SourceVo sv) throws JudgeException {
        judgeConflictWithFormer4Save(sv);
    }

    @Override
    protected void preUpdate(SourceVo newV, SourceVo oldV) throws JudgeException {
        //JudgeException.when(newV.getType() != null, "不允许更改来源类型");
        JudgeException.when(sourceMapper.getSourceListByType(oldV.getType()).stream().anyMatch(x -> x.equals(newV.getSource())), "已存在该来源");
    }

    /**
     * @param type
     * @return 该来源种类所对应的所有来源
     * @throws JudgeException
     */
    public List<String> getSourceListByType(Byte type) throws JudgeException {
        JudgeException.when(Arrays.stream(SourceEnum.values()).noneMatch(x -> x.getCode() == type), "不存在该类型的来源");
        return sourceMapper.getSourceListByType(type);
    }

    /**
     * @param type
     * @param source
     * @return 根据类型和具体来源获得id
     * @throws JudgeException
     */
    public Integer getIdByTypeAndSource(Byte type, String source) throws JudgeException {
        JudgeException.when(Arrays.stream(SourceEnum.values()).noneMatch(x -> x.getCode() == type), "不存在该类型的来源");
        JudgeException.when(sourceMapper.getSourceListByType(type).stream().noneMatch(x -> x.equals(source)), "该类型下不存在该种来源");
        return sourceMapper.getIdByTypeAndSource(type, source);
    }
}
