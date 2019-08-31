package ru.muchnik.yota.mobileservices.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.muchnik.yota.mobileservices.model.entity.PackageOfMinutes;
import ru.muchnik.yota.mobileservices.model.exception.NotFoundException;
import ru.muchnik.yota.mobileservices.repository.PackageOfMinutesRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PackageOfMinutesServiceTest {
    @Mock
    private PackageOfMinutesRepository repository;

    @InjectMocks
    private PackageOfMinutesService service;

    @Mock
    private PackageOfMinutes packageOfMinutes;
    private Optional<PackageOfMinutes> optionalMinutesPackage;

    @Before
    public void setUp() throws Exception {
        optionalMinutesPackage = Optional.of(packageOfMinutes);
    }

    @Test
    public void getPackage() {
        when(repository.findById(eq(1L))).thenReturn(optionalMinutesPackage);

        PackageOfMinutes result = service.getPackage(1L);
        Assert.assertEquals(packageOfMinutes, result);
    }

    @Test(expected = NotFoundException.class)
    public void getPackageNotFound() {
        when(repository.findById(eq(1L))).thenReturn(Optional.empty());

        service.getPackage(1L);
    }

    @Test
    public void savePackage() {
        when(repository.save(eq(this.packageOfMinutes))).thenReturn(this.packageOfMinutes);
        PackageOfMinutes packageOfMinutes = service.savePackage(this.packageOfMinutes);

        Assert.assertEquals(packageOfMinutes, this.packageOfMinutes);
        verify(repository).save(eq(this.packageOfMinutes));
    }
}