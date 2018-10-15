package cn.idea.modules.security.dao;

import cn.idea.modules.security.vo.UserVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    /**
     * 通过手机号查询市场经理密码
     *
     * @param phone 手机号
     * @return pwd 密码；null 未查到
     */
    String getPwd4MarketingManager(@Param("phone") String phone);

    /**
     * 承运商更新密码
     *
     * @param uv 用户实体
     */
    void updatePwd4MarketingManager(UserVo uv);

    /**
     * 通过手机号查询员工密码
     *
     * @param phone 手机号
     * @return pwd 密码；null 未查到
     */
    String getPwd4Employee(@Param("phone") String phone);

    /**
     * 内部员工更新密码
     *
     * @param uv 用户实体
     */
    void updatePwd4Employee(UserVo uv);

    /**
     * 重置承运商密码
     */
    void resetMarketingManagerPwd(@Param("id") Integer id, @Param("pwd") String pwd);

    /**
     * 重置场内员工密码
     */
    void resetEmployeePwd(@Param("id") Integer id, @Param("pwd") String defaultPwd);
}