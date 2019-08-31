package ru.muchnik.yota.mobileservices.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.muchnik.yota.mobileservices.model.entity.minutes.MinutesPackageCatalog;
import ru.muchnik.yota.mobileservices.model.exception.NotFoundException;
import ru.muchnik.yota.mobileservices.repository.minutes.MinutesPackageCatalogRepository;
import ru.muchnik.yota.mobileservices.service.minutes.MinutesPackageService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BaseMinutesPackageServiceTest {
    @Mock
    private MinutesPackageCatalogRepository repository;

    @InjectMocks
    private MinutesPackageService service;

    @Mock
    private MinutesPackageCatalog minutesPackageCatalog;
    @Mock
    private MinutesPackageCatalog minutesPackageCatalog2;
    private Optional<MinutesPackageCatalog> optionalMinutesPackage;

    @Before
    public void setUp() throws Exception {
        optionalMinutesPackage = Optional.of(minutesPackageCatalog);
    }

    @Test
    public void getPackage() {
        when(repository.findById(eq("1"))).thenReturn(optionalMinutesPackage);

        MinutesPackageCatalog result = service.getPackage("1");
        Assert.assertEquals(minutesPackageCatalog, result);
    }

    @Test
    public void getPackageы() {
        when(repository.findAll()).thenReturn(Arrays.asList(minutesPackageCatalog, minutesPackageCatalog2));

        List<MinutesPackageCatalog> result = service.getPackages();
        Assert.assertEquals(Arrays.asList(minutesPackageCatalog, minutesPackageCatalog2), result);
    }

    @Test(expected = NotFoundException.class)
    public void getPackageNotFound() {
        when(repository.findById(eq("1"))).thenReturn(Optional.empty());

        service.getPackage("1");
    }

    @Test
    public void savePackage() {
        when(repository.save(eq(this.minutesPackageCatalog))).thenReturn(this.minutesPackageCatalog);
        MinutesPackageCatalog minutesPackageCatalog = service.savePackage(this.minutesPackageCatalog);

        Assert.assertEquals(minutesPackageCatalog, this.minutesPackageCatalog);
        verify(repository).save(eq(this.minutesPackageCatalog));
    }
}