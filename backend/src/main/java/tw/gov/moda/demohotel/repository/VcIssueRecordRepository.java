package tw.gov.moda.demohotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.gov.moda.demohotel.domain.VcIssueRecord;

import java.util.Optional;

/**
 * 繁體中文註解：提供 VcIssueRecord 的基本 CRUD 能力。
 */
public interface VcIssueRecordRepository extends JpaRepository<VcIssueRecord, Long> {

    Optional<VcIssueRecord> findByTransactionId(String transactionId);

    java.util.List<VcIssueRecord> findByVcUidAndRoomNbOrderByCreatedAtDesc(String vcUid, String roomNb);
}
