package uk.gov.companieshouse.addresslookup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.gov.companieshouse.addresslookup.entity.AddressEntity;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    /**
     * Find addresses by postcode
     */
    @Query("SELECT a FROM AddressEntity a WHERE a.postcode = :postcode")
    List<AddressEntity> findByPostcode(@Param("postcode") String postcode);

    /**
     * Find multiple addresses by postcode
     */
    @Query("SELECT a FROM AddressEntity a WHERE a.postcode = :postcode")
    List<AddressEntity> findMultipleByPostcode(@Param("postcode") String postcode);

}
