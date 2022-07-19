package com.xuanwu.ipaas.plugin.mail.util;

import com.xuanwu.ipaas.plugin.mail.dao.Mail;
import com.xuanwu.ipaas.plugin.sdk._enum.PluginErrorCode;
import com.xuanwu.ipaas.plugin.sdk.exception.PluginException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailCreate {

    public MimeMessage createMail(Session session, Mail mail) throws Exception {
        MimeMessage mimeMessage = new MimeMessage(session);

        //邮件发件人
        mimeMessage.setFrom(new InternetAddress(mail.getFromAdd()));
        //邮件接收人
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(mail.getToAdd()));
        //邮件主题
        mimeMessage.setSubject(mail.getTitle());

        //设置图片资源
        MimeBodyPart image = new MimeBodyPart();
        String img = mail.getProp();
        image.setDataHandler(new DataHandler(new FileDataSource(img)));
        image.setContentID("img.png");//给图片设置一个ID，便于调用

        //邮件内容:图片及文本
        MimeBodyPart text = new MimeBodyPart();
        //cid就是ContentID的缩写，即这里调用的就是之前给图片设置的ID
        text.setContent("<h1 style='color: red'>" + mail.getContent() + "</h1><img src='cid:img.png'>", "text/html;charset=UTF-8");

        //附件
        MimeBodyPart prop = new MimeBodyPart();
        if (mail.getProp() != null) {
            String file = mail.getProp();
            int fileIndex = file.lastIndexOf(".");
            prop.setDataHandler(new DataHandler(new FileDataSource(file)));
            String propSuffix = file.substring(fileIndex + 1);
            prop.setFileName(file.substring(file.lastIndexOf("\\") + 1, fileIndex) + "." + propSuffix);    //附件设置名字
        } else {
            prop = null;
        }


        //拼装文件正文内容
        MimeMultipart multipart1 = new MimeMultipart();
        multipart1.addBodyPart(image);
        multipart1.addBodyPart(text);
        multipart1.setSubType("related");
        MimeBodyPart contentText = new MimeBodyPart();
        contentText.setContent(multipart1);

        //拼接附件
        MimeMultipart allFile = new MimeMultipart();
        allFile.addBodyPart(contentText);
        if (prop != null) {
            allFile.addBodyPart(prop);
        }
        allFile.setSubType("mixed");//正文和附件拼接，用mixed

        //放到Message消息中，保存修改
        mimeMessage.setContent(allFile);//将编辑好的数据关系放入message中
        mimeMessage.saveChanges();

        return mimeMessage;
    }
}
