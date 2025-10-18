package com.example.productcomparison.exception.service;

 import com.example.productcomparison.exception.GlobalExceptionHandler;
 import com.example.productcomparison.service.ProductService;
 import lombok.Getter;

 /**
  * Exception thrown when a rating value is outside the valid range (0.0 - 5.0).
  *
  * <p>This exception is used for product rating validation in the service layer.
  * It's handled in the controller layer and results in a 400 Bad Request response.</p>
  *
  * <p>Rating validation rules:</p>
  * <ul>
  *   <li>Must be between 0.0 and 5.0 inclusive</li>
  *   <li>Null ratings are allowed (optional field)</li>
  * </ul>
  *
  * @see ProductService
  * @see GlobalExceptionHandler
  */
 @Getter
 public class InvalidRatingException extends RuntimeException {

     private final double rating;

     /**
      * Constructs a new InvalidRatingException with the invalid rating value.
      *
      * @param rating the invalid rating value that was provided
      */
     public InvalidRatingException(double rating) {
         super(String.format("Invalid rating value: %.1f. Rating must be between 0.0 and 5.0", rating));
         this.rating = rating;
     }
 }