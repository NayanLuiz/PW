package ufrn.br.aulaweb.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TestesSession {

    @RequestMapping("/getSession")
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var name = request.getParameter("name");

        HttpSession s = request.getSession();

        if (name != null){
            s.setAttribute("name", name);

        }

        var nameNaSessao = s.getAttribute("name");

        response.getWriter().println("Nome na sessão:" + nameNaSessao);

    }
}