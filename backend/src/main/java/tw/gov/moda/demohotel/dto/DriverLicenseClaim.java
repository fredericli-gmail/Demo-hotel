package tw.gov.moda.demohotel.dto;

/**
 * 繁體中文註解：駕照驗證結果中的欄位。
 */
public class DriverLicenseClaim {

    private String ename;
    private String label;
    private String value;

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
