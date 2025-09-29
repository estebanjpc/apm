package com.app.service;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.app.entity.Email;
import com.app.entity.Usuario;

@Service
public class EmailServiceImp implements IEmailService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${mail.set.from}")
	public String emailSetFrom;
	
	public void creacionUsuario(Usuario usuario) {
		
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(emailSetFrom);
			message.setTo(usuario.getEmail());
			message.setSubject("Creacion Usuario");
			message.setText("Se ha creado correctamente el nuevo usuario: "+ usuario.getEmail() + " con pass: " + usuario.getPassAux());
			sendMail(message);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendMail(SimpleMailMessage message) {
		try {
			javaMailSender.send(message);		
		}catch(Exception e) {
			System.out.println("######## ERROR ENVIO EMAIL ########");
			e.printStackTrace();
		}
	}

	@Override
	public void creacionUsuario(Email email) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(email.getEmailFrom());
			message.setTo(email.getEmailTo());
			message.setSubject(email.getSubject());
			message.setText(email.getBody());
			javaMailSender.send(message);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void enviarPdfConAdjunto(Usuario usuario, byte[] pdfBytes) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            // true = mensaje multiparte
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(emailSetFrom);
            helper.setTo(usuario.getEmail());
            helper.setSubject("Liquidación PDF");
            helper.setText("Estimado " + usuario.getNombre() + ", adjuntamos su liquidación en formato PDF.");

            // Adjuntar PDF
            ByteArrayResource pdfSource = new ByteArrayResource(pdfBytes);
            helper.addAttachment("liquidacion.pdf", pdfSource);

            javaMailSender.send(mimeMessage);
            System.out.println("Correo con PDF enviado correctamente");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
