package cn.idea.modules.student.dao;

import cn.idea.modules.common.dao.BaseMapper;
import cn.idea.modules.student.bean.StudentQVo;
import cn.idea.modules.student.bean.StudentVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface StudentMapper extends BaseMapper<StudentVo, StudentQVo> {
    /**
     * 通过手机号查询
     */
    StudentVo findByPhone(@Param("phone") String phone);

    /**
     * 通过意向时间查询要试听的学员
     */
    List<StudentVo> viewStudentToAudition(@Param("expectedDate") String expectedDate);

    /**
     * 通过员工id找到学员
     */
    List<StudentVo> findByEid(@Param("eid") Integer eid);

    /**
     * 修改多名学员的员工id
     */
    int batchUpdateEid(@Param("ids") Set<Integer> idSet, @Param("eid") Integer eid);

    /**
     * 通过生日查找学员id
     */
    List<Integer> findIdByBirthday(@Param("birthday") String birthday);

    /**
     * 批量修改学员年龄
     */
    int batchUpdateAge(@Param("ids") List<Integer> id);
}
