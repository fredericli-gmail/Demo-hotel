package tw.gov.moda.demohotel.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 繁體中文註解：早餐券發卡使用的輸入資料。
 */
public class BreakfastIssueCommand {

    @NotBlank(message = "VC 模板代碼不可為空白")
    private String vcUid;

    @NotBlank(message = "房間號碼不可為空白")
    @Size(max = 32, message = "房間號碼長度超過限制")
    private String roomNb;

    @Size(max = 32, message = "票券類別長度超過限制")
    private String ticketType;

    @Size(max = 64, message = "餐廳位置長度超過限制")
    private String location;

    @NotBlank(message = "使用日期不可為空白")
    @Size(min = 8, max = 8, message = "使用日期格式錯誤")
    private String validDate;

    private String dataTag;

    @NotBlank(message = "申請人不可為空白")
    @Size(max = 64, message = "申請人名稱長度超過限制")
    private String applicant;

    public String getVcUid() {
        return vcUid;
    }

    public void setVcUid(String vcUid) {
        this.vcUid = vcUid;
    }

    public String getRoomNb() {
        return roomNb;
    }

    public void setRoomNb(String roomNb) {
        this.roomNb = roomNb;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getDataTag() {
        return dataTag;
    }

    public void setDataTag(String dataTag) {
        this.dataTag = dataTag;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }
}
