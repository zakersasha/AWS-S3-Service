package com.AWS.s3.repo;

import com.AWS.s3.models.S3Object;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface S3ObjectRepository extends JpaRepository<S3Object, Long> {
    List<S3Object> findByName(String name);
}
