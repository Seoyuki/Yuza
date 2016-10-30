package seoyuki.yuza;

import android.graphics.drawable.Drawable;

/**
 * Created by jaewon on 2016-10-02.
 */
public class Student {

    public String name;
    public String address;
    public String content;
    public String image;
    public String wido;
    public String kyungdo;
    public int id;
    public Drawable imgId; // 이미지
    public int searchImgId;

    public Student() {
        super();
    }

    public Student(int id) {
        super();
        this.id = id;
    }

    public Student(Integer searchImgId, String name) {
        this.searchImgId = searchImgId;
        this.name = name;
    }

    public Student(Drawable imgId, String name) {
        this.imgId = imgId;
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Drawable getIcon() {
        return this.imgId;
    }

    public void setIcon(Drawable icon) {
        imgId = icon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWido(String wido) {
        this.wido = wido;
    }

    public String getWido() {
        return wido;
    }

    public void setKyungdo(String kyungdo) {
        this.kyungdo = kyungdo;
    }

    public String getKyungdo() {
        return kyungdo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSearchImgId() {
        return searchImgId;
    }

    public void setSearchImgId(int searchImgId) {
        this.searchImgId = searchImgId;
    }

    public int getId() {
        return id;
    }
}