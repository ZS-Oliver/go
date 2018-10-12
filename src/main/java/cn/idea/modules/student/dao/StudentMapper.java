package cn.idea.modules.student.dao;

import cn.idea.modules.common.dao.BaseMapper;
import cn.idea.modules.student.bean.StudentQVo;
import cn.idea.modules.student.bean.StudentVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

}
