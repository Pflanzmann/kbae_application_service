package com.kbe.application.controller;

import com.kbe.application.api.MetaDataExtractorApi;
import com.kbe.application.model.Gif;
import com.kbe.application.repository.GifRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/gif")
public class GifController {

    private GifRepository gifRepository;

    public GifController(GifRepository gifRepository) {
        this.gifRepository = gifRepository;
    }

    @GetMapping("/all")
    public List<Gif> temp() {
        try {
            MetaDataExtractorApi metaDataExtractorApi = new MetaDataExtractorApi();
            metaDataExtractorApi.getMetaDataByUrl("https://media.giphy.com/media/4N5vB4aErlVtVsywBw/giphy.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gifRepository.findAll();
    }

    @PutMapping("/{url}")
    @ResponseBody
    public ResponseEntity<Gif> postNewPicture(@PathVariable(value = "url") String url) {
        Gif gif = new Gif(url);
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
        gif.setUpvotes(gif.getUpvotes() - 1);

        gif = gifRepository.save(gif);

        return ResponseEntity.ok(gif);
    }
}
