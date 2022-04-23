package pdp.uz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pdp.uz.entity.*;
import pdp.uz.entity.Number;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.repository.NumberRepo;
import pdp.uz.repository.SimCardRepo;
import pdp.uz.repository.SubscriberRepo;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SimCardService {

    private final NumberRepo numberRepo;

    private final SubscriberRepo subscriberRepo;

    private final SimCardRepo simCardRepo;

    private final ActivityAndActivityType activityAndActivityType;


    public ApiResponse getSubscriberFromNumber(String number) {
        Optional<Subscriber> optionalSubscriber = subscriberRepo.findBySimCard_Number_CodeAndSimCard_Number_Number(Short.parseShort(number.substring(0, 2)), number.substring(2));
        return optionalSubscriber.map(subscriber -> new ApiResponse("OK", true, subscriber)).orElseGet(() -> new ApiResponse("Subscriber not found", false));
    }

    public boolean activity(Subscriber subscriber, ActivityType activityType, double amount) {
        List<ResidueOpportunity> opportunities = subscriber.getOpportunities();
        for (ResidueOpportunity opportunity : opportunities) {
            if (opportunity.isTariff() && opportunity.getKey().equals(activityType.getName())) {
                if (opportunity.getValue() < amount) {
                    amount -= opportunity.getValue();
                    opportunity.setValue(0L);
                } else {
                    opportunity.setValue((long) (opportunity.getValue() - amount));
                    amount = 0;
                    return true;
                }
            }
        }
        if (amount != 0) {
            for (ResidueOpportunity opportunity : opportunities) {
                if (!opportunity.isTariff() && opportunity.getKey().equals(activityType.getName())) {
                    if (opportunity.getValue() < amount) {
                        amount -= opportunity.getValue();
                        opportunity.setValue(0L);
                    } else {
                        opportunity.setValue((long) (opportunity.getValue() - amount));
                        amount = 0;
                        return true;
                    }
                }
            }
        }

        if (amount != 0) {
            for (TariffOpportunity tariffOpportunity : subscriber.getTariff().getTariffOpportunities()) {
                if (!tariffOpportunity.isAdditional() && tariffOpportunity.getKey().equals(activityType.getName())) {
                    double price = Double.parseDouble(tariffOpportunity.getValue()) * amount;
                    if ((subscriber.getSimCard().getBalance() > price || subscriber.isServiceDebt()) && subscriber.getSimCard().getBalance() > -subscriber.getTariff().getPrice()) {
                        decreaseBalance(subscriber.getSimCard().getId(), price, " activity");
                        return true;
                    } else return false;
                }
            }
        }
        return false;
    }

    public void decreaseBalance(Long simCardId, Double price, String reason) {
        SimCard simCard = simCardRepo.getById(simCardId);
        simCard.setBalance(simCard.getBalance() - price);
        ActivityType activityType = activityAndActivityType.createActivityType("DECREASE_BALANCE");
        Map<String, Object> details = new HashMap<>();
        details.put("activityName", activityType.getName());
        details.put("amount", price);
        details.put("other", "Your balance decrease for " + reason);
        activityAndActivityType.createActivity(
                subscriberRepo.findBySimCard_Number_CodeAndSimCard_Number_Number(simCard.getNumber().getCode(), simCard.getNumber().getNumber()).get(),
                activityType, details);
        simCardRepo.save(simCard);
    }

    public ApiResponse getSimCards() {
        return new ApiResponse("Ok", true, simCardRepo.findAllByStatusTrue());
    }

    public ApiResponse getSimCard(Long id) {
        Optional<SimCard> optionalSimCard = simCardRepo.findByIdAndStatusTrue(id);
        return optionalSimCard.map(simCard -> new ApiResponse("OK", true, simCard)).orElseGet(() -> new ApiResponse("SimCard not found", false));
    }

    public ApiResponse reportSimCards() {
        return new ApiResponse("OK", true, simCardRepo.getReport());
    }

    public ApiResponse reportSimCards(String date1, String date2) {
        try {
            LocalDate start = LocalDate.parse(date1);
            LocalDate end = LocalDate.parse(date2);

            Double income = simCardRepo.getIncome(start, end);
            return new ApiResponse("Your income = " + income, true);
        } catch (Exception e) {
            return new ApiResponse("Date format is not correct. (Use YYYY-MM-DD", false);
        }
    }

    public ApiResponse deActivate(Long id) {
        Optional<SimCard> optionalSimCard = simCardRepo.findByIdAndStatusTrue(id);
        if (!optionalSimCard.isPresent())
            return new ApiResponse("SimCard not found", false);
        SimCard simCard = optionalSimCard.get();
        Number number = simCard.getNumber();
        number.setOwned(false);
        numberRepo.save(number);
        simCard.setNumber(null);
        simCard.setStatus(false);
        simCardRepo.save(simCard);
        return new ApiResponse("SimCard deactivated", false);
    }
}
