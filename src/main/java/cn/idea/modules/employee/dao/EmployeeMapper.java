package cn.idea.modules.employee.dao;

import cn.idea.modules.common.dao.BaseMapper;
import cn.idea.modules.employee.bean.EmployeeVo;
import cn.idea.modules.employee.bean.EmployeeQVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
