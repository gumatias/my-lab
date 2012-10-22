package com.films.mailet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeMultipart;

import org.apache.mailet.GenericMailet;
import org.apache.mailet.Mail;
import org.apache.mailet.MailAddress;
import org.apache.mailet.MailetConfig;

import com.films.test.MailClient;

public class VideoDownloader extends GenericMailet {

	private MailetConfig mailetConfig;

	private static String destFolder = null;
	
	@Override
	public void init(MailetConfig mailetConfig) {
		this.mailetConfig = mailetConfig;
		destFolder = mailetConfig.getInitParameter("saveInFolder");
	}

	@Override
	public void service(Mail mail) {
		try {
			if (mail.getMessage().getContent() instanceof MimeMultipart) {
				MimeMultipart mmp = (MimeMultipart) mail.getMessage()
						.getContent();

				for (int i = 0; i < mmp.getCount(); i++) {
					Part part = (Part) mmp.getBodyPart(i);
					
					String disposition = part.getDisposition();
					if (disposition != null && disposition.equals(Part.ATTACHMENT)) {
						
						String videoExt = part.getFileName().substring(
												part.getFileName().lastIndexOf("."), 
												part.getFileName().length()
												);
						
						if (part.isMimeType("video/*") || videoExts.contains(videoExt.toLowerCase())) {
							String senderAddress = mail.getSender().toInternetAddress().getAddress();
							saveVideo(senderAddress, part.getInputStream(), videoExt);
							replyToSender(senderAddress);
						}
					}
				}
			}
			/* discard the email */
			mail.setState(Mail.GHOST);
		} catch (MessagingException e) {
			log("exception:" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log("exception:" + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			log("exception:" + e.getMessage());
			e.printStackTrace();
		}
	}

	/* 
	 * <hardcoded>
	 * Just sending some confirmation, it could be 
	 * defined inside config.xml as an element.
	 */
	private void replyToSender(String user) throws MessagingException {
		new MailClient("upload", "4321", "localhost")
		.sendMessage(user, "Video Uploaded", "Your video was successfully uploaded!");
	}

	private void saveVideo(String user, InputStream inputStream, String videoExt) {
		String now = new SimpleDateFormat("yyyyMMDD_HHmmss").format(Calendar.getInstance().getTime());
		String fileName = "VID_" + user + "_" + now + videoExt;
		
		try {
			File file = new File(destFolder + fileName);
			OutputStream out = new FileOutputStream(file);
			byte buf[] = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0)
				out.write(buf, 0, len);
			out.close();
			inputStream.close();
		} catch (FileNotFoundException e) {
			log("exception:" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log("exception:" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void log(String message) {
		mailetConfig.getMailetContext().log(message);
	}
	
	/* 
	 * FIXME
	 * for some reason the android mail client wasn't sending 
	 * the content-type of the video. below is just a list of 
	 * the most common video extensions.
	 */
	private static List<String> videoExts = new ArrayList<String>(){{
		add(".3g2");
		add(".3gp");
		add(".asf");
		add(".asx");
		add(".avi");
		add(".flv");
		add(".mov");
		add(".mp4");
		add(".mpg");
		add(".rm");
		add(".swf");
		add(".vob");
		add(".wmv");
	}};

}