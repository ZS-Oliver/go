package cn.idea.modules.student.task;

import cn.idea.modules.student.bean.StudentVo;
import cn.idea.modules.student.dao.StudentMapper;
import cn.idea.utils.assistant.CommonUtil;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
public class StudentTask {
    @Autowired
    StudentMapper studentMapper;

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkBirthday() {
        _checkBirthday();
    }

    @VisibleForTesting
    protected void _checkBirthday() {
//        String birthday = CommonUtil.curTimeStr();
//        List<Integer> svList = studentMapper.findIdByBirthday(birthday);
        List<StudentVo> svList = studentMapper.findBirthdayNotNull();
        int n = 0;
        for (StudentVo sv : svList) {
            int res = studentMapper.updateAge(sv.getId(), CommonUtil.getAgeByBirth(sv.getBirthday()));
            if (res == 0) {
                log.info(sv.getId() + "更新失败");
            } else {
                n++;
            }
        }
        log.info("总条数为:" + svList.size() + "成功修改条数为:" + n);
    }
}
