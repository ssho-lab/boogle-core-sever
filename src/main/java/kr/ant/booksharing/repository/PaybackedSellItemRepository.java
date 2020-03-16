package kr.ant.booksharing.repository;

import kr.ant.booksharing.domain.OpenedSubject;
import kr.ant.booksharing.domain.PaybackedSellItem;
import kr.ant.booksharing.domain.SellItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PaybackedSellItemRepository extends MongoRepository<PaybackedSellItem, String> {
    Optional<PaybackedSellItem> findBySellItemId(String sellItemId);
}
