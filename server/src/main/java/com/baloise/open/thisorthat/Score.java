package com.baloise.open.thisorthat;

public class Score {
    private String imageId;
    private String file;
    private Integer score;

    public Score(String imageId, String file, Integer score) {
        this.imageId = imageId;
        this.file = file;
        this.score = score;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}