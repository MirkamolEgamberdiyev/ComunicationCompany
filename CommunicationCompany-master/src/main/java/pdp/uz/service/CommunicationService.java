package pdp.uz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pdp.uz.entity.ServiceCategory;
import pdp.uz.entity.Services;
import pdp.uz.entity.enums.ServiceCategoryEnum;
import pdp.uz.entity.enums.TariffType;
import pdp.uz.payload.ServiceDto;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.repository.ServiceCategoryRepo;
import pdp.uz.repository.ServiceRepo;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunicationService {

    private final ServiceRepo serviceRepo;

    private final ServiceCategoryRepo serviceCategoryRepo;

    public ApiResponse getAll() {
        return new ApiResponse("OK", true, serviceRepo.findAll());
    }

    public ApiResponse get(Long id) {
        Optional<Services> optionalService = serviceRepo.findByIdAndStatusTrue(id);
        return optionalService.map(service -> new ApiResponse("OK", true, service)).orElseGet(() -> new ApiResponse("Service not found", false));
    }

    public ApiResponse create(ServiceDto dto) {
        if (serviceRepo.existsByName(dto.getName())) {
            return new ApiResponse("Service has already existed", false);
        }
        Services service = new Services();
        if (!saveService(dto, service)) {
            return new ApiResponse("Wrong value of service category enum. (Look enums)", false);
        }
        return new ApiResponse("Created", true);
    }


    public ApiResponse deleteService(Long id) {
        Optional<Services> optionalServiceEntity = serviceRepo.findByIdAndStatusTrue(id);
        if (!optionalServiceEntity.isPresent()) {
            return new ApiResponse("Service not found", false);
        }
        Services service = optionalServiceEntity.get();
        service.setStatus(false);
        serviceRepo.save(service);
        return new ApiResponse("Deleted", true);
    }


    public ApiResponse update(ServiceDto dto, Long id) {
        if (serviceRepo.existsByNameAndIdNot(dto.getName(), id)) {
            return new ApiResponse("Service has already existed", false);
        }
        Optional<Services> optionalService = serviceRepo.findByIdAndStatusTrue(id);
        if (!optionalService.isPresent()) {
            return new ApiResponse("Service not found", false);
        }
        Services service = optionalService.get();
        if (!saveService(dto, service)) {
            return new ApiResponse("Wrong value of service category enum. (Look enums)", false);
        }
        return new ApiResponse("Updated", true);
    }


    public ApiResponse getTopServices() {
        return new ApiResponse("OK", true, serviceRepo.getTopServices());
    }

    public ApiResponse getPoorServices() {
        return new ApiResponse("OK", true, serviceRepo.getPoorServices());
    }


    private boolean saveService(ServiceDto dto, Services service) {
        try {
            ServiceCategory serviceCategory;
            Optional<ServiceCategory> optionalServiceCategory = serviceCategoryRepo.findByName(ServiceCategoryEnum.valueOf(dto.getServiceCategory()));
            if (optionalServiceCategory.isPresent()) {
                serviceCategory = optionalServiceCategory.get();
            } else {
                serviceCategory = new ServiceCategory();
                serviceCategory.setName(ServiceCategoryEnum.valueOf(dto.getServiceCategory()));
            }

            service.setServiceCategory(serviceCategoryRepo.save(serviceCategory));
            service.setName(dto.getName());
            service.setType(TariffType.valueOf(dto.getType()));
            service.setPrice(dto.getPrice());
            serviceRepo.save(service);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
