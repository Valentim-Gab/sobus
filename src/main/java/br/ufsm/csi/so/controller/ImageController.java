package br.ufsm.csi.so.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import br.ufsm.csi.so.server.Controller;
import br.ufsm.csi.so.util.Header;
import br.ufsm.csi.so.util.Resource;

public class ImageController extends Controller {
    public ImageController(String resource) {
        super(resource);
    }

    @Override
    public void onGET(Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();

        Resource resource = Resource.from(this.resource);

        out.write(new Header(200).mime("image/png").build().getBytes());

        out.write(resource.bytes);
    }
}
