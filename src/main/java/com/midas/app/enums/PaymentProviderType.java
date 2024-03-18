package com.midas.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Enum representing payment provider types. */
@AllArgsConstructor
@Getter
public enum PaymentProviderType {
  /** Stripe payment provider. */
  STRIPE("stripe");

  /** The name of the payment provider. */
  private final String providerName;
}
