package com.kbe.application.repository;

import com.kbe.application.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public interface PictureRepository extends JpaRepository<Picture, UUID> {

}
