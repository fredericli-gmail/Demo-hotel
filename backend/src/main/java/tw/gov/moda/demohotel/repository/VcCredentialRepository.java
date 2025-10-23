package tw.gov.moda.demohotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.gov.moda.demohotel.domain.VcCredential;

import java.util.Optional;

/**
 * 繁體中文註解：提供 VC 憑證資訊的資料存取服務。
 */
public interface VcCredentialRepository extends JpaRepository<VcCredential, Long> {

    Optional<VcCredential> findByCid(String cid);
}
