package api.daoImpl;

import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

public class HangoutMsg implements PacketListener{
	 private static String username = "info@parrotinfosoft.com";
	 private static String password = "info@123";

    ConnectionConfiguration connConfig;
    XMPPConnection connection;
    private List<Message> messages = new ArrayList();

    public HangoutMsg() throws XMPPException {
        connConfig =
                new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
        connection = new XMPPConnection(connConfig);
        connection.connect();
        connection.login(username, password);
       // tell the world (or at least our friends) that we are around
      //  Presence presence = new Presence(Presence.Type.available);
       // presence.setMode(Presence.Mode.chat);
    //    connection.sendPacket(presence);

        // to listen for incoming messages on this connection
        /*PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
        connection.addPacketListener((PacketListener)this, filter);

       Roster friendsRoster = connection.getRoster();
        Collection<RosterEntry> rosterIter = friendsRoster.getEntries();
        for (RosterEntry entry : rosterIter) {
            System.out.println(entry.getName() + " (" + entry.getUser() + ")");
        } */// rosterIter

    }

    public void sendMessage(String to, String message) {
        Message msg = new Message(to, Message.Type.chat);
        msg.setBody(message);
        
        connection.sendPacket(msg);
        

    }

    public void disconnect() {
        connection.disconnect();
    }


    public void processPacket(Packet packet) {
        Message message = (Message)packet;
        System.out.println("Message (from: " + message.getFrom() + "): " +
                           message.getBody());
        messages.add(0, message);

    }


    public static void main(String[] args) throws XMPPException {
    	HangoutMsg messageSender = new HangoutMsg();
    	 messageSender.sendMessage("shivali@virtuosonetsoft.com","Sent message by Java");
        messageSender.sendMessage("neeraj@virtuosonetsoft.in","System = Setup-0 \r\n" + 
        		"Name = virtuoso_t3_SMSC_DLT_3\r\n" + 
        		"Account = virtuso_t3\r\n" + 
        		"Success = 193,406\r\n" + 
        		"Pending = 253,225\r\n" + 
        		"Failed = 60,004\r\n" + 
        		"Total = 506,635");
        System.out.println("===============");
       // messageSender.sendMessage("starting-account-lhmskpi2hu8u@directed-gasket-270306.iam.gserviceaccount.com","your message chat");
        messageSender.disconnect();
    }

}

