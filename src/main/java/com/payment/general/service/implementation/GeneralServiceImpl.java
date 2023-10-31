package com.payment.general.service.implementation;

import com.payment.general.dto.Response;
import com.payment.general.enums.ResponseCodeAndMessage;
import com.payment.general.service.GeneralService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class GeneralServiceImpl implements GeneralService {

    @Override
    public Response prepareResponse(ResponseCodeAndMessage codeAndMessage, Object data) {
        return getResponse(codeAndMessage.responseCode, codeAndMessage.responseMessage, data);
    }

    @Override
    public Response prepareResponse(String responseCode, String responseMessage, Object data) {
        return getResponse(responseCode, responseMessage, data);
    }

    @Override
    public Pageable getPageableObject(int size, int page) {
        log.info("Getting pageable object, initial size => {} and page {}", size, page);

        Pageable paged;

        if (size > 100) {
            log.info("{} greater than max size of {}, defaulting to max", size, 100);

            size = 100;
        }

        if (size > 0 && page >= 0) {
            paged = PageRequest.of(page, size, Sort.by("id").descending());
        } else {
            paged = PageRequest.of(0, size, Sort.by("id").descending());
        }

        return paged;
    }


    @Override
    public Pageable getPageableObject(int size, int page, Sort sort) {
        log.info("Getting pageable object, initial size => {} and page {}", size, page);

        Pageable paged;

        if (size > 100) {
            log.info("{} greater than max size of {}, defaulting to max", size, 100);

            size = 100;
        }

        if (size > 0 && page >= 0) {
            paged = PageRequest.of(page, size, sort);
        } else {
            paged = PageRequest.of(0, size, sort);
        }

        return paged;
    }

    @Override
    public void createDTOFromModel(Object from, Object to) {
        BeanUtils.copyProperties(from, to);
    }


    private Response getResponse(String responseCode, String responseMessage, Object data) {
        Response response = new Response();
        response.setResponseCode(responseCode);
        response.setResponseMessage(responseMessage);
        response.setData(data);

        log.info("ResponseCode => {}, message => {} and data => {}", responseCode, responseMessage, data);

        return response;
    }

}
