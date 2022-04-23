package pdp.uz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pdp.uz.entity.Package;
import pdp.uz.entity.Tariff;
import pdp.uz.payload.PackageDto;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.repository.PackageRepo;
import pdp.uz.repository.TariffRepo;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepo packageRepo;

    private final TariffRepo tariffRepo;

    public ApiResponse getAll() {
        return new ApiResponse("OK", true, packageRepo.findAll());
    }

    public ApiResponse get(Long id) {
        Optional<Package> optionalPackage = packageRepo.findById(id);
        return optionalPackage.map(aPackage -> new ApiResponse("OK", true, aPackage)).orElseGet(() -> new ApiResponse("Package not found", false));
    }

    public ApiResponse create(PackageDto dto) {
        if (packageRepo.existsByName(dto.getName())) {
            return new ApiResponse("Package has already existed", false);
        }
        List<Tariff> tariffs = new ArrayList<>();
        for (Long aLong : dto.getTariffsId()) {
            Optional<Tariff> optionalTariff = tariffRepo.findByIdAndActiveTrue(aLong);
            if (!optionalTariff.isPresent()) {
                return new ApiResponse("Tariff not found", false);
            }
            tariffs.add(optionalTariff.get());
        }
        Package aPackage = new Package();
        aPackage.setAddResidue(dto.isAddResidue());
        aPackage.setKey(dto.getKey());
        aPackage.setName(dto.getName());
        aPackage.setPrice(dto.getPrice());
        aPackage.setTariffs(tariffs);
        aPackage.setValidityDay(dto.getValidityDay());
        aPackage.setValue(dto.getValue());
        packageRepo.save(aPackage);

        return new ApiResponse("Created", true);
    }

    public ApiResponse update(Long id, PackageDto dto) {
        if (packageRepo.existsByNameAndIdNot(dto.getName(), id)) {
            return new ApiResponse("Package has already existed", false);
        }
        Optional<Package> optionalPackage = packageRepo.findByIdAndStatusTrue(id);
        if (!optionalPackage.isPresent()) {
            return new ApiResponse("Package not found", false);
        }
        List<Tariff> tariffs = new ArrayList<>();
        for (Long aLong : dto.getTariffsId()) {
            Optional<Tariff> optionalTariff = tariffRepo.findByIdAndActiveTrue(aLong);
            if (!optionalTariff.isPresent()) {
                return new ApiResponse("Tariff not found", false);
            }
            tariffs.add(optionalTariff.get());
        }
        Package aPackage = optionalPackage.get();
        aPackage.setValue(dto.getValue());
        aPackage.setTariffs(tariffs);
        aPackage.setName(dto.getName());
        aPackage.setKey(dto.getKey());
        aPackage.setPrice(dto.getPrice());
        aPackage.setValidityDay(dto.getValidityDay());
        aPackage.setAddResidue(dto.isAddResidue());
        packageRepo.save(aPackage);
        return new ApiResponse("Updated", true);
    }

    public ApiResponse delete(Long id) {
        Optional<Package> optionalPackage = packageRepo.findByIdAndStatusTrue(id);
        if (!optionalPackage.isPresent())
            return new ApiResponse("Package not found", false);
        Package aPackage = optionalPackage.get();
        aPackage.setStatus(false);
        packageRepo.save(aPackage);
        return new ApiResponse("Deleted", true);
    }

    public ApiResponse getReport() {
        return new ApiResponse("OK", true, packageRepo.getReport());
    }
}
