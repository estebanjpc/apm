package com.app.service;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.app.entity.Email;
import com.app.entity.Liquidacion;
import com.app.entity.Usuario;

@Service
public class EmailServiceImp implements IEmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private SpringTemplateEngine templateEngine;
	
	@Value("${mail.set.from}")
	public String emailSetFrom;
	
	public void creacionUsuario(Usuario usuario) {
	    try {
	        MimeMessage mimeMessage = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

	        // Crear contexto Thymeleaf
	        Context context = new Context();
	        context.setVariable("nombre", usuario.getNombre());
	        context.setVariable("apellido", usuario.getApellido());
	        context.setVariable("email", usuario.getEmail());
	        context.setVariable("password", usuario.getPassAux());

	        // Procesar plantilla Thymeleaf
	        String htmlContent = templateEngine.process("email/creacionUsuario.html", context);

	        // Configurar correo
	        helper.setTo(usuario.getEmail());
	        helper.setSubject("Bienvenido a la Plataforma - Creación de cuenta");
	        helper.setFrom(emailSetFrom);
	        helper.setText(htmlContent, true); // true = HTML

	        // Adjuntar logo embebido
	        FileSystemResource logo = new FileSystemResource(new File("src/main/resources/static/images/logo.png"));
	        helper.addInline("logoImage", logo);

	        mailSender.send(mimeMessage);

	        System.out.println("Correo moderno de creación de usuario enviado correctamente");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void sendMail(SimpleMailMessage message) {
		try {
			mailSender.send(message);		
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
			mailSender.send(message);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void enviarPdfConAdjunto(Usuario usuario, byte[] pdfBytes, Liquidacion liquidacion) {
	    try {
	        MimeMessage mimeMessage = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

	        // Crear contexto Thymeleaf
	        Context context = new Context();
	        String mes = new java.text.SimpleDateFormat("MMMM yyyy", new java.util.Locale("es")).format(liquidacion.getFecha());
	        mes = mes.substring(0, 1).toUpperCase() + mes.substring(1);

	        context.setVariable("nombre", usuario.getNombre());
	        context.setVariable("apellido", usuario.getApellido());
	        context.setVariable("empresa", liquidacion.getEmpresa().getNombre());
	        context.setVariable("mes", mes);
	        context.setVariable("nombreArchivo", liquidacion.getNombreArchivo());

	        // Procesar plantilla Thymeleaf
	        String htmlContent = templateEngine.process("email/liquidacion.html", context);

	        // Configurar correo
	        helper.setTo(usuario.getEmail());
	        helper.setFrom(emailSetFrom);
	        helper.setSubject("Liquidación - " + mes);
	        helper.setText(htmlContent, true); // true = HTML

	        // Adjuntar logo inline
	        FileSystemResource logo = new FileSystemResource(new File("src/main/resources/static/images/logo.png"));
	        helper.addInline("logoImage", logo);

	        // Adjuntar PDF
	        ByteArrayResource pdfSource = new ByteArrayResource(pdfBytes);
	        helper.addAttachment(liquidacion.getNombreArchivo() != null ? liquidacion.getNombreArchivo() : "liquidacion.pdf", pdfSource);

	        mailSender.send(mimeMessage);

	        System.out.println("Correo moderno con liquidación PDF enviado correctamente");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
