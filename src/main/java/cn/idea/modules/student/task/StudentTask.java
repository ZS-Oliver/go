package cn.idea.modules.student.task;

import cn.idea.modules.student.dao.StudentMapper;
import cn.idea.utils.assistant.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentTask {
    @Autowired
    StudentMapper studentMapper;

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkBirthday() {
        String birthday = CommonUtil.curTimeStr();
        List<Integer> svList = studentMapper.findIdByBirthday(birthday);
        studentMapper.batchUpdateAge(svList);
    }
}
