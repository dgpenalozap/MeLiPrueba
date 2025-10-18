package com.example.productcomparison.repository;

import com.example.productcomparison.model.Product;
import com.example.productcomparison.model.ProductDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toDomain(ProductDTO dto);

    ProductDTO toDto(Product product);
}
