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
		String txt_contant="<html>\n" + 
        		"<head>\n" + 
        		"<title>Astrological.ly</title>\n" + 
        		"<link href='https//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css' rel='stylesheet' id='bootstrap-css'><script src='https//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js'></script><script src='https//code.jquery.com/jquery-1.11.1.min.js'></script><link rel='stylesheet' href='https://use.fontawesome.com/releases/v5.8.1/css/all.css' integrity='sha384-50oBUHEmvpQ+1lW4y57PTFmhCaXp0ML5d60M1M7uH2+nqUivzIebhndOJK28anvf' crossorigin='anonymous'><style type='text/css'>body {margin: 0 !important;padding: 0 !important;-webkit-text-size-adjust: 100% !important;-ms-text-size-adjust: 100% !important;-webkit-font-smoothing: antialiased !important;}img {border: 0 !important;outline: none !important;}p{Margin: 0px !important;Padding: 0px !important;color: #fff}table {border-collapse: collapse;mso-table-lspace: 0px;mso-table-rspace: 0px;}td, a, span {border-collapse: collapse;mso-line-height-rule: exactly;}.ExternalClass * {	line-height: 100%;}.em_defaultlink a {color: inherit !important;text-decoration: none !important;}span.MsoHyperlink {mso-style-priority: 99;color: inherit;}span.MsoHyperlinkFollowed {mso-style-priority: 99;color: inherit;}ul.social-network {list-style: none;display: inline;margin-left: 0 !important;padding: 0;}ul.social-network li {display: inline;margin: 0 5px;}.social-network a.icoinstagram:hover {background-color: #0078d7;}.social-network a.icoyoutube:hover {background-color: #f00;}.social-network a.icoFacebook:hover {background-color: #3B5998;}.social-network a.icoTwitter:hover {background-color: #33ccff;}.social-network a.icoGoogle:hover {background-color: #BD3518;}.social-network a.icoVimeo:hover {background-color: #0590B8;}.social-network a.icoLinkedin:hover {background-color: #007bb7;}.social-network a.icoRss:hover i, .social-network a.icoFacebook:hover i,.social-network a.icoTwitter:hover i, .social-network a.icoGoogle:hover i,.social-network a.icoVimeo:hover i, .social-network a.icoLinkedin:hover i{color: #fff;}a.socialIcon:hover, .socialHoverClass {color: #44BCDD;}.social-circle li a {display: inline-block;position: relative;margin: 0 auto 0 auto;-moz-border-radius: 50%;-webkit-border-radius: 50%;border-radius: 50%;text-align: center;width: 50px;height: 50px;font-size: 20px;}.social-circle li i {margin: 0;line-height: 50px;text-align: center;}a {background-color: #D3D3D3;}</style>\n" + 
        		"</head>\n" + 
        		"<body class='em_body'>\n" + 
        		"	<table class='em_full_wrap' valign='top' width='100%' cellspacing='0'\n" + 
        		"		cellpadding='0' border='0' bgcolor='#efefef' align='center'>\n" + 
        		"		<tbody>\n" + 
        		            
        		"			<tr>\n" + 
        		"				<td valign='top' align='center'><table class='em_main_table'\n" + 
        		"						style='width: 700px;' width='700' cellspacing='0' cellpadding='0'\n" + 
        		"						border='0' align='center'>\n" + 
        		"						<!--Header section-->\n" + 
        		"						<tbody>\n" + 
        		"        <tr style='background-color: #e0e0e0;text-align: center;'><span><img src='http://49.50.105.175:8088/ParrotResume/logo_parrot.png'></span></tr>"+
        		"							<!--//Header section-->\n" + 
        		"							\n" + 
        		"							<!--Content Text Section-->\n" + 
        		"							<tr>\n" + 
        		"								<td style='padding: 35px 20px 30px;' class='em_padd'\n" + 
        		"									valign='top' bgcolor='#fff' align='center'>\n" + 
        		"									<table width='100%' cellspacing='0' cellpadding='0' border='0'\n" + 
        		"										align='center'>\n" + 
        		"										<tbody>\n" + 
        		"\n" + 
        		"											<tr>\n" + 
        		"												<td\n" + 
        		"													style='font-family: 'Open Sans', Arial, sans-serif; font-size: 14px; line-height: 22px;'\n" + 
        		"													valign='top'>Dear Member<i class='fab fa-facebook-f'></i></td>\n" + 
        		"											</tr>\n" + 
        		"											<tr>\n" + 
        		"												<td class='em_h20'\n" + 
        		"													style='font-size: 0px; line-height: 0px; height: 25px;'\n" + 
        		"													height='25'>&nbsp;</td>\n" + 
        		"												<!--�this is space of 25px to separate two paragraphs ---->\n" + 
        		"											</tr>\n" + 
        		"											<tr>\n" + 
        		"												<td\n" + 
        		"													style='font-family: 'Open Sans', Arial, sans-serif; font-size: 14px; line-height: 22px;'\n" + 
        		"													valign='top'>"+txt_msg+"</td>\n" + 
        		"											</tr><tr><td>&nbsp;</td></tr><tr><td style='font-family: 'Open Sans', Arial, sans-serif; font-size: 14px; line-height: 22px;'>Thankyou</td></tr>\n" + 
        		"										</tbody>\n" + 
        		"									</table>\n" + 
        		"								</td>\n" + 
        		"							</tr>\n" + 
        		"\n" + 
        		"\n" + 
        		"\n" + 
        		"							<!--//Content Text Section-->\n" + 
        		"							<!--Footer Section-->\n" + 
        		"							<tr>\n" + 
        		"								<td style='padding: 38px 0px;' class='em_padd' valign='top'\n" + 
        		"									bgcolor='#fff' align='center'><table width='100%'\n" + 
        		"										cellspacing='0' cellpadding='0' border='0' align='center'>\n" + 
        		"										<tbody>\n" + 
        		"											<tr>\n" + 
        		"												<td valign='top' align='center' bgcolor='#125985'\n" + 
        		"													style='padding: 20px'>\n" + 
        		"													<div class='col-md-12'>\n" + 
        		"														<ul style='margin: 0;padding: 0;list-style: none;display: inline-flex;' class='social-network social-circle'>\n" + 
        		"															<li style='margin:10px;'><a\n" + 
        		"																href='https://www.facebook.com/parrotinfosoftpvt/'\n" + 
        		"																class='icoFacebook' title='Fb'>\n" + 
        		"																<img alt='' src='https://upload.wikimedia.org/wikipedia/commons/d/d4/Facebook-128.png' height='50px' width='50px;'>\n" + 
        		"																	</a></li>\n" + 
        		"\n" + 
        		"\n" + 
        		"															<li style='margin:10px;'><a\n" + 
        		"																href='https://www.instagram.com/parrotinfosoftpvt.ltd.1'\n" + 
        		"																class='icoInstagram' title='instagram'>\n" + 
        		"																<img alt='' src='https://image.flaticon.com/icons/png/128/174/174855.png' height='50px' width='50px;'>\n" + 
        		"																	\n" + 
        		"																</a></li>\n" + 
        		"															<li style='margin:10px;'><a\n" + 
        		"																href='https://www.linkedin.com/company/parrot-infosoft-pvt-ltd'\n" + 
        		"																class='icoLinkedin' title='Linkedin'>\n" + 
        		"																<img style='border-radius: 4px;' alt='' src='https://web.iceportal.com/wp-content/uploads/2018/03/square-linkedin-512.png' height='50px' width='50px;'>\n" + 
        		"																</a></li>\n" + 
        		"\n" + 
        		"														</ul>\n" + 
        		"														<hr>\n" + 
        		"														<p style='color:#fff;padding-bottom: 0px;margin-bottom: 0px;'>Copyright � 2016 Parrot Infosoft, All rights\n" + 
        		"															reserved.</p>\n" + 
        		"														<br>\n" + 
        		"\n" + 
        		"														<p style='color:#fff;margin-top: 0px;padding-top: 0;line-height: 0px;'>Ground Floor, Aeren Building Plot No. 14 Rajiv\n" + 
        		"															Gandhi Information Technology Park, Chandigarh,</p>\n" + 
        		"														<br>\n" + 
        		"\n" + 
        		"\n" + 
        		"													</div>\n" + 
        		"												</td>\n" + 
        		"											</tr>\n" + 
        		"										</tbody>\n" + 
        		"									</table></td>\n" + 
        		"							</tr>\n" + 
        		"\n" + 
        		"						</tbody>\n" + 
        		"					</table></td>\n" + 
        		"			</tr>\n" + 
        		"			<tr>\n" + 
        		"				<td class='em_hide'\n" + 
        		"					style='line-height: 1px; min-width: 700px; background-color: #ffffff;'><img\n" + 
        		"					alt='' src='file:///C|/Users/Nidhi/Desktop/images/spacer.gif'\n" + 
        		"					style='max-height: 1px; min-height: 1px; display: block; width: 700px; min-width: 700px;'\n" + 
        		"					width='700' border='0' height='1'></td>\n" + 
        		"			</tr>\n" + 
        		"		</tbody>\n" + 
        		"	</table>\n" + 
        		"	</td>\n" + 
        		"	</tr>\n" + 
        		"	</tbody>\n" + 
        		"	</table>\n" + 
        		"	<div class='em_hide'\n" + 
        		"		style='white-space: nowrap; display: none; font-size: 0px; line-height: 0px;'>&nbsp;\n" + 
        		"		&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;\n" + 
        		"		&nbsp; &nbsp; &nbsp;</div>\n" + 
        		"</body>\n" + 
        		"</html>\n" + 
        		""
        		+ "";
		SendEmail sendEmail=new SendEmail("neerajbhagat9872@gmail.com", "Testing", txt_contant);
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
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
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
