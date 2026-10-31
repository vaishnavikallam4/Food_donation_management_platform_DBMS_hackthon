package com.foodbridge.dto;
import jakarta.validation.constraints.*;
public record FoodRequestDto(@NotNull Long donationId,@Positive Double requestedKg,@Positive Integer peopleToServe,
 String purpose,String urgency) {}
