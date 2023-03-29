package com.ccsw.tutorial.loan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.UriComponentsBuilder;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.game.model.LoanSearchDto;
import com.ccsw.tutorial.loan.model.LoanDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoanIT {

    private static final String LOCALHOST = "http://localhost:";
    private static final String SERVICE_PATH = "/loan/";

    private static final Long DELETE_LOAN_ID = 7L;
    private static final Long FREE_GAME_ID = 3L;
    private static final Long LENDED_GAME_ID = 1L;
    private static final Long CLIENT_WITH_TWO_ACTIVE_LOANS_ID = 1L;
    private static final Long CLIENT_WITHOUT_ACTIVE_LOANS_ID = 5L;

    private static final String CLIENT_ID_PARAM = "idClient";
    private static final String GAME_ID_PARAM = "idGame";
    private static final String DATE_PARAM = "date";

    private static final int TOTAL_LOANS = 7;
    private static final int PAGE_SIZE = 5;

    private static Date begin;
    private static Date endOk;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<Page<LoanDto>> responseTypePage = new ParameterizedTypeReference<Page<LoanDto>>() {
    };

    ParameterizedTypeReference<List<LoanDto>> responseTypeList = new ParameterizedTypeReference<List<LoanDto>>() {
    };

    private String getUrlWithParams() {
        return UriComponentsBuilder.fromHttpUrl(LOCALHOST + port + SERVICE_PATH)
                .queryParam(CLIENT_ID_PARAM, "{" + CLIENT_ID_PARAM + "}")
                .queryParam(GAME_ID_PARAM, "{" + GAME_ID_PARAM + "}").queryParam(DATE_PARAM, "{" + DATE_PARAM + "}")
                .encode().toUriString();
    }

    @BeforeAll
    static void setUp() throws ParseException {
        begin = new SimpleDateFormat("dd/MM/yyyy").parse("01/03/2023");
        endOk = new SimpleDateFormat("dd/MM/yyyy").parse("10/03/2023");
    }

    @Test
    public void findFirstPageWithFiveSizeWithoutFiltersShouldReturnFirstFiveResults() {
        LoanSearchDto loanSearchDto = new LoanSearchDto();
        loanSearchDto.setPageable(PageRequest.of(0, PAGE_SIZE));

        ResponseEntity<Page<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST,
                new HttpEntity<>(loanSearchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }

    @Test
    public void findSecondPageWithFiveSizeWithoutFiltersShouldReturnLastResult() {
        int elementsCount = TOTAL_LOANS - PAGE_SIZE;

        LoanSearchDto loanSearchDto = new LoanSearchDto();
        loanSearchDto.setPageable(PageRequest.of(1, PAGE_SIZE));

        ResponseEntity<Page<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST,
                new HttpEntity<>(loanSearchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(elementsCount, response.getBody().getContent().size());
    }

    @Disabled("tofix")
    @Test
    public void findExistingClientFirstPageWithFiveSizeWithFiltersShouldReturnLoans() {
        int LOANS_WITH_FILTER = 2;

        Map<String, Object> params = new HashMap<>();
        params.put(CLIENT_ID_PARAM, CLIENT_WITH_TWO_ACTIVE_LOANS_ID.toString());
        params.put(GAME_ID_PARAM, null);
        params.put(DATE_PARAM, null);

        LoanSearchDto loanSearchDto = new LoanSearchDto();
        loanSearchDto.setPageable(PageRequest.of(0, PAGE_SIZE));

        ResponseEntity<Page<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST,
                new HttpEntity<>(loanSearchDto), responseTypePage, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().getNumberOfElements());
    }

    @Test
    public void saveLoanWithClientWithoutActiveLoansAndFreeGameShouldCreate() {

        long newLoanId = TOTAL_LOANS + 1;
        long newLoanSize = TOTAL_LOANS + 1;

        LoanDto loanDto = new LoanDto();

        ClientDto clientDto = new ClientDto();
        clientDto.setId(CLIENT_WITHOUT_ACTIVE_LOANS_ID);

        GameDto gameDto = new GameDto();
        gameDto.setId(FREE_GAME_ID);

        loanDto.setBegin(begin);
        loanDto.setEnd(endOk);
        loanDto.setClient(clientDto);
        loanDto.setGame(gameDto);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(loanDto), Void.class);
        LoanSearchDto loanSearchDto = new LoanSearchDto();
        loanSearchDto.setPageable(PageRequest.of(0, (int) newLoanSize));

        ResponseEntity<Page<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST,
                new HttpEntity<>(loanSearchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(newLoanSize, response.getBody().getTotalElements());

        LoanDto loan = response.getBody().getContent().stream().filter(item -> item.getId().equals(newLoanId))
                .findFirst().orElse(null);
        assertNotNull(loan);
        assertEquals(begin, loan.getBegin());
        assertEquals(endOk, loan.getEnd());
    }

    @Test
    public void saveLoanWithClientWithTwoActiveLoansAndFreeGameShouldThrowException() {

        LoanDto loanDto = new LoanDto();

        ClientDto clientDto = new ClientDto();
        clientDto.setId(CLIENT_WITH_TWO_ACTIVE_LOANS_ID);

        GameDto gameDto = new GameDto();
        gameDto.setId(FREE_GAME_ID);

        loanDto.setBegin(begin);
        loanDto.setEnd(endOk);
        loanDto.setClient(clientDto);
        loanDto.setGame(gameDto);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT,
                new HttpEntity<>(loanDto), Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void saveLoanWithClientWithoutActiveLoansAndLendedGameShouldThrowException() {

        LoanDto loanDto = new LoanDto();

        ClientDto clientDto = new ClientDto();
        clientDto.setId(CLIENT_WITHOUT_ACTIVE_LOANS_ID);

        GameDto gameDto = new GameDto();
        gameDto.setId(LENDED_GAME_ID);

        loanDto.setBegin(begin);
        loanDto.setEnd(endOk);
        loanDto.setClient(clientDto);
        loanDto.setGame(gameDto);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT,
                new HttpEntity<>(loanDto), Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void deleteWithExistsIdShouldDeleteLoan() {

        long newLoansSize = TOTAL_LOANS - 1;

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + DELETE_LOAN_ID, HttpMethod.DELETE, null, Void.class);

        LoanSearchDto loanSearchDto = new LoanSearchDto();
        loanSearchDto.setPageable(PageRequest.of(0, TOTAL_LOANS));

        ResponseEntity<Page<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST,
                new HttpEntity<>(loanSearchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(newLoansSize, response.getBody().getTotalElements());

    }

    @Test
    public void deleteWithNotExistsIdShouldThrowException() {

        long deleteLoanId = TOTAL_LOANS + 1;

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + deleteLoanId,
                HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}