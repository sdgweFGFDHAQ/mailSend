import com.sun.mail.util.MailSSLSocketFactory;
import com.xuanwu.ipaas.plugin.mail.dao.Mail;
import com.xuanwu.ipaas.plugin.mail.util.MailCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;

@Slf4j
public class SendMailTest {
    public static final String mailHost = "smtp.qq.com";
    public static final String protocol = "smtp";
    public static final String auth = "true";

    public static final String sendName = "1796822664@qq.com";
    public static final String password = "ehhmnyahepgvebae";

    public static void send1() throws GeneralSecurityException, MessagingException {
        Properties prop = new Properties();
        prop.setProperty("mail.host", mailHost);
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.smtp.auth", "true");

        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.stmp.ssl.socketFactory", sf);

        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sendName, password);
            }
        });

        //..
        session.setDebug(true);

        Transport transport = session.getTransport();
        transport.connect(mailHost, sendName, password);

        MimeMessage message = new MimeMessage(session);

        //指明邮件的发件人
        message.setFrom(new InternetAddress("1796822664@qq.com"));

        //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("1796822664@qq.com"));

        //邮件标题
        message.setSubject("测试邮件标题");

        //邮件内容
        message.setContent("测试邮件内容！", "text/html;charset=UTF-8");

        //5、发送邮件
        transport.sendMessage(message, message.getAllRecipients());

        transport.close();


    }

    public static void send2() throws Exception {
        Properties prop = new Properties();
        prop.setProperty("mail.host", mailHost);
        prop.setProperty("mail.transport.protocol", protocol);
        prop.setProperty("mail.smtp.auth", auth);

        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.stmp.ssl.socketFactory", sf);

        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sendName, password);
            }
        });

        //..
        session.setDebug(true);

        //Transport transport = session.getTransport();
        //transport.connect(mailHost, sendName, password);

        MimeMessage message = new MimeMessage(session);

        //指明邮件的发件人
        message.setFrom(new InternetAddress("1796822664@qq.com"));

        //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("1796822664@qq.com"));

        //邮件标题
        message.setSubject("测试邮件标题");

        //邮件内容
        message.setContent("测试邮件内容！", "text/html;charset=UTF-8");

        //5、发送邮件
        //transport.sendMessage(message, message.getAllRecipients());

        //transport.close();
        Transport.send(message, message.getAllRecipients());
    }

    public static void send3() throws Exception {
        MailCreate mailCreate = new MailCreate();

        Properties prop = new Properties();
        prop.setProperty("mail.host", mailHost);
        prop.setProperty("mail.transport.protocol", protocol);
        prop.setProperty("mail.smtp.auth", auth);

        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.stmp.ssl.socketFactory", sf);

        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sendName, password);
            }
        });

        //..
        session.setDebug(true);

        //Transport transport = session.getTransport();
        //transport.connect(mailHost, sendName, password);
        Mail mail = new Mail();
        mail.setFromAdd("1796822664@qq.com");
        mail.setToAdd("1796822664@qq.com");
        mail.setTitle("邮件主题");
        mail.setContent("尊敬的用户，您好：");
        mail.setImg("C:\\Users\\86158\\Pictures\\Saved Pictures\\2022-07-19 100708.png");
        mail.setProp("C:\\Users\\86158\\Desktop\\授权码.txt");
        MimeMessage message = mailCreate.createMail(session, mail);

        //5、发送邮件
        //transport.sendMessage(message, message.getAllRecipients());

        //transport.close();
        Transport.send(message, message.getAllRecipients());
    }

    public static void main(String[] args) throws Exception {
        //send1();

        //send2();

        send3();
    }
}
