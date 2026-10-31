package com.foodbridge.dto;
import jakarta.validation.constraints.*; import java.time.LocalDateTime;
public record DonationRequest(@NotBlank String foodName,@NotBlank String category,@Positive Double quantityKg,
 String foodType,String description,@NotNull LocalDateTime preparedAt,@NotNull LocalDateTime expiryAt,
 @NotBlank String pickupAddress,String city) {}
