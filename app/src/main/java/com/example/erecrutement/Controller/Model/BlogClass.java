package com.example.erecrutement.Controller.Model;

public class BlogClass {
    private long Id;
    private String Title ;
    private String Description ;
    private String imageUrl;
    public BlogClass(String title, String description) {
        Title = title;
        Description = description;
    }

    public BlogClass(long id, String title, String description) {
        Id = id;
        Title = title;
        Description = description;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public BlogClass() {
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void setDescription(String description) {
        Description = description;
    }

    @Override
    public String toString() {
        return "BlogClass{" +
                "Title='" + Title + '\'' +
                '}';
    }
}
