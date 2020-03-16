package kr.ant.booksharing.model;

import kr.ant.booksharing.domain.Transaction;
import lombok.Data;

@Data
public class AdminTransaction {
    private Transaction transaction;
    private String accountInfo;
}
