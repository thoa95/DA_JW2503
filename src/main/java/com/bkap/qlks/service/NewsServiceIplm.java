package com.bkap.qlks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bkap.qlks.entity.News;
import com.bkap.qlks.repository.NewsRepository;
@Service
public class NewsServiceIplm implements NewsService {

	@Autowired
	private NewsRepository newsRepository;


	@Override
	public Boolean create(News news) {
		// TODO Auto-generated method stub
		try {
			this.newsRepository.save(news);
			return true;
		} catch (Exception e) {
			System.out.println("Đăng bài thất bại!");
			// TODO: handle exception
		}
		return false;
	}


	@Override
	public List<News> findAll() {
		// TODO Auto-generated method stub
		return newsRepository.findAllOrderByCreatedAtDesc();
	}
    @Override
    public void delete(Long id) {
        newsRepository.deleteById(id);
    }
}
