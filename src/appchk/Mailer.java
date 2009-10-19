package appchk;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

public class Mailer {
    
    private static Logger log = Logger.getLogger(Mailer.class);
    
    public static void mailPingResponse(PingResponse response, PingUrl url) {
        Session session = Session.getDefaultInstance(new Properties(), null);
        
        String msgBody ="Url: "+url.getUrl()+ 
                      "\nHeaders: "+(response.getHeaders()==null?"null":response.getHeaders().toString())+
                      "\nStatus: "+(response.getStatus()==null?"unknown":response.getStatus());

        if(log.isDebugEnabled()){
            log.debug("mailto:"+url.getOwner()+" "+msgBody);
        }
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(Stuff.MAILFROM, Stuff.MAILNAME));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(url.getOwner()));
            msg.setSubject(Stuff.MAILPRE+"Error report");
            msg.setText(msgBody);
            Transport.send(msg);
        } catch (AddressException e) {
            log.error("", e);
        } catch (MessagingException e) {
            log.error("", e);
        } catch (UnsupportedEncodingException e) {
            log.error("", e);
        }
    }

}
