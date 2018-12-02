package cn.idea.modules.audiClasses.service;

import cn.idea.modules.audiClasses.bean.AudiClassesQVo;
import cn.idea.modules.audiClasses.bean.AudiClassesVo;
import cn.idea.modules.common.consts.Role;
import cn.idea.modules.common.exception.JudgeException;
import cn.idea.modules.common.exception.ServiceException;
import cn.idea.modules.common.service.BaseService;
import cn.idea.modules.employee.dao.EmployeeMapper;
import cn.idea.modules.marketingManager.dao.MarketingManagerMapper;
import cn.idea.modules.student.bean.StudentVo;
import cn.idea.modules.student.dao.StudentMapper;
import cn.idea.utils.assistant.CommonUtil;
import com.google.common.base.Strings;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Log4j2
@Service
public class AudiClassesService extends BaseService<AudiClassesVo, AudiClassesQVo> {
    private static final Predicate<String> sidsChecker = Pattern.compile("([0-9]+:?)+").asPredicate();
    private static final String BEAN_NAME = "试听班级";

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private MarketingManagerMapper marketingManagerMapper;
    @Autowired
    private EmployeeMapper employeeMapper;

    protected AudiClassesService() {
        super(BEAN_NAME);
    }

    @Override
    protected void preSave(AudiClassesVo av) throws JudgeException, ServiceException {
        if (!Strings.isNullOrEmpty(av.getSids())) {
            Set<Integer> sidSets = CommonUtil.str2IntSet(sidsChecker, av.getSids());
            ServiceException.when(sidSets.size() > av.getTotal(), "添加学员人数超过班级容量");
        }
    }

    /**
     * 更新班级，新班级的学员列表直接替换旧班级的
     *
     * @param newV 变更的实体，一定拥有id属性
     * @param oldV 原有的实体
     * @throws JudgeException
     */
    @Override
    protected void preUpdate(AudiClassesVo newV, AudiClassesVo oldV) throws JudgeException {
        if (!Strings.isNullOrEmpty(newV.getSids())) {
            Set<Integer> newSids = CommonUtil.str2IntSet(sidsChecker, newV.getSids());
            // 判断更新的班级人数是否超出total
            if (newV.getTotal() != null && !CollectionUtils.isEmpty(newSids)) {
                JudgeException.when(newSids.size() > newV.getTotal(), "增加的学员超出人数限制");
            } else {
                JudgeException.when(newSids.size() > oldV.getTotal(), "增加的学员超出人数限制");
            }
        }
    }

    @Override
    protected void postView(AudiClassesVo av) throws ServiceException {
        av.setOpName(acquireNameByAuthAndId(av.getOpAuth(), av.getOpId()));
    }

    private String acquireNameByAuthAndId(Byte auth, Integer id) {
        if (auth.equals(Role.ADMIN.getCode())) {
            return "管理员";
        } else if (auth.equals(Role.MARKETING_MANAGER.getCode())) {
            return marketingManagerMapper.findAll(id).getName();
        } else {
            return employeeMapper.findAll(id).getName();
        }
    }

    /**
     * 将学生列表填充到班级list中
     *
     * @param vl 实体列表
     * @throws ServiceException
     */
    @Override
    protected void postList(List<AudiClassesVo> vl) throws ServiceException {
        Map<Integer, List<StudentVo>> tempMap = new HashMap<>();
        int stuTemp = 0, vlTemp = 0;
        List<String> sids = vl.stream().map(AudiClassesVo::getSids).collect(Collectors.toList());
        List<Set<Integer>> sidSetList = sids.stream().map(x -> CommonUtil.str2IntSet(sidsChecker, x)).collect(Collectors.toList());
        // List<List<StudentVo>> stuList = sidSetList.stream().map(x-> studentMapper.findBySetId(x)).collect(Collectors.toList());
        // Map<Integer,List<StudentVo>> stuList2 = stuList.stream().collect(Collectors.groupingBy(AudiClassesVo::getId));
        for (Set<Integer> set : sidSetList) {
            if (!CollectionUtils.isEmpty(set)) {
                tempMap.put(stuTemp, studentMapper.findBySetId(set));
            }
            stuTemp++;
        }
        for (AudiClassesVo vo : vl) {
            vo.setOpName(acquireNameByAuthAndId(vo.getOpAuth(), vo.getOpId()));
            vo.setStuList(tempMap.get(vlTemp));
            vlTemp++;
        }
    }
}
