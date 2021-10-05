package ru.cinema.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.cinema.models.Ticket;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class PaymentServlet extends HttpServlet {
    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("payment.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Ticket ticket = GSON.fromJson(req.getReader(), Ticket.class);
        session.setAttribute("row", ticket.getRow());
        session.setAttribute("cell", ticket.getCell());
        session.setAttribute("hallId", ticket.getHallId());
        req.getRequestDispatcher("payment.jsp").forward(req, resp);
    }
}
