package com.railansantana.e_commerce.domain;

import com.google.gson.Gson;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
public class Payment {
    private static final Logger logger = LoggerFactory.getLogger(Payment.class);

    @Value(value = "${spring.prod.access.token.mp}")
    private String prodSecret;

    @PostConstruct
    public void init() {
        System.out.println("Valor injetado: " + prodSecret);
    }

    public Preference configMercadoPago(Product product, int quantity) throws MPApiException, MPException {
        try {
            logger.info("Configuring Mercado PAGO...");
            logger.info("secret {}", prodSecret);
            MercadoPagoConfig.setAccessToken(prodSecret);
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id(product.getId())
                    .title(product.getName())
                    .description(product.getDescription())
                    .pictureUrl(product.getImage())
                    .categoryId(product.getCategory())
                    .quantity(quantity)
                    .currencyId("BRL")
                    .unitPrice(BigDecimal.valueOf(product.getPrice()))
                    .build();
            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items).build();
            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);
            return preference;
        } catch (MPApiException e) {
            logger.error("MPApiException: {} - {}", e.getStatusCode(), e.getMessage());
            logger.error("Response: {}", e.getApiResponse());
            throw e;
        } catch (MPException e) {
            logger.error("MPException: {}", e.getMessage());
            throw e;
        }
    }

    public String convertObjectToJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
}

