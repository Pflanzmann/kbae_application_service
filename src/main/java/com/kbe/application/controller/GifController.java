package com.kbe.application.controller;

import com.kbe.application.model.Gif;
import com.kbe.application.model.NewGifUrl;
import com.kbe.application.repository.GifRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/gifs")
public class GifController {

    private GifRepository gifRepository;

    public GifController(GifRepository gifRepository) {
        this.gifRepository = gifRepository;
    }

    @GetMapping("/all")
    public List<Gif> temp() {
        return gifRepository.findAll();
    }

    @PutMapping("")
    @ResponseBody
    public ResponseEntity<Gif> postNewPicture(@RequestBody NewGifUrl newGifUrl) {
        List<Gif> gifs = gifRepository.findAll();
        if (gifs.stream().anyMatch(gif -> gif.getUrl().equals(newGifUrl.getUrl()))) {
            return ResponseEntity.badRequest().build();
        }

        Gif gif = new Gif(newGifUrl.getUrl());
        gif = gifRepository.save(gif);

        return ResponseEntity.ok(gif);
    }

    @PostMapping("/{id}/upvote")
    @ResponseBody
    public ResponseEntity<Gif> upvotePicture(@PathVariable(value = "id") UUID id) {
        Gif gif = gifRepository.getById(id);
        gif.setUpvotes(gif.getUpvotes() + 1);

        gif = gifRepository.save(gif);

        return ResponseEntity.ok(gif);
    }

    @PostMapping("/{id}/downvote")
    @ResponseBody
    public ResponseEntity<Gif> downvotePicture(@PathVariable(value = "id") UUID id) {
        Gif gif = gifRepository.getById(id);
        gif.setDownvotes(gif.getDownvotes() + 1);

        gif = gifRepository.save(gif);

        System.out.println("down");
        return ResponseEntity.ok(gif);
    }
}
