package pdp.uz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pdp.uz.entity.Client;
import pdp.uz.entity.SimCard;
import pdp.uz.entity.Tariff;
import pdp.uz.entity.enums.ClientType;
import pdp.uz.payload.ClientDto;
import pdp.uz.payload.ClientUpdateDto;
import pdp.uz.payload.helpers.ApiResponse;
import pdp.uz.payload.helpers.CurrentUser;
import pdp.uz.entity.Number;
import pdp.uz.repository.*;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepo clientRepo;

    private final NumberRepo numberRepo;

    private final CurrentUser currentUser;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepo roleRepo;

    private final TariffRepo tariffRepo;

    private final SubscriberService subscribeService;

    private final SimCardRepo simCardRepo;


    public ApiResponse getAll() {
        return new ApiResponse("OK", true, clientRepo.findAll());
    }


    public ApiResponse get(Long id) {
        Optional<Client> optionalClient = clientRepo.findById(id);
        return optionalClient.map(client -> new ApiResponse("Ok", true, client)).orElseGet(() -> new ApiResponse("Client not found", false));
    }


    public ApiResponse create(ClientDto dto) {
        try {
            if (clientRepo.existsByUsername(dto.getUsername()))
                return new ApiResponse("Client has already existed", false);
            Optional<Tariff> optionalTariff = tariffRepo.findByIdAndActiveTrue(dto.getTariffId());
            if (!optionalTariff.isPresent())
                return new ApiResponse("Tariff not found", false);
            Tariff tariff = optionalTariff.get();
            if (!tariff.getClientType().name().equals(dto.getClientType()))
                return new ApiResponse("Chosen tariff is not for this client type", false);
            if (tariff.getConnectPrice() > dto.getBalance())
                return new ApiResponse("You do not have enough money for the tariff", false);

            Client client = new Client();

            Number number = new Number();
            Optional<Number> optionalNumber = numberRepo.findByCodeAndNumber(dto.getCode(), dto.getNumber());
            if (optionalNumber.isPresent()) {
                number = optionalNumber.get();
                if (number.isOwned())
                    return new ApiResponse("This number is in use", false);

                number.setOwned(true);
                number = numberRepo.save(number);
            } else {
                number.setNumber(dto.getNumber());
                number.setCode(dto.getCode());
                number = numberRepo.save(number);
            }
            SimCard simCard = new SimCard();
            simCard.setClient(client);
            simCard.setNumber(number);
            simCard.setBalance(dto.getBalance());
            simCard.setBranch(currentUser.getCurrentUser().getBranch());
            simCard.setPrice(dto.getPrice());

            Optional<Client> optionalClient = clientRepo.findByPassportSeriesAndPassportNumber(dto.getPassportSeries(), dto.getPassportNumber());
            if (optionalClient.isPresent()) {
                client = optionalClient.get();
            } else {
                client.setFirstname(dto.getFirstname());
                client.setLastname(dto.getLastname());
                client.setUsername(dto.getUsername());
                client.setClientType(ClientType.valueOf(dto.getClientType()));
                client.setPassword(passwordEncoder.encode(dto.getPassword()));
                client.setPassportSeries(dto.getPassportSeries());
                client.setPassportNumber(dto.getPassportNumber());
                client.setRole(roleRepo.findByName("ROLE_USER").get());
            }
            client.getSimCards().add(simCard);

            subscribeService.createSubscriber(simCardRepo.findByNumber(simCard.getNumber()), tariff, dto.isServiceDebt());
            return new ApiResponse("Created", true, clientRepo.save(client));
        } catch (Exception e) {
            return new ApiResponse("Wrong format client type (Use PHYSICAL_PERSON, LEGAL_PERSON)", false);
        }
    }

    public ApiResponse update(Long id, ClientUpdateDto dto) {
        if (clientRepo.existsByUsernameAndIdNot(dto.getUsername(), id))
            return new ApiResponse("This login is in use", false);
        Optional<Client> optionalClient = clientRepo.findById(id);
        if (!optionalClient.isPresent())
            return new ApiResponse("Client not found", false);
        Client client = optionalClient.get();
        client.setFirstname(dto.getFirstname());
        client.setLastname(dto.getLastname());
        client.setUsername(dto.getUsername());
        client.setPassword(passwordEncoder.encode(dto.getPassword()));
        clientRepo.save(client);
        return new ApiResponse("Updated", true);
    }
}
