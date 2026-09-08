package uk.gov.companieshouse.addresslookup.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.addresslookup.entity.AddressEntity;
import uk.gov.companieshouse.addresslookup.repository.AddressRepository;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Initializes the database with test data when using H2 in-memory database
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final AddressRepository addressRepository;

    @Autowired
    public DataInitializer(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only initialize if database is empty
        if (addressRepository.count() == 0) {
            addressRepository.saveAll(Arrays.asList(
                    AddressEntity.builder()
                            .uprn(100001L)
                            .udprn(50001)
                            .uprnMatchType("match")
                            .premises("10")
                            .addressLine1("Downing Street")
                            .addressLine2("")
                            .postTown("London")
                            .country("England")
                            .postcode("SW1A1AA")
                            .isWelsh(false)
                            .isResidential(true)
                            .classicationCode("RH05")
                            .classicationShortDescription("Royal Household")
                            .classicationFullDescription("Royal Household")
                            .latitude(new BigDecimal("51.5033"))
                            .longitude(new BigDecimal("-0.1276"))
                            .build(),
                    AddressEntity.builder()
                            .uprn(100002L)
                            .udprn(50002)
                            .uprnMatchType("match")
                            .premises("11")
                            .addressLine1("Downing Street")
                            .addressLine2("Annexe")
                            .postTown("London")
                            .country("England")
                            .postcode("SW1A1AA")
                            .isWelsh(false)
                            .isResidential(false)
                            .classicationCode("OP27")
                            .classicationShortDescription("Central Government Office")
                            .classicationFullDescription("Central Government Office")
                            .latitude(new BigDecimal("51.5034"))
                            .longitude(new BigDecimal("-0.1275"))
                            .build(),
                    AddressEntity.builder()
                            .uprn(100003L)
                            .udprn(50003)
                            .uprnMatchType("match")
                            .premises("70")
                            .addressLine1("Whitehall")
                            .addressLine2("")
                            .postTown("London")
                            .country("England")
                            .postcode("SW1A2AA")
                            .isWelsh(false)
                            .isResidential(false)
                            .classicationCode("OP27")
                            .classicationShortDescription("Central Government Office")
                            .classicationFullDescription("Central Government Office")
                            .latitude(new BigDecimal("51.5035"))
                            .longitude(new BigDecimal("-0.1265"))
                            .build(),
                    AddressEntity.builder()
                            .uprn(100004L)
                            .udprn(50004)
                            .uprnMatchType("match")
                            .premises("Tower 42")
                            .addressLine1("25 Old Broad Street")
                            .addressLine2("")
                            .postTown("London")
                            .country("England")
                            .postcode("EC2N1HQ")
                            .isWelsh(false)
                            .isResidential(false)
                            .classicationCode("CO")
                            .classicationShortDescription("Commercial")
                            .classicationFullDescription("Commercial")
                            .latitude(new BigDecimal("51.5140"))
                            .longitude(new BigDecimal("-0.0857"))
                            .build(),
                    AddressEntity.builder()
                            .uprn(100005L)
                            .udprn(50005)
                            .uprnMatchType("match")
                            .premises("")
                            .addressLine1("Abbey Road")
                            .addressLine2("")
                            .postTown("London")
                            .country("England")
                            .postcode("NW89AY")
                            .isWelsh(false)
                            .isResidential(false)
                            .classicationCode("CMP")
                            .classicationShortDescription("Unclassified Commercial")
                            .classicationFullDescription("Unclassified Commercial")
                            .latitude(new BigDecimal("51.5358"))
                            .longitude(new BigDecimal("-0.1821"))
                            .build()
            ));
        }
    }
}
