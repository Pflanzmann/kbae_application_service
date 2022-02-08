package com.kbe.application.controller;

import com.kbe.application.api.CalculatorApi;
import com.kbe.application.api.GifInformationStorageApi;
import com.kbe.application.api.MetaDataExtractorApi;
import com.kbe.application.helper.CSVExporter;
import com.kbe.application.model.Gif;
import com.kbe.application.model.GifDetails;
import com.kbe.application.model.GifInformation;
import com.kbe.application.model.NewGifRequest;
import com.kbe.application.repository.GifRepository;
import com.kbe.application.sftp.FileTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/gifs")
public class GifController {

    private GifRepository gifRepository;
    private MetaDataExtractorApi metaDataExtractorApi;
    private GifInformationStorageApi gifInformationStorageApi;
    private CalculatorApi calculatorApi;
    private CSVExporter csvExporter;
    private FileTransferService fileTransferService;

    @Autowired
    public GifController(CalculatorApi calculatorApi, GifRepository gifRepository, MetaDataExtractorApi metaDataExtractorApi, GifInformationStorageApi gifInformationStorageApi, CSVExporter csvExporter, FileTransferService fileTransferService) {
        this.csvExporter = csvExporter;
        this.calculatorApi = calculatorApi;
        this.gifRepository = gifRepository;
        this.fileTransferService = fileTransferService;
        this.metaDataExtractorApi = metaDataExtractorApi;
        this.gifInformationStorageApi = gifInformationStorageApi;
    }

    @GetMapping("")
    public List<Gif> getAll() {
        return gifRepository.findAll();
    }

    @PostMapping("")
    @ResponseBody
    public ResponseEntity<Gif> postNewGif(@RequestBody NewGifRequest newGifRequest) {
        List<Gif> gifs = gifRepository.findAll();
        if (gifs.stream().anyMatch(gif -> gif.getUrl().equals(newGifRequest.getUrl()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
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
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        return ResponseEntity.ok(gif);
    }

    @PostMapping("/{id}/upvote")
    @ResponseBody
    public ResponseEntity<Gif> upvoteGif(@PathVariable(value = "id") UUID id) {
        Gif gif = gifRepository.getById(id);
        gif.setUpvotes(gif.getUpvotes() + 1);

        gif = gifRepository.save(gif);

        return ResponseEntity.ok(gif);
    }

    @PostMapping("/{id}/downvote")
    @ResponseBody
    public ResponseEntity<Gif> downvoteGif(@PathVariable(value = "id") UUID id) {
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

            return ResponseEntity.ok(gifInformation);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}/details")
    @ResponseBody
    public ResponseEntity<GifDetails> getGifDetails(@PathVariable(value = "id") UUID id) {
        try {
            Gif gif = gifRepository.getById(id);

            return ResponseEntity.ok(metaDataExtractorApi.getMetaDataByUrl(gif));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/export")
    @ResponseBody
    public ResponseEntity exportData() {
        try {
            csvExporter.generateCSV();

            fileTransferService.uploadFile("all_informations.csv", "all_informations.csv");

            boolean didSucceed = gifInformationStorageApi.getToStartExport();

            if (!didSucceed) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok().build();
    }
}
