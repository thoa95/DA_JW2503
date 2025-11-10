package com.bkap.qlks.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bkap.qlks.entity.News;

@Service
public interface NewsService {
	List<News> findAll();
	Boolean create(News news);
	void delete(Long id);
}
