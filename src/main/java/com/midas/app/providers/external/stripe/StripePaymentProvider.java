package com.midas.app.providers.external.stripe;

import com.midas.app.enums.PaymentProviderType;
import com.midas.app.models.Account;
import com.midas.app.providers.payment.PaymentProvider;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class StripePaymentProvider implements PaymentProvider {
  private final Logger logger = LoggerFactory.getLogger(StripePaymentProvider.class);
  private final StripeConfiguration configuration;

  /** providerName is the name of the payment provider */
  @Override
  public String providerName() {
    return PaymentProviderType.STRIPE.getProviderName();
  }

  /**
   * createAccount creates a new account in the payment provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Override
  public Account createAccount(Account details) {
    setAPIKey();

    try {
      CustomerCreateParams params =
          CustomerCreateParams.builder()
              .setName(details.getFirstName() + " " + details.getLastName())
              .setEmail(details.getEmail())
              .build();

      Customer customer = Customer.create(params);

      details.setProviderId(customer.getId());
      details.setProviderType(PaymentProviderType.STRIPE);
      return details;

    } catch (StripeException stripeException) {
      logger.error("Stripe error: {}", stripeException.getMessage());
    } catch (Exception exception) {
      logger.error(exception.getMessage());
    }

    return null;
  }

  /**
   * updateAccount updates an existing account in the payment provider.
   *
   * @param details is the details of the account to be updated.
   * @return Account
   */
  @Override
  public Account updateAccount(Account details) {
    setAPIKey();
    try {
      Customer existingCustomer = Customer.retrieve(details.getProviderId());
      if (Objects.isNull(existingCustomer)) return createAccount(details);

      String fullName = details.getFirstName() + " " + details.getLastName();

      CustomerUpdateParams params =
          CustomerUpdateParams.builder().setName(fullName).setEmail(details.getEmail()).build();

      existingCustomer.update(params);

      details.setProviderId(existingCustomer.getId());
      details.setProviderType(PaymentProviderType.STRIPE);
      return details;

    } catch (StripeException stripeException) {
      logger.error("Stripe error: {}", stripeException.getMessage());
    } catch (Exception exception) {
      logger.error(exception.getMessage());
    }

    return null;
  }

  /**
   * Setting API Key for Stripe for the current method.
   */
  private void setAPIKey() {
    Stripe.apiKey = configuration.getApiKey();
  }
}
