package pdp.uz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pdp.uz.entity.USSDCodes;
import pdp.uz.payload.USSDCodeDto;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.repository.USSDRepo;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class USSDService {

    private final USSDRepo ussdRepo;


    public ApiResponse getCodes() {
        return new ApiResponse("OK", true, ussdRepo.findAllByActiveTrue());
    }


    public ApiResponse getCode(String code) {
        Optional<USSDCodes> optionalUSSDCode = ussdRepo.findByCodeAndActiveTrue(code);
        return optionalUSSDCode.map(ussdCode -> new ApiResponse("OK", true, ussdCode)).orElseGet(() -> new ApiResponse("Code not found", false));
    }


    public ApiResponse deActivate(String code) {
        Optional<USSDCodes> optionalUSSDCode = ussdRepo.findByCodeAndActiveTrue(code);
        if (!optionalUSSDCode.isPresent()) {
            return new ApiResponse("Code not found", false);
        }
        USSDCodes ussdCode = optionalUSSDCode.get();
        ussdCode.setActive(false);
        ussdRepo.save(ussdCode);
        return new ApiResponse("OK", true);
    }


    public ApiResponse create(USSDCodeDto dto) {
        if (ussdRepo.existsByCode(dto.getCode()))
            return new ApiResponse("Code has already existed",false);
        USSDCodes code = new USSDCodes();
        code.setCode(dto.getCode());
        code.setDescription(dto.getDescription());
        ussdRepo.save(code);
        return new ApiResponse("Created", true);
    }


    public ApiResponse update(USSDCodeDto dto, Long id) {
        if (ussdRepo.existsByCodeAndIdNot(dto.getCode(), id))
            return new ApiResponse("Code has already existed",false);
        Optional<USSDCodes> optionalUSSDCode = ussdRepo.findByIdAndActiveTrue(id);
        if (!optionalUSSDCode.isPresent())
            return new ApiResponse("Code not found", false);
        USSDCodes code = optionalUSSDCode.get();
        code.setCode(dto.getCode());
        code.setDescription(dto.getDescription());
        ussdRepo.save(code);
        return new ApiResponse("Updated", true);
    }
}
