package ru.cinema.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cinema.models.Hall;
import ru.cinema.store.psql.PsqlHallStore;
import ru.cinema.store.psql.PsqlTicketStore;
import ru.cinema.store.psql.exeptions.NoDataInDataBase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HallServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(HallServlet.class);

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");
        String hallJson;
        List<Hall> allHalls = PsqlHallStore.instOf().findAll();
        String id = req.getParameter("id");
        Hall hall;
        if (allHalls.size() == 0) {
            throw new NoDataInDataBase("Table hall is empty");
        } else if (id != null) {
            int hallId = Integer.parseInt(id);
            hall = PsqlHallStore.instOf().findById(hallId);
            setUnavailablePlaces(hall);
            hallJson = GSON.toJson(hall);
        } else {
            hallJson = GSON.toJson(allHalls);
        }
        OutputStream outputStream = resp.getOutputStream();
        outputStream.write(hallJson.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }

    private void setUnavailablePlaces(Hall hall) {
        PsqlTicketStore ticketStore = PsqlTicketStore.instOf();
        hall.setUnavailablePlaces(ticketStore.findByHallId(hall.getId()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
