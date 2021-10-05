package ru.cinema.servlets;

import ru.cinema.models.Session;
import ru.cinema.store.psql.PsqlSessionStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Session> cinemaSessions = PsqlSessionStore.instOf().findAll();
        if (cinemaSessions.size() == 1) {
            HttpSession session = req.getSession();
            session.setAttribute("session_id", cinemaSessions.get(0).getId());
        }
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
