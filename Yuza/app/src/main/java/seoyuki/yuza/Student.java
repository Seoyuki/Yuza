package seoyuki.yuza;

/**
 * Created by jaewon on 2016-10-02.
 */
public class Student {

    String name;
    String address;
    String content;
    String image;

    public Student()
    {
        super();
    }

    public Student(String name, String address, String image, String content)
    {
        super();
        this.name = name;
        this.address = address;
        this.image = image;
        this.content = content;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
