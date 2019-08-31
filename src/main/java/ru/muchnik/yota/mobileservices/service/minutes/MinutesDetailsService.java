package ru.muchnik.yota.mobileservices.service.minutes;

import org.springframework.stereotype.Service;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesDetails;
import ru.muchnik.yota.mobileservices.repository.minutes.MinutesDetailsRepository;
import ru.muchnik.yota.mobileservices.service.BaseDetailsService;

@Service
public class MinutesDetailsService extends BaseDetailsService<MinutesDetails, MinutesDetailsRepository> {
    public MinutesDetailsService(final MinutesDetailsRepository repository) {
        super(repository);
    }
}
