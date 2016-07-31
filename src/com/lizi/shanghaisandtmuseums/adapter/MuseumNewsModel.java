package com.lizi.shanghaisandtmuseums.adapter;


public class MuseumNewsModel {
    private String title;
    private String museum;
    private String category;
    private String time;
    private String content_url;
    private String pic_url;
    public MuseumNewsModel(String title,String museum,String category,String time,String content_url,String pic_url){
        setTitle(title);
        setMuseum(museum);
        setCategory(category);
        setTime(time);
        setContent_url(content_url);
        setPic_url(pic_url);
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent_url() {
		return content_url;
	}
	public void setContent_url(String content_url) {
		this.content_url = content_url;
	}
	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public String getMuseum() {
		return museum;
	}
	public void setMuseum(String museum) {
		this.museum = museum;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
}