package com.kbe.application.controller;

import com.kbe.application.api.GifInformationStorageApi;
import com.kbe.application.api.MetaDataExtractorApi;
import com.kbe.application.model.Gif;
import com.kbe.application.model.GifDetails;
import com.kbe.application.model.GifInformation;
import com.kbe.application.model.NewGifRequest;
import com.kbe.application.repository.GifRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/gifs")
public class GifController {

    private GifRepository gifRepository;
    private MetaDataExtractorApi metaDataExtractorApi;
    private GifInformationStorageApi gifInformationStorageApi;

    @Autowired
    public GifController(GifRepository gifRepository, MetaDataExtractorApi metaDataExtractorApi, GifInformationStorageApi gifInformationStorageApi) {
        this.gifRepository = gifRepository;
        this.metaDataExtractorApi = metaDataExtractorApi;
        this.gifInformationStorageApi = gifInformationStorageApi;
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

        GifInformation gifInformation = new GifInformation(
                gif.getId(),
                newGifRequest.getTitle(),
                newGifRequest.getAuthor(),
                newGifRequest.getDescription(),
                newGifRequest.getTopic()
        );

        try {
            System.out.println(gifInformationStorageApi.postNewGifInformation(gifInformation));
        } catch (IOException e) {
            e.printStackTrace();
            gifRepository.delete(gif);
            return ResponseEntity.internalServerError().build();
        }

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

    @GetMapping("/{id}/information")
    @ResponseBody
    public ResponseEntity<GifInformation> getGifInformation(@PathVariable(value = "id") UUID id) {
        try {
            GifInformation gifInformation = gifInformationStorageApi.getGifInformation(id);

            System.out.println(gifInformation);

            return ResponseEntity.ok(gifInformation);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/details")
    @ResponseBody
    public ResponseEntity<GifDetails> getGifDetails(@PathVariable(value = "id") UUID id) {
        try {
            Gif gif = gifRepository.getById(id);

            return ResponseEntity.ok(metaDataExtractorApi.getMetaDataByUrl(gif));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
