package com.kbe.application.controller;

import com.kbe.application.entity.Picture;
import com.kbe.application.repository.PictureRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/picture")
public class PictureController {

    private PictureRepository pictureRepository;

    public PictureController(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    @GetMapping("/all")
    public List<Picture> temp() {
        return pictureRepository.findAll();
    }

    @PutMapping("/{url}")
    public @ResponseBody
    ResponseEntity<Picture> postNewPicture(@PathVariable(value = "url") String url) {
        Picture picture = new Picture(url);
        picture = pictureRepository.save(picture);

        return ResponseEntity.ok(picture);
    }

    @PostMapping("/{id}/upvote")
    public @ResponseBody
    ResponseEntity<Picture> upvotePicture(@PathVariable(value = "id") UUID id) {
        Picture picture = pictureRepository.getById(id);
        picture.setUpvotes(picture.getUpvotes() + 1);

        picture = pictureRepository.save(picture);

        return ResponseEntity.ok(picture);
    }

    @PostMapping("/{id}/downvote")
    public @ResponseBody
    ResponseEntity<Picture> downvotePicture(@PathVariable(value = "id") UUID id) {
        Picture picture = pictureRepository.getById(id);
        picture.setUpvotes(picture.getUpvotes() - 1);

        picture = pictureRepository.save(picture);

        return ResponseEntity.ok(picture);
    }
}
