package kr.ant.booksharing.repository;

import com.mongodb.lang.Nullable;
import kr.ant.booksharing.domain.Transaction;
import kr.ant.booksharing.domain.TransactionHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionHistoryRepository extends MongoRepository<TransactionHistory, String> {
    void deleteBySellItemId(final String sellItemId);

    @Nullable
    List<TransactionHistory> findAll();
}
