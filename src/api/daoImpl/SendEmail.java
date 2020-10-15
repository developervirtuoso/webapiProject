package api.daoImpl;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class SendEmail {

	public static void main(String[] args) {
		String txt_msg="Neeraj";
		
		SendEmail sendEmail=new SendEmail("neerajbhagat9872@gmail.com,neerajbhagat7355@gmail.com,neeraj@virtuosonetsoft.in", "Testing", "kjhjkhkjhk");
	}
	public  SendEmail(String email,String subject,String txt_msg)
	{

	    
		
		String host ="smtp.gmail.com" ; 
		String user = "info@parrotinfosoft.com";
		String pass = "info@123"; 
		String to = email; 
		String from = "info@parrotinfosoft.com";
		
		Properties props = new Properties();
        //props.put("mail.smtp.host", "smtp.mail.yahoo.com");
        props.put("mail.smtp.host", "smtp.gmail.com");  
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(user, pass);//change accordingly  
            }
        });

        //compose message  
        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));//change accordingly  
            InternetAddress[] addresses = InternetAddress.parse(email);
            message.addRecipients(Message.RecipientType.TO, addresses);
            message.setSubject(subject);
            message.setContent(txt_msg,"text/html" );  
            //send message  
            Transport.send(message);
            System.out.println("message sent to ----" + to);

        } catch (MessagingException e) {
            throw new RuntimeException(e);

        }

		
		

	}
	
}
