package com.kbe.application.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbe.application.model.Gif;
import com.kbe.application.model.GifVoteRatio;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CalculatorApi {

    public GifVoteRatio getCalculation(Gif gif) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        CalculatorRequestBody calculatorRequestBody = new CalculatorRequestBody(gif.getUpvotes(), gif.getDownvotes());
        String bodyString = objectMapper.writeValueAsString(calculatorRequestBody);

        RequestBody requestBody = RequestBody.create(bodyString, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url("http://localhost:8082/api/calculator/calculate")
                .post(requestBody)
                .build();

        Response response = okHttpClient.newCall(request).execute();

        String jsonString = response.body().string();
        CalculatorResponseBody calculatorResponseBody = objectMapper.readValue(jsonString, CalculatorResponseBody.class);

        GifVoteRatio gifVoteRatio = new GifVoteRatio(gif.getId(), calculatorResponseBody.upvoteRate, calculatorResponseBody.downvoteRate);

        return gifVoteRatio;
    }

    public static class CalculatorRequestBody {
        public int upvotes;
        public int downvotes;

        public CalculatorRequestBody(int upvotes, int downvotes) {
            this.upvotes = upvotes;
            this.downvotes = downvotes;
        }
    }

    public static class CalculatorResponseBody {
        public float upvoteRate;
        public float downvoteRate;
    }
}
