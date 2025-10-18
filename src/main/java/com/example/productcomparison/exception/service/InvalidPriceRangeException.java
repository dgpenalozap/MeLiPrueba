package com.example.productcomparison.exception.service;

 import com.example.productcomparison.service.ProductService;
 import lombok.Getter;

 /**
  * Exception thrown when price range parameters are invalid.
  *
  * <p>Handles validation failures for price values in service operations.
  * Used when prices don't meet business requirements.</p>
  *
  * <p>Validation rules:</p>
  * <ul>
  *   <li>Prices must be non-negative (>= 0)</li>
  *   <li>Minimum price must not exceed maximum price</li>
  * </ul>
  *
  * @see ProductService
  */
 @Getter
 public class InvalidPriceRangeException extends RuntimeException {

     private final double minPrice;
     private final double maxPrice;

     /**
      * Constructs a new InvalidPriceRangeException with price values and reason.
      *
      * @param minPrice the minimum price value (or single price in non-range validations)
      * @param maxPrice the maximum price value (or 0 in non-range validations)
      * @param reason explanation of why the price range is invalid
      */
     public InvalidPriceRangeException(double minPrice, double maxPrice, String reason) {
         super(String.format("Invalid price range [%.2f, %.2f]: %s", minPrice, maxPrice, reason));
         this.minPrice = minPrice;
         this.maxPrice = maxPrice;
     }
 }