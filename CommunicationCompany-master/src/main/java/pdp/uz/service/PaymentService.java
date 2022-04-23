package pdp.uz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pdp.uz.entity.ActivityType;
import pdp.uz.entity.Payment;
import pdp.uz.entity.Subscriber;
import pdp.uz.entity.enums.PaymentType;
import pdp.uz.payload.PaymentDto;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.repository.PaymentRepo;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepo paymentRepo;

    private final SimCardService simCardService;

    private final ActivityAndActivityType activityAndActivityType;

    public ApiResponse getAll() {
        return new ApiResponse("OK", true, paymentRepo.findAll());
    }

    public ApiResponse get(Long id) {
        Optional<Payment> optionalPayment = paymentRepo.findById(id);
        return optionalPayment.map(payment -> new ApiResponse("OK", true, payment)).orElseGet(() -> new ApiResponse("Payment not found", false));
    }

    public ApiResponse create(PaymentDto dto) {
        try {
            ApiResponse apiResponse = simCardService.getSubscriberFromNumber(dto.getNumber());
            if (!apiResponse.isStatus())
                return new ApiResponse("Number not found", false);
            Subscriber subscriber = (Subscriber) apiResponse.getObject();
            Payment payment = new Payment();
            payment.setPaymentType(PaymentType.valueOf(dto.getPaymentType()));
            payment.setPayer(dto.getPayer());
            payment.setSubscriber(subscriber);
            payment.setAmount(dto.getAmount());

            ActivityType activityType = activityAndActivityType.createActivityType("PAYMENT");

            Map<String, Object> details = new HashMap<>();
            details.put("activityName", activityType.getName());
            details.put("amount", dto.getAmount());
            details.put("other", "Money was transferred to " + subscriber.getSimCard().getNumber().getFullNumber());

            activityAndActivityType.createActivity(subscriber, activityType, details);

            paymentRepo.save(payment);
            return new ApiResponse("Saved", true);
        } catch (Exception e) {
            return new ApiResponse("Payment type is given incorrect", false);
        }
    }
}
