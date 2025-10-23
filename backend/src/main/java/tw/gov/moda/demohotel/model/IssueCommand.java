package tw.gov.moda.demohotel.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 繁體中文註解：封裝前端呼叫發卡服務時的輸入資料。
 */
public class IssueCommand {

    @NotBlank(message = "房客姓名不可為空白")
    @Size(max = 64, message = "房客姓名長度超過限制")
    private String guestName;

    @NotBlank(message = "房號不可為空白")
    @Size(max = 32, message = "房號長度超過限制")
    private String roomNumber;

    @NotBlank(message = "入住日期不可為空白")
    @Size(min = 8, max = 8, message = "入住日期格式錯誤")
    private String checkInDate;

    @NotBlank(message = "退房日期不可為空白")
    @Size(min = 8, max = 8, message = "退房日期格式錯誤")
    private String checkOutDate;

    @NotBlank(message = "VC 模板代碼不可為空白")
    private String vcUid;

    private String dataTag;

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getVcUid() {
        return vcUid;
    }

    public void setVcUid(String vcUid) {
        this.vcUid = vcUid;
    }

    public String getDataTag() {
        return dataTag;
    }

    public void setDataTag(String dataTag) {
        this.dataTag = dataTag;
    }
}
