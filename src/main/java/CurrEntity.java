import java.util.Calendar;
import java.util.regex.Pattern;

public class CurrEntity {
    private String curr;
    private String spotBuying;
    private String spotSelling;
    private String cashBuying;
    private String cashSelling;
    private String timeStamp = Singleton.getDateFormat().format(Calendar.getInstance().getTime());

    public String getCurr() {
        curr = curr.replaceAll("[^a-zA-Z]*", "").toUpperCase();
        return curr;
    }

    public void setCurr(String curr) {
        this.curr = curr;
    }


    public String getSpotBuying() {
        return Pattern.compile("^[0-9.]").matcher(spotBuying).matches() ? null : spotBuying;
    }

    public void setSpotBuying(String spotBuying) {
        this.spotBuying = spotBuying;
    }

    public String getSpotSelling() {
        return Pattern.compile("^[0-9.]").matcher(spotSelling).matches() ? null : spotSelling;
    }

    public void setSpotSelling(String spotSelling) {
        this.spotSelling = spotSelling;
    }

    public String getCashBuying() {
        return Pattern.compile("[^0-9.]").matcher(cashBuying).matches() ? null : cashBuying;
    }

    public void setCashBuying(String cashBuying) {
        this.cashBuying = cashBuying;
    }

    public String getCashSelling() {
        return Pattern.compile("[^0-9.]").matcher(cashSelling).matches() ? null : cashSelling;
    }

    public void setCashSelling(String cashSelling) {
        this.cashSelling = cashSelling;
    }


    // 複寫toString 作為Sql插入關鍵字
    @Override
    public String toString() {
        return "CurrEntity{" +
                "curr='" + getCurr() + '\'' +
                ", spotBuying=" + getSpotBuying() +
                ", spotSelling=" + getSpotSelling() +
                ", cashBuying=" + getCashBuying() +
                ", cashSelling=" + getCashSelling() +
                ", timestamp = " + getTimeStamp() +
                '}';
    }

    public String genInserSql(String tableName) {
        return String.format("Insert into %s (%s,%s,%s,%s,%s,%s) values ('%s','%s','%s','%s','%s','%s');",
                tableName, "CURR", "SPOT_BUYING", "SPOT_SELLING", "CASH_BUYING", "CASH_SELLING", "TIMESTAMP",
                getCurr(), getSpotBuying(), getSpotSelling(), getCashBuying(), getCashSelling(), timeStamp);
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
