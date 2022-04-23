package pdp.uz.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pdp.uz.entity.Tariff;
import pdp.uz.entity.TariffOpportunity;
import pdp.uz.entity.enums.ClientType;
import pdp.uz.entity.enums.TariffType;
import pdp.uz.payload.TariffDto;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.repository.TariffRepo;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TariffService {

    private final TariffRepo tariffRepo;

    public ApiResponse getTariffs() {
        return new ApiResponse("OK", true, tariffRepo.findAllByActiveTrue());
    }

    public ApiResponse getTariff(Long id) {
        Optional<Tariff> optionalTariff = tariffRepo.findByIdAndActiveTrue(id);
        return optionalTariff.map(tariff -> new ApiResponse("OK", true, tariff)).orElseGet(() -> new ApiResponse("Tariff not found", false));
    }

    public ApiResponse createTariff(TariffDto dto) {
        try {
            if (tariffRepo.existsByName(dto.getName())) {
                return new ApiResponse("This tariff has already existed", false);
            }
            Tariff tariff = new Tariff();
            List<TariffOpportunity> opportunities = new ArrayList<>();
            Map<String, Map<String, String>> map = new Gson().fromJson(dto.getOpportunities(), Map.class);
            map.forEach((s, longDoubleMap) -> {
                longDoubleMap.forEach((s1, s2) -> {
                    TariffOpportunity opportunity = new TariffOpportunity();
                    opportunity.setTariff(tariff);
                    opportunity.setKey(s);
                    opportunity.setAdditional(Boolean.parseBoolean(s1));
                    opportunity.setValue(s2);
                    opportunities.add(opportunity);
                });
            });

            tariff.setTariffType(TariffType.valueOf(dto.getTariffType()));
            tariff.setClientType(ClientType.valueOf(dto.getClientType()));
            tariff.setConnectPrice(dto.getConnectPrice());
            tariff.setName(dto.getName());
            tariff.setPrice(dto.getPrice());
            tariff.setDescription(dto.getDescription());
            tariff.setTariffOpportunities(opportunities);
            tariffRepo.save(tariff);
            return new ApiResponse("Created", true);
        } catch (Exception e) {
            return new ApiResponse("An error variant of the map was given, Please look to example in TariffDto.class", false);
        }
    }

    public ApiResponse editTariff(Long id, TariffDto dto) {
        try {
            if (tariffRepo.existsByNameAndIdNot(dto.getName(), id)) {
                return new ApiResponse("This tariff has already existed", false);
            }
            Optional<Tariff> optionalTariff = tariffRepo.findByIdAndActiveTrue(id);
            if (!optionalTariff.isPresent()) {
                return new ApiResponse("Tariff not found", false);
            }
            Map<String, Map<String, String>> map = new Gson().fromJson(dto.getOpportunities(), Map.class);
            Tariff tariff = optionalTariff.get();
            List<TariffOpportunity> opportunities = tariff.getTariffOpportunities();

            map.forEach((s, stringStringMap) -> {
                stringStringMap.forEach((s1, s2) -> {
                    boolean exist = false;
                    for (TariffOpportunity opportunity : opportunities) {
                        if (opportunity.getKey().equals(s) && opportunity.isAdditional() == Boolean.parseBoolean(s1)) {
                            opportunity.setValue(s2);
                            exist = true;
                        }
                    }
                    if (!exist) {
                        TariffOpportunity opportunity = new TariffOpportunity();
                        opportunity.setTariff(tariff);
                        opportunity.setKey(s);
                        opportunity.setAdditional(Boolean.parseBoolean(s1));
                        opportunity.setValue(s2);
                        opportunities.add(opportunity);
                    }
                });
            });

            tariff.setTariffType(TariffType.valueOf(dto.getTariffType()));
            tariff.setClientType(ClientType.valueOf(dto.getClientType()));
            tariff.setConnectPrice(dto.getConnectPrice());
            tariff.setName(dto.getName());
            tariff.setPrice(dto.getPrice());
            tariff.setDescription(dto.getDescription());
            tariffRepo.save(tariff);
            return new ApiResponse("Edited", true);
        } catch (Exception e) {
            return new ApiResponse("An error variant of the map was given, Please look to example in TariffDto.class", false);
        }
    }

    public ApiResponse deleteTariff(Long id) {
        Optional<Tariff> optionalTariff = tariffRepo.findByIdAndActiveTrue(id);
        if (!optionalTariff.isPresent())
            return new ApiResponse("Tariff not found", false);
        Tariff tariff = optionalTariff.get();
        tariff.setActive(false);
        return new ApiResponse("Deleted", false);
    }

    public ApiResponse getReport() {
        return new ApiResponse("OK", true, tariffRepo.getReport());
    }
}
