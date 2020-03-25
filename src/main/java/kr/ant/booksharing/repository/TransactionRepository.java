package kr.ant.booksharing.repository;

import com.mongodb.lang.Nullable;
import kr.ant.booksharing.domain.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    Optional<Transaction> findBySellItemId(final String sellItemId);
    Optional<List<Transaction>> findAllBySellerId(final int sellerId);
    Optional<List<Transaction>> findAllByBuyerId(final int buyerId);
    void deleteBySellItemId(final String sellItemId);
    Optional<List<Transaction>> findAllByStepEquals(final int step);

    @Nullable
    List<Transaction> findAll();
}
