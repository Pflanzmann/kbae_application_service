package com.kbe.application.helper;

import com.kbe.application.api.CalculatorApi;
import com.kbe.application.api.GifInformationStorageApi;
import com.kbe.application.api.MetaDataExtractorApiType;
import com.kbe.application.model.Gif;
import com.kbe.application.model.GifDetails;
import com.kbe.application.model.GifInformation;
import com.kbe.application.model.GifVoteRatio;
import com.kbe.application.repository.GifRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVExporter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String CSV_FILE_NAME = "all_informations.csv";
    private final String[] CSV_HEADER = {"id", "url", "upvotes", "downvotes", "upvoteRate", "downvoteRate", "title", "author", "description", "topic", "fileSize", "imageWidth", "imageHeight", "frameCount", "duration"};

    private GifRepository gifRepository;
    private MetaDataExtractorApiType metaDataExtractorApi;
    private GifInformationStorageApi gifInformationStorageApi;
    private CalculatorApi calculatorApi;

    @Autowired
    public CSVExporter(GifRepository gifRepository, MetaDataExtractorApiType metaDataExtractorApi, GifInformationStorageApi gifInformationStorageApi, CalculatorApi calculatorApi) {
        this.gifRepository = gifRepository;
        this.metaDataExtractorApi = metaDataExtractorApi;
        this.gifInformationStorageApi = gifInformationStorageApi;
        this.calculatorApi = calculatorApi;
    }

    public void generateCSV() throws IOException {
        logger.info("Generate csv file");

        List<Gif> allGifs = gifRepository.findAll();
        List<GifInformation> allGifInformations = gifInformationStorageApi.getAllGifInformations();
        List<GifDetails> allGifDetails = new ArrayList<>();
        List<GifVoteRatio> allGifVoteRatios = new ArrayList<>();

        for (Gif gif : allGifs) {
            allGifDetails.add(metaDataExtractorApi.getMetaDataByUrl(gif));
            allGifVoteRatios.add(calculatorApi.getCalculation(gif));
        }

        List<GifInformation> orderedList = new ArrayList<>();
        for (Gif gif : allGifs) {

            boolean found = false;
            for (GifInformation allGifInformation : allGifInformations) {

                if (gif.getId().equals(allGifInformation.getId())) {
                    orderedList.add(allGifInformation);
                    found = true;

                    break;
                }
            }

            if (!found) {
                orderedList.add(new GifInformation(gif.getId()));
            }
        }
        allGifInformations = orderedList;

        logger.info("Start sftp connection");
        CSVFormat format = CSVFormat.Builder.create().setHeader(CSV_HEADER).build();

        FileWriter fileWriter = new FileWriter(CSV_FILE_NAME);
        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, format);
        for (int index = 0; index < allGifs.size(); index++) {
            csvPrinter.printRecord(
                    allGifs.get(index).getId(),
                    allGifs.get(index).getUrl(),
                    allGifs.get(index).getUpvotes(),
                    allGifs.get(index).getDownvotes(),
                    allGifVoteRatios.get(index).getUpvoteRate(),
                    allGifVoteRatios.get(index).getDownvoteRate(),
                    allGifInformations.get(index).getTitle(),
                    allGifInformations.get(index).getAuthor(),
                    allGifInformations.get(index).getDescription(),
                    allGifInformations.get(index).getTopic(),
                    allGifDetails.get(index).getFileSize(),
                    allGifDetails.get(index).getImageWidth(),
                    allGifDetails.get(index).getImageHeight(),
                    allGifDetails.get(index).getFrameCount(),
                    allGifDetails.get(index).getDuration()
            );
        }

        csvPrinter.flush();
    }
}
