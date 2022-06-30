package br.ufsm.csi.so.controller;

import java.util.concurrent.Semaphore;

import br.ufsm.csi.so.App;
import br.ufsm.csi.so.data.Seat;
import br.ufsm.csi.so.server.Controller;
import br.ufsm.csi.so.server.Request;
import br.ufsm.csi.so.server.Response;
import br.ufsm.csi.so.util.Terminal;
import lombok.SneakyThrows;

public class ConfirmController extends Controller {
    private Semaphore mutex;

    public ConfirmController(Semaphore mutex) {
        super("");

        this.mutex = mutex;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    @SneakyThrows
    public void onGET(Request req, Response res) {
        int id = Integer.parseInt(req.query.params.get("id"));
        String name = req.query.params.get("name");
        String[] date = req.query.params.get("date").split("T");

        Seat seat = App.seats.get(id);

        // id válido & data válida & assento vago
        if (seat != null && date.length == 2 && !seat.isTaken()) {
            this.mutex.acquire();

            seat.setName(name);
            seat.setDate(date[0]);
            seat.setHour(date[1]);
            seat.setTaken(true);

            App.logger.log(req.socket, seat);
            Terminal.printLog(seat);

            res.redirect("/home?success=true");

            this.mutex.release();
        } else {
            res.redirect("/home?failure=true");
        }

        res.send();
    }
}
