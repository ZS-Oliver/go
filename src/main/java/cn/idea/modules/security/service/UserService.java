package cn.idea.modules.security.service;

import cn.idea.modules.common.bean.BaseVo;
import cn.idea.modules.common.consts.Role;
import cn.idea.modules.common.consts.SessionConst;
import cn.idea.modules.common.consts.UserConst;
import cn.idea.modules.common.exception.ServiceException;
import cn.idea.modules.employee.bean.EmployeeVo;
import cn.idea.modules.employee.dao.EmployeeMapper;
import cn.idea.modules.marketingManager.bean.MarketingManagerVo;
import cn.idea.modules.marketingManager.dao.MarketingManagerMapper;
import cn.idea.modules.security.dao.UserMapper;
import cn.idea.modules.security.util.UserUtil;
import cn.idea.modules.security.vo.PwdPair;
import cn.idea.modules.security.vo.UserVo;
import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Collection;

@Log4j2
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private MarketingManagerMapper marketingManagerMapper;

    @Autowired
    private SessionDAO sessionDAO;

    public void login(UserVo uv, HttpSession session) throws ServiceException {
        try {
            log.info("登录信息：uv = {}", JSON.toJSONString(uv));
            String code = uv.getCode();
            SecurityUtils.getSubject().login(new UsernamePasswordToken(uv.getCode(), uv.getPwd()));

            // 登录成功，设置session信息
            if (UserUtil.isAdminCode(uv.getCode())) {
                session.setAttribute(SessionConst.UID, SessionConst.ADMIN_UID);
                session.setAttribute(SessionConst.AUTH, Role.ADMIN.getCode());
            } else {
                MarketingManagerVo mv = marketingManagerMapper.findByPhone(code);
                if (mv != null) {

                    session.setAttribute(SessionConst.UID, mv.getId());
                    session.setAttribute(SessionConst.AUTH, Role.MARKETING_MANAGER.getCode());
                } else {
                    EmployeeVo ev = employeeMapper.findByPhone(code);

                    session.setAttribute(SessionConst.UID, ev.getId());
                    session.setAttribute(SessionConst.AUTH, Role.EMPLOYEE.getCode());
                }
            }

        } catch (AuthenticationException e) {
            throw new ServiceException("用户名或密码错误");
        }
    }

    public BaseVo info(HttpSession session) throws ServiceException {
        Integer uid = ((Integer) session.getAttribute(SessionConst.UID));
        Byte auth = ((Byte) session.getAttribute(SessionConst.AUTH));

        if (uid != null && auth != null) {
            BaseVo vo;
            if (uid.equals(SessionConst.ADMIN_UID)) {
                vo = UserUtil.ADMIN;
            } else {
                if (auth == Role.MARKETING_MANAGER.getCode()) {
                    vo = marketingManagerMapper.find(uid);
                } else {
                    vo = employeeMapper.find(uid);
                }
            }
            log.info("获得当前用户的信息:info = {}", JSON.toJSONString(vo));
            return vo;
        }

        throw new ServiceException("会话超时，请重新登录");

    }

    public void logout(HttpSession session) {
        log.info("用户退出登录：code = {}", SecurityUtils.getSubject().getPrincipal());
        session.removeAttribute(SessionConst.AUTH);
        session.removeAttribute(SessionConst.UID);

        SecurityUtils.getSubject().logout();
    }

    @Transactional
    public void updatePwd(PwdPair pair, HttpSession session) throws ServiceException {
        Integer uid = ((Integer) session.getAttribute(SessionConst.UID));
        Byte auth = (Byte) session.getAttribute(SessionConst.AUTH);
        if (uid.equals(SessionConst.ADMIN_UID)) {
            throw new ServiceException("抱歉，管理员账号无法修改密码");
        }

        String code = (String) SecurityUtils.getSubject().getPrincipal();
        log.info("更新密码：code/phone = {}", code);

        boolean isMarketingManager = auth == Role.MARKETING_MANAGER.getCode();
        String oldPwd;

        if (isMarketingManager) {
            oldPwd = userMapper.getPwd4MarketingManager(code);
        } else {
            oldPwd = userMapper.getPwd4Employee(code);
        }

        if (pair.getOldPwd().equals(oldPwd)) {
            UserVo uv = new UserVo();
            uv.setCode(code);
            uv.setPwd(pair.getNewPwd());

            if (isMarketingManager) userMapper.updatePwd4MarketingManager(uv);
            else userMapper.updatePwd4Employee(uv);

            session.removeAttribute(SessionConst.AUTH);
            session.removeAttribute(SessionConst.UID);
            SecurityUtils.getSubject().logout(); // 缓存中的密码尚未更新，需要重新登录
        } else {
            throw new ServiceException("原密码错误");
        }
    }

    @Transactional
    public void resetCarrierPwd(Integer id) throws ServiceException {
        MarketingManagerVo mv = marketingManagerMapper.find(id);
        if (mv == null) {
            throw new ServiceException("当前承运商不存在");
        }
        log.info("重置承运商(id = {})密码", id);
        userMapper.resetMarketingManagerPwd(id, UserConst.DEFAULT_PWD);
        logOutAfterResetPw(mv.getPhone());
    }

    @Transactional
    public void resetEmployeePwd(Integer id) throws ServiceException {
        EmployeeVo ev = employeeMapper.find(id);
        if (ev == null) {
            throw new ServiceException("当前员工不存在");
        }
        log.info("重置场内员工(id = {})密码", id);
        userMapper.resetEmployeePwd(id, UserConst.DEFAULT_PWD);
        logOutAfterResetPw(ev.getPhone());
    }

    private void logOutAfterResetPw(String phone) {
        Collection<Session> activeSessions = sessionDAO.getActiveSessions();
        log.info("当前登陆的用户有{}个", activeSessions.size());
        for (Session session : activeSessions) {
            if (phone.equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
                log.info("找到该账号{}", phone);
                session.removeAttribute(SessionConst.AUTH);
                session.removeAttribute(SessionConst.UID);
                session.setTimeout(0);
            }
        }
    }

    public void updateInfo(EmployeeVo employeeVo, Integer uid) throws ServiceException {
        if (uid.equals(SessionConst.ADMIN_UID)) {
            throw new ServiceException("抱歉，管理员账号无法修改信息");
        }

        EmployeeVo evo = employeeMapper.findByPhone(employeeVo.getPhone());
        ServiceException.when(evo != null && !evo.getId().equals(uid), "手机号同已有账号重复");

        employeeVo.setId(uid);
        int res = employeeMapper.update(employeeVo);
        ServiceException.when(res == 0, "修改错误，请重试");
    }

    @Transactional
    public void updateInfo(MarketingManagerVo marketingManagerVo, Integer uid) throws ServiceException {
        if (uid.equals(SessionConst.ADMIN_UID)) {
            throw new ServiceException("抱歉，管理员账号无法修改信息");
        }

        MarketingManagerVo mv = marketingManagerMapper.findByPhone(marketingManagerVo.getPhone());
        ServiceException.when(mv != null && !mv.getId().equals(uid), "手机号同已有账号重复");

        marketingManagerVo.setId(uid);
        int res = marketingManagerMapper.update(marketingManagerVo);
        ServiceException.when(res == 0, "修改错误，请重试");
    }
}
