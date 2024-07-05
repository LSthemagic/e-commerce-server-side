package com.railansantana.e_commerce.resource.stock;

import com.railansantana.e_commerce.domain.Stock;
import com.railansantana.e_commerce.services.stock.StockService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/stock")
@AllArgsConstructor
public class StockResource {
    private final StockService stockService;

    @GetMapping
    public ResponseEntity<List<Stock>> findAll(){
        return ResponseEntity.ok().body(stockService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> findById(@PathVariable String id){
        return ResponseEntity.ok().body(stockService.findById(id));
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Void> save(@PathVariable String productId){
        Stock res = stockService.save(productId);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(res.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/addProduct/{productId}/{stockId}")
    public ResponseEntity<Void> addProduct(@PathVariable String productId, @PathVariable String stockId){
        Stock res = stockService.findById(stockId);
        stockService.addProductToStock(productId, stockId);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(res.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/removeProduct/{productId}/{stockId}/{quantity}")
    public ResponseEntity<Void> removeProduct(@PathVariable String productId, @PathVariable String stockId, @PathVariable int quantity){
        stockService.removeProductFromStock(productId, stockId, quantity);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{stockId}")
    public ResponseEntity<Void> removeStock(@PathVariable String stockId){
        stockService.deleteStock(stockId);
        return ResponseEntity.noContent().build();
    }



}
