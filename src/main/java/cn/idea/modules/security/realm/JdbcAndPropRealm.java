package cn.idea.modules.security.realm;

import cn.idea.modules.common.consts.Role;
import cn.idea.modules.security.dao.UserMapper;
import cn.idea.modules.security.util.UserUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.SimpleRole;
import org.apache.shiro.realm.text.PropertiesRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Optional;

/**
 * 带有jdbc的PropertiesRealm
 */
public class JdbcAndPropRealm extends PropertiesRealm {

    @Autowired
    private UserMapper userMapper;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String code = (String) token.getPrincipal();

        if (UserUtil.isAdminCode(code)) {
            // 执行IniRealm原有的方法
            return super.doGetAuthenticationInfo(token);
        }

        // 通过mapper执行
        String pwd;
        pwd = userMapper.getPwd4MarketingManager(code);
        if (pwd == null) {
            pwd = userMapper.getPwd4Employee(code);
        }

        if (pwd == null) {
            throw new UnknownAccountException();//没找到帐号
        }
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以在此判断或自定义实现
        return new SimpleAuthenticationInfo(code, pwd, getName());
    }

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 获得工号
        String code = (String) principals.getPrimaryPrincipal();
        if (UserUtil.isAdminCode(code)) {
            // 执行IniRealm原有的方法
            return super.doGetAuthorizationInfo(principals);
        }
        // 交给mapper执行
        byte auth;
        if (userMapper.getPwd4MarketingManager(code) != null) {
            auth = Role.MARKETING_MANAGER.getCode();
        } else {
            auth = Role.EMPLOYEE.getCode();
        }
        Optional<String> roleNameOpt = Role.acquireName(auth);
        if (!roleNameOpt.isPresent()) {
            return null;
        }
        String roleName = roleNameOpt.get();

        // 生成AuthorizationInfo对象
        SimpleAuthorizationInfo ai = new SimpleAuthorizationInfo(Collections.singleton(roleName));
        SimpleRole role = getRole(roleName);
        ai.setObjectPermissions(role.getPermissions());
        return ai;
    }
}
