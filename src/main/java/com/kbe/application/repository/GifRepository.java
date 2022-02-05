package com.kbe.application.repository;

import com.kbe.application.model.Gif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public interface GifRepository extends JpaRepository<Gif, UUID> {

}
