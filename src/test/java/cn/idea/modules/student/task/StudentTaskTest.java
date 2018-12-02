package cn.idea.modules.student.task;

import cn.idea.utils.mock.test.MyTxTestNGSpringContextTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;


public class StudentTaskTest extends MyTxTestNGSpringContextTests {
    @Autowired
    StudentTask studentTask;

    @Test
    @Rollback(false)
    public void checkBirthdayTest() {
        studentTask._checkBirthday();
    }

}