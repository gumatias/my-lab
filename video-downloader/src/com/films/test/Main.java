package com.films.test;

import java.io.IOException;

import javax.mail.MessagingException;

public class Main {

	public static void main(String[] args) throws MessagingException,
			IOException, InterruptedException {

		// args = new String[5];
		// args[0] = "john";
		// args[1] = "1234";
		// args[2] = "localhost";
		// args[3] = "upload@192.168.0.103";
		// args[4] = "/home/gustavo/Videos/android test/engvideo.avi";

		if (args.length != 5)
			System.out.println("usage example: "
							+ "java Main user password localhost "
							+ "john@localhost /path/to/attachment/MyVideo.avi");

		MailClient client = new MailClient(args[0], args[1], args[2]);

		client.sendMessageWithAttachment(args[3], "Attachment", "Hello!", args[4]);
	}
}
