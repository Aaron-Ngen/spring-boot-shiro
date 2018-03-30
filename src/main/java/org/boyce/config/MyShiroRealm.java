package org.boyce.config;

import org.apache.shiro.authc.*;
import org.boyce.entity.SysPermission;
import org.boyce.entity.SysRole;
import org.boyce.entity.UserInfo;
import org.boyce.sevice.UserInfoService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

public class MyShiroRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(MyShiroRealm.class);

    @Resource
    private UserInfoService userInfoService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        //System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        logger.info("inform:{}", "权限配置 --> MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        UserInfo userInfo = (UserInfo) principals.getPrimaryPrincipal();
        for (SysRole role : userInfo.getRoleList()) {
            authorizationInfo.addRole(role.getRole());
            for (SysPermission p : role.getPermissions()) {
                authorizationInfo.addStringPermission(p.getPermission());
            }
        }
        return authorizationInfo;
    }

    /*身份认证，验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        //System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
        logger.info("inform: {}", "认证需求 --> MyShiroRealm.doGetAuthenticationInfo()");
        UsernamePasswordToken upToken = (UsernamePasswordToken)token;
        System.out.println("======================="+upToken.getPassword()+","+upToken.getCredentials()+"=======================");
        //获取用户的输入的账号
        String username = (String) token.getPrincipal();
        System.out.println("==========" + token.getCredentials() + "==========");
        //通过username从数据库中查找 User对象
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        UserInfo userInfo = userInfoService.findByUsername(username);
        //System.out.println("----->>userInfo=" + userInfo);
        logger.info("userInfo: {}", userInfo);
        if (userInfo == null) {
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userInfo, //用户名
                userInfo.getPassword(), //密码
                ByteSource.Util.bytes(userInfo.getCredentialsSalt()),//salt=username+salt
                this.getClass().getSimpleName()  //realm name
        );
        return authenticationInfo;
    }

}