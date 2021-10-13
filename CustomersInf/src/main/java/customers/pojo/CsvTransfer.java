package customers.pojo;


import java.util.ArrayList;
import java.util.List;

public class CsvTransfer {

    private List<CsvBean> csvList;

    public CsvTransfer() {}

    public void setCsvList(List<CsvBean> csvList) {
        this.csvList = csvList;
    }

    public List<CsvBean> getCsvList() {
        if (csvList != null) return csvList;
        return new ArrayList<CsvBean>();
    }
}