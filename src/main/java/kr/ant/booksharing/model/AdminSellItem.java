package kr.ant.booksharing.model;

import kr.ant.booksharing.domain.SellItem;
import lombok.Data;

@Data
public class AdminSellItem {
    private SellItem sellItem;
    private String bankAccountInfo;
}
