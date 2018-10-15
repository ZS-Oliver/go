package cn.idea.modules.audiClasses.service;

import cn.idea.modules.audiClasses.bean.AudiClassesQVo;
import cn.idea.modules.audiClasses.bean.AudiClassesVo;
import cn.idea.modules.audiClasses.dao.AudiClassesMapper;
import cn.idea.modules.common.exception.JudgeException;
import cn.idea.modules.common.exception.ServiceException;
import cn.idea.modules.common.service.BaseService;
import cn.idea.modules.student.dao.StudentMapper;
import cn.idea.utils.assistant.CommonUtil;
import com.google.common.collect.Sets;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Log4j2
@Service
public class AudiClassesService extends BaseService<AudiClassesVo, AudiClassesQVo> {
    private static final Predicate<String> sidsChecker = Pattern.compile("([0-9]+:?)+").asPredicate();
    private static final String BEAN_NAME = "试听班级";

    @Autowired
    private AudiClassesMapper audiClassesMapper;
    @Autowired
    private StudentMapper studentMapper;

    protected AudiClassesService() {
        super(BEAN_NAME);
    }

    @Override
    protected void preSave(AudiClassesVo av) throws JudgeException, ServiceException {
        if (av.getSids() != null) {
            Set<Integer> sidSets = CommonUtil.str2IntSet(sidsChecker, av.getSids());
            ServiceException.when(sidSets.size() > av.getTotal(), "添加学员人数超过班级容量");
        }
    }

    @Override
    protected void preUpdate(AudiClassesVo newV, AudiClassesVo oldV) throws JudgeException {
        JudgeException.when(oldV.getSids() != null && newV.getAuditionTime() != null, "班级已有学员则不能更改试听时间");

        if (newV.getSids() != null) {
            // 判断传入学员是否是想要试听的学员
            Set<Integer> newSidSets = CommonUtil.str2IntSet(sidsChecker, newV.getSids());
            if (newSidSets == null) {
                newSidSets = new HashSet<>();
            }
            Set<Integer> oldSidSets = CommonUtil.str2IntSet(sidsChecker, oldV.getSids());
            if (oldSidSets == null) {
                oldSidSets = new HashSet<>();
            }

//            if (newV.getAuditionTime() != null) {
//                List<StudentVo> svList = studentMapper.viewStudentToAudition(newV.getAuditionTime());
//                Set<Integer> svSet = new HashSet<>(svList.stream().map(StudentVo::getId).collect(Collectors.toList()));
//                Sets.SetView<Integer> integsection1 = Sets.intersection(svSet, newSidSets);
//                JudgeException.when(integsection1.size() != newSidSets.size(), "存在不是想试听的学员或试听时间不在该时间段");
//            } else if (newV.getAuditionTime() == null) {
//                List<StudentVo> svList = studentMapper.viewStudentToAudition(oldV.getAuditionTime());
//                Set<Integer> svSet = new HashSet<>(svList.stream().map(StudentVo::getId).collect(Collectors.toList()));
//                Sets.SetView<Integer> integsection2 = Sets.intersection(svSet, newSidSets);
//                JudgeException.when(integsection2.size() != newSidSets.size(), "存在不是想试听的学员或试听时间不在该时间段");
//            }

            Sets.SetView<Integer> intersection3 = Sets.intersection(oldSidSets, newSidSets);
            JudgeException.when(!intersection3.isEmpty(), "存在更新的学员已经在班级里了");

            // 判断更新的班级人数是否超出total
            if (newV.getTotal() != null) {
                JudgeException.when(newSidSets.size() + oldSidSets.size() > newV.getTotal(), "增加的学员超出人数限制");
            } else {
                JudgeException.when(newSidSets.size() + oldSidSets.size() > oldV.getTotal(), "增加的学员超出人数限制");
            }
        }
    }

}
