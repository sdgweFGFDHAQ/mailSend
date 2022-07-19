package com.xuanwu.ipaas.plugin.mail.spi;

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
        String password = (String) map.get("password");

        //获取session对象
        Session session = (Session) connection.getConnection();


        MimeMessage message = new MimeMessage(session);

        //指明邮件发件人
        try {
            message.setFrom(new InternetAddress(fromAdd));
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        //指明邮件收件人
        try {
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAdd));
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        try {
            //邮件的标题
            message.setSubject("这是个简单的标题");
            //邮件的文本内容
            message.setContent("尊敬的用户，您好", "text/html;charset=UTF-8");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        //通过session得到transport对象
        Transport transport = null;
        try {
            transport = session.getTransport();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        //发送邮件
        try {
            transport.connect(fromAdd, password);
            transport.sendMessage(message, message.getAllRecipients());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        try {
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return ResultMap.success("发送成功");
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("mailHost","smtp.qq.com");
        map.put("protocol","smtp");
        map.put("fromAdd","1796822664@qq.com");
        map.put("toAdd","1796822664@qq.com");
        map.put("password","ehhmnyahepgvebae");

        MailSendAction ms = new MailSendAction();
        ConnectionFactory cf = new ConnectionFactory();
        Connection connection = cf.getConnection(map);
        ms.action(connection, map);
    }
}
