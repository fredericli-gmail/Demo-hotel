package tw.gov.moda.demohotel.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 繁體中文註解：封裝前端呼叫發卡服務時的輸入資料。
 */
public class IssueCommand {

    @NotBlank(message = "房間號碼不可為空白")
    @Size(max = 32, message = "房間號碼長度超過限制")
    private String roomNb;

    @Size(max = 32, message = "房間型態長度超過限制")
    private String roomType;

    @Size(max = 64, message = "備註長度超過限制")
    private String roomMemo;

    @NotBlank(message = "入住日期不可為空白")
    @Size(min = 8, max = 8, message = "入住日期格式錯誤")
    private String checkInDate;

    @NotBlank(message = "退房日期不可為空白")
    @Size(min = 8, max = 8, message = "退房日期格式錯誤")
    private String checkOutDate;

    @NotBlank(message = "VC 模板代碼不可為空白")
    private String vcUid;

    private String dataTag;

    public String getRoomNb() {
        return roomNb;
    }

    public void setRoomNb(String roomNb) {
        this.roomNb = roomNb;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomMemo() {
        return roomMemo;
    }

    public void setRoomMemo(String roomMemo) {
        this.roomMemo = roomMemo;
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
