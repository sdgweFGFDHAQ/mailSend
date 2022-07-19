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
import java.util.Objects;

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
        MimeBodyPart text = new MimeBodyPart();
        MimeMultipart multipart = new MimeMultipart();
        if (mail.getImg() == null || Objects.equals(mail.getImg(), "")) {
            image = null;
            text.setContent(mail.getContent(), "text/html;charset=UTF-8");
        } else{
            String img = mail.getImg();
            int imgIndex = img.lastIndexOf(".");
            image.setDataHandler(new DataHandler(new FileDataSource(img)));
            String imgSuffix = img.substring(imgIndex + 1);
            image.setContentID("img." + imgSuffix);//给图片设置一个ID，便于调用

            //邮件内容:图片及文本
            //cid就是ContentID的缩写，即这里调用的就是之前给图片设置的ID
            text.setContent("<h1 style='color: red'>" + mail.getContent() + "</h1><img src='cid:img." + imgSuffix + " '>", "text/html;charset=UTF-8");
            multipart.addBodyPart(image);
        }



        //附件
        MimeBodyPart prop = new MimeBodyPart();
        if (mail.getProp() == null || Objects.equals(mail.getProp(), "")) {
            prop = null;
        } else {
            String file = mail.getProp();
            int fileIndex = file.lastIndexOf(".");
            prop.setDataHandler(new DataHandler(new FileDataSource(file)));
            String propSuffix = file.substring(fileIndex + 1);
            prop.setFileName(file.substring(file.lastIndexOf("\\") + 1, fileIndex) + "." + propSuffix);    //附件设置名字

        }

        //拼装文件正文内容
        multipart.addBodyPart(text);
        multipart.setSubType("related");
        MimeBodyPart contentText = new MimeBodyPart();
        contentText.setContent(multipart);

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
