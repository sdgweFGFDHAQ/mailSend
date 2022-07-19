package com.xuanwu.ipaas.plugin.mail.spi;

import com.sun.mail.util.MailSSLSocketFactory;
import com.xuanwu.ipaas.plugin.sdk._enum.ConnectType;
import com.xuanwu.ipaas.plugin.sdk.domain.Connection;
import com.xuanwu.ipaas.plugin.sdk.spi.ConnectSPI;

import javax.mail.*;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Properties;

public class ConnectionFactory implements ConnectSPI {
    @Override
    public Connection getConnection(Map<String, Object> map) {
        String mailHost = (String) map.get("mailHost");
        String protocol = (String) map.get("protocol");
        String fromAdd = (String) map.get("fromAdd");
        String password = (String) map.get("password");

        Properties prop = new Properties();
        prop.setProperty("mail.host", mailHost);
        prop.setProperty("mail.transport.protocol", protocol);
        prop.setProperty("mail.smtp.auth", "auth");

        //QQ邮箱需要设置SSL加密
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        sf.setTrustAllHosts(true);
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.socketFactory", sf);
        //QQ独有的设置session?
        //session是获取连接
        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromAdd, password);
            }
        });

        //开启Session的Debug模式，可以查看程序发送Email的运行状态
        session.setDebug(true);

        return new Connection(session, null, ConnectType.SESSION);
    }

}
