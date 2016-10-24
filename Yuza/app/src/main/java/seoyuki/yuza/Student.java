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
    public Student()
    {
        super();
    }

    public Student(int id)
    {
        super();
        this.name = name;
        this.address = address;
        this.image = image;
        this.content = content;
        this.wido = wido;
        this.kyungdo = kyungdo;
        this.id = id;
        this.imgId = imgId;
    }

    public String getImage() { return image; }

    public void setImage(String image) {this.image = image;}



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

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name + "\n" + address;
    }
}
