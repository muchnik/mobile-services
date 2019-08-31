package ru.muchnik.yota.mobileservices.service.traffic;

import org.springframework.stereotype.Service;
import ru.muchnik.yota.mobileservices.model.entity.traffic.TrafficDetails;
import ru.muchnik.yota.mobileservices.repository.traffic.TrafficDetailsRepository;
import ru.muchnik.yota.mobileservices.service.BaseDetailsService;

@Service
public class TrafficDetailsService extends BaseDetailsService<TrafficDetails, TrafficDetailsRepository> {
    public TrafficDetailsService(TrafficDetailsRepository repository) {
        super(repository);
    }
}
