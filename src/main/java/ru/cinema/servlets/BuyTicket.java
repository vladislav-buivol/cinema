package ru.cinema.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cinema.models.Ticket;
import ru.cinema.store.psql.PsqlTicketStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class BuyTicket extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(BuyTicket.class);
    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (isValidSession(req.getSession())) {
            Ticket ticket = GSON.fromJson(req.getReader(), Ticket.class);
            ticket.setSessionId(1);
            ticket.setAccountId(1);
            Ticket t = PsqlTicketStore.instOf().save(ticket);
            if (t != null) {
                resp.setStatus(200);
            } else {
                resp.setStatus(500);
            }

        }
    }

    private boolean isValidSession(HttpSession session) {
        LOGGER.info("Session validation");
        return session != null && session.getAttribute("cell") != null
                && session.getAttribute("row") != null
                && session.getAttribute("hallId") != null;
    }
}
