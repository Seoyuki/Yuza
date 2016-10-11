package seoyuki.yuza;


public class YuzaRanking {

    int tid;
    int yuza_id;
    String name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
