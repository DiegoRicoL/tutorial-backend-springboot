package com.ccsw.tutorial.loan;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ccsw.tutorial.loan.model.Loan;

/**
 * @author DiegoRicoL
 */
public interface LoanRepository extends CrudRepository<Loan, Long> {

    /**
     * Método para recuperar un listado paginado de
     * {@link com.ccsw.tutorial.loan.model.Loan}
     * 
     * @param pageable, client_id, game_id, date
     * @return
     */
    @Query("select l from Loan l where (:client_id is null or l.client.id = :client_id ) and (:game_id is null or l.game.id = :game_id) and (:date is null or (:date between l.begin and l.end))")
    Page<Loan> find(Pageable pageable, @Param("client_id") Number clientId, @Param("game_id") Number gameId,
            @Param("date") Date date);

    /*
     * Método para realizar una cuenta de los prestamos realizados por un cliente en
     * un rango de tiempo
     * 
     * @param client_id, begin, end
     * 
     * @return
     */
    @Query("select count(l) from Loan l where l.client.id = :client_id and ((:begin between l.begin and l.end) or (:end between l.begin and l.end) or (:begin < l.begin and :end > l.end))")
    Long countLoansByClientBetweenDates(@Param("client_id") Number clientId, @Param("begin") Date begin,
            @Param("end") Date end);

    /*
     * Método para realizar saber si un juego ha sido prestado en un rango de tiempo
     * (0 No, >=1 Si)
     * 
     * @param client_id, begin, end
     * 
     * @return
     */
    @Query("select count(l) from Loan l where l.game.id = :game_id and ((:begin between l.begin and l.end) or (:end between l.begin and l.end))")
    Long countLoansByGameBetweenDates(@Param("game_id") Number gameId, @Param("begin") Date begin,
            @Param("end") Date end);

}