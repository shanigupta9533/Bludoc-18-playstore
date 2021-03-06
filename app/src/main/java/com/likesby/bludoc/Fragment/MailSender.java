//package com.likesby.bludoc.Fragment;
//import javax.activation.DataHandler;
//import javax.activation.DataSource;
//import javax.mail.Message;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.security.AccessController;
//import java.security.Provider;
//import java.security.Security;
//import java.util.Properties;
//
//public class MailSender extends javax.mail.Authenticator {
//    private String mailhost = "smtp.gmail.com";
//    private String user;
//    private String password;
//    private Session session;
//
//    static {
//        Security.addProvider(new JSSEProvider());
//    }
//
//    public MailSender(String user, String password) {
//        this.user = user;
//        this.password = password;
//
//        Properties props = new Properties();
//        props.setProperty("mail.transport.protocol", "smtp");
//        props.setProperty("mail.host", mailhost);
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.port", "465");
//        props.put("mail.smtp.socketFactory.port", "465");
//        props.put("mail.smtp.socketFactory.class",
//                "javax.net.ssl.SSLSocketFactory");
//        props.put("mail.smtp.socketFactory.fallback", "false");
//        props.setProperty("mail.smtp.quitwait", "false");
//
//        session = Session.getDefaultInstance(props, this);
//    }
//
//    protected PasswordAuthentication getPasswordAuthentication() {
//        return new PasswordAuthentication(user, password);
//    }
//
//    public synchronized void sendMail(String subject, String body, String sender, String from, String recipients) throws Exception {
//        try {
//            MimeMessage message = new MimeMessage(session);
//            DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/html"));
//            /*message.setSender(new InternetAddress(sender, "Dhaval Solanki"));*/
//            message.setFrom(new InternetAddress(sender, from));
//            message.setSubject(subject);
//
//            message.setDataHandler(handler);
//            if (recipients.indexOf(',') > 0)
//                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
//            else
//                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
//            Transport.send(message);
//        } catch (Exception e) {
//
//        }
//    }
//
//    public class ByteArrayDataSource implements DataSource {
//        private byte[] data;
//        private String type;
//
//        public ByteArrayDataSource(byte[] data, String type) {
//            super();
//            this.data = data;
//            this.type = type;
//        }
//
//        public ByteArrayDataSource(byte[] data) {
//            super();
//            this.data = data;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//
//        public String getContentType() {
//            if (type == null)
//                return "application/octet-stream";
//            else
//                return type;
//        }
//
//        public InputStream getInputStream() throws IOException {
//            return new ByteArrayInputStream(data);
//        }
//
//        public String getName() {
//            return "ByteArrayDataSource";
//        }
//
//        public OutputStream getOutputStream() throws IOException {
//            throw new IOException("Not Supported");
//        }
//    }
//}
//
//
//
//
//
//*  Licensed to the Apache Software Foundation (ASF) under one or more
//        *  contributor license agreements.  See the NOTICE file distributed with
//        *  this work for additional information regarding copyright ownership.
//        *  The ASF licenses this file to You under the Apache License, Version 2.0
//        *  (the "License"); you may not use this file except in compliance with
//        *  the License.  You may obtain a copy of the License at
//        *
//        *     http://www.apache.org/licenses/LICENSE-2.0
//        *
//        *  Unless required by applicable law or agreed to in writing, software
//        *  distributed under the License is distributed on an "AS IS" BASIS,
//        *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//        *  See the License for the specific language governing permissions and
//        *  limitations under the License.
//        */
//
///**
// * @author Alexander Y. Kleymenov
// * @version $Revision$
// */
//
//
//        import java.security.AccessController;
//        import java.security.Provider;
//
//public final class JSSEProvider extends Provider {
//
//    public JSSEProvider() {
//        super("HarmonyJSSE", 1.0, "Harmony JSSE Provider");
//        AccessController.doPrivileged(new java.security.PrivilegedAction<Void>() {
//            public Void run() {
//                put("SSLContext.TLS",
//                        "org.apache.harmony.xnet.provider.jsse.SSLContextImpl");
//                put("Alg.Alias.SSLContext.TLSv1", "TLS");
//                put("KeyManagerFactory.X509",
//                        "org.apache.harmony.xnet.provider.jsse.KeyManagerFactoryImpl");
//                put("TrustManagerFactory.X509",
//                        "org.apache.harmony.xnet.provider.jsse.TrustManagerFactoryImpl");
//                return null;
//            }
//        });
//    }
//}