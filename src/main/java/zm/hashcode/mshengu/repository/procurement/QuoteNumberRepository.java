/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.repository.procurement;

import org.springframework.data.repository.PagingAndSortingRepository;
import zm.hashcode.mshengu.domain.procurement.QuoteNumber;

/**
 *
 * @author Luckbliss
 */
public interface QuoteNumberRepository extends PagingAndSortingRepository<QuoteNumber, String>{
    
}
