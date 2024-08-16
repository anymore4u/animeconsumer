package com.example.animeconsumer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Anime {

    private String id;

    @JsonProperty("mal_id")
    private String malId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("synopsis")
    private String synopsis;

    @JsonProperty("score")
    private Double score;

    @JsonProperty("members")
    private Integer members;

    @JsonProperty("broadcast")
    private Broadcast broadcast;

    @JsonProperty("studios")
    private Studio[] studios;

    @JsonProperty("images")
    private Images images;

    @JsonProperty("url")
    private String url;

    @JsonProperty("status")
    private String status;

    private long offset;
}

@Data
class Broadcast {
    private String day;
    private String time;
    private String timezone;
    private String string;
}

@Data
class Studio {
    private String name;
}

@Data
class Images {
    private ImageType jpg;
    private ImageType webp;
}

@Data
class ImageType {
    private String image_url;
    private String small_image_url;
    private String large_image_url;
}