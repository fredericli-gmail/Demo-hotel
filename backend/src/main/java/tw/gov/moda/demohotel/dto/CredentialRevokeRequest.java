package tw.gov.moda.demohotel.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 繁體中文註解：撤銷憑證所需資訊，由前端提供。 
 */
public class CredentialRevokeRequest {

    @NotBlank
    private String vcUid;

    @NotBlank
    private String roomNb;

    private String roomType;

    private String roomMemo;

    private String ticketType;

    private String location;

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
}
