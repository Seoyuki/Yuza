package seoyuki.yuza;


public class YuzaRanking {

    int tid;
    int yuza_id;
    String name;
    String ret_date;
    String ret_time;
    float ret_km;


    public YuzaRanking() {
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getYuza_id() {
        return yuza_id;
    }

    public void setYuza_id(int yuza_id) {
        this.yuza_id = yuza_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRet_date() {
        return ret_date;
    }

    public void setRet_date(String ret_date) {
        this.ret_date = ret_date;
    }

    public String getRet_time() {
        return ret_time;
    }

    public void setRet_time(String ret_time) {
        this.ret_time = ret_time;
    }

    public float getRet_km() {
        return ret_km;
    }

    public void setRet_km(float ret_km) {
        this.ret_km = ret_km;
    }
}
