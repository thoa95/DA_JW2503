package com.bkap.qlks.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class MailController {

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/contact")
    public String contactForm() {
        return "lienhe"; 
    }

    @PostMapping("/contact/send")
    public String sendEmail(@RequestParam String name,
                            @RequestParam String email,
                            @RequestParam String message,
                            Model model) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(email);
            mailMessage.setTo("baongocnghech04@gmail.com"); 
            mailMessage.setSubject("Liên hệ từ " + name);
            mailMessage.setText("Người gửi: " + name + "\nEmail: " + email + "\n\nNội dung:\n" + message);
            mailSender.send(mailMessage);

            model.addAttribute("success", "Gửi liên hệ thành công!");
        } catch (Exception e) {
            model.addAttribute("error", "Gửi liên hệ thất bại: " + e.getMessage());
        }
        return "redirect:/contact";
    }
}

