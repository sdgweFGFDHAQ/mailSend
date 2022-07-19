package com.xuanwu.ipaas.plugin.mail.spi;

import com.xuanwu.ipaas.plugin.mail.dao.Mail;
import com.xuanwu.ipaas.plugin.mail.util.MailCreate;
import com.xuanwu.ipaas.plugin.sdk._enum.PluginErrorCode;
import com.xuanwu.ipaas.plugin.sdk.domain.Connection;
import com.xuanwu.ipaas.plugin.sdk.domain.ResultMap;
import com.xuanwu.ipaas.plugin.sdk.exception.PluginException;
import com.xuanwu.ipaas.plugin.sdk.spi.ActionSPI;
import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MailSendAction implements ActionSPI {

    @Override
    public Map<String, Object> action(Connection connection, Map<String, Object> map) {
        if (connection == null) {
            throw PluginException.asPluginException(PluginErrorCode.CONNECT_NOT_FOUND_ERROR, "连接邮件服务器失败");
        }
        String fromAdd = (String) map.get("fromAdd");
        String toAdd = (String) map.get("toAdd");
//        String password = (String) map.get("password");
        Mail mail = new Mail(fromAdd, toAdd, (String) map.get("title"), (String) map.get("content"), (String) map.get("img"), (String) map.get("prop"));
        //获取session对象
        Session session = (Session) connection.getConnection();

        MailCreate mailCreate = new MailCreate();
        MimeMessage message = null;
        try {
            message = mailCreate.createMail(session, mail);
            Transport transport = session.getTransport();
            transport.connect((String) map.get("mailHost"), (String) map.get("fromAdd"), (String) map.get("password"));
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        //通过session得到transport对象
//        Transport transport = null;
//        try {
//            transport = session.getTransport();
//        } catch (NoSuchProviderException e) {
//            e.printStackTrace();
//        }
//        //发送邮件
//        try {
//            transport.connect(fromAdd, password);
//            transport.sendMessage(message, message.getAllRecipients());
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//        try {
//            transport.close();
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }

        return ResultMap.success("发送成功");
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("mailHost", "smtp.qq.com");
        map.put("protocol", "smtp");
        map.put("fromAdd", "1796822664@qq.com");
        map.put("toAdd", "1796822664@qq.com");
        map.put("password", "ehhmnyahepgvebae");
        map.put("title", "这是一个比较好的标题");
        map.put("content", "你好啊，小精灵");
        map.put("img", "C:\\Users\\86158\\Pictures\\Saved Pictures\\161650zc9n9zbcb2mxbhhq.gif");
        map.put("prop", "C:\\Users\\86158\\Desktop\\授权码.txt");

        MailSendAction ms = new MailSendAction();
        ConnectionFactory cf = new ConnectionFactory();
        Connection connection = cf.getConnection(map);
        ms.action(connection, map);
    }
}
