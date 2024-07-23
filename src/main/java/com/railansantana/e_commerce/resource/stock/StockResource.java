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

    @PostMapping("/{productId}/{quantity}")
    public ResponseEntity<Void> save(@PathVariable String productId, @PathVariable Integer quantity){
        Stock res = stockService.save(productId, quantity);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(res.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/addProduct/{productId}/{stockId}/{quantity}")
    public ResponseEntity<Void> addProduct(@PathVariable String productId, @PathVariable String stockId, @PathVariable Integer quantity){
        Stock res = stockService.findById(stockId);
        stockService.addQuantityProducts(productId, stockId, quantity);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(res.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/removeProduct/{productId}/{stockId}/{quantity}")
    public ResponseEntity<Void> removeProduct(@PathVariable String productId, @PathVariable String stockId, @PathVariable int quantity){
        stockService.removeQuantityProductFromStock(productId, stockId, quantity);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{stockId}")
    public ResponseEntity<Void> removeStock(@PathVariable String stockId){
        stockService.deleteStock(stockId);
        return ResponseEntity.noContent().build();
    }



}
