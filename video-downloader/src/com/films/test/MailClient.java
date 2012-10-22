package com.films.test;

import java.io.*;
import java.util.*;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

public class MailClient extends Authenticator {

	protected String from;

	protected String host;

	protected String user;

	protected String password;

	protected Session session;

	protected PasswordAuthentication authentication;

	public MailClient(String user, String password, String host) {
		this.user = user;
		this.password = password;
		this.host = host;

		from = user + '@' + host;
		Properties properties = new Properties();
		properties.put("mail.user", user);
		properties.put("mail.password", password);
		properties.put("mail.host", host);
		properties.put("mail.debug", "false");
		properties.put("mail.store.protocol", "pop3");
		properties.put("mail.transport.protocol", "smtp");

		session = Session.getInstance(properties, this);
		authentication = new PasswordAuthentication(user, password);
	}

	public void sendMessage(String to, String subject, String content)
			throws MessagingException {
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));

			message.setSubject(subject);
			message.setText(content);
			
			Transport.send(message);
			System.out.println("Email sent successfully");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessageWithAttachment(String to, String subject, String content, String attachment)
			throws MessagingException {
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			message.setSubject(subject);
			
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setText(content);
			MimeBodyPart mbp2 = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(attachment);
			mbp2.setDataHandler(new DataHandler(fds));
			mbp2.setFileName(fds.getName());
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			mp.addBodyPart(mbp2);
			message.setContent(mp);
			message.setSentDate(new java.util.Date());
			Transport.send(message);
			
			Transport.send(message);
			System.out.println("Email sent successfully");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void checkInbox() throws MessagingException, IOException {
		Properties properties = System.getProperties();
		Session session = Session.getDefaultInstance(properties);
		Store store = session.getStore("pop3");
		store.connect(host, user, password);
		Folder folder = store.getFolder("inbox");
		folder.open(Folder.READ_ONLY);

		Message[] message = folder.getMessages();

		if (message.length == 0) System.out.println("No messages in inbox");
		
		for (int i = 0; i < message.length; i++) {
			System.out.println("--- Message " + (i + 1) + "---");
			System.out.println("Size : " + message[i].getSize());
			System.out.println("SentDate : " + message[i].getSentDate());
			System.out.println("From : " + message[i].getFrom()[0]);
			System.out.println("Subject : " + message[i].getSubject());
			System.out.print("Message : ");
			InputStream stream = message[i].getInputStream();
			StringBuilder msg = new StringBuilder();
			while (true) {
				char c = (char) stream.read();
				if (c == '\uffff') break;
				msg.append(c);
			}
			System.out.println(msg);
		}
		folder.close(true);
		store.close();
	}

	public void clearInbox() throws MessagingException, IOException {
		Store store = session.getStore();
		store.connect();
		Folder root = store.getDefaultFolder();
		Folder inbox = root.getFolder("inbox");
		inbox.open(Folder.READ_WRITE);
		Message[] msgs = inbox.getMessages();
		if (msgs.length == 0) {
			System.out.println("No messages in inbox");
		}
		for (int i = 0; i < msgs.length; i++) {
			MimeMessage msg = (MimeMessage) msgs[i];
			msg.setFlag(Flags.Flag.DELETED, true);
		}
		System.out.println("Inbox cleared!");
		inbox.close(true);
		store.close();
	}

	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		return authentication;
	}
}