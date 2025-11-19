package com.bkap.qlks.controller;

import com.bkap.qlks.service.NewsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import com.bkap.qlks.entity.Account;
import com.bkap.qlks.entity.News;
import com.bkap.qlks.repository.NewsRepository;
import com.bkap.qlks.repository.UserRepository;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class ContactController {
	@Autowired
	private NewsService newsService;
	@Autowired
	private NewsRepository newsRepository;
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/blog")
	public String tt(Model model) {
		model.addAttribute("ne", newsRepository.findAllOrderByCreatedAtDesc());
		return "tintuc";
	}

	@GetMapping("/createblogger")
	public String createblo(Model model) {
		News news = new News();
		model.addAttribute("news", news);
		return "addnews";
	}

	@PostMapping("/createblog")
	public String storeblog(@ModelAttribute("news") News news, @RequestParam("imageFile") MultipartFile imageFile) {

		// Kiểm tra đăng nhập
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String account = auth.getName();

		if (account == null || account.equals("anonymousUser")) {
			return "redirect:/login"; // nếu chưa đăng nhập
		}

		try {
			// Thư mục lưu ảnh
			String uploadDir = "src/main/resources/static/images/";
			String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();

			Path path = Paths.get(uploadDir);

			// Tạo thư mục nếu chưa có
			if (!Files.exists(path)) {
			    Files.createDirectories(path);
			}

			// Đường dẫn file đầy đủ
			Path filePath = path.resolve(fileName);

			// Lưu file
			Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			news.setImage(fileName);

		} catch (IOException e) {
			e.printStackTrace();
		}

		Account acc = new Account();
		acc.setAccountId(account);
		news.setAccount(acc);
		news.setCreatedat(LocalDateTime.now());

		// Lưu bài viết
		newsService.create(news);

		return "redirect:/blog";
	}

	@GetMapping("/news/delete/{id}")
	public String deleteNews(@PathVariable Long id, Authentication authentication) {
		Optional<News> newsOpt = newsRepository.findById(id);
		if (newsOpt.isPresent()) {
			News news = newsOpt.get();
			String currentUserId = authentication.getName();

			if (news.getAccount() != null && news.getAccount().getAccountId().equals(currentUserId)) {
				newsRepository.delete(news);
				return "redirect:/blog?deleted";
			} else {
				return "redirect:/blog?error=notowner";
			}
		}
		return "redirect:/blog?error=notfound";
	}
}
