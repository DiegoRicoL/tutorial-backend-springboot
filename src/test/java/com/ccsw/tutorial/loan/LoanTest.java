package com.ccsw.tutorial.loan;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ccsw.tutorial.client.ClientService;
import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.exception.GameNotAvailableException;
import com.ccsw.tutorial.exception.TooManyActiveLoansException;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;

/**
 * 
 * @author caliagaq
 *
 */
@ExtendWith(MockitoExtension.class)
public class LoanTest {

    private static final Long EXISTING_LOAN_ID = 1L;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private GameService gameService;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private LoanServiceImpl loanService;

    private static Date begin;
    private static Date end;

    @BeforeAll
    static void setUp() throws ParseException {
        begin = new SimpleDateFormat("dd/MM/yyyy").parse("01/03/2023");
        end = new SimpleDateFormat("dd/MM/yyyy").parse("10/03/2023");
    }

    // FindToDo

    @Test
    public void saveLoanWithClientWithoutActiveLoansAndFreeGameAndDatesBetween14DaysShouldCreate()
            throws ParseException {
        // Al service le paso el LoanDto, debe buscar que el cliente solo tenga 2 o
        // menos loans, que el juego no esté en otro loan activo y que las fechas sean
        // en un rengo de 14 días

        ClientDto clientDto = mock(ClientDto.class);
        GameDto gameDto = mock(GameDto.class);

        LoanDto loanToCreate = new LoanDto();
        loanToCreate.setClient(clientDto);
        loanToCreate.setGame(gameDto);
        loanToCreate.setBegin(begin);
        loanToCreate.setEnd(end);

        when(gameService.get(gameDto.getId())).thenReturn(new Game());
        when(clientService.get(gameDto.getId())).thenReturn(new Client());

        loanService.save(loanToCreate);
        verify(loanRepository).save(any(Loan.class));
    }

    @Test
    public void saveLoanWithClientWithTwoActiveLoansShouldError() throws ParseException {

        List<Loan> clientsActiveLoans = new ArrayList<>();
        clientsActiveLoans.add(mock(Loan.class));
        clientsActiveLoans.add(mock(Loan.class));

        ClientDto client = mock(ClientDto.class);
        LoanDto loanToCreate = new LoanDto();
        loanToCreate.setClient(client);
        loanToCreate.setGame(mock(GameDto.class));
        loanToCreate.setBegin(begin);
        loanToCreate.setEnd(end);

        // FindByClientAndBeginAndEnd (?)
        when(loanRepository.findByClientAndActiveLoans(client.getId(), begin, end)).thenReturn(clientsActiveLoans);

        assertThrows(TooManyActiveLoansException.class, () -> {
            loanService.save(loanToCreate);
        });
    }

    @Test
    public void saveLoanWithLendedGameShouldError() {
        GameDto game = mock(GameDto.class);

        List<Loan> gamesActiveLoans = new ArrayList<>();
        gamesActiveLoans.add(mock(Loan.class));

        LoanDto loanToCreate = new LoanDto();
        loanToCreate.setClient(mock(ClientDto.class));
        loanToCreate.setGame(game);
        loanToCreate.setBegin(begin);
        loanToCreate.setEnd(end);

        // FindByGameAndBeginAndEnd (?)
        when(loanRepository.findByGameAndActiveLoans(game.getId(), begin, end)).thenReturn(gamesActiveLoans);

        assertThrows(GameNotAvailableException.class, () -> {
            loanService.save(loanToCreate);
        });
    }

    @Test
    public void deleteExistingIdShouldDelete() {
        loanService.delete(EXISTING_LOAN_ID);

        verify(loanRepository).deleteById(EXISTING_LOAN_ID);

    }
}