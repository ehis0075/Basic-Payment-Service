package com.payment.general.service;

import com.payment.general.dto.Response;
import com.payment.general.enums.ResponseCodeAndMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;

public interface GeneralService {

    //used to format response body
    Response prepareResponse(ResponseCodeAndMessage codeAndMessage, Object data);

    Response prepareResponse(String responseCode, String responseMessage, Object data);

    Pageable getPageableObject(int size, int page);

    Pageable getPageableObject(int size, int page, Sort sort);

    void createDTOFromModel(Object from, Object to);
}
