package sorochinskiy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ImgEntry {

    private Integer eventCode;
    private String hwAddress;
    private Calendar time;
    private String md5Hex;

    public ImgEntry() {
    }

    public ImgEntry(Integer eventCode, String hwAddress, Calendar time, String md5Hex) {
        this.eventCode = eventCode;
        this.hwAddress = hwAddress;
        this.time = time;
        this.md5Hex = md5Hex;
    }

    @Override
    public String toString() {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "{" +
                "eventCode=" + eventCode +
                ", hwAddress='" + hwAddress + '\'' +
                ", time=" + formatter.format(time.getTime()) +
                ", md5Hex='" + md5Hex + '\'' +
                '}';
    }

    public Integer getEventCode() {
        return eventCode;
    }

    public void setEventCode(Integer eventCode) {
        this.eventCode = eventCode;
    }

    public String getHwAddress() {
        return hwAddress;
    }

    public void setHwAddress(String hwAddress) {
        this.hwAddress = hwAddress;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public String getMd5Hex() {
        return md5Hex;
    }

    public void setMd5Hex(String md5Hex) {
        this.md5Hex = md5Hex;
    }
}
