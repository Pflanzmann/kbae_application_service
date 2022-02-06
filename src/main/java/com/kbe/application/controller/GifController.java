package com.kbe.application.controller;

import com.kbe.application.api.MetaDataExtractorApi;
import com.kbe.application.model.Gif;
import com.kbe.application.model.NewGifRequest;
import com.kbe.application.repository.GifRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/gifs")
public class GifController {

    private GifRepository gifRepository;
    private MetaDataExtractorApi metaDataExtractorApi;

    @Autowired
    public GifController(GifRepository gifRepository, MetaDataExtractorApi metaDataExtractorApi) {
        this.gifRepository = gifRepository;
        this.metaDataExtractorApi = metaDataExtractorApi;
    }

    @GetMapping("")
    public List<Gif> temp() {
        return gifRepository.findAll();
    }

    @PostMapping("")
    @ResponseBody
    public ResponseEntity<Gif> postNewPicture(@RequestBody NewGifRequest newGifRequest) {
        List<Gif> gifs = gifRepository.findAll();
        if (gifs.stream().anyMatch(gif -> gif.getUrl().equals(newGifRequest.getUrl()))) {
            return ResponseEntity.badRequest().build();
        }

        Gif gif = new Gif(newGifRequest.getUrl());
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

        return ResponseEntity.ok(gif);
    }
}
