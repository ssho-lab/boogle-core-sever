package kr.ant.booksharing.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SearchService {

     public boolean search(final String searchKeyword, String str){
        return StringUtils.containsIgnoreCase(searchKeyword, str) || StringUtils.containsIgnoreCase(str, searchKeyword);
    }
}
