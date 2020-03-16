package kr.ant.booksharing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ant.booksharing.domain.PaybackedSellItem;
import kr.ant.booksharing.domain.SellItem;
import kr.ant.booksharing.model.ImageFileReq;
import kr.ant.booksharing.model.SellItemReq;
import kr.ant.booksharing.repository.PaybackedSellItemRepository;
import kr.ant.booksharing.service.SellItemService;
import kr.ant.booksharing.utils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static kr.ant.booksharing.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("sell")
public class SellItemController {
    private final SellItemService sellItemService;
    private final PaybackedSellItemRepository paybackedSellItemRepository;

    public SellItemController(final SellItemService sellItemService, final PaybackedSellItemRepository paybackedSellItemRepository) {
        this.sellItemService = sellItemService;
        this.paybackedSellItemRepository = paybackedSellItemRepository;
    }

    /**
     * 판매 상품 조회
     *
     * @return ResponseEntity
     */
    @GetMapping("")
    public ResponseEntity getAllSellItemsByItemId(@RequestParam(value="itemId", defaultValue="") String itemId) {
        try {
            return new ResponseEntity<>(sellItemService.findAllSellItemsByItemId(itemId), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 판매 상품 전체 조회
     *
     * @return ResponseEntity
     */
    @GetMapping("/all")
    public ResponseEntity getAllSellItems() {
        try {
            return new ResponseEntity<>(sellItemService.findAllSellItems(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 판매 상품 상세 조회
     *
     * @return ResponseEntity
     */
    @GetMapping("/detail")
    public ResponseEntity getSellItem(@RequestParam(value="id", defaultValue="") String id,
                                      @RequestHeader("Authorization") String token) {
        try {
            return new ResponseEntity<>(sellItemService.findSellItem(token, id), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 판매 상품 등록
     *
     * @return ResponseEntity
     */
    @Auth
    @PostMapping("")
    public ResponseEntity saveItem(final SellItemReq sellItemReq,
                                   @RequestPart(value="imageFileList", required = false)
                                   final List<MultipartFile> imageFileList,
                                   final HttpServletRequest httpServletRequest) {
        try {
            final int userIdx = (int) httpServletRequest.getAttribute("userIdx");
            ObjectMapper objectMapper = new ObjectMapper();
            SellItem sellItem = objectMapper.readValue(sellItemReq.getSellItemString(), SellItem.class);
            sellItem.setSellerId(userIdx);
            return new ResponseEntity<>(sellItemService.saveItem(sellItem,
                   imageFileList), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 판매 상품 삭제
     *
     * @return ResponseEntity
     */
    @Auth
    @DeleteMapping("")
    public ResponseEntity deleteItem(final String sellItemId,
                                   final HttpServletRequest httpServletRequest) {
        try {
            final int userIdx = (int) httpServletRequest.getAttribute("userIdx");
            return new ResponseEntity<>(sellItemService.deleteItem(sellItemId, userIdx),HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 선 지급 완료 판매 상품인지 확인
     *
     * @return ResponseEntity
     */
    @GetMapping("/payback")
    public ResponseEntity checkPaybackedItem(@RequestParam("sellItemId") String sellItemId) {
        try {
            return new ResponseEntity<>(sellItemService.checkPaybackedItem(sellItemId),HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 선 지급 완료 판매 상품인지 확인
     *
     * @return ResponseEntity
     */
    @PostMapping("/payback")
    public ResponseEntity registerPaybackedItem() {
        try {
            String[] sellItemIdArray = {"5e6da1719870902d7152f91d","5e6ce27a9870901e02c4e0f4", "5e6ccfed9870901e02c4e0c4", "5e6ca3aa9870901e02c4e0b9",
            "5e6db1869870902d7152f923", "5e6db2189870902d7152f925", "5e6db2c99870902d7152f927", "5e6ddcab9870902d7152f931", "5e6de3189870902d7152f937",
            "5e6de3ff9870902d7152f939", "5e6e02789870902d7152f943"};

            List<PaybackedSellItem> paybackedSellItemList = new ArrayList<>();

            for(int i = 0; i < sellItemIdArray.length; i++){
                PaybackedSellItem paybackedSellItem = new PaybackedSellItem();
                paybackedSellItem.setSellItemId(sellItemIdArray[i]);
                paybackedSellItemList.add(paybackedSellItem);
            }

            return new ResponseEntity<>(paybackedSellItemRepository.saveAll(paybackedSellItemList),HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }
}
