package com.beautyline.polimi.service.stripe;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripeService {
	@Value("STRIPE_SECRET_KEY") String STRIPE_SECRET_KEY;

	static void validateIntent(PaymentIntent intent) {
		switch (intent.getStatus()) {
			case "succeeded":
				break;
			case "requires_action":
			case "requires_source_action":
				throw new IllegalStateException(String.format("3D Secure required for: %s", intent.getId()));
			case "requires_payment_method":
			case "requires_source":
				throw new IllegalStateException("Your card was denied");
			default:
				throw new IllegalStateException("Invalid transaction");
		}
	}

	public void pay(BigDecimal amount, String paymentId, Boolean isIntentId) {
		Stripe.apiKey = STRIPE_SECRET_KEY;

		PaymentIntent intent;
		try {
			if (isIntentId) {
				// Confirm the PaymentIntent to collect the money
				intent = PaymentIntent.retrieve(paymentId);
				if (!intent.getAmount().equals(amount.movePointRight(2).longValue())) {
					throw new IllegalStateException("Invalid transaction");
				}
				intent = intent.confirm();
			} else {
				// Create new PaymentIntent for the order
				PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
					.setCurrency("EUR").setAmount(amount.movePointRight(2).longValue())
					.setPaymentMethod(paymentId)
					.setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
					.setCaptureMethod(PaymentIntentCreateParams.CaptureMethod.MANUAL).setConfirm(true).build();
				// Create a PaymentIntent with the order amount and currency
				intent = PaymentIntent.create(createParams);
			}
		} catch (Exception e) {
			throw new IllegalStateException("Invalid transaction");
		}

		validateIntent(intent);
	}
}
