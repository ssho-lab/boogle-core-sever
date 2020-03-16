package kr.ant.booksharing.domain;

import kr.ant.booksharing.model.SemesterSubject;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("paybacked_sell_item")
public class PaybackedSellItem {
    private String _id;

    private String sellItemId;
}
