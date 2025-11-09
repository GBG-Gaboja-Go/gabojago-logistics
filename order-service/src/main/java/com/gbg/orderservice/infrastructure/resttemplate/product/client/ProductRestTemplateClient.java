package com.gbg.orderservice.infrastructure.resttemplate.product.client;

import com.gabojago.dto.BaseResponseDto;
import com.gabojago.exception.AppException;
import com.gbg.orderservice.infrastructure.resttemplate.product.dto.response.ProductResponseDto;
import com.gbg.orderservice.presentation.advice.OrderErrorCode;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductRestTemplateClient {

    private static final String PRODUCT_SERVICE_BASE_URL = "http://product-service";
    private static final String GET_PRODUCT_URL =
        PRODUCT_SERVICE_BASE_URL + "/v1/products/{productId}";

    private static final ParameterizedTypeReference<BaseResponseDto<ProductResponseDto>> RES_GET_PRODUCT_TYPE =
        new ParameterizedTypeReference<>() {
        };

    private final RestTemplate restTemplate;

    public ProductResponseDto getProduct(UUID productId) {
        try {
            ResponseEntity<BaseResponseDto<ProductResponseDto>> responseEntity = restTemplate.exchange(
                GET_PRODUCT_URL,
                HttpMethod.GET,
                null,
                RES_GET_PRODUCT_TYPE,
                productId
            );
            return extractData(responseEntity);
        } catch (HttpStatusCodeException exception) {
            throw mapException(exception);
        } catch (RestClientException exception) {
            throw new AppException(OrderErrorCode.ORDER_BAD_REQUEST);
        }
    }

    private <T> T extractData(ResponseEntity<BaseResponseDto<T>> responseEntity) {
        BaseResponseDto<T> body = responseEntity.getBody();
        if (body.getData() == null) {
            throw new AppException(OrderErrorCode.ORDER_BAD_REQUEST);
        }
        return body.getData();
    }

    private AppException mapException(HttpStatusCodeException exception) {
        HttpStatusCode status = exception.getStatusCode();

        // productId로 상품을 찾지 못했을 경우 exception 처리
        if (status == HttpStatus.NOT_FOUND) {
            return new AppException(OrderErrorCode.ORDER_PRODUCT_NOT_FOUND);
        }

        // 재고가 0인 경우 exception 처리
        if (status == HttpStatus.BAD_REQUEST || status == HttpStatus.CONFLICT) {
            return new AppException(OrderErrorCode.ORDER_PRODUCT_OUT_OF_STOCK);
        }

        // 응답 바디가 유의미하면 로그로 남기고(디버그용) 기본 매핑으로 처리
        String responseBody = exception.getResponseBodyAsString();
        if (StringUtils.hasText(responseBody)) {
            log.debug("Product service error body: {}", responseBody);
        }

        return new AppException(OrderErrorCode.ORDER_BAD_REQUEST);
    }
}
