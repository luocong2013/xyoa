package org.jeecgframework.web.system.sms.util.task;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
@Service("Tasktest")
public class Tasktest {
	
	public void run(){
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();  
        ServletContext servletContext = webApplicationContext.getServletContext(); 
        
		System.out.println("这是定时任务测试！");
		
		//${pageContext.request.contextPath}
		Properties props = new Properties();//不设置任何配置，发送时需要
		props.setProperty("mail.host", "smtp.sina.cn");
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.auth", "true");//请求认证，与JavaMail的实现有关
		Session session = Session.getInstance(props);
		session.setDebug(true);
		MimeMessage msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress("17761311510@sina.cn"));
		
		msg.setRecipients(Message.RecipientType.TO,"455697968@qq.com");
		msg.setSubject("JavaMail发送的邮件测试");
		
		//邮件主体内容:组装过程
			//文本部分
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setContent("aaa<img src='cid:mm'/>aaa", "text/html");
			
			//图片部分
			MimeBodyPart imagePart = new MimeBodyPart();
				//搞数据进来需要用到JAF的API ServletActionContext.getServletContext().getRealPath("/user/photo/" + username)/xyoa/src/main/webapp/imge/1.jpg
			DataHandler dh = new DataHandler(new FileDataSource(servletContext.getRealPath("/imge/1.jpg")));//自动探测文件的MIME类型
			//request.getRealPath("/main/webapp/imge/1.jpg")
			imagePart.setDataHandler(dh);
			imagePart.setContentID("mm");
			
			//描述关系：
			MimeMultipart mmpart = new MimeMultipart();
			mmpart.addBodyPart(textPart);
			mmpart.addBodyPart(imagePart);
			mmpart.setSubType("related");//有关系的
		
		//msg.setContent("这是测试邮件！！sdsdf", "text/html;charset=UTF-8");
		msg.setContent(mmpart);
		msg.saveChanges();
//		msg.writeTo(new FileOutputStream("d:/2.eml"));
		//发送邮件
		Transport ts = session.getTransport();
		ts.connect("17761311510@sina.cn", "zzz111");
		ts.sendMessage(msg, msg.getAllRecipients());
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
