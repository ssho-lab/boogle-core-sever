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
            String[] sellItemIdArray = {"5e6da1719870902d7152f91d",
                    "5e6ce27a9870901e02c4e0f4",
                    "5e6ccfed9870901e02c4e0c4",
                    "5e6ca3aa9870901e02c4e0b9",
                    "5e6ddd1e9870902d7152f933",
                    "5e6db1869870902d7152f923",
                    "5e6de3189870902d7152f937",
                    "5e6ddcab9870902d7152f931",
                    "5e6db2189870902d7152f925",
                    "5e6db2c99870902d7152f927",
                    "5e6de3ff9870902d7152f939",
                    "5e6e02789870902d7152f943",
                    "5e6e317298709048ab4520e8",
                    "5e6dfbc69870902d7152f940",
                    "5e6dfcc29870902d7152f942",
                    "5e6e3e4698709048ab452121",
                    "5e6e3f8798709048ab452127",
                    "5e6e48f598709048ab45212f",
                    "5e6e497398709048ab452134",
                    "5e6e4abc98709048ab452136",
                    "5e6e4b0098709048ab452138",
                    "5e6e4b8098709048ab45213a",
                    "5e6e4bda98709048ab45213c",
                    "5e6e4c8098709048ab452140",
                    "5e6f73b29870905ccf110bb3",
                    "5e6f82019870905ccf110bc1",
                    "5e6f85e49870905ccf110bc7",
                    "5e6fbf189870907137560d84",
                    "5e709adb98709007f30d5222",
                    "5e709d1998709007f30d5228",
                    "5e718b0798709021712bfcf4"};


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
