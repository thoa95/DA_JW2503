package com.bkap.qlks.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bkap.qlks.entity.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    @Query("SELECT n FROM News n ORDER BY n.createdat DESC")
    List<News> findAllOrderByCreatedAtDesc();

    // Tùy chọn: lấy tin theo người đăng
    List<News> findByAccount_AccountIdOrderByCreatedatDesc(String accountId);


}
