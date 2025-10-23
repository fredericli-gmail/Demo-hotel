package tw.gov.moda.demohotel.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * 繁體中文註解：保存 VC 憑證的唯一識別與狀態，方便後續管理。
 */
@Entity
@Table(name = "vc_credential")
public class VcCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cid", nullable = false, unique = true, length = 128)
    private String cid;

    @Column(name = "vc_uid", nullable = false, length = 128)
    private String vcUid;

    @Column(name = "credential_status", nullable = false, length = 32)
    private String credentialStatus;

    @Column(name = "issuance_time")
    private LocalDateTime issuanceTime;

    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getVcUid() {
        return vcUid;
    }

    public void setVcUid(String vcUid) {
        this.vcUid = vcUid;
    }

    public String getCredentialStatus() {
        return credentialStatus;
    }

    public void setCredentialStatus(String credentialStatus) {
        this.credentialStatus = credentialStatus;
    }

    public LocalDateTime getIssuanceTime() {
        return issuanceTime;
    }

    public void setIssuanceTime(LocalDateTime issuanceTime) {
        this.issuanceTime = issuanceTime;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
