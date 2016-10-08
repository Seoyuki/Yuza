package seoyuki.yuza;

/**
 * Created by jaewon on 2016-10-02.
 */
public class Student {

    String name;
    String address;
    String content;
    String image;
    String wido;
    String kyungdo;
    int id;

    public Student()
    {
        super();
    }

    public Student(String name, String address, String image, String content,String wido,String kyungdo,int id)
    {
        super();
        this.name = name;
        this.address = address;
        this.image = image;
        this.content = content;
        this.wido = wido;
        this.kyungdo = kyungdo;
        this.id = id;
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


}
