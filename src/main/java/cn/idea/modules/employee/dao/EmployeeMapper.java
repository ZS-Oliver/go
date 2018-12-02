package cn.idea.modules.employee.dao;

import cn.idea.modules.common.dao.BaseMapper;
import cn.idea.modules.employee.bean.EmployeeVo;
import cn.idea.modules.employee.bean.EmployeeQVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EmployeeMapper extends BaseMapper<EmployeeVo, EmployeeQVo> {

    /**
     * 通过手机号查询
     */
    EmployeeVo findByPhone(@Param("phone") String phone);

    /**
     * 通过用户名查询
     */
    EmployeeVo findByName(@Param("name") String name);

    /**
     * 通过市场经理id查询
     */
    List<EmployeeVo> findByMid(@Param("mid") Integer mid);

    /**
     * 批量查询指定eid的员工
     */
    List<EmployeeVo> findByIds(@Param("eIds") Set<Integer> eIds);

    /**
     * 无视valid查找
     */
    EmployeeVo findAll(Integer id);

    /**
     * 通过市场经理id和学校名字更新员工学校名
     */
    void updateSchoolByMid(@Param("mid") Integer mid, @Param("school") String school);
}
