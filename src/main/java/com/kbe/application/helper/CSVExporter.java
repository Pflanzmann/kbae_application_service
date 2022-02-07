package com.kbe.application.helper;

import com.kbe.application.api.CalculatorApi;
import com.kbe.application.api.GifInformationStorageApi;
import com.kbe.application.api.MetaDataExtractorApi;
import com.kbe.application.model.Gif;
import com.kbe.application.model.GifDetails;
import com.kbe.application.model.GifInformation;
import com.kbe.application.model.GifVoteRatio;
import com.kbe.application.repository.GifRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVExporter {

    private GifRepository gifRepository;
    private MetaDataExtractorApi metaDataExtractorApi;
    private GifInformationStorageApi gifInformationStorageApi;
    private CalculatorApi calculatorApi;

    @Autowired
    public CSVExporter(GifRepository gifRepository, MetaDataExtractorApi metaDataExtractorApi, GifInformationStorageApi gifInformationStorageApi, CalculatorApi calculatorApi) {
        this.gifRepository = gifRepository;
        this.metaDataExtractorApi = metaDataExtractorApi;
        this.gifInformationStorageApi = gifInformationStorageApi;
        this.calculatorApi = calculatorApi;
    }

    public void generateCSV() throws IOException {
        List<Gif> allGifs = gifRepository.findAll();
        List<GifInformation> allGifInformations = gifInformationStorageApi.getAllGifInformations();
        List<GifDetails> allGifDetails = new ArrayList<>();
        List<GifVoteRatio> allGifVoteRatios = new ArrayList<>();

        for (Gif gif : allGifs) {
            //  allGifDetails.add(metaDataExtractorApi.getMetaDataByUrl(gif));
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
                orderedList.add(new GifInformation(gif.getId(), "", "", "", ""));
            }
        }
        allGifInformations = orderedList;

        FileWriter fileWriter = new FileWriter("all_informations.csv");
        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);
        for (int index = 0; index < allGifs.size(); index++) {
            csvPrinter.printRecord(
                    index,
                    allGifs.get(index).getId(),
                    allGifs.get(index).getUrl(),
                    allGifs.get(index).getUpvotes(),
                    allGifs.get(index).getDownvotes(),
                    allGifVoteRatios.get(index).getUpvoteRate(),
                    allGifVoteRatios.get(index).getDownvoteRate(),
                    allGifInformations.get(index).getTitle(),
                    allGifInformations.get(index).getAuthor(),
                    allGifInformations.get(index).getDescription(),
                    allGifInformations.get(index).getTopic()
            );
        }

        csvPrinter.flush();

    }
}
