package com.xuanwu.ipaas.plugin.mail.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

public class SessionCreate {
    public Session createSession(Properties prop, String fromAdd,String password){
        //QQ独有的设置session
        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromAdd, password);
            }
        });
        return session;
    }
}
