package com.ccsw.tutorial.loan;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ccsw.tutorial.client.ClientService;
import com.ccsw.tutorial.exception.ClientCantLoanException;
import com.ccsw.tutorial.exception.GameAlreadyLoanedException;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;

/**
 * @author DiegoRicoL
 */
@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    GameService gameService;

    @Autowired
    ClientService clientService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Loan get(Long id) {

        return this.loanRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Loan> findPage(LoanSearchDto dto, Long clientId, Long gameId, Date date) {

        return this.loanRepository.find(dto.getPageable(), clientId, gameId, date);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, LoanDto data) {

        // Comprobamos que el cliente no tenga 2 prestamos activos, si los tiene
        // lanzamos una excepcion
        if (this.loanRepository.countLoansByClientBetweenDates(data.getClient().getId(), data.getBegin(),
                data.getEnd()) >= 2) {
            throw new ClientCantLoanException();
        }

        // Comprobamos que el juego no está siendo prestado ya. Si lo está, lanzamos una
        // excepcion
        if (this.loanRepository.countLoansByGameBetweenDates(data.getGame().getId(), data.getBegin(),
                data.getEnd()) >= 1) {
            throw new GameAlreadyLoanedException();
        }

        // Si está todo bien...
        Loan loan = null;
        if (id != null)
            loan = this.get(id);
        else
            loan = new Loan();

        BeanUtils.copyProperties(data, loan, "id");

        // le asignamos el juego y el cliente al prestamo
        loan.setGame(gameService.getById(data.getGame().getId()));
        loan.setClient(clientService.getById(data.getClient().getId()));

        this.loanRepository.save(loan);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {

        this.loanRepository.deleteById(id);

    }

}